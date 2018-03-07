package trackctrl.model;

import java.io.File;
import java.util.HashMap;
import trackmodel.model.Block;

public class TrackController implements TrackControllerInterface {

  private String id;
  private final int CAPACITY = 64;
  private HashMap<Integer, Block> myZone = new HashMap<Integer, Block>(CAPACITY);
  private TrackController neighborCtrlr1 = new TrackController();
  private TrackController neighborCtrlr2 = new TrackController();

  public TrackController() {
    //Zero Id indicates Controller is not initialized
    this.id = "0";
  }

  public TrackController(TrackController tc) {
    this.id = tc.id;
    this.myZone = tc.myZone;
    this.neighborCtrlr1 = tc.neighborCtrlr1;
    this.neighborCtrlr2 = tc.neighborCtrlr2;
  }

  @Override
  public Boolean setAuthority(int block, float authority) {
    return null;
  }

  @Override
  public Boolean setSuggestedSpeed(int block, float setSpeed) {
    return null;
  }

  @Override
  public Boolean relayAuthority(int block, double authority) {
    return null;
  }

  @Override
  public Boolean relaySetSpeed(int block, double setSpeed) {
    return null;
  }

  @Override
  public Boolean setInfrastructureState(int block, Boolean state) {
    return null;
  }

  @Override
  public Boolean setId(String Id) {
    this.id = Id;
    return null;
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

  @Override
  public Boolean addBlock(Block newBlock) {
    return null;
  }

  @Override
  public Boolean hasBlock(int block) {
    return myZone.containsKey(block);
  }

  @Override
  public int getId() {
    return 0;
  }

  @Override
  public Boolean importLogic(File myplc) {
    return null;
  }
}
