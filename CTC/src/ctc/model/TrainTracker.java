package ctc.model;

import javafx.collections.ObservableList;
import trackmodel.model.Block;
import trackmodel.model.Track;
import traincontroller.model.TrainControllerFactory;

/**
 * This class is used to map the train instances to their routes.
 */
public class TrainTracker {

  private ObservableList<ScheduleRow> schedule;
  private Track track;
  private String id;
  private String departure;
  private boolean isDispatched;
  private float speed;
  private int passengers;
  private float authority;
  private float distanceTravelled;
  private String line;
  private Block location;
  private String locationId;
  private Route route;

  /**
   * Default constructor.
   */
  public TrainTracker(){}

  /**
   * Constructor for the TrainList items.
   * @param departure String for initial departure time of train
   * @param id String for name of train
   * @param line String for name of line train is running on
   */
  public TrainTracker(
      String id,
      String departure,
      String line,
      ObservableList<ScheduleRow> schedule) {
    this.id = id;
    this.departure = departure;
    this.schedule = schedule;
    this.isDispatched = false;
    this.speed = 0;
    this.passengers = 0;
    this.authority = 0;
    this.line = line;
    this.track = Track.getListOfTracks().get(line);
    // this.location = track.getStartBlock();
    // this.locationId = location.getSection() + location.getNumber();
    this.route = new Route();

    TrainControllerFactory.create(id, line);
  }

  public ObservableList<ScheduleRow> getSchedule() {
    return schedule;
  }

  public void setSchedule(ObservableList<ScheduleRow> schedule) {
    this.schedule = schedule;
  }

  public String getId() {
    return id;
  }

  public String getDeparture() {
    return departure;
  }

  public boolean isDispatched() {
    return isDispatched;
  }

  public void setDispatched(boolean dispatched) {
    isDispatched = dispatched;
  }

  public float getSpeed() {
    return speed;
  }

  public void setSpeed(float speed) {
    this.speed = speed;
  }

  public int getPassengers() {
    return passengers;
  }

  public void updatePassengers(int passengers) {
    this.passengers += passengers;
  }

  public float getAuthority() {
    return authority;
  }

  public void setAuthority(float authority) {
    this.authority = authority;
  }

  public Block getLocation() {
    return location;
  }

  /**
   * Sets the location of the train.
   * @param location Block object
   */
  public void setLocation(Block location) {

    this.location = location;
    this.locationId = location.getSection() + location.getNumber();
  }

  public Track getTrack() {
    return track;
  }

  public void setTrack(Track track) {
    this.track = track;
  }

  public float getDistanceTravelled() {
    return distanceTravelled;
  }

  public void setDistanceTravelled(float distanceTravelled) {
    this.distanceTravelled = distanceTravelled;
  }

  public String getLine() {
    return line;
  }

  public void setLine(String line) {
    this.line = line;
  }

  public Route getRoute() {
    return route;
  }

  public void setRoute(Route route) {
    this.route = route;
  }
}