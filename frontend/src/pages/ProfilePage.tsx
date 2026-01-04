import "./ProfilePage.css";

type ProfilePageProps = {
  playerName: string | null;
  playerId: string | null;
  onHome: () => void;
  onPlay: () => void;
};

export const ProfilePage = ({
  playerName,
  playerId,
  onHome,
  onPlay,
}: ProfilePageProps) => (
  <section className="profile-view">
    <div className="profile-card">
      <h2>Player Profile</h2>
      <p className="profile-name">{playerName ?? "Guest Explorer"}</p>
      <p className="profile-id">
        {playerId ? `Player ID: ${playerId}` : "Create a player to assign an ID."}
      </p>
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
