package com.example.backend.controller;

import com.example.backend.model.DinosaurModel;
import com.example.backend.repository.DinosaurRepository;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.lang.NonNull;

@RestController
@RequestMapping("/api/dinosaurs")
@CrossOrigin
public class DinosaurController {

  private final DinosaurRepository repo;

  public DinosaurController(DinosaurRepository repo) {
    this.repo = repo;
  }

  @GetMapping
  public List<DinosaurModel> all() {
    return repo.findAll();
  }

  @GetMapping("/{id}")
  public DinosaurModel one(@PathVariable @NonNull Long id) {
    return repo.findById(id).orElseThrow();
  }

}
