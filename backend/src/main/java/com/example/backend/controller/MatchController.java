package com.example.backend.controller;

import com.example.backend.dto.StartMatchResponse;
import com.example.backend.dto.StartMatchResponse.CardView;
import com.example.backend.repository.DinosaurRepository;
import com.example.backend.service.DinoCard;
import com.example.backend.service.GameStateManager;
import com.example.backend.service.MatchState;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

  private final GameStateManager state;
  private final DinosaurRepository repo;

  public MatchController(GameStateManager state, DinosaurRepository repo) {
    this.state = state;
    this.repo = repo;
  }

  @PostMapping("/start")
  @Transactional(readOnly = true)
  public StartMatchResponse startMatch() {
    // 1) Retrieving all dinosaurs from the database
    var dinos = repo.findAll();

    // 2) Transforming Dinosaurs (Raw Data) to DinoCards
    var cards = dinos
      .stream()
      .map(d -> {
        var c = new DinoCard();
        c.dinosaurId = d.getId();
        c.species = d.getSpecies();
        c.groupCode = d.getGroupCode();
        c.lifespanYears = optInt(d.getLifespanYears());
        c.lengthM = opt(d.getLengthM());
        c.speedKmh = opt(d.getSpeedKmh());
        c.intelligence = optInt(d.getIntelligence());
        c.attack = optInt(d.getAttack());
        c.defense = optInt(d.getDefense());
        return c;
      })
      .collect(Collectors.toList());

    // 3) Mixing & Handing out cards to human and ai
    Collections.shuffle(cards, new Random());
    var human = new ArrayDeque<DinoCard>(cards.subList(0, 15));
    var ai = new ArrayDeque<DinoCard>(cards.subList(16, 32));

    // 4) Create a new match state and store both decks in memory
    var ms = new MatchState();
    ms.setActivePlayer(null);
    ms.getHumanDeck().addAll(human);
    ms.getAiDeck().addAll(ai);

    //Save MatchState (current Game) in Redis
    state.put(ms);

    // 5) Build the API response: include match ID, starting player, and the human's top card
    var top = ms.getHumanDeck().peekFirst();
    var view = new CardView(
      top.species,
      top.groupCode,
      top.lifespanYears,
      top.lengthM,
      top.speedKmh,
      top.intelligence,
      top.attack,
      top.defense
    );
    return new StartMatchResponse(ms.getId(), "HUMAN", view);
  }

  // Safe double conversion: returns 0.0 if the value is null
  private static double opt(Number n) {
    return n == null ? 0.0 : n.doubleValue();
  }

  // Safe int conversion: returns 0 if the value is null
  private static int optInt(Number n) {
    return n == null ? 0 : n.intValue();
  }
}
