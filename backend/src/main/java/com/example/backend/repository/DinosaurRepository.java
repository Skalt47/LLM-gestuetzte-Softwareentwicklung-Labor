package com.example.backend.repository;

import com.example.backend.model.DinosaurModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

// Connect to Database for CRUD. Work with the Dinosaur entity, which has a primary key of type Long
public interface DinosaurRepository extends JpaRepository<DinosaurModel, Long> {
  // Finding a Dinosaur by its species
  Optional<DinosaurModel> findBySpecies(String species);
}
