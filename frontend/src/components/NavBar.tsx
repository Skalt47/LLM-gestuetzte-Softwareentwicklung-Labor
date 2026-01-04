import "./NavBar.css";

type PageView = "home" | "game" | "profile";

type NavbarProps = {
  playerName: string | null;
  activePage: PageView;
  onNavigate: (page: PageView) => void;
};

export const Navbar = ({
  playerName,
  activePage,
  onNavigate,
}: NavbarProps) => (
  <nav className="navbar">
    <button
      className="nav-icon"
      aria-label="Go to homepage"
      onClick={() => onNavigate("home")}
    >
      <span role="img" aria-label="home">
        🏠
      </span>
    </button>
    <div className={`nav-title ${activePage === "game" ? "active" : ""}`}>
      <span role="img" aria-label="dino">
        🦕
      </span>
      Stack Attack 
      <span role="img" aria-label="dino">
        🦕
      </span>
    </div>
    <button
      className="nav-icon player-link"
      aria-label="Open profile"
      onClick={() => onNavigate("profile")}
    >
      <span role="img" aria-label="profile">
        👤
      </span>
      <span className="player-name-nav">{playerName || "Player"}</span>
    </button>
  </nav>
);
