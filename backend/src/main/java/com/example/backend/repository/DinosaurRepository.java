package com.example.backend.repository;

import com.example.backend.model.Dinosaur;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DinosaurRepository extends JpaRepository<Dinosaur, Long> {
  Optional<Dinosaur> findBySpecies(String species);
}
