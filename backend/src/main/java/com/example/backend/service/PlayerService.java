package com.example.backend.service;

import com.example.backend.model.PlayerModel;
import com.example.backend.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.lang.NonNull;

import java.util.Optional;

@Service
public class PlayerService {

  private final PlayerRepository repo;

  public PlayerService(PlayerRepository repo) {
    this.repo = repo;
  }

  @Transactional
  public PlayerModel create(String name) {
    var p = new PlayerModel(name);
    return repo.save(p);
  }

  @Transactional(readOnly = true)
  public Optional<PlayerModel> findById(@NonNull Long id) {
    return repo.findById(id);
  }

  @Transactional
  public void applyMatchResult(@NonNull Long playerId, String winner) {
    if (winner.equals("HUMAN")) {
      recordWin(playerId);
    } else if (winner.equals("AI")) {
      recordLoss(playerId);
    }
  }

  @Transactional
  public PlayerModel recordWin(@NonNull Long id) {
    var p = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Unknown player id: " + id));
    p.incrementWins();
    return repo.save(p);
  }

  @Transactional
  public PlayerModel recordLoss(@NonNull Long id) {
    var p = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Unknown player id: " + id));
    p.incrementLosses();
    return repo.save(p);
  }
}
