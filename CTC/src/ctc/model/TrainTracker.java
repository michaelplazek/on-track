package ctc.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import mainmenu.Clock;
import trackctrl.model.TrackControllerLineManager;
import trackmodel.model.Block;
import trackmodel.model.Track;
import traincontroller.model.TrainControllerFactory;
import utils.general.Authority;

/**
 * This class is used to map the train instances to their routes.
 */
public class TrainTracker {

  private Schedule schedule;
  private Track track;
  private String id;
  private String departure;
  private boolean isDispatched;
  private boolean isStopped;
  private float speed;
  private int passengers;
  private double currentDwell;
  private Authority authority;
  private float distanceTravelled;
  private String line;
  private Block location;
  private String locationId;
  private Route route;
  private String displayAuthority;
  private TrackControllerLineManager controller;
  private CentralTrafficControl ctc;
  private Clock clock;

  /**
   * Default constructor.
   */
  public TrainTracker() { }

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
      Schedule schedule) {
    this.id = id;
    this.departure = departure;
    this.schedule = schedule;
    this.isDispatched = false;
    this.isStopped = false;
    this.passengers = 0;
    this.authority = Authority.SEND_POWER;
    this.currentDwell = 0;
    this.line = line;
    this.track = Track.getListOfTracks().get(line);
    this.location = track.getStartBlock();
    this.locationId = location.getSection() + location.getNumber();
    this.route = new Route(line, this);
    this.speed = location.getSpeedLimit();
    this.controller = TrackControllerLineManager.getInstance(line);
    this.ctc = CentralTrafficControl.getInstance();
    this.clock = Clock.getInstance();

    TrainControllerFactory.create(id, line);
  }

  /**
   * This is called every clock tick to update the train.
   */
  void update() {

    // see if the train has completed its run
    updateLifecycle();

    // update the user interface
    updateDisplay();

    if (!isStopped) {

      // update the position of the train
      updatePosition();

      // update the speed and authority
      updateTrackSignals();

    } else {
      currentDwell = currentDwell - clock.getChangeInTime();
      if (currentDwell < 0) {
        isStopped = false;
      }
    }
  }

  private void updateDisplay() {
    computeDisplayAuthority();
    computeDisplayLocation();
  }

  private void updatePosition() {

    // TODO: use this call once the Track Controller is connected
//    if (controller.getOccupancy(route.getNext().getNumber())) {
//      this.location = route.getNext();
//      this.route.incrementCurrentIndex();
//    }

    Block next = route.getNext();

    if (next != null && next.isOccupied()) {
      this.location = next;
      this.route.incrementCurrentIndex();
    }
  }

  private void updateLifecycle() {

    // check if train has reached the yard
    if (route.getCurrent().getNumber() == -1) {
      TrainControllerFactory.delete(id);
      ctc.removeTrain(this);
    }
  }

  private void updateTrackSignals() {

    ScheduleRow stop = route.getNextStop();

    // check to see if we have reached a station
    if ((location.isLeftStation() || location.isLeftStation())
        && location.getStationName().compareTo(stop.getStop()) == 0) {
      isStopped = true;
      currentDwell = convertTimeToMilliseconds(stop.getTime());
      route.incrementNextStationIndex();
    }

    // when we reach a switch, we check the next fork
    if (location.isSwitch()) {
//      speed = route.getNextDirection() ? (-1 * speed) : speed;
      speed = location.getSpeedLimit();
    }

    String nextStationOnSchedule = stop.getStop();

    // determine next authority
    String nextStationOnRoute = route.getNextStation();
    if (!isStopped) {
      if (nextStationOnSchedule != null
          && nextStationOnRoute != null) {

        if (nextStationOnRoute.compareTo(nextStationOnSchedule) == 0) {
          authority = Authority.STOP_AT_NEXT_STATION;
        }
      } else {
        authority = Authority.SEND_POWER;
      }
    } else {
      authority = Authority.SERVICE_BRAKE_STOP;
    }

    // TODO: use this call once the Track Controller is ready
//    controller.sendTrackSignals(location.getNumber(), authority, speed);

    location.setAuthority(authority);
    location.setSetPointSpeed(speed);
  }

  private void computeDisplayLocation() {
    this.locationId = location.getSection() + location.getNumber();
  }

  private void computeDisplayAuthority() {

    String stop = route.getNextStop().getStop();
    if (stop == null || stop.compareTo("") == 0) {
      if (route.getLast().getNumber() != -1) {
        stop = route.getLast().getSection() + route.getLast().getNumber();
      } else {
        stop = "Yard";
      }
    }

    this.displayAuthority = stop;
  }

  private long convertTimeToMilliseconds(String time) {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    try {
      Date date = sdf.parse("1970-01-01 " + time);
      return date.getTime();
    } catch (ParseException e) {
      System.out.println(e);
      return 0;
    }
  }

  public Schedule getSchedule() {
    return schedule;
  }

  public void setSchedule(Schedule schedule) {
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

  void updatePassengers(int passengers) {
    this.passengers += passengers;
  }

  public String getDisplayAuthority() {
    return displayAuthority;
  }

  public Authority getAuthority() {
    return authority;
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

  public String getLocationId() {
    return locationId;
  }

  public void setLocationId(String locationId) {
    this.locationId = locationId;
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

  /**
   * Give the train a new route.
   * @param route new Route
   */
  public void setRoute(Route route) {
    this.route = route;

    updateDisplay();
  }
}
