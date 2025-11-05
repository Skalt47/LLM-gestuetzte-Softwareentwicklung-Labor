package com.example.backend.service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

public class MatchState {

  private final UUID id = UUID.randomUUID();
  private String activePlayer; // Human or AI
  private final Deque<DinoCard> humanDeck = new ArrayDeque<>();
  private final Deque<DinoCard> aiDeck = new ArrayDeque<>();

  // Getters + Setters
  public UUID getId() {
    return id;
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
}
