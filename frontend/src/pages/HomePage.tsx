import "./HomePage.css";

type HomePageProps = {
  onPlay: () => Promise<void>;
  loading: boolean;
};

export const HomePage = ({ onPlay, loading }: HomePageProps) => (
  <section className="home-hero">
    <h1 className="hero-title">Welcome to the  Dinosaur Top Trumps Game StackAttack</h1>
    <p className="hero-subtitle">
      How TopTrumps works: Aim for the strongest attribute, outsmart the AI, and collect the entire
      deck. Use jokers wisely and win against an AI oppponent!
    </p>
    <div className="hero-actions">
      <button
        className="primary-button"
        onClick={onPlay}
        disabled={loading}
      >
        {loading ? "Preparing the arena..." : "Play against AI opponent"}
      </button>
    </div>
  </section>
);
