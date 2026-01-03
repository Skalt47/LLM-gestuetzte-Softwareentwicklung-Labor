package com.example.backend.dto;

public class OllamaRequestDto {
  private String model;
  private String prompt;

  public OllamaRequestDto(String model, String prompt) {
    this.model = model;
    this.prompt = prompt;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getPrompt() {
    return prompt;
  }

  public void setPrompt(String prompt) {
    this.prompt = prompt;
  }
}