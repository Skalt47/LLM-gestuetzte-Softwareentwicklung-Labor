package com.example.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "players")
public class Player {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private int wins = 0;

  @Column(nullable = false)
  private int losses = 0;

  public Player() {}

  public Player(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getWins() {
    return wins;
  }

  public void setWins(int wins) {
    this.wins = wins;
  }

  public int getLosses() {
    return losses;
  }

  public void setLosses(int losses) {
    this.losses = losses;
  }

  public void incrementWins() {
    this.wins++;
  }

  public void incrementLosses() {
    this.losses++;
  }
}
