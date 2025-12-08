package com.example.backend.model;

import jakarta.persistence.*;

@Entity
@Table(
  name = "dinosaurs",
  uniqueConstraints = {
    @UniqueConstraint(name = "uq_dinosaurs_species", columnNames = "species"),
    @UniqueConstraint(
      name = "uq_dinosaurs_group_code",
      columnNames = "group_code"
    ),
  }
)
public class Dinosaur {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "image_url")
  private String imgUrl;

  @Column(nullable = false)
  private String species; // "Tyrannosaurus rex"

  @Column(name = "group_code", nullable = false, length = 4)
  private String groupCode; // "1A"

  @Column(name = "lifespan_years")
  private Integer lifespanYears;

  @Column(name = "length_m")
  private Double lengthM;

  @Column(name = "speed_kmh")
  private Integer speedKmh;

  private Integer intelligence;
  private Integer attack;
  private Integer defense;

  // Getters
  public String getImageUrl() {
    return imgUrl;
  }

  public Long getId() {
    return id;
  }

  public String getSpecies() {
    return species;
  }

  public String getGroupCode() {
    return groupCode;
  }

  public Integer getLifespanYears() {
    return lifespanYears;
  }

  public Double getLengthM() {
    return lengthM;
  }

  public Integer getSpeedKmh() {
    return speedKmh;
  }

  public Integer getIntelligence() {
    return intelligence;
  }

  public Integer getAttack() {
    return attack;
  }

  public Integer getDefense() {
    return defense;
  }

  public String getImgUrl(){
    return imgUrl;
  }

  // Setter for imgUrl
  public void setImageUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }
}
