package trackmodel.model;

public class Switch extends Block{

  private int next2;
  private int status;

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
   * @param newNextBlock A Block will be passed in for the other possible next block.
   */
  public void setNextBlockOne(int newNextBlock) {
    next2 = newNextBlock;
  }

  /**
   * This method will return the next block in the track.
   * @return A integer will be returned
   */
  public int getNextBlock() {
    return next2;
  }

  /**
  * This method will update the status of the switch.
  * The status will be set to a block
  * @param newStatus This is the block number that the switch is to be set too
  */
  public void setStatus(int newStatus) {
    if (newStatus == getNextBlock() || newStatus == next2) {
      status = newStatus;
    }
  }

  /**
  * This method will return the status of a switch
  * @return a block number that the switch is currently connected to
  */
  public int getStatus() {
    return status;
  }

}
