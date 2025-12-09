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

@Service
public class MatchService {

  private final DinosaurRepository repo;
  private final GameStateManager state;
  private final PlayerService playerService;
  private final LlmClient llmClient;
  private final LlmService llmService;

  public MatchService(
    DinosaurRepository repo,
    GameStateManager state,
    PlayerService playerService,
    LlmClient llmClient,
    LlmService llmService
  ) {
    this.repo = repo;
    this.state = state;
    this.playerService = playerService;
    this.llmClient = llmClient;
    this.llmService = llmService;
  }

  @Transactional(readOnly = true)
  public StartMatchResponse startNewMatch(Long playerId) {
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
        c.imgUrl = d.getImgUrl();
        return c;
      })
      .collect(Collectors.toList());

    // 3) Mixing & Handing out cards to human and ai
    Collections.shuffle(cards, new Random());
    var human = new ArrayDeque<DinoCard>(cards.subList(0, 16));
    var ai = new ArrayDeque<DinoCard>(cards.subList(16, 32));

    // 4) Create a new match state and store both decks in memory
    var ms = new MatchState();
    ms.setActivePlayer(null);
    // Attach starter player (if provided) to match state so we can credit wins/losses later
    ms.setPlayerId(playerId);
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
      top.defense,
      top.imgUrl
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
  public PlayCardResponse playCard(String matchId, PlayCardRequest request) {
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
    // 3) Get the chosen attribute value from human or ai
    String attribute = request.getAttribute();
    if (attribute == null || attribute.isBlank()) {
      // if it’s AI’s turn, use AI’s top card; otherwise use human’s top card
      var chooserCard = "AI".equalsIgnoreCase(ms.getActivePlayer())
        ? aiCard
        : humanCard;
      attribute = llmClient.chooseAttribute(chooserCard);
    }
    double humanValue = getAttributeValue(humanCard, request.getAttribute());
    double aiValue = getAttributeValue(aiCard, request.getAttribute());

    // 4) Compare and determine the winner
    int result = Double.compare(humanValue, aiValue);
    String winner = result > 0 ? "HUMAN" : result < 0 ? "AI" : "DRAW";

    // update whose turn is next
    ms.setActivePlayer("DRAW".equals(winner) ? ms.getActivePlayer() : winner);

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

    String finalWinner = null;

    if (ms.getHumanDeck().isEmpty() && !ms.getAiDeck().isEmpty()) {
      finalWinner = "AI";
    } else if (ms.getAiDeck().isEmpty() && !ms.getHumanDeck().isEmpty()) {
      finalWinner = "HUMAN";
    }

    // 6b) If game ends → update player record + delete match from redis
    if (finalWinner != null) {
      // remove match from storage
      state.remove(ms.getMatchId());

      // 6c) Increase win/loss (call to PlayerService) if a player is associated
      if (ms.getPlayerId() != null) {
        playerService.applyMatchResult(ms.getPlayerId(), finalWinner);
      }

      // 6d) Return final game-over response
      return new PlayCardResponse(
        finalWinner,
        humanValue,
        aiValue,
        ms.getHumanDeck().size(),
        ms.getAiDeck().size(),
        null, // no next card
        true, // gameOver=true
        finalWinner
      );
    }

    // 6) Save state back to Redis
    state.put(ms);

    // 7) Prepare next top card view
    var next = ms.getHumanDeck().peekFirst();
    var nextView = next == null
      ? null
      : new CardView(
        next.species,
        next.groupCode,
        next.lifespanYears,
        next.lengthM,
        next.speedKmh,
        next.intelligence,
        next.attack,
        next.defense,
        next.imgUrl
      );

    // 8) Return the result
    return new PlayCardResponse(
      winner,
      humanValue,
      aiValue,
      ms.getHumanDeck().size(),
      ms.getAiDeck().size(),
      nextView,
      false,
      null
    );
  }

  @Transactional(readOnly = true)
  public String suggestAttribute(String matchId) {
    var ms = state.get(UUID.fromString(matchId));
    if (ms == null) {
      throw new IllegalArgumentException("Invalid match ID: " + matchId);
    }
    var card = ms.getHumanDeck().peekFirst();
    if (card == null) {
      throw new IllegalStateException("No card available.");
    }
    // Use the LLM to pick an attribute
    return llmService.pickAttribute();
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
