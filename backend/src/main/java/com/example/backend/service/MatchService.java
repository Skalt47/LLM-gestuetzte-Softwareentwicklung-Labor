package com.example.backend.service;

import com.example.backend.dto.StartMatchResponse;
import com.example.backend.dto.StartMatchResponse.CardView;
import com.example.backend.model.DinoCard;
import com.example.backend.model.MatchState;
import com.example.backend.repository.DinosaurRepository;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MatchService {

  private final DinosaurRepository repo;
  private final GameStateManager state;

  public MatchService(DinosaurRepository repo, GameStateManager state) {
    this.repo = repo;
    this.state = state;
  }

  @Transactional(readOnly = true)
  public StartMatchResponse startNewMatch() {
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

  private static double opt(Number n) {
    return n == null ? 0.0 : n.doubleValue();
  }

  private static int optInt(Number n) {
    return n == null ? 0 : n.intValue();
  }
}
