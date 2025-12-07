package com.example.backend.controller;

import com.example.backend.dto.StartMatchResponse;
import com.example.backend.service.MatchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
  public StartMatchResponse startMatch() {
    return matchService.startNewMatch();
  }

  @PostMapping("{matchId}/play")
  public PlayCardResponse playCard(@PathVariable String matchId, @RequestBody PlayCardRequest request) {
    return matchService.playCard(matchId, request);
  }
}
