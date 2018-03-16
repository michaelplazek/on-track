package trackmodel.model;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

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
  private boolean closedForMaintenance;

  //Track Circuit
  private boolean isOccupied;

  //Track Signal
  private float setPointSpeed;
  private float authority;
  private boolean beacon;
  
  //Neighbors
  private boolean biDirectional;
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
   *@param biDirectional This indicates if a block is bidirectional
   *@param previous This indicates the block prior to this one
   *@param next1 This indicates the next logical block for the track after the current block
   *@param leftStation This indicates that the station is on the left of the track
   *@param rightStation This indicates that the station is on the right of the track
   */
  public Block(String line, String section, String number, float length,
               float grade, int speedLimit, String infrastructure, float elevation,
               float cumElevation, boolean biDirectional, int previous, int next1,
               boolean leftStation, boolean rightStation) {

    setLine(line);
    setSection(section);
    setNumber(Integer.parseInt(number));
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
    setBiDirectional(biDirectional);
    setPreviousBlock(previous);
    setNextBlock1(next1);
    setLeftStation(leftStation);
    setRightStation(rightStation);

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
      } else if (parts[i].equals("RAILWAY CROSSING")) {
        setCrossing(true);
        setCrossingStatus(false);
      } else if (parts[i].equals("UNDERGROUND")) {
        setUnderground(true);
      } else if (parts[i].equals("SWITCH")) {
        setSwitchHere(true);
      }
    }
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
    return closedForMaintenance;
  }

  public void setClosedForMaintenance(boolean closedForMaintenance) {
    this.closedForMaintenance = closedForMaintenance;
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

  public float getAuthority() {
    return authority;
  }

  public void setAuthority(float authority) {
    this.authority = authority;
  }

  public boolean isBeacon() {
    return beacon;
  }

  public void setBeacon(boolean beacon) {
    this.beacon = beacon;
  }

  public boolean isBiDirectional() {
    return biDirectional;
  }

  public void setBiDirectional(boolean biDirectional) {
    this.biDirectional = biDirectional;
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

  /**
   * This method will display output for the track model and a block's status.
   * @return A String with the output shall be returned
   */
  public String toDisplay() {
    String output = "<html><body style='width: 500px;vertical-align: text-top'>";
    output += "<b>ID</b><br>&#09;Line: " + this.line
        + "<br>&#09;Section: " + this.section
        + "<br>&#09;Number: " + this.number
        + "<br><br><b>Parameters</b><br>&#09;Length: " + this.size
        + "<br>&#09;Grade: " + this.grade
        + "<br>&#09;Speed Limit: " + this.speedLimit
        + "<br>&#09;Elevation: " + this.elevation
        + "<br>&#09;Cum Elevation: " + this.cumElevation
        + "<br><br><b>Infrastructure</b><br>&#09;Train Present: " + this.isOccupied
        + "<br>&#09;isHeated: " + this.isHeated;


    output += "<br><br><b>Failures</b><br>&#09;Broken Rail: " + this.brokenRailStatus
        + "<br>&#09;Track Circuit Failure: " + this.powerStatus
        + "<br>&#09;Power Failure: " + this.trackCircuitStatus;

    output += "</body></html>";
    return output;
  }

  /**
   * This method will return the block as a string object.
   * @return A string with block data will be returned
   */
  public String toString() {
    String output;
    output = "\tID\n\t\tLine: " + this.line
        + "\n\t\tSection: " + this.section
        + "\n\t\tNumber: " + this.number
        + "\n\tParameters\n\t\tLength: " + this.size
        + "\n\t\tGrade: " + this.grade
        + "\n\t\tSpeed Limit: " + this.speedLimit
        + "\n\t\tElevation: " + this.elevation
        + "\n\t\tCum Elevation: " + this.cumElevation
        + "\n\tInfrastructure\n\t\tTrain Present: " + this.isOccupied
        + "\n\t\tisHeated: " + this.isHeated;

    output += "\n\tFailures\n\t\tBroken Rail: " + this.brokenRailStatus
        + "\n\t\tTrack Circuit Failure: " + this.powerStatus
        + "\n\t\tPower Failure: " + this.trackCircuitStatus;

    return output;

  }
}
