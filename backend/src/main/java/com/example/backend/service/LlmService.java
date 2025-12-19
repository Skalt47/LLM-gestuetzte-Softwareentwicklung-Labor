package com.example.backend.service;

import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LlmService {

  private final RestTemplate restTemplate;
  private static final String[] ATTRIBUTES = {
      "lifespan",
      "length",
      "speed",
      "intelligence",
      "attack",
      "defense",
  };

  public LlmService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  /**
   * Call the LLM to pick an attribute (1-6) and return the attribute name
   */
  public String pickAttribute() {
    try {
      String prompt = "Pick a number between 1 and 6. Reply with ONLY the number, nothing else.";

      Map<String, Object> requestBody = new java.util.HashMap<>();
      requestBody.put("model", "phi3:mini");
      requestBody.put("prompt", prompt);
      requestBody.put("stream", false);

      HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody);

      ResponseEntity<Map> response = restTemplate.postForEntity(
          "http://localhost:11434/api/generate",
          entity,
          Map.class);

      if (response.getBody() == null) {
        return ATTRIBUTES[0]; // Default to first attribute
      }

      String responseText = (String) response.getBody().get("response");
      int number = extractNumber(responseText);

      // Convert 1-6 to 0-5 index
      int index = Math.max(0, Math.min(5, number - 1));
      return ATTRIBUTES[index];
    } catch (Exception e) {
      System.err.println("LLM error: " + e.getMessage());
      return ATTRIBUTES[0]; // Fallback to first attribute
    }
  }

  /**
   * Extract the first number (1-6) from the LLM response
   */
  private int extractNumber(String text) {
    if (text == null || text.isEmpty()) {
      return 1; // Default
    }

    // Look for digits in the response
    for (char c : text.toCharArray()) {
      if (Character.isDigit(c)) {
        int num = Character.getNumericValue(c);
        if (num >= 1 && num <= 6) {
          return num;
        }
      }
    }

    return 1; // Default to 1 if no valid number found
  }
}
