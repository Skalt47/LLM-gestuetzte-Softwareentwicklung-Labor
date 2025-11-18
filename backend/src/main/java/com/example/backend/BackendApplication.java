package com.example.backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	private static final Logger logger = LoggerFactory.getLogger(BackendApplication.class);

	public static void main(String[] args) {
		// Load local .env into system properties for convenience during development
		try {
			Dotenv dotenv = Dotenv.configure()
					.ignoreIfMissing()
					.load();

			dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

			// Log presence of important keys without printing the secret
			if (System.getProperty("API_KEY") != null) {
				logger.info("Loaded API_KEY from .env (value not displayed)");
			} else {
				logger.debug("No API_KEY found in .env");
			}
		} catch (Exception e) {
			logger.debug("Failed to load .env (continuing without it)", e);
		}

		SpringApplication.run(BackendApplication.class, args);
	}

}
