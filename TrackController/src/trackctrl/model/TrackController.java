package trackctrl.model;

import java.io.File;
import java.util.HashMap;
import trackmodel.model.Block;

public class TrackController implements TrackControllerInterface {

  private int id;
  private int trackOffset;
  private final int capacity = 16;
  private HashMap<Integer, Block> myZone = new HashMap<Integer, Block>(capacity);
  private TrackController neighborCtrlr1;
  private TrackController neighborCtrlr2;

  /**
   * Constructor for a new TrackController that is uninitialized.
   */
  public TrackController() {
    //Zero Id indicates Controller is not initialized
    this.id = 0;
    neighborCtrlr1 = new TrackController();
    neighborCtrlr2 = new TrackController();
  }

  public TrackController(int id, int offset) {
    this.id = id;
    this.trackOffset = offset;
  }

  /**
   * Constructor for a new TrackController based on a previous one.
   * @param tc accepts a TrackController object as an argument for cloning
   */
  public TrackController(TrackController tc) {
    this.id = tc.id;
    this.myZone = tc.myZone;
    this.neighborCtrlr1 = tc.neighborCtrlr1;
    this.neighborCtrlr2 = tc.neighborCtrlr2;
  }

  //TODO
  @Override
  public boolean sendTrackSignals(int block, float authority, float speed) {
    return false;
  }

  //TODO
  @Override
  public boolean setSwitchOverride(int block, boolean state) {
    return false;
  }

  //TODO
  @Override
  public boolean relayAuthority(int block, float authority) {
    return false;
  }

  //TODO
  @Override
  public boolean relaySetSpeed(int block, float setSpeed) {
    return false;
  }

  //TODO
  @Override
  public boolean setInfrastructureState(int block, boolean state) {
    return false;
  }

  @Override
  public boolean setId(int id) {
    this.id = id;
    return true;
  }

  @Override
  public void setZone(HashMap<Integer, Block> blocks) {
    if (myZone == null) {
      myZone = new HashMap<Integer, Block>(blocks);
    } else {
      //Adds key-value pairs.
      //Will duplicate...
      myZone.putAll(blocks);
    }
  }

  //TODO
  @Override
  public boolean addBlock(Block newBlock) {
    return false;
  }

  @Override
  public boolean hasBlock(int id) {
    return myZone.containsKey(id);
  }

  //TODO
  @Override
  public boolean closeBlock(int id) {
    return false;
  }

  //TODO
  @Override
  public boolean repairBlock(int id) {
    return false;
  }

  //TODO
  @Override
  public boolean toggleSwitch(int id) {
    return false;
  }

  //TODO
  @Override
  public boolean toggleCrossing(int id) { return false; }

  //TODO
  @Override
  public boolean setTrackLights(int id, boolean state, boolean direction) { return false; }

  @Override
  public int getId() {
    return this.id;
  }

  //TODO
  @Override
  public boolean importLogic(File myplc) {
    return false;
  }

  @Override
  public boolean checkLogic() { return false; }
}
