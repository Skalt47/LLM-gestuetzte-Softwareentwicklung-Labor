package com.example.backend.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.backend.dto.PlayCardRequest;
import com.example.backend.dto.PlayCardResponse;
import com.example.backend.dto.StartMatchResponse;
import com.example.backend.service.MatchService;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

  private final MatchService matchService;

  public MatchController(MatchService matchService, RestTemplate restTemplate) {
    this.matchService = matchService;
  }

  @PostMapping("/start")
  public StartMatchResponse startMatch(
      @RequestParam(name = "playerId", required = false) Long playerId) {
    return matchService.startNewMatch(playerId);
  }

  @PostMapping("/{matchId}/play")
  public PlayCardResponse playCard(
      @PathVariable String matchId,
      @RequestBody PlayCardRequest request) {
    return matchService.playCard(matchId, request);
  }

  @PostMapping("/{matchId}/suggest-attribute")
  public Map<String, String> suggest(@PathVariable String matchId) {
    return Map.of("attribute", matchService.suggestAttribute(matchId));
  }
}
