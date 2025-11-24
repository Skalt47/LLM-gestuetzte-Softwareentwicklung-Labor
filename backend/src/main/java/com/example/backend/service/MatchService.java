package com.example.backend.service;

import com.example.backend.dto.PlayCardRequest;
import com.example.backend.dto.PlayCardResponse;
import com.example.backend.dto.StartMatchResponse;
import com.example.backend.dto.StartMatchResponse.CardView;
import com.example.backend.model.DinoCard;
import com.example.backend.model.MatchState;
import com.example.backend.repository.DinosaurRepository;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

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
    var ai = new ArrayDeque<DinoCard>(cards.subList(16, 31));

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
    return new StartMatchResponse(ms.getMatchId(), "HUMAN", view);
  }

  private static double opt(Number n) {
    return n == null ? 0.0 : n.doubleValue();
  }

  private static int optInt(Number n) {
    return n == null ? 0 : n.intValue();
  }
  @Transactional
  public PlayCardResponse playCard(
    @PathVariable String matchId,
    @RequestBody PlayCardRequest request
  ) {
    // 1) Load the match state from Redis
    var ms = state.get(UUID.fromString(matchId));
    if (ms == null) {
      throw new IllegalArgumentException("Invalid match ID: " + matchId);
    }

    // 2) Get top cards from both decks
    var humanCard = ms.getHumanDeck().peekFirst();
    var aiCard = ms.getAiDeck().peekFirst();

    if (humanCard == null || aiCard == null) {
      throw new IllegalStateException("One of the decks is empty.");
    }
    // 3) Get the chosen attribute value from human
    double humanValue = getAttributeValue(humanCard, request.getAttribute());
    double aiValue = getAttributeValue(aiCard, request.getAttribute());

    // 4) Compare and determine the winner
    int result = Double.compare(humanValue, aiValue);
    String winner = result > 0 ? "HUMAN" : result < 0 ? "AI" : "DRAW";

    // 5) Move cards to winner's deck
    ms.getHumanDeck().removeFirst();
    ms.getAiDeck().removeFirst();

    if (result > 0) {
      // Human wins
      ms.getHumanDeck().addLast(humanCard);
      ms.getHumanDeck().addLast(aiCard);
    } else if (result < 0) {
      // AI wins
      ms.getAiDeck().addLast(aiCard);
      ms.getAiDeck().addLast(humanCard);
    } else {
      // Draw: each player takes back their card
      ms.getHumanDeck().addLast(humanCard);
      ms.getAiDeck().addLast(aiCard);
    }
    // 6) Save state back to Redis
    state.put(ms);

    // 7) Prepare next top card view
    var next = ms.getHumanDeck().peekFirst();
    var nextView = next == null ? null : new CardView(
      next.species,
      next.groupCode,
      next.lifespanYears,
      next.lengthM,
      next.speedKmh,
      next.intelligence,
      next.attack,
      next.defense
    );

    // 8) Return the result
    return new PlayCardResponse(
      winner,
      humanValue,
      aiValue,
      ms.getHumanDeck().size(),
      ms.getAiDeck().size(),
      nextView
    );
  }

  private double getAttributeValue(DinoCard card, String attribute) {
    return switch (attribute.toLowerCase()) {
      case "lifespan" -> opt(card.lifespanYears);
      case "length" -> opt(card.lengthM);
      case "speed" -> opt(card.speedKmh);
      case "intelligence" -> opt(card.intelligence);
      case "attack" -> opt(card.attack);
      case "defense" -> opt(card.defense);
      default -> throw new IllegalArgumentException(
        "Unknown attribute: " + attribute
      );
    };
  }
}
