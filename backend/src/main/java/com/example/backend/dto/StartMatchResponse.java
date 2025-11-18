package com.example.backend.dto;

import java.util.UUID;

public record StartMatchResponse(
  UUID matchId,
  String activePlayer,
  CardView myTopCard
) {
  public record CardView(
    String species,
    String groupCode,
    int lifespanYears,
    double lengthM,
    double speedKmh,
    int intelligence,
    int attack,
    int defense,
    String imgUrl
  ) {}
}
