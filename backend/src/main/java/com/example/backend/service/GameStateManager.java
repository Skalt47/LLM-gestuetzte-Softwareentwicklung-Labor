package com.example.backend.service;

import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class GameStateManager {

  private final RedisTemplate<String, MatchState> redisMatchesTemplate;
  private static final String key_prefix = "match:";

  public GameStateManager(
    RedisTemplate<String, MatchState> redisMatchesTemplate
  ) {
    this.redisMatchesTemplate = redisMatchesTemplate;
  }

  public MatchState get(UUID id) {
    return redisMatchesTemplate.opsForValue().get(key_prefix + id);
  }

  public void put(MatchState s) {
    redisMatchesTemplate.opsForValue().set(key_prefix + s.getId(), s);
  }

  public void remove(UUID id) {
    redisMatchesTemplate.delete(key_prefix + id);
  }
}
