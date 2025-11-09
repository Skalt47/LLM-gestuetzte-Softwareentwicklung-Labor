package com.example.backend.service;
import java.io.Serializable;

public class DinoCard implements Serializable{

  public long dinosaurId;
  public String species;
  public String groupCode;
  public int lifespanYears;
  public double lengthM;
  public double speedKmh;
  public int intelligence;
  public int attack;
  public int defense;
}
