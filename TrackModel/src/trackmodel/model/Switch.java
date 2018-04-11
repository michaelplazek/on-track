package trackmodel.model;

public class Switch extends Block {

  private int next2;
  private int status;
  private boolean switchState;

  private boolean[] previousLightStatus = {false, false};
  private boolean[] nextLightStatus1 = {false, false};
  private boolean[] nextLightStatus2 = {false, false};

  /**
   * Default constructor.
   */
  public Switch() {}

  /**
   * This method is the constructor to create a new switch.
   * @param line The line the switch lays on
   * @param section The section of the track that the block is on
   * @param number The number of hte block that the switch is on
   * @param length The length of the block
   * @param grade The grade of the block
   * @param speedLimit The speed limit on the block
   * @param infrastructure The infrastructure string for the block
   * @param elevation The elevation of a block
   * @param cumElevation The cumulative elevation of the block
   * @param biDirectional If the block is bidirectional
   * @param previous The previous block in the track
   * @param next1 The first of two possible next blocks in the track
   * @param next2 The second of two possible next blocks in the track
   * @param leftDoors If the track is on the left of the track
   * @param rightDoors If the station is on the right of the track
   * @param beacon Beacon object for a block
   */
  public Switch(String line, String section, int number, float length,
                float grade, int speedLimit, String infrastructure, float elevation,
                float cumElevation, boolean biDirectional, int previous, int next1,
                int next2, boolean leftDoors, boolean rightDoors, Beacon beacon) {

    super(line, section, number, length, grade, speedLimit, infrastructure, elevation,
        cumElevation, biDirectional, previous, next1, leftDoors, rightDoors, beacon);
    this.next2 = next2;
    this.status = next1;
    this.switchState = true;
  }

  /**
   * This method will set the other possible next block.
   * @param next2 A Block will be passed in for the other possible next block.
   */
  public void setNextBlock2(int next2) {
    this.next2 = next2;
  }

  /**
   * This method will return the next block in the track.
   * @return A integer will be returned
   */
  public int getNextBlock2() {

    return next2;
  }

  /**
  * This method will update the status of the switch.
  * The status will be set to a block
  * @param status This is the block number that the switch is to be set too
  */
  public boolean setStatus(int status) {
    if (status == getNextBlock1() || status == next2) {
      this.status = status;
      return true;
    }

    return false;
  }

  /**
  * This method will return the status of a switch.
  * @return a block number that the switch is currently connected to
  */
  public int getStatus() {
    return status;
  }

  // TODO: fill out this function to toggle the switch

  /**
   * Toggle the boolean value of switchState.
   */
  public boolean toggle() {

    switchState = !switchState;

    if(switchState) {
      status = this.getNextBlock1();
    } else {
      status = this.getNextBlock2();
    }

    return switchState;
  }

  // TODO: fill out this function to return the switch state

  /**
   * For setting the state of elements in the user interface.
   * @return true is the switch is straight, and false if it is the fork.
   */
  public boolean getSwitchState() {
    return switchState;
  }

  /**
   * This method will allow the user to set the status of previous lights.
   * @param status status of the Light
   * @param frontOrBack Boolean value for the side of the light (true = front)
   */
  public void setPreviousLightStatus(boolean status, boolean frontOrBack) {
    int index;
    if (frontOrBack) {
      index = 0;
    } else {
      index = 1;
    }
    previousLightStatus[index] = status;
  }

  /**
   * This method will allow the user to set the status of next 1 lights.
   * @param status status of the Light
   * @param frontOrBack Boolean value for the side of the light (true = front)
   */
  public void setNext1LightStatus(boolean status, boolean frontOrBack) {
    int index;
    if (frontOrBack) {
      index = 0;
    } else {
      index = 1;
    }
    nextLightStatus1[index] = status;
  }

  /**
   * This method will allow the user to set the status of next 2 lights.
   * @param status status of the Light
   * @param frontOrBack Boolean value for the side of the light (true = front)
   */
  public void setNext2LightStatus(boolean status, boolean frontOrBack) {
    int index;
    if (frontOrBack) {
      index = 0;
    } else {
      index = 1;
    }
    nextLightStatus2[index] = status;
  }

  /**
   * This method will get the status of the front or back of previous light.
   * @param frontOrBack Value for the from or back of the light(true = front)
   * @return boolean value for if the light is on
   */
  public boolean getPreviousLightStatus(boolean frontOrBack) {
    if (frontOrBack) {
      return previousLightStatus[0];
    } else {
      return previousLightStatus[1];
    }
  }

  /**
   * This method will get the status of the front or back of next 1 light.
   * @param frontOrBack Value for the from or back of the light(true = front)
   * @return boolean value for if the light is on
   */
  public boolean getNext1LightStatus(boolean frontOrBack) {
    if (frontOrBack) {
      return nextLightStatus1[0];
    } else {
      return nextLightStatus1[1];
    }
  }

  /**
   * This method will get the status of the front or back of next 2 light.
   * @param frontOrBack Value for the from or back of the light(true = front)
   * @return boolean value for if the light is on
   */
  public boolean getNext2LightStatus(boolean frontOrBack) {
    if (frontOrBack) {
      return nextLightStatus2[0];
    } else {
      return nextLightStatus2[1];
    }
  }
}
