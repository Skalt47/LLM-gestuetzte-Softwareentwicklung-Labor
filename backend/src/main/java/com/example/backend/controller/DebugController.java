package com.example.backend.controller;

import com.example.backend.service.LlmService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Debug-only endpoints for testing LLM integration.
 * These endpoints are NOT part of the main game API.
 * Remove this controller for production.
 */
@RestController
@RequestMapping("/debug")
public class DebugController {

  private final RestTemplate restTemplate;
  private final LlmService llmService;

  public DebugController(RestTemplate restTemplate, LlmService llmService) {
    this.restTemplate = restTemplate;
    this.llmService = llmService;
  }

  /**
   * Test direct connection to Ollama API
   */
  @GetMapping("/llm-test")
  public Map<String, Object> testLLM() {
    try {
      String prompt = "Pick a number: 1, 2, 3, 4, 5, or 6. Reply with only the number.";

      Map<String, Object> requestBody = new java.util.HashMap<>();
      requestBody.put("model", "phi3:mini");
      requestBody.put("prompt", prompt);
      requestBody.put("stream", false);

      org.springframework.http.HttpEntity<Map<String, Object>> entity = new org.springframework.http.HttpEntity<>(
          requestBody);

      ResponseEntity<Map> response = restTemplate.postForEntity(
          "http://localhost:11434/api/generate",
          entity,
          Map.class);

      return response.getBody();
    } catch (Exception e) {
      Map<String, Object> errorMap = new java.util.HashMap<>();
      errorMap.put("error", e.getMessage());
      errorMap.put("errorType", e.getClass().getSimpleName());
      return errorMap;
    }
  }

  /**
   * Test the LlmService attribute picker
   */
  @GetMapping("/llm-pick-attribute")
  public Map<String, String> testLlmPickAttribute() {
    try {
      String attribute = llmService.pickAttribute();
      return Map.of("attribute", attribute, "status", "success");
    } catch (Exception e) {
      return Map.of("error", e.getMessage(), "status", "failed");
    }
  }

  /**
   * Test LLM multiple times to see variance
   */
  @GetMapping("/llm-pick-multiple")
  public Map<String, Object> testLlmPickMultiple() {
    try {
      var picks = new java.util.ArrayList<String>();
      for (int i = 0; i < 3; i++) {
        picks.add(llmService.pickAttribute());
      }
      return Map.of("picks", picks, "status", "success");
    } catch (Exception e) {
      return Map.of("error", e.getMessage(), "status", "failed");
    }
  }
}
