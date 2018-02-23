package mbo.model;

public class TrainInfoItem {
  private String trainID;
  private String line;
  private double coordinates;
  private int passengerCount;
  private double velocity;
  private double authority;
  private double safeBrakeDist;

  public TrainInfoItem(String trainID, String line,double coordinates,int passengerCount,double velocity,double authority, double safeBrakeDist) {
    this.trainID = trainID;
    this.line = line;
    this.coordinates = coordinates;
    this. passengerCount = passengerCount;
    this.velocity = velocity;
    this.authority = authority;
    this.safeBrakeDist = safeBrakeDist;
  }

  public String getTrainID() {
    return trainID;
  }

  public void setTrainID(String trainID) {
    this.trainID = trainID;
  }

  public String getLine() {
    return line;
  }

  public void setLine(String line) {
    this.line = line;
  }

  public double getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(double coordinates) {
    this.coordinates = coordinates;
  }

  public int getPassengerCount() {
    return passengerCount;
  }

  public void setPassengerCount(int passengerCount) {
    this.passengerCount = passengerCount;
  }

  public double getVelocity() {
    return velocity;
  }

  public void setVelocity(double velocity) {
    this.velocity = velocity;
  }

  public double getAuthority() {
    return authority;
  }

  public void setAuthority(double authority) {
    this.authority = authority;
  }

  public double getSafeBrakeDist() {
    return safeBrakeDist;
  }

  public void setSafeBrakeDist(double safeBrakeDist) {
    this.safeBrakeDist = safeBrakeDist;
  }
}
