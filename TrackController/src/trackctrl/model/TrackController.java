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
  private final int capacity = 32;
  private HashMap<Integer, Block> myZone = new HashMap<Integer, Block>(capacity);
  private ArrayList<String> blockList = new ArrayList<>();
  private TrackController neighborCtrlr1;
  private TrackController neighborCtrlr2;
  private int endBlock;
  private Track myLine;

  //Dimension = number of block statements
  private ArrayList<String> plcBlockSection = new ArrayList<String>();
  private ArrayList<Boolean> blockInputEval = new ArrayList<Boolean>();

  //Dimension = number of swtich statements
  private ArrayList<String> plcSwitchSection = new ArrayList<String>();
  private ArrayList<Boolean> switchInputEval = new ArrayList<Boolean>();

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

  /** Function to update UI and assert all logic from PLC code.
   *
   */
  public void run() {
    readOccupancy();
    readSuggestion();
    assertLogic();
  }

  @Override
  public boolean sendTrackSignals(int block, Authority authority, float speed) {
    if (myLine != null) {

      //Take snapshot of CTC suggestions
      if ((block - trackOffset >= 0) && (block - trackOffset < getZone().size())) {
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
      if (b.getValue().isSwitch()) {
        blockList.add("Switch" + " " + b.getKey().toString());
      } else {
        blockList.add("Block" + " " + b.getKey().toString());
      }
    }
    //TODO sort block output here for UI
    return blockList;
  }

  public void setEndBlock(int end) {
    endBlock = end;
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
    if (newBlock.isSwitch()) {
      switches++;
    }
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
        inSwitch = false;
      }

      while ((line = br.readLine()) != null) {
        if (line.equals("SWITCH LOGIC")) {
          inSwitch = true;
        } else if (!inSwitch) {
          plcBlockSection.add(lineNum, line);
        } else {
          plcSwitchSection.add(line);
        }
        lineNum++;
      }

      //Initialize arrays to lineNum value
      blockInputEval = new ArrayList<>(lineNum);
      switchInputEval = new ArrayList<>(lineNum);

    } catch (FileNotFoundException ex) {
      System.out.println("Unable to find the file at: " + myplc);
    } catch (IOException ex) {
      System.out.println("Error reading file at:" + myplc);
    }


    if (myplc.exists()) {
      System.out.println("PLC Found at: " + myplc);
    }

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

  /** This is called after Controller initialization
   * to set occupancy and ctc suggestions.
   *
   */
  public void setBlockNumber() {
    //Create boolean arrays based on number of blocks

    if (!loaded) {

      //initialize array values to  number of blocks
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
  private boolean isOccupied(char sign, int blocks, String item, Block start) {

    int prev = start.getPreviousBlock();
    int next = start.getNextBlock1();

    if (prev < start.getNumber() && prev != -1) {
      if (sign == '-') {
        //Check blocks previous

        Block currBlock = myZone.get(start.getPreviousBlock());
        Boolean occupied = false;
        for (int i = 0; i < blocks; i++) {
          if (currBlock.getNextBlock1() != -1 && myZone.get(currBlock.getPreviousBlock()) != null) {
            currBlock = myZone.get(currBlock.getPreviousBlock());
            occupied = occupied | currBlock.isOccupied();
          } else {
            break;
          }
        }

        return occupied;

      } else {
        //check blocks next

        Block currBlock = myZone.get(start.getNextBlock1());
        Boolean occupied = false;
        for (int i = 0; i < blocks; i++) {
          if (currBlock.getNextBlock1() != -1 && myZone.get(currBlock.getNextBlock1()) != null) {
            currBlock = myZone.get(currBlock.getNextBlock1());
            occupied = occupied | currBlock.isOccupied();
          } else {
            return occupied;
          }
        }
        return occupied;
      }
    } else {
      //blocks increase from next to previous

      if (sign == '-') {
        //Check blocks next

        Block currBlock = myZone.get(start.getNextBlock1());
        Boolean occupied = false;
        for (int i = 0; i < blocks; i++) {
          occupied = occupied | currBlock.isOccupied();
          if (currBlock.getNextBlock1() != -1 && myZone.get(currBlock.getNextBlock1()) != null) {
            currBlock = myZone.get(currBlock.getNextBlock1());
            occupied = occupied | currBlock.isOccupied();
          } else {
            break;
          }
        }

        return occupied;
      } else {
        //check blocks previous

        Block currBlock = myZone.get(start.getPreviousBlock());
        Boolean occupied = false;
        for (int i = 0; i < blocks; i++) {
          occupied = occupied | currBlock.isOccupied();
          if (currBlock.getNextBlock1() != -1 && myZone.get(currBlock.getPreviousBlock()) != null) {
            currBlock = myZone.get(currBlock.getPreviousBlock());
            occupied = occupied | currBlock.isOccupied();
          } else {
            break;
          }
        }

        return occupied;
      }
    }
  }

  /** This function searches the current controller jurisdiction for a station, and
   * if found, it will then check if the temperature reading at that station is
   * indicating freezing levels.
   *
   * @return true if temperature is freezing
   */
  private boolean isFreezing(String item, Block start) {
    return false;
  }

  /** This will use the previous and current readings to detect if a train is
   * moving towards a certain block.
   *
   * @param sign direction to check readings
   * @param blocks number of blocks in that direction to check
   * @return true if train found moving in that direction.
   */
  private boolean movingTo(char sign, int blocks, Block start) {
    return false;
  }

  /** This will use the previous and current readings to detect if a train is
   * moving away from a certain block.
   *
   * @param sign direction to check readings
   * @param blocks number of blocks in that direction to check
   * @return true if train found moving in that direction.
   */
  private boolean movingFrom(char sign, int blocks, Block start) {
    return false;
  }

  //-------------------------------------------------------------------------------------

  /** Uses block occupancies read on this tick to safely change switches/crossings/lights.
   *
   */
  public void assertLogic() {

    //Terms are grouped in threes
    String blockInputTerms[] = new String[3];
    String blockOutputTerms[] = new String[3];

    ArrayList<String[]> switchInputTerms = new ArrayList<String[]>();
    String switchOutputTerms[] = new String[3];


    //IN currBlock iteration, assert PLC evaluated for block above based on outputs
    // 1 iterate through block statements first for every block

    //-----------------------ONE iteration of Block ASSERTION---------------------------------
    Block currBlock = myZone.get(trackOffset);
    Block prevBlock = null;

    while (currBlock.getNumber() != endBlock) {

      //Iterate & brake up BLOCK statements
      //-------------------------------------------------------------------------------------
      for (int j = 0 ; j < plcBlockSection.size() ; j++) {

        //READ line at j and evaluate
        String code = plcBlockSection.get(j);

        String funct = code.split(" THEN ")[0];
        String output = code.split(" THEN ")[1];

        //remove parenthesis
        int paren;
        paren = funct.indexOf('(');
        funct = funct.substring(paren + 1, funct.length() - 1);

        paren = 0;
        paren = output.indexOf('(');
        output = output.substring(paren + 1, output.length() - 1);

        //1 - block/crossing/station keyword
        //2 - relative checking distance and direction
        //3 - function name
        blockInputTerms = funct.split(" ");

        //1 - blocks/crossing/station
        //2 - relative checking distance and direction
        //3 - state out
        blockOutputTerms = output.split(" ");

        boolean eval = false;

        if (blockInputTerms[2].contains("Occupied")) {
          String s = blockInputTerms[1].substring(1,2);
          String bstring = blockInputTerms[1].substring(2,blockInputTerms[1].length() - 1);

          int checkBlocks = Integer.parseInt(bstring);
          char sign = s.toCharArray()[0];

          if (blockInputTerms[2].equals("isOccupied")) {
            eval = isOccupied(sign,checkBlocks,blockInputTerms[0], currBlock);
            blockInputEval.add(j,eval);

          } else if (blockInputTerms[2].equals("notOccupied")) {
            eval = !isOccupied(sign,checkBlocks,blockInputTerms[0], currBlock);
            blockInputEval.add(j,eval);

          } else {
            //Invalid function name
          }
        } else if (blockInputTerms[2].contains("Broken")) {
          if (blockInputTerms[2].equals("isBroken")) {
            //TODO
          } else if (blockInputTerms[2].equals("notBroken")) {

          } else {
            //Invalid function name
          }
        } else if (blockInputTerms[2].contains("Freezing")) {
          if (blockInputTerms[2].equals("isFreezing")) {

          } else if (blockInputTerms[2].equals("notFreezing")) {

          } else {
            //Invalid function name
          }
        } else if (blockInputTerms[2].contains("moving")) {
          if (blockInputTerms[2].equals("movingTo")) {

          } else if (blockInputTerms[2].equals("movingFrom")) {

          } else {
            //Invalid function name
          }
        } else {
          //Unknown input
        }

//        //TODO assert block outputs
//        if ((blockOutputTerms[0].equals("blocks")) {
//
//        } else if (blockOutputTerms[0].equals(""))


      }
      //-------------------------------------------------------------------------------------

      //Iterate through SWITCH statements and set evals
      //-------------------------------------------------------------------------------------
      if (currBlock.isSwitch()) {

        Switch currSwitch = (Switch) currBlock;
        boolean preferred = true; //(false = nb2 true = nb1)

        //Evaluate each PLC switch statement
        for (int j = 0; j < plcSwitchSection.size(); j++) {

          //READ line at j and evaluate
          String code = plcSwitchSection.get(j);

          String funct = code.split(" THEN ")[0];
          String output = code.split(" THEN ")[1];
          String clauses[] = funct.split(" AND ");

          //Fetch individual function terms
          switchInputTerms.clear();
          for(int c = 0; c < clauses.length; c++) {
            int paren;
            paren = clauses[c].indexOf('(');
            clauses[c] = clauses[c].substring(paren + 1, clauses[c].length() - 1);

            switchInputTerms.add(c, clauses[c].split(" "));
          }

          int paren = 0;
          paren = output.indexOf('(');
          output= output.substring(paren + 1, output.length() - 1);

          switchOutputTerms = output.split(" ");

          boolean termEvals[] = new boolean[switchInputTerms.size()];

          //Evaluate LHS Terms of Switch statement
          int t = 0;
          for (String[] switchInputTerm : switchInputTerms) {

            boolean eval = false;

            if (switchInputTerm[2].contains("Occupied")) {
              //OCCUPIED
              String s = switchInputTerm[1].substring(1, 2);
              String bstring = switchInputTerm[1].substring(2, switchInputTerm[1].length() - 1);

              int checkBlocks = Integer.parseInt(bstring);
              char sign = s.toCharArray()[0];

              if (switchInputTerm[2].equals("isOccupied")) {

                if (switchInputTerm[0].equals("pblock")) {
                  termEvals[t] = isOccupied(sign, checkBlocks, switchInputTerm[0], myLine.getBlock(currSwitch.getPreviousBlock()));
                } else if (switchInputTerm[0].equals("n1block")) {
                  termEvals[t] = isOccupied(sign, checkBlocks, switchInputTerm[0], myLine.getBlock(currSwitch.getNextBlock1()));
                } else if (switchInputTerm[0].equals("n2block")) {
                  termEvals[t] = isOccupied(sign, checkBlocks, switchInputTerm[0], myLine.getBlock(currSwitch.getNextBlock2()));
                } else {
                  //invalid term
                }

              } else if (switchInputTerm[2].equals("notOccupied")) {

                if (switchInputTerm[0].equals("pblock")) {
                  termEvals[t] = !isOccupied(sign, checkBlocks, switchInputTerm[0], myLine.getBlock(currSwitch.getPreviousBlock()));
                } else if (switchInputTerm[0].equals("n1block")) {
                  termEvals[t] = !isOccupied(sign, checkBlocks, switchInputTerm[0], myLine.getBlock(currSwitch.getNextBlock1()));
                } else if (switchInputTerm[0].equals("n2block")) {
                  termEvals[t] = !isOccupied(sign, checkBlocks, switchInputTerm[0], myLine.getBlock(currSwitch.getNextBlock2()));
                } else if (switchInputTerm[0].equals("preferred")){
                  if (switchInputTerm[0].equals("switchN1")) {
                    preferred = true;
                  } else if (switchInputTerm[0].equals("switchN2")) {
                    preferred = false;
                  } else if (switchInputTerm[0].equals("ignore")) {
                    termEvals[t] = true;
                  }
                  else {
                    //invalid term
                  }
                } else {
                  //invalid term
                }

              } else {
                //Invalid function name
              }
            } else if (blockInputTerms[2].contains("Broken")) {

              //BROKEN
              if (blockInputTerms[2].equals("isBroken")) {

              } else if (blockInputTerms[2].equals("notBroken")) {

              } else {
                //Invalid function name
              }
            } else if (blockInputTerms[2].contains("Freezing")) {

              //FREEZING
              if (blockInputTerms[2].equals("isFreezing")) {

              } else if (blockInputTerms[2].equals("notFreezing")) {

              } else {
                //Invalid function name
              }
            } else if (blockInputTerms[2].contains("moving")) {

              //MOVING
              if (blockInputTerms[2].equals("movingTo")) {

              } else if (blockInputTerms[2].equals("movingFrom")) {

              } else {
                //Invalid function name
              }
            } else if (blockInputTerms[2].equals("ignore")) {
              termEvals[t] = true;
            } else {
              //Unknown input
            }
            t++;
          }

          //Finished evaluated all terms on LHS
          //find result of all terms
          boolean result = true;
          for (t = 0; t < termEvals.length; t++) {
            result = result & termEvals[t];
          }
          switchInputEval.add(j,result);
        }
      }
      //-------------------------------------------------------------------------------------

      //iterate through eval statements



      //DEBUG: is it smart to ignore block logic on a switch? time will tell
      if (currBlock.isSwitch()) {
        //apply switch logic

        //******************************************************************


        //******************************************************************

      } else {
        //apply block logic
      }




      //TODO this should check the direction when integrating red line
//      if (currBlock.getNextBlock1() != -1) {
//        currBlock = myLine.getBlock(currBlock.getNextBlock1());
//      } else if(currBlock.getPreviousBlock() != -1) {
//        currBlock = myLine.getBlock(currBlock.getPreviousBlock());
//      }

      if (prevBlock == null) {
        //initialize first prev block
        if (myLine.getBlock(currBlock.getNextBlock1()) != null) {
          prevBlock = myLine.getBlock(currBlock.getNextBlock1());
        } else if (myLine.getBlock(currBlock.getPreviousBlock()) != null) {
          prevBlock = myLine.getBlock(currBlock.getPreviousBlock());
        } else {
          //DEBUG nb1 and prev found to be null
        }
      } else {
        prevBlock = currBlock;
        currBlock = myLine.getNextBlock(currBlock.getNumber(), prevBlock.getNumber());
        if (currBlock == null) {
          //DEBUG
          break;
        }
      }

    }

    for (Block b : myZone.values()) {


    }

    //TODO these will be voted upon or updated prior to asserting on the track based on above logic
    for(Integer index : myZone.keySet()) {
      myLine.getBlock(index).setAuthority(ctcAuthCurrent.get(index));
      myLine.getBlock(index).setSetPointSpeed(Math.abs(index));
    }
  }
}
