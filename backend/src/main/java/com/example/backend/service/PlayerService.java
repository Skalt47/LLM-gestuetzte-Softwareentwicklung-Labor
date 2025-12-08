package com.example.backend.service;

import com.example.backend.model.Player;
import com.example.backend.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PlayerService {

  private final PlayerRepository repo;

  public PlayerService(PlayerRepository repo) {
    this.repo = repo;
  }

  @Transactional
  public Player create(String name) {
    var p = new Player(name);
    return repo.save(p);
  }

  @Transactional(readOnly = true)
  public Optional<Player> findById(Long id) {
    return repo.findById(id);
  }

  @Transactional
   public void applyMatchResult(Long playerId, String winner) {
        if (winner.equals("HUMAN")) {
            recordWin(playerId);
        } else if (winner.equals("AI")) {
            recordLoss(playerId);
        }
    }
 
  @Transactional
  public Player recordWin(Long id) {
    var p = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Unknown player id: " + id));
    p.incrementWins();
    return repo.save(p);
  }

  @Transactional
  public Player recordLoss(Long id) {
    var p = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Unknown player id: " + id));
    p.incrementLosses();
    return repo.save(p);
  }
}
