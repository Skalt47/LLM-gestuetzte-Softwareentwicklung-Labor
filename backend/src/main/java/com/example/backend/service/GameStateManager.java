package com.example.backend.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class GameStateManager {

  private final Map<UUID, MatchState> matches = new ConcurrentHashMap<>();

  public MatchState get(UUID id) {
    return matches.get(id);
  }

  public void put(MatchState s) {
    matches.put(s.getId(), s);
  }

  public void remove(UUID id) {
    matches.remove(id);
  }
}
