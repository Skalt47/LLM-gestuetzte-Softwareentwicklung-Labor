import { useState } from "react";
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
  activePlayer: string;
  topCard: Card;
}

interface PlayResult {
  winner: string;
  humanValue: number;
  aiValue: number;
  humanDeckSize: number;
  aiDeckSize: number;
  nextTopCard: Card | null;
}

function App() {
  const [matchState, setMatchState] = useState<MatchState | null>(null);
  const [playResult, setPlayResult] = useState<PlayResult | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const startMatch = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch("http://localhost:8080/api/matches/start", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
      });
      if (!response.ok) throw new Error("Failed to start match");
      const data = await response.json();
      setMatchState(data);
      setPlayResult(null);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unknown error");
    } finally {
      setLoading(false);
    }
  };

  const playCard = async (attribute: string) => {
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
      setPlayResult(result);
      setMatchState((prev) =>
        prev && result.nextTopCard
          ? { ...prev, topCard: result.nextTopCard }
          : prev
      );

      // Fetch next card state (optional: implement a GET endpoint to fetch new top card)
      // For now, you could refresh the match or implement auto-play for AI
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unknown error");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="App">
      <h1>🦕 Stack Attack 🦕</h1>

      {!matchState ? (
        <div className="start-section">
          <button onClick={startMatch} disabled={loading}>
            {loading ? "Starting..." : "Start New Match"}
          </button>
        </div>
      ) : (
        <div className="game-section">
          <div className="deck-info">
            <p>Your Deck: {playResult?.humanDeckSize || 16} cards</p>
            <p>AI Deck: {playResult?.aiDeckSize || 16} cards</p>
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
              <div className="attributes">
                <button onClick={() => playCard("lifespan")}>
                  ⏱️ Lifespan: {matchState.topCard.lifespanYears} years
                </button>
                <button onClick={() => playCard("length")}>
                  📏 Length: {matchState.topCard.lengthM}m
                </button>
                <button onClick={() => playCard("speed")}>
                  💨 Speed: {matchState.topCard.speedKmh} km/h
                </button>
                <button onClick={() => playCard("intelligence")}>
                  🧠 Intelligence: {matchState.topCard.intelligence}
                </button>
                <button onClick={() => playCard("attack")}>
                  ⚔️ Attack: {matchState.topCard.attack}
                </button>
                <button onClick={() => playCard("defense")}>
                  🛡️ Defense: {matchState.topCard.defense}
                </button>
              </div>
            </div>
          )}

          {playResult && (
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

          {(playResult?.humanDeckSize === 0 ||
            playResult?.aiDeckSize === 0) && (
            <div className="game-over">
              <h2>Game Over!</h2>
              <p>
                {playResult?.humanDeckSize === 0
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
