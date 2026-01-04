import type { MatchState, PlayResult } from "../types/game";
import "./GamePageAi.css";

type GamePageProps = {
  matchState: MatchState | null;
  playResult: PlayResult | null;
  loading: boolean;
  startMatch: () => Promise<void>;
  handleAttributeSelect: (attribute: string) => void;
  suggestAttribute: () => void;
  suggested: string | null;
  suggestLoading: boolean;
  suggestUsesLeft: number;
  attributeButtonsDisabled: boolean;
  isHumanTurn: boolean;
  aiThinking: boolean;
  jokerActive: boolean;
};

export const GamePageAi = ({
  matchState,
  playResult,
  loading,
  startMatch,
  handleAttributeSelect,
  suggestAttribute,
  suggested,
  suggestLoading,
  suggestUsesLeft,
  attributeButtonsDisabled,
  isHumanTurn,
  aiThinking,
  jokerActive,
}: GamePageProps) => (
  <section className="game-stage">
    {!matchState ? (
      <div className="start-section">
        <button onClick={startMatch} disabled={loading}>
          {loading ? "Starting..." : "Start New Match"}
        </button>
      </div>
    ) : (
      <div className="game-section">
        {!playResult?.gameOver ? (
          <>
            <div className="deck-info">
              <p className="deck-pill human">Your Deck: {playResult?.humanDeckSize ?? 16} cards</p>
              <p className="deck-pill ai">AI Deck: {playResult?.aiDeckSize ?? 16} cards</p>
            </div>

            {matchState.topCard && (
              <div className="card">
                <div className="card-header">
                  <h2>{matchState.topCard.species}</h2>
                  <p className="card-group">{matchState.topCard.groupCode}</p>
                </div>
                {matchState.topCard.imgUrl && (
                  <img
                    src={`http://localhost:8080${matchState.topCard.imgUrl}`}
                    alt={matchState.topCard.species}
                    className="card-image"
                  />
                )}
                <div className="suggestion-panel">
                  <button
                    className="suggestion-button"
                    onClick={suggestAttribute}
                    disabled={
                      !isHumanTurn ||
                      suggestLoading ||
                      suggestUsesLeft <= 0 ||
                      loading ||
                      jokerActive
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
  </section>
);
