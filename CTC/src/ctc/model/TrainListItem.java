package ctc.model;

import javafx.collections.ObservableList;
import traincontroller.model.TrainController;
import traincontroller.model.TrainControllerFactory;

/**
 * This class is used to map the train instances to their routes.
 */
public class TrainListItem {

  private TrainController train;
  private ObservableList<TrainStopRow> schedule;
  private String name;
  private String departure;
  private boolean isDispatched;
  private double speed;
  private int passengers;
  private double authority;
  private double distanceTravelled;
  private String location;
  private String track;
  // private Block location;
  // private Route route;

  /**
   * Base constructor for the TrainList items.
   * @param departure String for initial departure time of train
   * @param id String for name of train
   * @param line String for name of line train is running on
   */
  public TrainListItem(
      String id,
      String departure,
      String line,
      ObservableList<TrainStopRow> schedule) {
    this.train = (TrainController) TrainControllerFactory.createTrainController(id, line);
    this.name = id;
    this.departure = departure;
    this.schedule = schedule;
    this.isDispatched = false;
    this.speed = 0.0;
    this.passengers = 0;
    this.authority = 0;
    this.location = "A1";
  }

  public TrainController getTrain() {
    return train;
  }

  public ObservableList<TrainStopRow> getSchedule() {
    return schedule;
  }

  public void setSchedule(ObservableList<TrainStopRow> schedule) {
    this.schedule = schedule;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDeparture() {
    return departure;
  }

  public void setDeparture(String departure) {
    this.departure = departure;
  }

  public boolean isDispatched() {
    return isDispatched;
  }

  public void setDispatched(boolean dispatched) {
    isDispatched = dispatched;
  }

  public double getSpeed() {
    return speed;
  }

  public void setSpeed(double speed) {
    this.speed = speed;
  }

  public int getPassengers() {
    return passengers;
  }

  public void setPassengers(int passengers) {
    this.passengers = passengers;
  }

  public double getAuthority() {
    return authority;
  }

  public void setAuthority(double authority) {
    this.authority = authority;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getTrack() {
    return track;
  }

  public void setTrack(String track) {
    this.track = track;
  }
}
