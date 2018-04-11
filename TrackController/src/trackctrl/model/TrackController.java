package trackctrl.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
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
  private final int capacity = 32;
  private HashMap<Integer, Block> myZone = new HashMap<Integer, Block>(capacity);
  private ArrayList<String> blockList = new ArrayList<>();
  private TrackController neighborCtrlr1;
  private TrackController neighborCtrlr2;
  private Track myLine;

  //Dimension = number of block statements
  private ArrayList<String> plcBlockCurrent = new ArrayList<String>();

  //Dimension = number of swtich statements
  private ArrayList<String> plcSwitchCurrent = new ArrayList<String>();

  //Dimension = #of blocks in controller
  private HashMap<Integer, Boolean> occPrevious = new HashMap<Integer, Boolean>();
  private HashMap<Integer, Boolean> occCurrent = new HashMap<Integer, Boolean>();

  private HashMap<Integer, Authority> ctcAuthPrevious = new HashMap<Integer, Authority>();
  private HashMap<Integer, Authority> ctcAuthCurrent = new HashMap<Integer, Authority>();
  private HashMap<Integer, Authority> ctcAuthTemp = new HashMap<Integer, Authority>();

  private HashMap<Integer, Float> ctcSpeedPrevious = new HashMap<Integer, Float>();
  private HashMap<Integer, Float> ctcSpeedCurrent = new HashMap<Integer, Float>();
  private HashMap<Integer, Float> ctcSpeedTemp = new HashMap<Integer, Float>();

  private boolean loaded = false;
  private int switches = 0;

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

  public void run() {
    readOccupancy();
    readSuggestion();
    assertLogic();
  }

  @Override
  public boolean sendTrackSignals(int block, Authority authority, float speed) {
    if (myLine != null) {

      //Take snapshot of CTC suggestions
      if((block - trackOffset >= 0) && (block - trackOffset < getZone().size())) {
        ctcAuthTemp.replace(block, authority);
        ctcSpeedTemp.replace(block,speed);
        return true;
      } else {
        return false;
      }
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
  public ArrayList<String> getZone() {

    blockList.clear();
    for (Map.Entry<Integer, Block> b : myZone.entrySet()) {
      if(b.getValue().isSwitch()) {
        blockList.add("Switch" + " " + b.getKey().toString());
      } else {
        blockList.add("Block" + " " + b.getKey().toString());
      }
    }
    //TODO sort block output here for UI
    return blockList;
  }

  public Block getBlock(int id) {
    return myZone.get(id);
  }

  public String getLine() {
    return myLine.getLine();
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
    if (newBlock.isSwitch()) { switches++; }
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
      toggleSwitch.toggle();
      return toggleSwitch.getSwitchState();
    }
    return false;
  }

  @Override
  public boolean toggleCrossing(int id) {
    Block toggle = myLine.getBlock(id);
    if (toggle.isCrossing()) {
      boolean before = toggle.getCrossingStatus();
      toggle.setCrossing(!before);
      return toggle.getCrossingStatus();
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

      String line;
      int lineNum = 0;
      boolean inSwitch = false;

      line = br.readLine();

      if (line.equals("BLOCK LOGIC")) {
        inSwitch= false;
      }

      while ((line = br.readLine()) != null) {
        if(line.equals("SWITCH LOGIC")) {
          inSwitch = true;
        } else if (!inSwitch) {
          plcBlockCurrent.add(lineNum, line);
        } else {
          plcSwitchCurrent.add(line);
        }
        lineNum++;
      }

    } catch (FileNotFoundException ex) {
      System.out.println("Unable to find the file at: " + myplc);
    } catch (IOException ex) {
      System.out.println("Error reading file at:" + myplc);
    }


    if (myplc.exists()) {
      System.out.println("PLC Found at: " + myplc);
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

  private void readOccupancy() {

    for (Block b : myZone.values()) {
      occPrevious.replace(b.getNumber(),occCurrent.get(b.getNumber()));
      occCurrent.replace(b.getNumber(), b.isOccupied());
    }
  }

  private void readSuggestion() {

    //Reads in current suggestion array into previous
    //Sets current to the temp (set by calls from ctc)

    for (Block b : myZone.values()) {
      int index = b.getNumber();
      ctcAuthPrevious.replace(index,ctcAuthCurrent.get(index));
      ctcAuthCurrent.replace(index, ctcAuthTemp.get(index));

      ctcSpeedPrevious.replace(index,ctcSpeedCurrent.get(index));
      ctcSpeedCurrent.replace(index,ctcSpeedTemp.get(index));
    }
  }

  public void setBlockNumber() {
    //Create boolean arrays based on number of blocks

    if(!loaded) {
//      occCurrent = new ArrayList<>(getZone().size());
//      occPrevious = new ArrayList<>(getZone().size());
//      ctcAuthPrevious = new ArrayList<>(getZone().size());
//      ctcAuthCurrent = new ArrayList<>(getZone().size());
//      ctcAuthTemp = new ArrayList<>(getZone().size());
//      ctcSpeedPrevious = new ArrayList<>(getZone().size());
//      ctcSpeedCurrent = new ArrayList<>(getZone().size());
//      ctcSpeedTemp = new HashMap<Integer, Float>;

      //initialize array values
      for (Integer i : myZone.keySet()) {
        occCurrent.put(i, false);
        occPrevious.put(i, false);

        ctcAuthCurrent.put(i, Authority.SEND_POWER);
        ctcAuthPrevious.put(i, Authority.SEND_POWER);
        ctcAuthTemp.put(i, Authority.SEND_POWER);

        ctcSpeedCurrent.put(i, 15.0f);
        ctcSpeedPrevious.put(i, 15.0f);
        ctcSpeedTemp.put(i, 15.0f);
      }

      loaded = true;
    }
  }

  //----------------------PLC helper functions-------------------------------------------

  /** This function checks the relative number of blocks, blocks, and returns true
   * if there is occupancy in that direction (or up until jurisdiction ends,
   * whichever is first).
   *
   * @param sign direction to travel relative to a block
   * @param blocks number of blocks we are applying boolean logic to
   * @return true if occupied block found, false otherwise
   */
    private boolean isOccupied(char sign, int blocks) {

      return false;
    }

  /** This function checks the relative number of blocks, blocks, and searches for
   * occupancy in that direction (or up until jurisdiction ends, whichever is first).
   *
   * @param sign direction to travel relative to a block
   * @param blocks number of blocks we are applying boolean logic to
   * @return true if no occupied block found, false otherwise
   */
    private boolean notOccupied(char sign, int blocks) {
      return true;
    }

  /** This function searches the current controller jurisdiction for a station, and
   * if found, it will then check if the temperature reading at that station is
   * indicating freezing levels.
   *
   * @return true if temperature is freezing
   */
  private boolean isFreezing() {
      return false;
    }

  /** This function searches the current controller jurisdiction for a station, and
   * if found, it will then check if the temperature reading at that station is
   * indicating freezing levels.
   *
   * @return true if temperature is not freezing
   */
  private boolean notFreezing() {
    return true;
  }

  /** This will use the previous and current readings to detect if a train is
   * moving towards a certain block.
   *
   * @param sign direction to check readings
   * @param blocks number of blocks in that direction to check
   * @return true if train found moving in that direction.
   */
  private boolean movingTo(char sign, int blocks) {
    return false;
  }

  /** This will use the previous and current readings to detect if a train is
   * moving away from a certain block.
   *
   * @param sign direction to check readings
   * @param blocks number of blocks in that direction to check
   * @return true if train found moving in that direction.
   */
  private boolean movingFrom(char sign, int blocks) {
    return false;
  }

  //-------------------------------------------------------------------------------------

  /** Uses block occupancies read on this tick to safely change switches/crossings/lights.
   *
   */
  public void assertLogic() {
    //this function picks correct state from PLC based on Occupancy and existing states
    //it also sets infrastructure if allowed by Track Model

    //TODO these will be voted upon or updated prior to asserting on the track based on above logic
    for(Integer index : myZone.keySet()) {
      myLine.getBlock(index).setAuthority(ctcAuthCurrent.get(index));
      myLine.getBlock(index).setSetPointSpeed(Math.abs(index));
    }
  }
}
