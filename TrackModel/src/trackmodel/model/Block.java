package trackmodel.model;

import ctc.model.CentralTrafficControl;

import java.util.Random;

import utils.general.Authority;


public class Block {

  //ID
  private String line;
  private String section;
  private int number;

  //Parameters
  private float size; //in meters
  private float grade;
  private int speedLimit;
  private float elevation;
  private float cumElevation;
  private int temp;
  private int passengersWaiting;
  private int numberToExit;

  //Infrastructure
  private String stationName = "";
  private boolean leftStation;
  private boolean rightStation;
  private boolean underground;
  private boolean isSwitch;
  private boolean isCrossing;
  private boolean crossingStatus;
  private boolean isHeated;

  //Failures
  private boolean brokenRailStatus;
  private boolean powerStatus;
  private boolean trackCircuitStatus;
  private boolean isClosedForMaintenance;

  //Track Circuit
  private boolean isOccupied;

  //Track Signal
  private float setPointSpeed;
  private Authority authority;
  private boolean hasBeacon;
  private Beacon blockBeacon;
  
  //Neighbors
  private boolean isBiDirectional;
  private int previous;
  private int nextBlock1;

  public Block() {

  }

  /**
   *Parse String and set block data.
   *@param line The string that indicates the line
   *@param section This string that indicates the section
   *@param number This string indicates the number for the block
   *@param length The length of a block
   *@param grade The grade of a block
   *@param speedLimit The speed limit on a block
   *@param infrastructure The special indicators for a block type
   *@param elevation The elevation of a block
   *@param cumElevation The cumulative elevation of a block
   *@param isBiDirectional This indicates if a block is bidirectional
   *@param previous This indicates the block prior to this one
   *@param next1 This indicates the next logical block for the track after the current block
   *@param leftStation This indicates that the station is on the left of the track
   *@param rightStation This indicates that the station is on the right of the track
   *@param blockBeacon This is the beacon stored on the track
   */
  public Block(String line, String section, int number, float length,
               float grade, int speedLimit, String infrastructure, float elevation,
               float cumElevation, boolean isBiDirectional, int previous, int next1,
               boolean leftStation, boolean rightStation, Beacon blockBeacon, int temp) {

    setLine(line);
    setSection(section);
    setNumber(number);
    setSize(length);
    setGrade(grade);
    setSpeedLimit(speedLimit);
    setStationName("");
    setUnderground(false);
    setSwitchHere(false);
    setCrossing(false);
    setInfrastructure(infrastructure);
    setElevation(elevation);
    setCumElevation(cumElevation);
    setBiDirectional(isBiDirectional);
    setPreviousBlock(previous);
    setNextBlock1(next1);
    setLeftStation(leftStation);
    setRightStation(rightStation);
    setBeaconValues(blockBeacon);
    setTemperature(temp);
    setRandomPassengers();
    this.numberToExit = 0;
  }

  public void setTemperature(int temp) {
    this.temp = temp;
  }

  public int getTemperature() {
    return this.temp;
  }

  public boolean isFreezing() {
    return this.temp <= 38;
  }

  public String getLine() {
    return line;
  }

  public void setLine(String line) {
    this.line = line;
  }

  public String getSection() {
    return section;
  }

