package com.example.backend.service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.example.backend.model.DinoCard;

@Component
public class LlmClient {
  private static final Set<String> ALLOWED = Set.of("lifespan", "length", "speed", "intelligence", "attack", "defense");

  private final RestTemplate rest;
  private final String baseUrl;
  private final String model;

  public LlmClient(
      RestTemplateBuilder builder,
      @Value("${ollama.base-url:http://localhost:8080}") String baseUrl,
      @Value("${ollama.model:phi3:mini}") String model,
      @Value("${ollama.timeout-ms:60000}") long timeoutMs) {
    this.rest = builder
        .connectTimeout(Duration.ofMillis(timeoutMs))
        .readTimeout(Duration.ofMillis(timeoutMs))
        .build();
    this.baseUrl = baseUrl;
    this.model = model;
  }

  public String chooseAttribute(DinoCard card) {
    String prompt = """
        You are playing a Top Trumps style game. Choose the SINGLE attribute key that maximizes winning.
        Allowed keys: lifespan, length, speed, intelligence, attack, defense. 0 is lowest and 100 is highest possible value.
        Reply with only the key.
        Card:
        species=%s
        lifespan=%d years
        length=%.2f m
        speed=%.2f km/h
        intelligence=%d
        attack=%d
        defense=%d
        """.formatted(
        card.species,
        card.lifespanYears,
        card.lengthM,
        card.speedKmh,
        card.intelligence,
        card.attack,
        card.defense);

    ChatRequest req = new ChatRequest(
        model,
        List.of(
            new Message("system", "Return only the best attribute key."),
            new Message("user", prompt)),
        false);

    ChatResponse resp = rest.postForObject(baseUrl + "/api/chat", req, ChatResponse.class);
    String pick = resp == null || resp.message == null ? "" : resp.message.content.trim().toLowerCase();
    if (!ALLOWED.contains(pick)) {
      // deterministic fallback: pick highest numeric attribute
      return fallback(card);
    }
    return pick;
  }

  private String fallback(DinoCard c) {
    Map<String, Double> vals = Map.of(
        "lifespan", (double) c.lifespanYears,
        "length", c.lengthM,
        "speed", c.speedKmh,
        "intelligence", (double) c.intelligence,
        "attack", (double) c.attack,
        "defense", (double) c.defense);
    return vals.entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse("attack");
  }

  public record Message(String role, String content) {
  }

  public record ChatRequest(String model, List<Message> messages, boolean stream) {
  }

  public record ChatResponse(ChatMessage message) {
    public record ChatMessage(String role, String content) {
    }
  }
}
