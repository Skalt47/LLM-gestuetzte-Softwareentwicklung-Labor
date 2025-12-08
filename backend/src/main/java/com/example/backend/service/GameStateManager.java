package com.example.backend.service;

import com.example.backend.model.MatchState;
import java.util.UUID;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class GameStateManager {

  private final RedisTemplate<String, MatchState> redisMatchesTemplate;
  private static final String KEY_PREFIX = "match:";

  public GameStateManager(
    RedisTemplate<String, MatchState> redisMatchesTemplate
  ) {
    this.redisMatchesTemplate = redisMatchesTemplate;
  }

  public MatchState get(UUID matchId) {
    return redisMatchesTemplate.opsForValue().get(KEY_PREFIX + matchId);
  }

  public void put(MatchState s) {
    redisMatchesTemplate.opsForValue().set(KEY_PREFIX + s.getMatchId(), s);
  }

  public void remove(UUID matchId) {
    redisMatchesTemplate.delete(KEY_PREFIX + matchId);
  }
}
