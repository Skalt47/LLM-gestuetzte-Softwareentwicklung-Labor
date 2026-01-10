import type { MatchState, PlayResult } from "../types/game";

const API_BASE = "http://localhost:8080/api";

export async function fetchPlayer(playerId: string) {
  const response = await fetch(`${API_BASE}/players/${playerId}`);
  if (!response.ok) throw new Error("Match-Start fehlgeschlagen");
    return response.json();
}

export async function startMatch(playerId?: string): Promise<MatchState> {
  const playerParam = playerId ? `?playerId=${playerId}` : "";
  const response = await fetch(`${API_BASE}/matches/start${playerParam}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
  });
  if (!response.ok) throw new Error("Match-Start fehlgeschlagen");
    return response.json();
}

export async function createPlayer(name: string) {
  const response = await fetch(`${API_BASE}/players`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ name: name.trim() }),
  });
  if (!response.ok) throw new Error("Failed to create player");
    return response.json();
}

export async function playCard(
  matchId: string,
  attribute: string | null
): Promise<PlayResult> {
  const response = await fetch(`${API_BASE}/matches/${matchId}/play`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ attribute }),
  });
  if (!response.ok) throw new Error("Failed to play card");
    return response.json();
}

export async function suggestAttribute(matchId: string) {
  const response = await fetch(
    `${API_BASE}/matches/${matchId}/suggest-attribute`,
    {
      method: "POST",
      headers: { "Content-Type": "application/json" },
    }
  );
  if (!response.ok) throw new Error("Failed to get suggestion");
    return response.json();
}
