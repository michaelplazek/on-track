package trackctrl.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import trackmodel.model.Block;
import trackmodel.model.Switch;
import trackmodel.model.Track;
import utils.general.Authority;

public class TrackController implements TrackControllerInterface {

  private int id;
  private int trackOffset;
  private final int capacity = 16;
  private HashMap<Integer, Block> myZone = new HashMap<Integer, Block>(capacity);
  private ArrayList<Integer> blockList = new ArrayList<>();
  private TrackController neighborCtrlr1;
  private TrackController neighborCtrlr2;
  private Track myLine;


  //States populate from Boolean Logic
  //private boolean[]

  /**
   * Constructor for a new TrackController that is uninitialized.
   */
  public TrackController() {
    //Zero Id indicates Controller is not initialized
    this.id = 0;
    //neighborCtrlr1 = new TrackController();
    //neighborCtrlr2 = new TrackController();
  }

  /** This constructor accepts some common arguments used when creating
   * TrackControllers.
   *
   * @param id sets id of the track controller to be created
   * @param offset indicates the first part of the track controller zone encountered
   *               coming from the yard
   * @param line string indicating the line this track controller belongs to
   */
  public TrackController(int id, int offset, String line) {
    this.id = id;
    this.trackOffset = offset;
    neighborCtrlr1 = new TrackController();
    neighborCtrlr2 = new TrackController();
    myLine = Track.getTrack(line);
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
  public boolean sendTrackSignals(int block, Authority authority, float speed) {
    if (myLine != null) {
      myLine.getBlock(block).setAuthority(authority);
      myLine.getBlock(block).setSetPointSpeed(speed);
    }
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
  public boolean setLine(String lineName) {
    if (myLine != null) {
      myLine = Track.getTrack(lineName);
      return true;
    }
    return false;
  }

  @Override
  public void setZone(HashMap<Integer, Block> blocks) {
    if (myZone == null) {
      myZone = new HashMap<Integer, Block>(blocks);
    } else {
      //Adds key-value pairs.
      //Will duplicate...
      myZone.putAll(blocks);

      //Create string list upon initialization
      for (Integer i : blocks.keySet()) {
        blockList.add(i);
      }
    }
  }

  @Override
  public ArrayList<Integer> getZone() {

    for (Map.Entry<Integer, Block> b : myZone.entrySet()) {
      blockList.add(b.getKey());
    }
    return blockList;
  }

  @Override
  public int getBlockCount() {
    return blockList.size();
  }

  @Override
  public boolean getOccupancy(int id) {
    return myLine.getBlock(id).isOccupied();
  }

  @Override
  public boolean addBlock(Block newBlock) {

    myZone.put(newBlock.getNumber(), newBlock);
    return myZone.containsValue(newBlock);
  }

  @Override
  public boolean hasBlock(int id) {
    return myZone.containsKey(id);
  }

  @Override
  public boolean closeBlock(int id) {
    myLine.getBlock(id).setClosedForMaintenance(true);
    return myLine.getBlock(id).getBrokenRailStatus() == true;
  }

  @Override
  public boolean repairBlock(int id) {
    myLine.getBlock(id).setClosedForMaintenance(false);
    return myLine.getBlock(id).getBrokenRailStatus() == false;
  }

  @Override
  public boolean toggleSwitch(int id) {
    Block toggle = myLine.getBlock(id);
    if (toggle.isSwitch()) {
      Switch toggleSwitch = (Switch) toggle;
      boolean before = toggleSwitch.getSwitchState();
      toggleSwitch.toggle();
      boolean after = toggleSwitch.getSwitchState();
      return before ^ after;
    }
    return false;
  }

  @Override
  public boolean toggleCrossing(int id) {
    Block toggle = myLine.getBlock(id);
    if (toggle.isCrossing()) {
      boolean before = toggle.getCrossingStatus();
      toggle.setCrossing(!before);
      boolean after = toggle.getCrossingStatus();
      return before ^ after;
    }
    return false;
  }

  //TODO
  @Override
  public boolean setTrackLights(int id, boolean state, boolean direction) {
    //Takes in id of switch and its state/directionality, sets track lights accordingly
    return false;
  }

  @Override
  public int getId() {
    return this.id;
  }

  //TODO
  @Override
  public boolean importLogic(File myplc) {

    try {
      BufferedReader br = new BufferedReader(new FileReader(myplc));

      String line = br.readLine();
      int i = 0;

      String filename = myplc.getName();
      int period = filename.indexOf('.');
      String lineName = filename.substring(0, period);
      lineName = lineName.toUpperCase();

      System.out.println("Hii");

    } catch (FileNotFoundException ex) {
      System.out.println("Unable to find the file.");
    } catch (IOException ex) {
      System.out.println("Error reading file");
    }


    if (myplc.exists()) {
      System.out.println("File Found");
    } else {
      System.out.println("File Not Found");
    }

    //Set Instance PLC fields
    //-convert to string
    //send to parseLogic

    //Throw error if row is of incorrect arity
    assertLogic();
    return false;
  }

  @Override
  public boolean checkLogic() {
    return false;
  }

  @Override
  public boolean parseLogic(String logic) {
    return false;
  }

  public void run() {
    readOccupancy();

  }

  private void readOccupancy() {
    for (Map.Entry<Integer, Block> b : myZone.entrySet()) {
      b.getValue();
    }
  }

  /** Uses block occupancies read on this tick to safely change switches/crossings/lights.
   *
   */
  public void assertLogic() {
    //this function picks correct state from PLC based on Occupancy and existing states
    //it also sets infrastructure if allowed by Track Model
  }

}
