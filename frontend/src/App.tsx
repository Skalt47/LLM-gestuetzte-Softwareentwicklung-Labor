import { useState, useEffect } from "react";
import "./App.css";

// Card DTO
type Card = {
  id: number;
  species: string;
  groupCode: string;
  lifespanYears?: number;
  lengthM?: number;
  speedKmh?: number;
  intelligence?: number;
  attack?: number;
  defense?: number;
  imgUrl?: string;
};

// Match DTOs
type CardView = {
  species: string;
  groupCode: string;
  lifespanYears: number;
  lengthM: number;
  speedKmh: number;
  intelligence: number;
  attack: number;
  defense: number;
  imgUrl: string;
};

interface MatchState {
  matchId: string;
  activePlayer: string | null;
  topCard: Card;
}

interface PlayResult {
  winner: string;
  humanValue: number;
  aiValue: number;
  humanDeckSize: number;
  aiDeckSize: number;
  nextTopCard: Card | null;
  activePlayer: string;
  gameOver?: boolean;
  matchWinner?: string | null;
}

function App() {
  // player stored in localStorage: id and name
  const [playerId, setPlayerId] = useState<string | null>(() =>
    localStorage.getItem("playerId")
  );
  const [playerName, setPlayerName] = useState<string | null>(() =>
    localStorage.getItem("playerName")
  );
  const [nameInput, setNameInput] = useState("");
  const [matchState, setMatchState] = useState<MatchState | null>(null);
  const [playResult, setPlayResult] = useState<PlayResult | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [suggested, setSuggested] = useState<string | null>(null);
  const [suggestLoading, setSuggestLoading] = useState(false);
  const [suggestUsesLeft, setSuggestUsesLeft] = useState(3);
  const [aiThinking, setAiThinking] = useState(false);

  const startMatch = async () => {
    setLoading(true);
    setError(null);
    setAiThinking(false);
    try {
      const playerParam = playerId ? `?playerId=${playerId}` : "";
      const response = await fetch(
        `http://localhost:8080/api/matches/start${playerParam}`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
        }
      );
      if (!response.ok) throw new Error("Failed to start match");
      const data = await response.json();
      setMatchState(data);
      setSuggested(null);
      setSuggestUsesLeft(3);
      setPlayResult(null);
    } catch (err) {
      setAiThinking(false);
      setError(err instanceof Error ? err.message : "Unknown error");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    // sync localStorage -> state if user manually changed storage elsewhere
    const id = localStorage.getItem("playerId");
    const name = localStorage.getItem("playerName");
    if (id && !playerId) setPlayerId(id);
    if (name && !playerName) setPlayerName(name);
  }, []);

  async function createPlayer() {
    if (!nameInput || nameInput.trim().length === 0)
      return setError("Please enter a name");
    setLoading(true);
    try {
      const res = await fetch("http://localhost:8080/api/players", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name: nameInput.trim() }),
      });
      if (!res.ok) throw new Error("Failed to create player");
      const player = await res.json();
      localStorage.setItem("playerId", String(player.id));
      localStorage.setItem("playerName", player.name);
      setPlayerId(String(player.id));
      setPlayerName(player.name);
      setNameInput("");
    } catch (e) {
      setError(e instanceof Error ? e.message : "Unknown error");
    } finally {
      setLoading(false);
    }
  }

  const isHumanTurn = matchState?.activePlayer?.toUpperCase() === "HUMAN";
  const attributeButtonsDisabled = !isHumanTurn || loading;

  const playCard = async (attribute: string | null) => {
    if (!matchState) return;
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(
        `http://localhost:8080/api/matches/${matchState.matchId}/play`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ attribute }),
        }
      );
      if (!response.ok) throw new Error("Failed to play card");
      const result = await response.json();
      setAiThinking(false);
      setPlayResult(result);
      setMatchState((prev) => {
        if (!prev) return prev;
        return {
          ...prev,
          activePlayer: result.activePlayer ?? prev.activePlayer,
          topCard: result.nextTopCard ?? prev.topCard,
        };
      });

      // AUTO-ADVANCE AI TURNS: if AI won and game not over, let AI play again

      if (!result.gameOver && result.winner === "AI") {
        setAiThinking(true);
        setTimeout(() => void playCard(null), 2000);
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unknown error");
    } finally {
      setLoading(false);
    }
  };

  const handleAttributeSelect = (attribute: string) => {
    setSuggested(null);
    playCard(attribute);
  };

  const suggestAttribute = async () => {
    if (
      !matchState ||
      !isHumanTurn ||
      suggestUsesLeft <= 0 ||
      suggestLoading
    ) {
      return;
    }

    setSuggestLoading(true);
    setError(null);
    try {
      const res = await fetch(
        `http://localhost:8080/api/matches/${matchState.matchId}/suggest-attribute`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
        }
      );
      if (!res.ok) throw new Error("Failed to get suggestion");
      const data = await res.json(); // { attribute: "attack" }
      const attribute = data.attribute;
      setSuggested(attribute);
      setSuggestUsesLeft((prev) => Math.max(prev - 1, 0));
      await playCard(attribute);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unknown error");
    } finally {
      setSuggestLoading(false);
    }
  };

  return (
    <div className="App">
      {/* Player name modal (blocks game until a name is provided) */}
      {!playerId && (
        <div
          style={{
            position: "fixed",
            inset: 0,
            background: "rgba(0,0,0,0.5)",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            zIndex: 1000,
          }}
        >
          <div
            style={{
              background: "white",
              padding: 24,
              borderRadius: 8,
              width: 320,
            }}
          >
            <h3>Enter your player name</h3>
            <input
              value={nameInput}
              onChange={(e) => setNameInput(e.target.value)}
              placeholder="Your name"
              style={{ width: "100%", padding: 8, marginBottom: 12 }}
            />
            <div style={{ display: "flex", justifyContent: "flex-end" }}>
              <button
                onClick={createPlayer}
                disabled={loading}
                style={{ padding: "8px 12px" }}
              >
                {loading ? "Saving..." : "Save"}
              </button>
            </div>
          </div>
        </div>
      )}

      <h1>🦕 Stack Attack 🦕</h1>

      {!matchState ? (
        <div className="start-section">
          <button onClick={startMatch} disabled={loading}>
            {loading ? "Starting..." : "Start New Match"}
          </button>
        </div>
      ) : (
        <div className="game-section">
          {/* non-game-over UI */}
          {!playResult?.gameOver ? (
            <>
              <div className="deck-info">
                <p>Your Deck: {playResult?.humanDeckSize ?? 16} cards</p>
                <p>AI Deck: {playResult?.aiDeckSize ?? 16} cards</p>
              </div>

              {matchState.topCard && (
                <div className="card">
                  <h2>{matchState.topCard.species}</h2>
                  {matchState.topCard.imgUrl && (
                    <img
                      src={`http://localhost:8080${matchState.topCard.imgUrl}`}
                      alt={matchState.topCard.species}
                      style={{
                        width: "300px",
                        borderRadius: "8px",
                        marginBottom: "12px",
                      }}
                    />
                  )}
                  <p>Group: {matchState.topCard.groupCode}</p>
                  <div className="suggestion-panel">
                    <button
                      className="suggestion-button"
                      onClick={suggestAttribute}
                      disabled={
                        !isHumanTurn ||
                        suggestLoading ||
                        suggestUsesLeft <= 0 ||
                        loading
                      }
                    >
                      {suggestLoading
                        ? "Asking the LLM..."
                        : `Ask the LLM (${suggestUsesLeft} use${
                            suggestUsesLeft === 1 ? "" : "s"
                          } left)`}
                    </button>
                    <p className="suggestion-info">
                      {suggested
                        ? `LLM chose ${suggested} for you.`
                        : `Let the LLM decide when you're unsure (${suggestUsesLeft} use${
                            suggestUsesLeft === 1 ? "" : "s"
                          } left).`}
                    </p>
                  </div>
                  {aiThinking && (
                    <div className="ai-thinking">
                      <div className="ai-spinner">
                        <span role="img" aria-label="dinosaur">
                          🦖
                        </span>
                      </div>
                      <span>AI opponent is thinking...</span>
                    </div>
                  )}
                  <div className="attributes">
                    <button
                      onClick={() => handleAttributeSelect("lifespan")}
                      disabled={attributeButtonsDisabled}
                    >
                      ⏱️ Lifespan: {matchState.topCard.lifespanYears} years
                    </button>
                    <button
                      onClick={() => handleAttributeSelect("length")}
                      disabled={attributeButtonsDisabled}
                    >
                      📏 Length: {matchState.topCard.lengthM}m
                    </button>
                    <button
                      onClick={() => handleAttributeSelect("speed")}
                      disabled={attributeButtonsDisabled}
                    >
                      💨 Speed: {matchState.topCard.speedKmh} km/h
                    </button>
                    <button
                      onClick={() => handleAttributeSelect("intelligence")}
                      disabled={attributeButtonsDisabled}
                    >
                      🧠 Intelligence: {matchState.topCard.intelligence}
                    </button>
                    <button
                      onClick={() => handleAttributeSelect("attack")}
                      disabled={attributeButtonsDisabled}
                    >
                      ⚔️ Attack: {matchState.topCard.attack}
                    </button>
                    <button
                      onClick={() => handleAttributeSelect("defense")}
                      disabled={attributeButtonsDisabled}
                    >
                      🛡️ Defense: {matchState.topCard.defense}
                    </button>
                  </div>
                </div>
              )}

              {/* round result */}
              {playResult && !playResult.gameOver && (
                <div className={`result ${playResult.winner.toLowerCase()}`}>
                  <h3>
                    {playResult.winner === "DRAW"
                      ? "It's a Draw!"
                      : `${playResult.winner} Wins!`}
                  </h3>
                  <p>Your Value: {playResult.humanValue}</p>
                  <p>AI Value: {playResult.aiValue}</p>
                  <button onClick={startMatch}>Play Next Round</button>
                </div>
              )}
            </>
          ) : (
            /* game-over panel */
            <div className="game-over">
              <h2>Game Over!</h2>
              <p>
                {playResult?.matchWinner === "AI"
                  ? "You lost all your cards!"
                  : "You won all the cards!"}
              </p>
              <button onClick={startMatch}>Play Again</button>
            </div>
          )}
        </div>
      )}

      {error && <div className="error">{error}</div>}
    </div>
  );
}

export default App;
