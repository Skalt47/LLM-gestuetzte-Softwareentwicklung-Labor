package com.example.backend.dto;

public class PlayCardResponseDto {

  private String winner;
  private double humanValue;
  private double aiValue;
  private int humanDeckSize;
  private int aiDeckSize;
  private StartMatchResponseDto.CardView nextTopCard;
  private boolean gameOver;
  private String activePlayer;
  private String matchWinner;

  public PlayCardResponseDto(
      String winner,
      double humanValue,
      double aiValue,
      int humanDeckSize,
      int aiDeckSize,
      StartMatchResponseDto.CardView nextTopCard,
      boolean gameOver,
      String activePlayer,
      String matchWinner) {
    this.winner = winner;
    this.humanValue = humanValue;
    this.aiValue = aiValue;
    this.humanDeckSize = humanDeckSize;
    this.aiDeckSize = aiDeckSize;
    this.nextTopCard = nextTopCard;
    this.gameOver = gameOver;
    this.activePlayer = activePlayer;
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

  public StartMatchResponseDto.CardView getNextTopCard() {
    return nextTopCard;
  }

  public boolean isGameOver() {
    return gameOver;
  }

  public String getActivePlayer() {
    return activePlayer;
  }

  public String getMatchWinner() {
    return matchWinner;
  }
}
