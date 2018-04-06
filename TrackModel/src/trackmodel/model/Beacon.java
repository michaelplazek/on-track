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
    this.distance = distance;
    this.stationId = stationId;
    this.right = right;
    this.blockId = blockId;
    this.underground = underground;
    this.userMessage = userMessage;
  }

  public Beacon() {

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
