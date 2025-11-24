package com.example.backend.dto;

public class PlayCardResponse {

  private String winner;
  private double humanValue;
  private double aiValue;
  private int humanDeckSize;
  private int aiDeckSize;
  private StartMatchResponse.CardView nextTopCard;

  public PlayCardResponse(
    String winner,
    double humanValue,
    double aiValue,
    int humanDeckSize,
    int aiDeckSize,
    StartMatchResponse.CardView nextTopCard
  ) {
    this.winner = winner;
    this.humanValue = humanValue;
    this.aiValue = aiValue;
    this.humanDeckSize = humanDeckSize;
    this.aiDeckSize = aiDeckSize;
    this.nextTopCard = nextTopCard;
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
}
