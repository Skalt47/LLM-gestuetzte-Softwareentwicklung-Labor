package com.example.backend.dto;

public class PlayCardRequest {

  private String attribute;

  public PlayCardRequest() {}

  public PlayCardRequest(String attribute) {
    this.attribute = attribute;
  }

  public String getAttribute() {
    return attribute;
  }

  public void setAttribute(String attribute) {
    this.attribute = attribute;
  }
}
