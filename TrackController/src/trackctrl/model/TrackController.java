package trackctrl.model;

import java.util.ArrayList;
import trackmodel.model.Block;

public class TrackController implements TrackControllerInterface {

  private int id;
  private final int CAPACITY = 64;
  private ArrayList<Block> myZone = new ArrayList<Block>(CAPACITY);
  private TrackController neighborCtrlr1 = new TrackController();
  private TrackController neighborCtrlr2 = new TrackController();


  public TrackController() {
    //Negative Id indicates Controller is not initialized
    this.id = -1;
  }

  public TrackController(TrackController tc) {
    this.id = tc.id;
    this.myZone = tc.myZone;
    this.neighborCtrlr1 = tc.neighborCtrlr1;
    this.neighborCtrlr2 = tc.neighborCtrlr2;
  }

  @Override
  public Boolean setAuthority(String section, int block, float authority) {
    return null;
  }

  @Override
  public Boolean setSuggestedSpeed(String section, int block, float setSpeed) {
    return null;
  }

  @Override
  public Boolean relayAuthority(String section, int block, double authority) {
    return null;
  }

  @Override
  public Boolean relaySetSpeed(String section, int block, double setSpeed) {
    return null;
  }

  @Override
  public Boolean setInfrastructureState(String section, int block, Boolean state) {
    return null;
  }

  @Override
  public Boolean setId(String Id) {
    return null;
  }

  @Override
  public Boolean setZone(ArrayList<Block> blocks) {
    return null;
  }

  @Override
  public Boolean addBlock(Block newBlock) {
    return null;
  }



  @Override
  public Boolean hasBlock(String section, int block) {
    return null;
  }

  @Override
  public Boolean hasBlock(Block block) {
    return null;
  }

  @Override
  public int getId() {
    return 0;
  }

  @Override
  public Boolean importLogic() {
    return null;
  }
}
