package trackmodel.model;

public class Beacon {

  private float distance;      //32
  private byte stationId;      //8
  private boolean right;       //1
  private int blockId;         //32
  private boolean underground; //1

  private String userMessage;  // max of 6 characters

  /**
   * This is the constructor for a beacon.
   * @param distance The distance to a station
   * @param stationId The station specified by the beacon
   * @param right What side of the track that station is on
   * @param blockId The id for the block of beacon
   * @param underground If the lights need turned on
   * @param userMessage User message for the train
   */
  public Beacon(float distance, byte stationId,
                boolean right, int blockId,
                boolean underground, String userMessage) {
    setDistance(distance);
    setStationId(stationId);
    setRight(right);
    setBlockId(blockId);
    setUnderground(underground);
    setUserMessage(userMessage);
  }

  public Beacon() {

  }

  /**
   * Copy a beacon object.
   * @param beacon Beacon to be copied.
   */
  public Beacon(Beacon beacon) {
    this.stationId = beacon.stationId;
    this.distance = beacon.distance;
    this.blockId = beacon.blockId;
    this.underground = beacon.underground;
    this.right = beacon.right;
    this.userMessage = beacon.userMessage;
  }

  public float getDistance() {
    return distance;
  }

  public void setDistance(float distance) {
    this.distance = distance;
  }

  public byte getStationId() {
    return stationId;
  }

  public void setStationId(byte stationId) {
    this.stationId = stationId;
  }

  public boolean isRight() {
    return right;
  }

  public void setRight(boolean right) {
    this.right = right;
  }

  public int getBlockId() {
    return blockId;
  }

  public void setBlockId(int blockId) {
    this.blockId = blockId;
  }

  public boolean isUnderground() {
    return underground;
  }

  public void setUnderground(boolean underground) {
    this.underground = underground;
  }

  public String getUserMessage() {
    return userMessage;
  }

  public void setUserMessage(String userMessage) {
    this.userMessage = userMessage;
  }
}
