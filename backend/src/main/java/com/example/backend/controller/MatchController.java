package com.example.backend.controller;

import com.example.backend.dto.StartMatchResponse;
import com.example.backend.service.MatchService;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

  private final MatchService matchService;

  public MatchController( MatchService matchService) {
    this.matchService = matchService;
  }

  @PostMapping("/start")
  public StartMatchResponse startMatch() {
     return matchService.startNewMatch();
  } 
}
