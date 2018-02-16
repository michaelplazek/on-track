package trackmodel.model;

public class Block implements BlockInterface {

  private String line;
  private String section;
  private int block;
  private double length;
  private double grade;
  private double speedLimit;
  private String stationName;
  private double elevation;
  private double cumulativeElevation;
  private boolean occupied;
  private boolean underground;
  private boolean railCrossing;
  private boolean beacon;
  private boolean trackHeating;
  private boolean station;

  private boolean railFailure;
  private boolean powerFailure;
  private boolean trackCircuitFailure;

  public Block(){

  }

  /**
   * This is the constructor for a block.
   * @param line The line the track is on
   * @param section The section of track the block is in
   * @param block The number identifier for a block
   * @param length The length in of yards the track block
   * @param grade The grade of the track
   * @param speedLimit The speed limit of the block in MPH
   * @param stationName The name of a station if one is at this block
   * @param elevation The degree of elevation of block
   * @param cumulativeElevation The cumulative elevation in yards
   * @param occupied Indication if the block is occupied
   * @param underground Indication if the block is underground
   * @param railCrossing Indication if the block is a rail crossing
   * @param beacon Indication if the block is a beacon
   * @param trackHeating Indication if the block has the heater on
   * @param station Indication if there is a station at the track
   * @param powerFailure Indication if there is a power failure
   * @param railFailure Indication if there is a rail failure
   * @param trackCircuitFailure Indication if there is a track circuit failure
   */
  public Block(String line, String section, int block, double length,
                    double grade, double speedLimit, String stationName, double elevation,
                    double cumulativeElevation, boolean occupied, boolean underground,
                    boolean railCrossing, boolean beacon, boolean trackHeating,
                    boolean station, boolean powerFailure, boolean railFailure,
                    boolean trackCircuitFailure) {
    this.line = line;
    this.section = section;
    this.block = block;
    this.length = length;
    this.grade = grade;
    this.speedLimit = speedLimit;
    this.stationName = stationName;
    this.elevation = elevation;
    this.cumulativeElevation = cumulativeElevation;
    this.occupied = occupied;
    this.underground = underground;
    this.railCrossing = railCrossing;
    this.beacon = beacon;
    this.trackHeating = trackHeating;
    this.station = station;
    this.railFailure = railFailure;
    this.powerFailure = powerFailure;
    this.trackCircuitFailure = trackCircuitFailure;
  }

  public String getLine() {
    return line;
  }

  public String getSection() {
    return section;
  }

  public int getBlock() {
    return block;
  }

  public double getLength() {
    return length;
  }

  public double getGrade() {
    return grade;
  }

  public double getSpeedLimit() {
    return speedLimit;
  }

  public String getStationName() {
    return stationName;
  }

  public double getElevation() {
    return elevation;
  }

  public double getCummulativeElevation() {
    return cumulativeElevation;
  }

  public boolean getOccupied() {
    return occupied;
  }

  public boolean getUnderground() {
    return underground;
  }

  public boolean getRailCrossing() {
    return railCrossing;
  }

  public boolean getBeacon() {
    return beacon;
  }

  public boolean getTrackHeating() {
    return trackHeating;
  }

  public boolean getStation() {
    return station;
  }

  public boolean getRailFailure() {
    return railFailure;
  }

  public void setRailFailure(boolean railFailure) {
    this.railFailure = railFailure;
  }

  public boolean getPowerFailure() {
    return powerFailure;
  }

  public void setPowerFailure(boolean powerFailure) {
    this.powerFailure = powerFailure;
  }

  public boolean getTrackCircuitFailure() {
    return trackCircuitFailure;
  }

  public void setTrackCircuitFailure(boolean trackCircuitFailure) {
    this.trackCircuitFailure = trackCircuitFailure;
  }
}
