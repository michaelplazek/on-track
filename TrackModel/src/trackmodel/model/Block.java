package trackmodel.model;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;


public class Block {

  //BLob Data
  private String[] params;

  //ID
  private String line;
  private String section;
  private int number;

  //Parameters
  private float size;
  private float grade;
  private float speedLimit;
  private float elevation;
  private float cumElevation;
  private int direction;

  //Infrastructure
  private String infrastructure;
  private boolean heaters;
  private int switchId;
  private Switch junction;
  private Crossing crossing;
  private Station station;

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
  private Block nextBlock;
  private Block previousBlock;

  public Block() {

  }

  public Block(String csvLine) {
    loadBlock(csvLine);
  }

  /**
  *Parse String and set block data.
  *@param csvLine string of Block data
  */
  private void loadBlock(String csvLine) {
    params = csvLine.split(",");

    //Line
    setLine(params[0]);

    //Section
    setSection(params[1]);

    //Block Number
    setNumber(Integer.parseInt(params[2]));

    //Block Length
    setSize(Float.parseFloat(params[3]));

    //Block Grade
    setGrade(Float.parseFloat(params[4]));

    //Speed Limit
    setSpeedLimit(Float.parseFloat(params[5]));

    //Infrastructure
    setInfrastructure(params[6]);

    //Elevation
    setElevation(Float.parseFloat(params[8]));

    //Cumulative Elevation
    setCumElevation(Float.parseFloat(params[9]));

    //Set No switch value
    setSwitchId(-1);

    //SwitchID
    if (params.length > 10 && params[10].length() > 1) {
      int switchNum = Integer.parseInt(params[10].split(" ")[1]);
      setSwitchId(switchNum);
    }     //Station

    if (getInfrastructure().equals("STATION")) {
      setStation(new Station(params[6]));
    } else if (getInfrastructure().equals("CROSSING")) {
      setCrossing(new Crossing());
    }
  }


  /**
   * This method return a list of parameter.
   * @return the array of parameters
   */
  public String[] getparams() {
    return params;
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
  public int getDirection() {
    return direction;
  }

  /**
   * This method will return the next block in the track.
   * @return A Block will be returned
   */
  public Block getNextBlock() {
    return nextBlock;
  }

  /**
   * This method will return the previous block in the track.
   * @return A Block will be returned
   */
  public Block getPreviousBlock() {
    return previousBlock;
  }

  /**
   * The infrastructure will be returned.
   * @return This will be returned in the form of a string
   */
  public String getInfrastructure() {
    return infrastructure;
  }

  /**
   * The status of the heaters will be returned by this method.
   * @return The status will be passed as a boolean value
   */
  public boolean getHeaters() {
    return heaters;
  }

  /**
   * The status of the switch id number will be returned if this block holds a switch.
   * @return The integer that is used as an id will be returned
   */
  public int getSwitchId() {
    return switchId;
  }

  /**
   * The switch at the current location will be returned.
   * @return Returns a switch object
   */
  public Switch getSwitch() {
    return junction;
  }

  /**
   * The switch will return a crossing object if one is at the block.
   * @return Returns a crossing object
   */
  public Crossing getCrossing() {
    return crossing;
  }

  /**
   * This method will return a station if there is one at this block.
   * @return A Station object will be returned
   */
  public Station getStation() {
    return station;
  }

  /**
   * Return the id of any train present.
   * @return An integer identification for a train is passed back
   */
  public int getTrainPresent() {
    return trainPresent;
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
   * This method update the parameter of the block.
   * @param newparams This is a string that updates the parameters.
   */
  public void setparams(String[] newparams) {
    params = newparams;
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
  public void setSpeedLimit(float newSpeedLimit) {
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
   * This method will set the new direction of a block.
   * @param newDirection A integer value will be passed in to set the direction
   */
  public void setDirection(int newDirection) {
    direction = newDirection;
  }

  /**
   * This method will set the next block for the track.
   * @param newNextBlock A Block will be passed in for the current block.
   */
  public void setNextBlock(Block newNextBlock) {
    nextBlock = newNextBlock;
  }

  /**
   * This method will set the previous block of the track.
   * @param newPreviousBlock A Block will be passed in for the current block.
   */
  public void setPreviousBlock(Block newPreviousBlock) {
    previousBlock = newPreviousBlock;
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
    String beginning1 = newInfrastructure.split(" ")[0];
    String beginning2 = beginning1.split(";")[0];
    String beginning3 = beginning2.split(":")[0];

    if (beginning3.equals("RAILWAY")) {
      infrastructure = "CROSSING";
    } else if (beginning3.equals("STATION")) {
      infrastructure = "STATION";
    } else {
      infrastructure = beginning3;
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
   * This method will update the switch id at the current block.
   * @param newSwitchId A integer will be passed in for the id number
   */
  public void setSwitchId(int newSwitchId) {
    switchId = newSwitchId;
  }

  /**
   * The switch will be updated for the block.
   * @param newSwitch A Switch will be passed in for the block
   */
  public void setSwitch(Switch newSwitch) {
    junction = newSwitch;
  }

  /**
   * This method will set the crossing on the block.
   * @param newCrossing A Crossing object should be passed in
   */
  public void setCrossing(Crossing newCrossing) {
    crossing = newCrossing;
  }

  /**
   * This method will create a new station.
   * @param newStation A Station object should be passed on to the method
   */
  public void setStation(Station newStation) {
    station = newStation;
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
    if (setPointSpeed > speedLimit) {
      speedLimit = setPointSpeed;
    }
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

    if (this.getInfrastructure().equals("SWITCH")) {
      output += "<br>&#09;Switch: " + this.switchId;
    } else if (this.getInfrastructure().equals("STATION")) {
      output += "<br>&#09;Station: " + this.station;
    } else if (this.getInfrastructure().equals("CROSSING")) {
      output += "<br>&#09;Crossing: " + this.crossing;
    }

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

    if (this.getInfrastructure().equals("SWITCH")) {
      output += "\n\t\tSwitch: " + this.switchId;
    } else if (this.getInfrastructure().equals("STATION")) {
      output += "\n\t\tStation: " + this.station;
    } else if (this.getInfrastructure().equals("CROSSING")) {
      output += "\n\t\tCrossing: " + this.crossing;
    }
    output += "\n\tFailures\n\t\tBroken Rail: " + this.brokenRailStatus
        + "\n\t\tTrack Circuit Failure: " + this.powerStatus
        + "\n\t\tPower Failure: " + this.trackCircuitStatus;

    return output;

  }
}
