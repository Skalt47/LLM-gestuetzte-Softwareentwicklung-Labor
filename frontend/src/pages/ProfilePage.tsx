
import "./ProfilePage.css";

type ProfilePageProps = {
  playerName: string | null;
  playerId: string | null;
  onHome: () => void;
  onPlay: () => void;
  wins: number;
  losses: number;
};

export const ProfilePage = ({
  playerName,
  playerId,
  onHome,
  onPlay,
  wins,
  losses,
}: ProfilePageProps) => {
  // Berechne den Maximalwert für die Skalierung der Y-Achse
  const maxValue = Math.max(wins, losses, 5); // Mindestens 5 für die Optik
  const yTicks = [maxValue, Math.round(maxValue / 2), 0];

  // Berechnung der Höhe in Prozent
  const getBarHeight = (value: number) => (value / maxValue) * 100;

  return (
    <section className="profile-view">
      <div className="profile-card">
        <header className="profile-header">
          <h2>Player Profile</h2>
          <p className="profile-name">{playerName ?? "Guest Explorer"}</p>
          <p className="profile-id">
            {playerId ? `ID: ${playerId}` : "No ID assigned"}
          </p>
        </header>

        <div className="stats-container">
          <h3>Battle Statistics</h3>
          
          <div className="chart-wrapper">
            {/* Y-Achse Beschriftung */}
            <div className="y-axis">
              {yTicks.map((tick) => (
                <span key={tick}>{tick}</span>
              ))}
            </div>

            <div className="chart-area">
              <div className="grid-lines">
                <div className="grid-line"></div>
                <div className="grid-line"></div>
                <div className="grid-line"></div>
              </div>

              <div className="bars-container">
                <div className="bar-group">
                  <div className="bar-wrapper">
                    <div 
                      className="bar win-bar" 
                      style={{ height: `${getBarHeight(wins)}%` }}
                    >
                      <span className="value-tooltip">{wins}</span>
                    </div>
                  </div>
                  <span className="x-label">Wins</span>
                </div>

                <div className="bar-group">
                  <div className="bar-wrapper">
                    <div 
                      className="bar loss-bar" 
                      style={{ height: `${getBarHeight(losses)}%` }}
                    >
                      <span className="value-tooltip">{losses}</span>
                    </div>
                  </div>
                  <span className="x-label">Losses</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className="profile-actions">
          <button className="primary-button" onClick={onPlay}>
            Jump into the fight
          </button>
          <button className="ghost-button" onClick={onHome}>
            Back to homepage
          </button>
        </div>
      </div>
    </section>
  );
};