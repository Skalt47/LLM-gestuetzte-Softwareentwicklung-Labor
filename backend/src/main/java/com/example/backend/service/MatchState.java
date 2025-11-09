package com.example.backend.service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;
import java.io.Serializable;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class MatchState implements Serializable{

  private UUID id = UUID.randomUUID();
  private String activePlayer; // Human or AI

  @JsonDeserialize(as = ArrayDeque.class)
  private Deque<DinoCard> humanDeck = new ArrayDeque<>();
  @JsonDeserialize(as = ArrayDeque.class)
  private Deque<DinoCard> aiDeck = new ArrayDeque<>();

  //Default Constructor for Jackson
  public MatchState() {}

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

  public void setHumanDeck(Deque<DinoCard> humanDeck){
    this.humanDeck = humanDeck;
  }

  public Deque<DinoCard> getAiDeck() {
    return aiDeck;
  }

  public void setAiDeck(Deque<DinoCard> aiDeck){
    this.aiDeck = aiDeck;
  }
}
