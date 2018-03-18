package trackmodel.model;

public class Switch extends Block {

  private int next2;
  private int status;

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
   */
  public Switch(String line, String section, String number, float length,
                float grade, int speedLimit, String infrastructure, float elevation,
                float cumElevation, boolean biDirectional, int previous, int next1,
                int next2, boolean leftDoors, boolean rightDoors) {

    super(line, section, number, length, grade, speedLimit, infrastructure, elevation,
        cumElevation, biDirectional, previous, next1, leftDoors, rightDoors);
    this.next2 = next2;
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
  public void toggle() {}

  // TODO: fill out this function to return the switch state
  public boolean getSwitchState() {
    return false;
  }

}
