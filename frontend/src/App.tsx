import { useState, useEffect } from "react";
import "./App.css";
import { Navbar } from "./components/NavBar";
import { HomePage } from "./pages/HomePage";
import { ProfilePage } from "./pages/ProfilePage";
import { GamePageAi } from "./pages/GamePageAi";
import type { MatchState, PlayResult } from "./types/game";

type PageView = "home" | "game" | "profile";

function App() {
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
  const [jokerActive, setJokerActive] = useState(false);
  const [aiThinking, setAiThinking] = useState(false);
  const [activePage, setActivePage] = useState<PageView>("home");

  const navigate = (page: PageView) => setActivePage(page);

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
      setJokerActive(false);
      setPlayResult(null);
      navigate("game");
    } catch (err) {
      setAiThinking(false);
      setError(err instanceof Error ? err.message : "Unknown error");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
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
  const attributeButtonsDisabled =
    !isHumanTurn || loading || jokerActive;

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
      suggestLoading ||
      jokerActive
    ) {
      return;
    }

    setSuggestLoading(true);
    setJokerActive(true);
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
      const data = await res.json();
      const attribute = data.attribute;
      setSuggested(attribute);
      setSuggestUsesLeft((prev) => Math.max(prev - 1, 0));
      await playCard(attribute);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unknown error");
    } finally {
      setSuggestLoading(false);
      setJokerActive(false);
    }
  };

  return (
    <div className="App">
      <Navbar
        playerName={playerName}
        activePage={activePage}
        onNavigate={navigate}
      />
      {!playerId && (
        <div className="player-modal-backdrop">
          <div className="player-modal">
            <h3 className="player-modal-title">
              <span className="dino-emoji" role="img" aria-label="dino">
                🦖
              </span>
              Enter your player name
            </h3>
            <input
              className="player-modal-input"
              value={nameInput}
              onChange={(e) => setNameInput(e.target.value)}
              placeholder="Your name"
            />
            <div className="player-modal-actions">
              <button className="save" onClick={createPlayer} disabled={loading}>
                {loading ? "Saving..." : "Save"}
              </button>
            </div>
          </div>
        </div>
      )}

      <main className="content-shell">
        {activePage === "home" && (
          <HomePage onPlay={startMatch} loading={loading} />
        )}
        {activePage === "profile" && (
          <ProfilePage
            playerName={playerName}
            playerId={playerId}
            onHome={() => navigate("home")}
            onPlay={() => navigate("game")}
          />
        )}
        {activePage === "game" && (
          <GamePageAi
            matchState={matchState}
            playResult={playResult}
            loading={loading}
            startMatch={startMatch}
            handleAttributeSelect={handleAttributeSelect}
            suggestAttribute={suggestAttribute}
            suggested={suggested}
            suggestLoading={suggestLoading}
            suggestUsesLeft={suggestUsesLeft}
            attributeButtonsDisabled={attributeButtonsDisabled}
            isHumanTurn={isHumanTurn}
            aiThinking={aiThinking}
            jokerActive={jokerActive}
          />
        )}
      </main>

      {error && <div className="error">{error}</div>}
    </div>
  );
}

export default App;
