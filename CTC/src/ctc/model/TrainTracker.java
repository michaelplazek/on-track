package ctc.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import mainmenu.Clock;
import trackctrl.model.TrackControllerLineManager;
import trackmodel.model.Block;
import trackmodel.model.Switch;
import trackmodel.model.Track;
import traincontroller.model.TrainControllerFactory;
import utils.general.Authority;
import utils.unitconversion.UnitConversions;

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
  private boolean isWaitingForAuthority;
  private boolean isDone;
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

    // update the user interface
    updateDisplay();

    if (!isStopped && !isWaitingForAuthority) {

      // update the position of the train
      updatePosition();

      // update the speed and authority
      updateTrackSignals();

      // see if the train has completed its run
      updateLifecycle();

    } else {
      currentDwell = currentDwell - clock.getChangeInTime();
      if (currentDwell < 0) {
        isStopped = false;
        updateTrackSignals();
      }
    }
  }

  private void updateDisplay() {
    computeDisplayAuthority();
    computeDisplayLocation();
  }

  private void updatePosition() {

    if (controller.getOccupancy(route.getNext().getNumber())) {
      this.location = route.getNext();
      this.route.incrementCurrentIndex();
    }

    Block next = route.getNext();

    if (next != null && next.isOccupied() && checkNeighboringTrains()) {
      this.location = next;
      this.route.incrementCurrentIndex();
    }
  }

  /**
   * To checks blocks around a train before setting the position. Used to detect trains
   * that are on neighboring blocks.
   * @return true is block is OK to move to.
   */
  private boolean checkNeighboringTrains() {

    List<TrainTracker> trains = ctc.getTrainList();
    for (TrainTracker train : trains) {
      if (train.getLocation() == route.getNext()) {
        return false;
      }
    }

    return true;
  }

  private void updateLifecycle() {

    // check if train has reached the yard
    if (route.getCurrent()  == route.getLast()) {
      isDispatched = false;
      isDone = true;

      if (route.getLast().getNumber() == -1) {
        route.getLast().setOccupied(false);
      } else {
        isWaitingForAuthority = true;
      }
    }
  }

  private void updateTrackSignals() {

    ScheduleRow stop = route.getNextStop();
    String nextStationOnSchedule = stop != null ? stop.getStop() : null;

    // check to see if we have reached a station
    if (nextStationOnSchedule != null
        && (location.isLeftStation() || location.isRightStation())
        && location.getStationName().compareTo(nextStationOnSchedule) == 0) {
      isStopped = true;
      currentDwell = convertTimeToMilliseconds(stop.getDwell());
      route.incrementNextStationIndex();
    } else if (location == route.getLast()) {
      isWaitingForAuthority = true;
    }

    // when we reach a switch, we check the next fork
    if (location.isSwitch()) {
//      speed = route.getNextDirection() ? (-1 * speed) : speed;
      speed = location.getSpeedLimit();

    } else {
      speed = location.getSpeedLimit();
    }

    // determine next authority
    String nextStationOnRoute = route.getNextStation();
    if (!isStopped) {
      if ((nextStationOnSchedule != null && nextStationOnSchedule.compareTo("") != 0)
          && nextStationOnRoute != null) {

        if (nextStationOnRoute.compareTo(nextStationOnSchedule) == 0) {
          authority = Authority.STOP_AT_NEXT_STATION;
        } else {
          authority = Authority.SEND_POWER;
        }
      } else if (((route.getSize() - 1) - route.getCurrentIndex() < 3)
          && route.getLast().getNumber() != -1) {
        authority = Authority.STOP_IN_THREE_BLOCKS;
      } else {
        authority = Authority.SEND_POWER;
      }
    } else {
      authority = Authority.SERVICE_BRAKE_STOP;
    }

    controller.sendTrackSignals(location.getNumber(), authority, speed);
  }

  private void computeDisplayLocation() {
    this.locationId = location.getSection() + location.getNumber();
  }

  private void computeDisplayAuthority() {

    String stop = route.getNextStop() != null ? route.getNextStop().getStop() : null;
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

  /**
   * Give the train a new route.
   * @param route new Route
   */
  public void setRoute(Route route) {
    this.route = route;

    updateDisplay();
  }

  public Schedule getSchedule() {
    return schedule;
  }

  public void setSchedule(Schedule schedule) {
    this.schedule = schedule;
  }

  /**
   * Sets the location of the train.
   * @param location Block object
   */
  public void setLocation(Block location) {

    this.location = location;
    this.locationId = location.getSection() + location.getNumber();
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
    return Math.abs(speed);
  }

  public void setSpeed(float speed) {
    this.speed = speed;
  }

  public String getDisplaySpeed() {
    return String.format("%.1f", (Math.abs(speed) * (float) UnitConversions.KPH_TO_MPH));
  }

  public boolean isStopped() {
    return isStopped;
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

  boolean isDone() {
    return isDone;
  }

  public Route getRoute() {
    return route;
  }

  public void setWaitingForAuthority(boolean waitingForAuthority) {
    isWaitingForAuthority = waitingForAuthority;
  }
}
