package com.example.backend.model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;
import java.io.Serializable;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class MatchStateModel implements Serializable {

  private final UUID matchId = UUID.randomUUID();
  private String activePlayer; // Human or AI
  private Long playerId;

  @JsonDeserialize(as = ArrayDeque.class)
  private final Deque<DinoCardModel> humanDeck = new ArrayDeque<>();
  @JsonDeserialize(as = ArrayDeque.class)
  private final Deque<DinoCardModel> aiDeck = new ArrayDeque<>();

  // Default Constructor for Jackson
  public MatchStateModel() {
  }

  // Getters + Setters
  public UUID getMatchId() {
    return matchId;
  }

  public String getActivePlayer() {
    return activePlayer;
  }

  public void setActivePlayer(String activePlayer) {
    this.activePlayer = activePlayer;
  }

  public Deque<DinoCardModel> getHumanDeck() {
    return humanDeck;
  }

  public Deque<DinoCardModel> getAiDeck() {
    return aiDeck;
  }

  public Long getPlayerId() {
    return playerId;
  }

  public void setPlayerId(Long playerId) {
    this.playerId = playerId;
  }

}
