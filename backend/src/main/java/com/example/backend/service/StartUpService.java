package com.example.backend.service;

import com.example.backend.model.Dinosaur;
import com.example.backend.repository.DinosaurRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StartUpService {

  @Autowired
  private DinosaurRepository dinosaurRepository;

  @Autowired
  private DinoImageGenerationService dinoImageService;

  @PostConstruct
  public void generateMissingDinoImages() {
    List<Dinosaur> dinos = dinosaurRepository.findAll();
    for (Dinosaur d : dinos) {
      if (d.getImageUrl() == null) {
        try {
          String prompt = "Create an image of the dinosaur species: " +
              d.getSpecies() +
              ". Style: Cute anime/cartoon with bright/ strong colors." +
              "Include characteristic features for each dinosaur and a background matching its natural habitat. Make the dinosaur easy to recognize by using some realistic charcteristic features."
              +
              "The image should be suitable for a quartett card game, similar in layout to a Pokémon card.";
          String url = dinoImageService.generateImage(
              prompt,
              "dino_" + d.getId());
          d.setImageUrl(url);
          dinosaurRepository.save(d);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }
}
