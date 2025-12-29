package com.example.backend.dto;

public class PlayCardRequestDto {

  private String attribute;

  public PlayCardRequestDto() {
  }

  public PlayCardRequestDto(String attribute) {
    this.attribute = attribute;
  }

  public String getAttribute() {
    return attribute;
  }

  public void setAttribute(String attribute) {
    this.attribute = attribute;
  }
}
