package trackctrl.model;

import java.io.*;
import java.util.HashMap;
import trackmodel.model.Block;

public interface TrackControllerInterface {

  //To be set by the CTC
  Boolean setAuthority(int block, float authority);

  Boolean setSuggestedSpeed(int block, float setSpeed);

  //To be relayed to the Track model
  Boolean relayAuthority(int block, double authority);

  Boolean relaySetSpeed(int block, double setSpeed);

  //To be called by TrackController and/or CTC
  Boolean setInfrastructureState(int block, Boolean state);

  //To be called by TrackControllerLineManager
  Boolean setId(String Id);

  void setZone(HashMap<Integer, Block> blocks);

  Boolean addBlock(Block newBlock);

  Boolean hasBlock(int block);

  int getId();

  //To be called within TrackController
  Boolean importLogic(File myplc);

}
