package com.example.backend.service;

import com.example.backend.model.MatchStateModel;
import java.util.UUID;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class GameStateManagerService {

  private final RedisTemplate<String, MatchStateModel> redisMatchesTemplate;
  private static final String KEY_PREFIX = "match:";

  public GameStateManagerService(
      RedisTemplate<String, MatchStateModel> redisMatchesTemplate) {
    this.redisMatchesTemplate = redisMatchesTemplate;
  }

  public MatchStateModel get(UUID matchId) {
    return redisMatchesTemplate.opsForValue().get(KEY_PREFIX + matchId);
  }

  public void put(MatchStateModel s) {
    redisMatchesTemplate.opsForValue().set(KEY_PREFIX + s.getMatchId(), s);
  }

  public void remove(UUID matchId) {
    redisMatchesTemplate.delete(KEY_PREFIX + matchId);
  }
}
