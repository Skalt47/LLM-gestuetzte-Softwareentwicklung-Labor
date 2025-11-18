package com.example.backend.service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DinoImageGenerationService {

    private static final String DEFAULT_HF_API_URL = "https://router.huggingface.co/hf-inference/models/stabilityai/stable-diffusion-xl-base-1.0";

    @Value("${MODEL_API_URL:}")
    private String modelApiUrl;

    @Value("${API_KEY:}")
    private String apiKey;

    // Advanced parameters for better quality
    private static final int NUM_INFERENCE_STEPS = 45;
    private static final double GUIDANCE_SCALE = 7.5;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateImage(String prompt, String fileName) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        String apiUrl = (modelApiUrl != null && !modelApiUrl.isBlank()) ? modelApiUrl : DEFAULT_HF_API_URL;
        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("HF API key is not set. Set HF_API_KEY in environment or .env");
        }

        // Add parameters to improve image quality
        Map<String, Object> parameters = Map.of(
            "num_inference_steps", NUM_INFERENCE_STEPS,
            "guidance_scale", GUIDANCE_SCALE
        );

        Map<String, Object> jsonMap = Map.of(
            "inputs", prompt,
            "parameters", parameters
        );

        String jsonBody = objectMapper.writeValueAsString(jsonMap);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl))
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();

        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        if (response.statusCode() != 200) {
            throw new RuntimeException("HF API returned: " + response.statusCode() + " - " + new String(response.body()));
        }

        Path path = Path.of("src/main/resources/images/" + fileName + ".png");
        Files.createDirectories(path.getParent());
        Files.write(path, response.body());

        return "/images/" + fileName + ".png"; // URL für DB
    }
}
