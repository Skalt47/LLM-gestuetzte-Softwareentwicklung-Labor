export type Card = {
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

export type MatchState = {
  matchId: string;
  activePlayer: string | null;
  topCard: Card;
};

export interface PlayResult {
  winner: string;
  humanValue: number;
  aiValue: number;
  humanDeckSize: number;
  aiDeckSize: number;
  nextTopCard: Card | null;
  activePlayer: string;
  gameOver?: boolean;
  matchWinner?: string | null;
}
