package trackctrl.model;

import java.io.File;
import java.util.HashMap;
import trackmodel.model.Block;

public interface TrackControllerInterface {

  //To be set by the CTC
  boolean setAuthority(int block, float authority);

  boolean setSuggestedSpeed(int block, float setSpeed);

  public boolean setSwitchOverride(int block, boolean state);

  //To be relayed to the Track model
  boolean relayAuthority(int block, double authority);

  boolean relaySetSpeed(int block, double setSpeed);

  //To be called by TrackController and/or CTC
  boolean setInfrastructureState(int block, boolean state);

  //To be called by TrackControllerLineManager
  boolean setId(int id);

  void setZone(HashMap<Integer, Block> blocks);

  boolean addBlock(Block newBlock);

  boolean hasBlock(int id);

  boolean closeBlock(int id);

  boolean repairBlock(int id);

  boolean toggleSwitch(int id);

  int getId();

  //To be called within TrackController
  boolean importLogic(File myplc);

}
