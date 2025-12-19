package com.example.backend.controller;

import com.example.backend.model.Player;
import com.example.backend.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/players")
@CrossOrigin
public class PlayerController {

  private final PlayerService playerService;

  public PlayerController(PlayerService playerService) {
    this.playerService = playerService;
  }

  public static class CreatePlayerRequest {
    public String name;
  }

  @PostMapping
  public ResponseEntity<Player> createPlayer(@RequestBody CreatePlayerRequest req) {
    if (req == null || req.name == null || req.name.isBlank()) {
      return ResponseEntity.badRequest().build();
    }
    var saved = playerService.create(req.name.trim());
    return ResponseEntity.ok(saved);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Player> getPlayer(@PathVariable Long id) {
    return playerService.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Record a match result for this player.
   * Example: POST /api/players/123/result?outcome=win
   * outcome: win | loss | draw
   */
  @PostMapping("/{id}/result")
  public ResponseEntity<Player> recordResult(@PathVariable Long id, @RequestParam String outcome) {
    if (outcome == null)
      return ResponseEntity.badRequest().build();
    switch (outcome.toLowerCase()) {
      case "win",
          "human" -> {
        var p = playerService.recordWin(id);
        return ResponseEntity.ok(p);
      }
      case "loss",
          "ai" -> {
        var p = playerService.recordLoss(id);
        return ResponseEntity.ok(p);
      }
      case "draw" -> {
        // no change for draw at the moment
        return playerService.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
      }
      default -> {
        return ResponseEntity.badRequest().build();
      }
    }
  }
}