  public void setSection(String section) {
    this.section = section;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public float getSize() {
    return size;
  }

  public void setSize(float size) {
    this.size = size;
  }

  public float getGrade() {
    return grade;
  }

  public void setGrade(float grade) {
    this.grade = grade;
  }

  public int getSpeedLimit() {
    return speedLimit;
  }

  public void setSpeedLimit(int speedLimit) {
    this.speedLimit = speedLimit;
  }

  public float getElevation() {
    return elevation;
  }

  public void setElevation(float elevation) {
    this.elevation = elevation;
  }

  public float getCumElevation() {
    return cumElevation;
  }

  public void setCumElevation(float cumElevation) {
    this.cumElevation = cumElevation;
  }

  /**
   * This method will set the various infrastructure field for the block.
   * @param infrastructure A string with data about the block.
   */
  public void setInfrastructure(String infrastructure) {
    String[] parts = infrastructure.split(";");

    for (int i = 0; i < parts.length; i++) {
      if (parts[i].equals("STATION")) {
        setStationName(parts[i + 1]);
      } else if (parts[i].contains("RAILWAY CROSSING")) {
        setCrossing(true);
        setCrossingStatus(false);
      } else if (parts[i].contains("UNDERGROUND")) {
        setUnderground(true);
      } else if (parts[i].equals("SWITCH")) {
        setSwitchHere(true);
      } else if (parts[i].contains("SWITCH")
          && (parts[i].contains("TO")
          || parts[i].contains("FROM"))
          && parts[i].contains("YARD")) {
        setSwitchHere(true);
      }
    }
  }

  /**
   * This will be called by the TrainModel when it arrives at a station.
   * @return number of passengers added to train
  **/
  public int getPassengers(int availableSeats) {
    Random randomPassengers = new Random();
    int passengers = randomPassengers.nextInt(availableSeats);

    if (passengers > passengersWaiting) {
      passengers = passengersWaiting;
    }

    this.passengersWaiting = this.passengersWaiting - passengers;

    // set passengers for train in CTC
    CentralTrafficControl.getInstance().addPassengers(this, passengers);

    return passengers; // return number of passenger to TrainModel
  }

  public int getPassengersWaiting() {
    return passengersWaiting;
  }

  /**
   * This method will set the number of passengers at a station.
   */
  public void setRandomPassengers() {
    Random randomPassengers = new Random();
    int passengers = randomPassengers.nextInt(200) + 100;

    if (this.stationName.equals("")) {
      passengers = 0;
    }
    this.passengersWaiting = passengers;
  }

  public int getNumberToExit() {
    return this.numberToExit;
  }

  public void setNumberToExit(int numberToExit) {
    this.numberToExit += numberToExit;
  }

  public String getStationName() {
    return stationName;
  }

  public void setStationName(String stationName) {
    this.stationName = stationName;
  }

  public boolean isLeftStation() {
    return leftStation;
  }

  public void setLeftStation(boolean leftStation) {
    this.leftStation = leftStation;
  }

  public boolean isRightStation() {
    return rightStation;
  }

  public void setRightStation(boolean rightStation) {
    this.rightStation = rightStation;
  }

  public boolean isUnderground() {
    return underground;
  }

  public void setUnderground(boolean underground) {
    this.underground = underground;
  }

  public boolean isSwitch() {
    return isSwitch;
  }

  public void setSwitchHere(boolean isSwitch) {
    this.isSwitch = isSwitch;
  }

  public boolean isCrossing() {
    return isCrossing;
  }

  public void setCrossing(boolean isCrossing) {
    this.isCrossing = isCrossing;
  }

  public boolean getCrossingStatus() {
    return crossingStatus;
  }

  public void setCrossingStatus(boolean crossingStatus) {
    this.crossingStatus = crossingStatus && isCrossing;
  }

  public boolean isHeated() {
    return isHeated;
  }

  public void setHeated(boolean isHeated) {
    this.isHeated = isHeated;
  }

  public boolean getBrokenRailStatus() {
    return brokenRailStatus;
  }

  public void setBrokenRailStatus(boolean brokenRailStatus) {
    this.brokenRailStatus = brokenRailStatus;
  }

  public boolean getPowerStatus() {
    return powerStatus;
  }

  public void setPowerStatus(boolean powerStatus) {
    this.powerStatus = powerStatus;
  }

  public boolean getTrackCircuitStatus() {
    return trackCircuitStatus;
  }

  public void setTrackCircuitStatus(boolean trackCircuitStatus) {
    this.trackCircuitStatus = trackCircuitStatus;
  }

  public boolean isClosedForMaintenance() {
    return isClosedForMaintenance;
  }

  public void setClosedForMaintenance(boolean closedForMaintenance) {
    this.isClosedForMaintenance = closedForMaintenance;
  }

  public boolean isOccupied() {
    return isOccupied;
  }

  public void setOccupied(boolean isOccupied) {
    this.isOccupied = isOccupied;
  }

  public float getSetPointSpeed() {
    return setPointSpeed;
  }

  public void setSetPointSpeed(float setPointSpeed) {
    this.setPointSpeed = setPointSpeed;
  }

  public Authority getAuthority() {
    return authority;
  }

  public void setAuthority(Authority authority) {
    this.authority = authority;
  }

  public boolean hasBeacon() {
    return hasBeacon;
  }

  public Beacon getBeacon() {
    return blockBeacon;
  }

  public void setBeacon(boolean hasBeacon) {
    this.hasBeacon = hasBeacon;
  }

  public boolean isBiDirectional() {
    return isBiDirectional;
  }

  public void setBiDirectional(boolean biDirectional) {
    this.isBiDirectional = biDirectional;
  }

  public int getPreviousBlock() {
    return previous;
  }

  public void setPreviousBlock(int previous) {
    this.previous = previous;
  }

  public int getNextBlock1() {
    return nextBlock1;
  }

  public void setNextBlock1(int nextBlock1) {
    this.nextBlock1 = nextBlock1;
  }

  public Beacon getBeaconValues() {
    return blockBeacon;
  }

  /**
   * This method will set up the beacon on the block.
   * @param blockBeacon The beacon object for the block.
   */
  public void setBeaconValues(Beacon blockBeacon) {
    if (blockBeacon == null) {
      setBeacon(false);
    } else {
      setBeacon(true);
    }

    this.blockBeacon = blockBeacon;
  }

  public String getUiData() {
    return null;
  }
}
