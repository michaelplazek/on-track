package trackmodel.model;

import java.util.Random;

public class Station {

  private String name;
  private int occupancy;

  public Station(String name) {
    this.name = name;
    generateOccupancy();
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getOccupancy() {
    return occupancy;
  }

  public void generateOccupancy() {
    Random rand = new Random();
    occupancy = rand.nextInt((1100 - 1) + 1) + 1;
  }

  public void setOccupancy(int occupancy) {
    this.occupancy = occupancy;
  }

  public String toString() {
    return "Station " + name + ", Occupancy = " + occupancy + " people";
  }

}
