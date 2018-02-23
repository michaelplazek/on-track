package mbo.model;

public class TrainInfoRow {
  private String trainID;
  private String line;
  private double coordinates;
  private int passengerCount;
  private double velocity;
  private double authority;
  private double safeBrakeDist;

  public TrainInfoRow(String trainID, String line,double coordinates,int passengerCount,double velocity,double authority, double safeBrakeDist) {
    this.trainID = trainID;
    this.line = line;
    this.coordinates = coordinates;
    this. passengerCount = passengerCount;
    this.velocity = velocity;
    this.authority = authority;
    this.safeBrakeDist = safeBrakeDist;
  }

  public void setTrainID(String trainID) {
    this.trainID = trainID;
  }

  public void setLine(String line) {
    this.line = line;
  }

  public void setCoordinates(double coordinates) {
    this.coordinates = coordinates;
  }

  public void setPassengerCount(int passengerCount) {
    this.passengerCount = passengerCount;
  }

  public void setVelocity(double velocity) {
    this.velocity = velocity;
  }

  public void setAuthority(double authority) {
    this.authority = authority;
  }

  public void setSafeBrakeDist(double safeBrakeDist) {
    this.safeBrakeDist = safeBrakeDist;
  }

  public String getTrainID() {
    return trainID;
  }

  public String getLine() {
    return line;
  }

  public int getPassengerCount() {
    return passengerCount;
  }

  public double getCoordinates() {
    return coordinates;
  }

  public double getSafeBrakeDist() {
    return safeBrakeDist;
  }

  public double getVelocity() {
    return velocity;
  }

  public double getAuthority() {
    return authority;
  }

}
