package com.example.backend.controller;

import com.example.backend.dto.StartMatchResponse;
import com.example.backend.service.MatchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
