package com.example.backend.model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;
import java.io.Serializable;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class MatchState implements Serializable {

  private final UUID matchId = UUID.randomUUID();
  private String activePlayer; // Human or AI
  private Long playerId;

  @JsonDeserialize(as = ArrayDeque.class)
  private final Deque<DinoCard> humanDeck = new ArrayDeque<>();
  @JsonDeserialize(as = ArrayDeque.class)
  private final Deque<DinoCard> aiDeck = new ArrayDeque<>();

  // Default Constructor for Jackson
  public MatchState() {
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

  public Deque<DinoCard> getHumanDeck() {
    return humanDeck;
  }

  public Deque<DinoCard> getAiDeck() {
    return aiDeck;
  }

  public Long getPlayerId() {
    return playerId;
  }

  public void setPlayerId(Long playerId) {
    this.playerId = playerId;
  }

}
