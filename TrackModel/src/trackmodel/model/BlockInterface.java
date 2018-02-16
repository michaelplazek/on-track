package trackmodel.model;

public interface BlockInterface {

  public String getLine();

  public String getSection();

  public int getBlock();

  public double getLength();

  public double getGrade();

  public double getSpeedLimit();

  public String getStationName();

  public double getElevation();

  public double getCummulativeElevation();

  public boolean getOccupied();

  public boolean getUnderground();

  public boolean getRailCrossing();

  public boolean getBeacon();

  public boolean getTrackHeating();

  public boolean getStation();

  public boolean getRailFailure();

  public void setRailFailure(boolean railFailure);

  public boolean getPowerFailure();

  public void setPowerFailure(boolean powerFailure);

  public boolean getTrackCircuitFailure();

  public void setTrackCircuitFailure(boolean trackCircuitFailure);

}
