package com.example.backend.controller;

import com.example.backend.model.Dinosaur;
import com.example.backend.repository.DinosaurRepository;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dinosaurs")
@CrossOrigin
public class DinosaurController {

  private final DinosaurRepository repo;

  public DinosaurController(DinosaurRepository repo) {
    this.repo = repo;
  }

  @GetMapping
  public List<Dinosaur> all() {
    return repo.findAll();
  }

  @GetMapping("/{id}")
  public Dinosaur one(@PathVariable Long id) {
    return repo.findById(id).orElseThrow();
  }

  @PostMapping
  public Dinosaur create(@RequestBody Dinosaur d) {
    return repo.save(d);
  }
}
