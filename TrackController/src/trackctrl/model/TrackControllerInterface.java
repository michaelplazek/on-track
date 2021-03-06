package trackctrl.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import trackmodel.model.Block;
import utils.general.Authority;

public interface TrackControllerInterface {

  //To be set by the CTC
  boolean sendTrackSignals(int blockId, Authority speed, float authority);

  //To be called by TrackControllerLineManager
  boolean setId(int id);

  boolean setLine(String lineName);

  boolean getOccupancy(int id);

  ArrayList<String> getZone();

  boolean addBlock(Block newBlock);

  boolean hasBlock(int id);

  boolean closeBlock(int id);

  boolean repairBlock(int id);

  boolean toggleSwitch(int id);

  boolean toggleCrossing(int id);

  boolean setTrackLights(int id, boolean state, boolean direction);

  int getId();

  //To be called within TrackController
  boolean importLogic(File myplc);

}
