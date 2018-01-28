package ctc.model;

public class TrainDispatchRow {

  private String train;
  private String location;
  private double authority;
  private double speed;
  private int passengers;

  /**
   * Full constructor for the TrainDispatchRow class. To be used when populating
   * rows of the dispatch table in the CTC.
   * @param train String that defines the train's name
   * @param location String that defines the current block or station of the train
   * @param authority double that defines the max distance the train is allowed to travel
   * @param speed double that defines the current actual speed of the train
   * @param passengers int that defines the total number of passengers currently
   *                   onboard the train
   */
  public TrainDispatchRow(
      String train, String location, double authority, double speed, int passengers) {
    this.train = train;
    this.location = location;
    this.authority = authority;
    this.speed = speed;
    this.passengers = passengers;
  }

  public String getTrain() {
    return train;
  }

  public void setTrain(String train) {
    this.train = train;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getAuthority() {
    return Double.toString(authority);
  }

  public void setAuthority(double authority) {
    this.authority = authority;
  }

  public String getSpeed() {
    return Double.toString(speed);
  }

  public void setSpeed(double speed) {
    this.speed = speed;
  }

  public String getPassengers() {
    return Integer.toString(passengers);
  }

  public void setPassengers(int passengers) {
    this.passengers = passengers;
  }
}
