import React, { useState } from "react";

type StartMatchResponse = {
  matchId: string;
  activePlayer: string;
  myTopCard: Record<string, unknown>; // flexible to match your current DTO
};

export default function MatchStartTest() {
  const [data, setData] = useState<StartMatchResponse | null>(null);
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState<string | null>(null);

  async function startMatch() {
    setLoading(true);
    setErr(null);
    try {
      const res = await fetch("/api/matches/start", { method: "POST" });
      if (!res.ok) throw new Error(`HTTP ${res.status} ${res.statusText}`);
      const json = (await res.json()) as StartMatchResponse;
      setData(json);
    } catch (e: any) {
      setErr(e.message ?? "Request failed");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div
      style={{ maxWidth: 520, margin: "2rem auto", fontFamily: "system-ui" }}
    >
      <h2>Match Start – Smoke Test</h2>
      <button
        onClick={startMatch}
        disabled={loading}
        style={{ padding: "8px 14px" }}
      >
        {loading ? "Starting…" : "Start Match"}
      </button>

      {err && (
        <div style={{ marginTop: 12, color: "#b00020" }}>
          <strong>Error:</strong> {err}
        </div>
      )}

      {data && (
        <div
          style={{
            marginTop: 16,
            padding: 12,
            border: "1px solid #ddd",
            borderRadius: 8,
          }}
        >
          <div style={{ marginBottom: 8 }}>
            <div>
              <strong>matchId:</strong> {data.matchId}
            </div>
            <div>
              <strong>activePlayer:</strong> {data.activePlayer}
            </div>
          </div>

          <h3 style={{ marginTop: 12 }}>My Top Card</h3>
          <ul style={{ paddingLeft: 18 }}>
            {Object.entries(data.myTopCard || {}).map(([k, v]) => (
              <li key={k}>
                <strong>{k}:</strong> {String(v)}
              </li>
            ))}
          </ul>

          <details style={{ marginTop: 10 }}>
            <summary>Raw JSON</summary>
            <pre
              style={{
                background: "#f6f8fa",
                padding: 12,
                borderRadius: 6,
                overflowX: "auto",
              }}
            >
              {JSON.stringify(data, null, 2)}
            </pre>
          </details>
        </div>
      )}
    </div>
  );
}
