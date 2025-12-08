package com.example.backend.repository;

import com.example.backend.model.Dinosaur;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

// Connect to Database for CRUD. Work with the Dinosaur entity, which has a primary key of type Long
public interface DinosaurRepository extends JpaRepository<Dinosaur, Long> {
  // Finding a Dinosaur by its species
  Optional<Dinosaur> findBySpecies(String species);
}
