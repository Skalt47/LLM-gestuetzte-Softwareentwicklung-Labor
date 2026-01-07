package com.example.backend.dto;

import java.util.UUID;

public record StartMatchResponseDto(
    UUID matchId,
    String activePlayer,
    CardView topCard,
    CardView aiTopCard) {
  public record CardView(
      String species,
      String groupCode,
      int lifespanYears,
      double lengthM,
      double speedKmh,
      int intelligence,
      int attack,
      int defense,
      String imgUrl) {
  }
}
