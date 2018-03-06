package trackctrl.model;

import java.io.*;
import java.util.ArrayList;
import trackmodel.model.Block;

public interface TrackControllerInterface {

  //To be set by the CTC
  Boolean setAuthority(String section, int block, float authority);

  Boolean setSuggestedSpeed(String section, int block, float setSpeed);

  //To be relayed to the Track model
  Boolean relayAuthority(String section, int block, double authority);

  Boolean relaySetSpeed(String section, int block, double setSpeed);

  //To be called by TrackController and/or CTC
  Boolean setInfrastructureState(String section, int block, Boolean state);

  //To be called by TrackControllerLineManager
  Boolean setId(String Id);

  Boolean setZone(ArrayList<Block> blocks);

  Boolean addBlock(Block newBlock);

  Boolean hasBlock(String section, int block);

  Boolean hasBlock(Block block);

  int getId();

  //To be called within TrackController
  Boolean importLogic();

}
