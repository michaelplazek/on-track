package mbo.model;

public class TrainInfoItem {
  private String trainId;
  private String line;
  private double coordinates;
  private int passengerCount;
  private double velocity;
  private double authority;
  private double safeBrakeDist;

  /**
   * Constructor for train information.
   *
   * @param trainId name of train
   * @param line line that train occupies
   * @param coordinates GPS coordinates of train
   * @param passengerCount number of passengers on the train
   * @param velocity velocity at which train travels in meters/second
   * @param authority calculated safe authority of the train in yards based on speed of the train
   * @param safeBrakeDist safe braking distance of the train in meters
   **/

  public TrainInfoItem(String trainId, String line, double coordinates,
                       int passengerCount, double velocity, double authority,
                       double safeBrakeDist) {
    this.trainId = trainId;
    this.line = line;
    this.coordinates = coordinates;
    this.passengerCount = passengerCount;
    this.velocity = velocity;
    this.authority = authority;
    this.safeBrakeDist = safeBrakeDist;
  }

  public String getTrainId() {
    return trainId;
  }

  public void setTrainId(String trainId) {
    this.trainId = trainId;
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
