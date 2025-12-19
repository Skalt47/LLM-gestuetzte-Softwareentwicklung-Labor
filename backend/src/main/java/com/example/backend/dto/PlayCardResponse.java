package com.example.backend.dto;

public class PlayCardResponse {

  private String winner;
  private double humanValue;
  private double aiValue;
  private int humanDeckSize;
  private int aiDeckSize;
  private StartMatchResponse.CardView nextTopCard;
  private boolean gameOver;
  private String matchWinner;

  public PlayCardResponse(
      String winner,
      double humanValue,
      double aiValue,
      int humanDeckSize,
      int aiDeckSize,
      StartMatchResponse.CardView nextTopCard,
      boolean gameOver,
      String matchWinner) {
    this.winner = winner;
    this.humanValue = humanValue;
    this.aiValue = aiValue;
    this.humanDeckSize = humanDeckSize;
    this.aiDeckSize = aiDeckSize;
    this.nextTopCard = nextTopCard;
    this.gameOver = gameOver;
    this.matchWinner = matchWinner;
  }

  public String getWinner() {
    return winner;
  }

  public double getHumanValue() {
    return humanValue;
  }

  public double getAiValue() {
    return aiValue;
  }

  public int getHumanDeckSize() {
    return humanDeckSize;
  }

  public int getAiDeckSize() {
    return aiDeckSize;
  }

  public StartMatchResponse.CardView getNextTopCard() {
    return nextTopCard;
  }

  public boolean isGameOver() {
    return gameOver;
  }

  public String getMatchWinner() {
    return matchWinner;
  }
}
