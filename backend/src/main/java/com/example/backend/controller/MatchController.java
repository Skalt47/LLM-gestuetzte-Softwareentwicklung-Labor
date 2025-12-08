package com.example.backend.controller;

import com.example.backend.dto.StartMatchResponse;
import com.example.backend.service.MatchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.backend.dto.PlayCardRequest;
import com.example.backend.dto.PlayCardResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

  private final MatchService matchService;

  public MatchController(MatchService matchService) {
    this.matchService = matchService;
  }

  @PostMapping("/start")
  public StartMatchResponse startMatch(@RequestParam(name = "playerId", required = false) Long playerId) {
    return matchService.startNewMatch(playerId);
  }

  @PostMapping("{matchId}/play")
  public PlayCardResponse playCard(@PathVariable String matchId, @RequestBody PlayCardRequest request) {
    return matchService.playCard(matchId, request);
  }
}
