import { useEffect, useState } from "react";

type Dino = {
  id: number;
  species: string;
  groupCode: string;
  lifespanYears?: number;
  lengthM?: number;
  speedKmh?: number;
  intelligence?: number;
  attack?: number;
  defense?: number;
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
};

type StartMatchResponse = {
  matchId: string;
  activePlayer: string; // "HUMAN"
  myTopCard: CardView;
};

export default function App() {
  const [data, setData] = useState<Dino[]>([]);
  const [match, setMatch] = useState<StartMatchResponse | null>(null);
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState<string | null>(null);

  // Load dinosaurs 
  useEffect(() => {
    fetch("/api/dinosaurs")
      .then((r) => r.json())
      .then(setData)
      .catch((e) => setErr(String(e)));
  }, []);

  async function startMatch() {
    setLoading(true);
    setErr(null);
    try {
      const res = await fetch("/api/matches/start", { method: "POST" });
      if (!res.ok) throw new Error(`HTTP ${res.status} ${res.statusText}`);
      const json = (await res.json()) as StartMatchResponse;
      setMatch(json);
    } catch (e: any) {
      setErr(e.message ?? "Request failed");
    } finally {
      setLoading(false);
    }
  }

  return (
    <main style={{ padding: 16, fontFamily: "system-ui" }}>
      <h1>Dinosaur Data</h1>

      {err && (
        <p style={{ color: "#b00020" }}>
          <strong>Error:</strong> {err}
        </p>
      )}

      <table
        style={{ borderCollapse: "collapse", width: "100%", maxWidth: 900 }}
      >
        <thead>
          <tr>
            <th>Group</th>
            <th>Species</th>
            <th>lifespanYears</th>
            <th>Length (m)</th>
            <th>Speed (km/h)</th>
            <th>Int</th>
            <th>Atk</th>
            <th>Def</th>
          </tr>
        </thead>
        <tbody>
          {data.map((d) => (
            <tr key={d.id}>
              <td>{d.species}</td>
              <td>{d.groupCode}</td>
              <td>{d.lifespanYears ?? "-"}</td>
              <td>{d.lengthM ?? "-"}</td>
              <td>{d.speedKmh ?? "-"}</td>
              <td>{d.intelligence ?? "-"}</td>
              <td>{d.attack ?? "-"}</td>
              <td>{d.defense ?? "-"}</td>
            </tr>
          ))}
        </tbody>
      </table>

      <hr style={{ margin: "24px 0" }} />

      <section>
        <h2>Match – quick test</h2>
        <button
          onClick={startMatch}
          disabled={loading}
          style={{ padding: "8px 14px" }}
        >
          {loading ? "Starting…" : "Start Match"}
        </button>

        {match && (
          <div
            style={{
              marginTop: 12,
              padding: 12,
              border: "1px solid #ddd",
              borderRadius: 8,
              maxWidth: 520,
            }}
          >
            <div style={{ marginBottom: 8 }}>
              <div>
                <strong>matchId:</strong> {match.matchId}
              </div>
              <div>
                <strong>activePlayer:</strong> {match.activePlayer}
              </div>
            </div>

            <h3>My Top Card</h3>
            <ul style={{ paddingLeft: 18, margin: 0 }}>
              <li>
                <strong>Species:</strong> {match.myTopCard.species}
              </li>
              <li>
                <strong>Group:</strong> {match.myTopCard.groupCode}
              </li>
              <li>
                <strong>Lifespan (years):</strong>{" "}
                {match.myTopCard.lifespanYears}
              </li>
              <li>
                <strong>Length (m):</strong> {match.myTopCard.lengthM}
              </li>
              <li>
                <strong>Speed (km/h):</strong> {match.myTopCard.speedKmh}
              </li>
              <li>
                <strong>Intelligence:</strong> {match.myTopCard.intelligence}
              </li>
              <li>
                <strong>Attack:</strong> {match.myTopCard.attack}
              </li>
              <li>
                <strong>Defense:</strong> {match.myTopCard.defense}
              </li>
            </ul>

            <details style={{ marginTop: 10 }}>
              <summary>Raw JSON</summary>
              <pre
                style={{
                  background: "#2781dbff",
                  padding: 12,
                  borderRadius: 6,
                  overflowX: "auto",
                }}
              >
                {JSON.stringify(match, null, 2)}
              </pre>
            </details>
          </div>
        )}
      </section>
    </main>
  );
}
