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
  private float size;
  private float grade;
  private int speedLimit;
  private float elevation;
  private float cumElevation;

  //Infrastructure
  private String stationName = "";
  private boolean leftStation;
  private boolean rightStation;
  private boolean underground;
  private boolean switchHere;
  private boolean crossing;
  private boolean crossingStatus;
  private boolean heaters;

  //Failures
  private boolean brokenRailStatus;
  private boolean powerStatus;
  private boolean trackCircuitStatus;
  private boolean closedForMaintenance;

  //Track Circuit
  private int trainPresent;

  //Track Signal
  private boolean go;
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
   *@param leftDoors This indicates that the station is on the left of the track
   *@param rightDoors This indicates that the station is on the right of the track
   */
  public Block(String line, String section, String number, float length,
               float grade, int speedLimit, String infrastructure, float elevation,
               float cumElevation, boolean biDirectional, int previous, int next1,
               boolean leftDoors, boolean rightDoors) {

    setLine(line);
    setSection(section);
    setNumber(Integer.parseInt(number));
    setSize(length);
    setGrade(grade);
    setSpeedLimit(speedLimit);
    setInfrastructure(infrastructure);
    setElevation(elevation);
    setCumElevation(cumElevation);
    setBiDirectional(biDirectional);
    setPrevious(previous);
    setNextBlockOne(next1);
    setLeftDoors(leftDoors);
    setRightDoors(rightDoors);

  }

  /**
   * This method return the line the block is on.
   * @return the string for the line
   */
  public String getLine() {
    return line;
  }

  /**
   * This method will return the section a block is on.
   * @return A string with the block name
   */
  public String getSection() {
    return section;
  }

  /**
   * This method will return a block number.
   * @return This method will return the block number
   */
  public int getNumber() {
    return number;
  }


  /**
   * This method will return the size of a block.
   * @return This will pass back the size of a block
   */
  public float getSize() {
    return size;
  }

  /**
   * This method will return the grade of a block.
   * @return The float for the grade of a block
   */
  public float getGrade() {
    return grade;
  }

  /**
   * Pass back the speed limit of a block.
   * @return The float value for a speed limit of a block
   */
  public float getSpeedLimit() {
    return speedLimit;
  }

  /**
   * This method will return the elevation of a block.
   * @return a float with the value of elevation
   */
  public float getElevation() {
    return elevation;
  }

  /**
   * This method will return the cumulative elevation.
   * @return A float value will be returned with the cumulative elevation
   */
  public float getCumElevation() {
    return cumElevation;
  }

  /**
   * This method will return if a beacon is at the location.
   * @return A boolean value will be returned indicating if there is a beacon there.
   */
  public boolean getBeacon() {
    return beacon;
  }

  /**
   * This method will return the direction based on an integer.
   * @return An integer value will be returned to indicate the direction
   */
  public boolean getBiDirection() {
    return biDirectional;
  }

  /**
   * This method will return the next block in the track.
   * @return A integer will be returned
   */
  public int getNextBlock() {
    return nextBlock1;
  }

  /**
   * This method will return the other possible next block.
   * @return A integer will be returned
   */
  public int getPrevious() {
    return previous;
  }

  /**
   * The status of the heaters will be returned by this method.
   * @return The status will be passed as a boolean value
   */
  public boolean getHeaters() {
    return heaters;
  }


  /**
   * Return the id of any train present.
   * @return An integer identification for a train is passed back
   */
  public int getTrainPresent() {
    return trainPresent;
  }

  /**
  * This method will alert the driver if the left doors are to be opened.
  * @return A Boolean value indicating if the station is on the left
  */
  public boolean getLeftDoors() {
    if (stationName.equals("")) {
      return leftStation;
    }
    return false;
  }

  /**
  * This method will alert the driver if the left doors are to be opened.
  * @return A Boolean value indicating if the station is on the right
  */
  public boolean getRightDoors() {
    if (stationName.equals("")) {
      return rightStation;
    }
    return false;
  }

  /**
  * This method will alert the user if the track block is underground.
  * @return A Boolean value indicating if the track is underground
  */
  public boolean getUnderground() {
    return underground;
  }

  /**
  * This method will let the user know if there is a switch at this block.
  * @return A Boolean value indicating if the block is a switch
  */
  public boolean getSwitch() {
    return switchHere;
  }

  /**
  * This method will let the user know if there is a crossing here.
  * @Return A Boolean value will be passed in that indicates if this block has a crossing
  */
  public boolean getCrossing() {
    return crossing;
  }

  /**
  * This method will return the status of the crossing if there is one at this block location.
  * @return A Boolean value will be returned indicating the status of a crossing
  */
  public boolean getCrossingStatus() {
    if (crossing == true) {
      return crossingStatus;
    } else {
      return false;
    }
  }

  /**
   * This method will tell the user if they are allowed to go.
   * @return A boolean value will be passed if a train can go
   */
  public boolean getGo() {
    return go;
  }

  /**
   * This method will return the set point speed.
   * @return The value passed will be a float value
   */
  public float getSetPointSpeed() {
    return setPointSpeed;
  }

  /**
   * This method will return the authority at a  block.
   * @return The value returned will be a float with the distance
   */
  public float getAuthority() {
    return authority;
  }

  /**
   * This will tell the user if there is a failure.
   * @return The method will return a boolean if there was a failure
   */
  public boolean getFailureStatus() {
    return (brokenRailStatus || powerStatus || trackCircuitStatus || closedForMaintenance);
  }

  /**
   * This method will tell if there was a broken rail failure.
   * @return A boolean value will be returned
   */
  public boolean getBrokenRailStatus() {
    return brokenRailStatus;
  }

  /**
   * This method will tell if the block is closed for maintenance.
   * @return A boolean value will be returned
   */
  public boolean getClosedForMaintenence() {
    return closedForMaintenance;
  }

  /**
   * This method will indicate if there is a power failure.
   * @return A boolean value will be returned
   */
  public boolean getPowerStatus() {
    return powerStatus;
  }

  /**
   * This method will tell the status of a track circuit failure.
   * @return A boolean value will be returned
   */
  public boolean getTrackCircuitStatus() {
    return trackCircuitStatus;
  }

  /**
   * This method will set a new line for the block.
   * @param newLine The new line that the block is part of
   */
  public void setLine(String newLine) {
    line = new String(newLine);
  }

  /**
   * This method will set the section for the block.
   * @param newSection The string value for the section of the track
   */
  public void setSection(String newSection) {
    section = new String(newSection);
  }

  /**
   * This will set a new number of the block.
   * @param newNumber The block number that is now to be set
   */
  public void setNumber(int newNumber) {
    number = newNumber;
  }

  /**
   * This method will set the size of a block.
   * @param newSize The new size of the block
   */
  public void setSize(float newSize) {
    size = newSize;
  }

  /**
   * This method will set the grade of the block.
   * @param newGrade The new grade of the block
   */
  public void setGrade(float newGrade) {
    grade = newGrade;
  }

  /**
   * This method will set the speed limit of a block.
   * @param newSpeedLimit The new speed limit as a float
   */
  public void setSpeedLimit(int newSpeedLimit) {
    speedLimit = newSpeedLimit;
  }

  /**
   * This method will set the elevation of a block.
   * @param newElevation A float value for the new elevation
   */
  public void setElevation(float newElevation) {
    elevation = newElevation;
  }

  /**
   * This method will set the cumulative elevation of a block.
   * @param newCumElevation A float value for the new cumulative elevation
   */
  public void setCumElevation(float newCumElevation) {
    cumElevation = newCumElevation;
  }

  /**
   * This method will set if the block is bidirectional.
   * @param newBiDirectional This states wether the block is bidirectional
   */
  public void setBiDirectional(Boolean newBiDirectional) {
    biDirectional = newBiDirectional;
  }

  /**
   * This method will set the next block for the track.
   * @param newPrevious A Block will be passed in for the current block.
   */
  public void setPrevious(int newPrevious) {
    previous = newPrevious;
  }

  /**
   * This method will set the other possible next block.
   * @param newNextBlock A Block will be passed in for the other possible next block.
   */
  public void setNextBlockOne(int newNextBlock) {
    nextBlock1 = newNextBlock;
  }

  /**
   * This method will set a new line for the block.
   * @param newBeacon The new line that the block is part of
   */
  public void setBeacon(boolean newBeacon) {
    beacon = newBeacon;
  }

  /**
   * This method will setup a new infrastructure.
   * @param newInfrastructure A String will the infrastructure will be passed in
   */
  public void setInfrastructure(String newInfrastructure) {
    String[] parts = newInfrastructure.split(";");

    for (int i = 0; i < parts.length; i++) {
      if (parts[i].equals("STATION")) {
        stationName = parts[i + 1];
      } else if (parts[i].equals("RAILWAY CROSSING")) {
        crossing = true;
        crossingStatus = false;
      } else if (parts[i].equals("UNDERGROUND")) {
        underground = true;
      } else if (parts[i].equals("SWITCH")) {
        switchHere = true;
      }
    }

  }

  /**
   * This method will update the heaters.
   * @param state A boolean will be passed in for the heater status
   */
  public void setHeaters(boolean state) {
    heaters = state;
  }

  /**
   * This method will alert tha user if a track is present.
   * @param trainId The id for the track on the block should be passed in
   */
  public void setTrainPresent(int trainId) {
    trainPresent = trainId;
  }

  /**
   * This method will aler the user if they can go.
   * @param newGo A boolean will be passed in if the user can go onto the block
   */
  public void setGo(boolean newGo) {
    go = newGo;
  }

  /**
   * This method will allow the user to set a new set speed.
   * @param newSetPointSpeed A float value is passed in allowing set speed to update
   */
  public void setSetPointSpeed(float newSetPointSpeed) {
    setPointSpeed = newSetPointSpeed;
  }

  /**
   * This method will set the authority of a block.
   * @param newAuthority A float value will be passed into the method.
   */
  public void setAuthority(float newAuthority) {
    authority = newAuthority;
    System.out.println("Set NEW Authority of " + authority + " on block num " + number);
  }

  /**
   * This method will set a failure status of the various failure modes.
   */
  public void setFailureStatus() {
    Random rand = new Random();
    int failure = rand.nextInt(3);
    if (failure == 0) {
      setBrokenRailStatus(true);
    } else if (failure == 1) {
      setPowerStatus(true);
    } else if (failure == 2) {
      setTrackCircuitStatus(true);
    }
  }

  /**
  * This method will set the if the doors for a station are on the left side.
  * @param leftDoors This using a boolean indicates if the doors are on the left
  */
  public void setLeftDoors(boolean leftDoors) {
    this.leftStation = leftDoors;
  }

  /**
  * This method will set the if the doors for a station are on the right side.
  * @param rightDoors This using a boolean indicates if the doors are on the left
  */
  public void setRightDoors(boolean rightDoors) {
    this.rightStation = rightDoors;
  }

  /**
   * This method will allow for the maintenance to be updated.
   * @param status This will be the maintenance status
   */
  public void setClosedForMaintenence(boolean status) {
    closedForMaintenance = status;
  }

  /**
   * This method will allow the user to reset the status of all failure modes.
   */
  public void resetFailureStatus() {
    setBrokenRailStatus(false);
    setPowerStatus(false);
    setTrackCircuitStatus(false);
  }

  /**
   * This method will set the broken rail failure.
   * @param state The boolean value of the failure.
   */
  public void setBrokenRailStatus(boolean state) {
    brokenRailStatus = state;
  }

  /**
   * This method will set the power rail failure.
   * @param state The boolean state of the power failure
   */
  public void setPowerStatus(boolean state) {
    powerStatus = state;
  }

  /**
   * This method will set the track circuit failure.
   * @param state The boolean value for the track circuit failure
   */
  public void setTrackCircuitStatus(boolean state) {
    trackCircuitStatus = state;
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
        + "<br><br><b>Infrastructure</b><br>&#09;Train Present: " + this.trainPresent
        + "<br>&#09;Heaters: " + this.heaters;


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
        + "\n\tInfrastructure\n\t\tTrain Present: " + this.trainPresent
        + "\n\t\tHeaters: " + this.heaters;

    output += "\n\tFailures\n\t\tBroken Rail: " + this.brokenRailStatus
        + "\n\t\tTrack Circuit Failure: " + this.powerStatus
        + "\n\t\tPower Failure: " + this.trackCircuitStatus;

    return output;

  }
}
