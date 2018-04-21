package trackctrl.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.*;

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

  //Parsing logic
  private ArrayList<String[]> blockInputTerms = new ArrayList<String[]>();
  private ArrayList<String[]> blockOutputTerms = new ArrayList<String[]>();
  private ArrayList<String[]> switchInputTerms = new ArrayList<String[]>();
  private ArrayList<String[]> switchOutputTerms = new ArrayList<String[]>();


  //Evaluations of Inputs
  private ArrayList<Boolean> blockInputEval = new ArrayList<Boolean>();
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
    //assertLogic();
  }

  @Override
  public boolean sendTrackSignals(int block, Authority authority, float speed) {
    if (myLine != null) {
      //myLine.getBlock(block).setAuthority(authority);
      //myLine.getBlock(block).setSetPointSpeed(Math.abs(speed));

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
      boolean inSwitch = true;

      line = br.readLine();

      if (line.equals("BLOCK LOGIC")) {
        inSwitch = false;
      }

      while ((line = br.readLine()) != null) {
        if (line.equals("SWITCH LOGIC")) {
          inSwitch = true;
          lineNum = 0;
        } else if (!inSwitch) {

          //Parsing BLOCK SECTION

          String funct = line.split(" THEN ")[0];

          //remove parenthesis
          int paren;
          paren = funct.indexOf('(');
          funct = funct.substring(paren + 1, funct.length() - 1);

          String output = line.split(" THEN ")[1];

          paren = 0;
          paren = output.indexOf('(');
          output = output.substring(paren + 1, output.length() - 1);

          //1 - block/crossing/station keyword
          //2 - relative checking distance and direction
          //3 - function name
          blockInputTerms.add(lineNum, funct.split(" "));

          //1 - blocks/crossing/station
          //2 - relative checking distance and direction
          //3 - state out
          blockOutputTerms.add(lineNum, output.split(" "));
          //System.out.println("DEBUG block: " + lineNum);
          lineNum++;
        } else {

          //Parsing SWITCH SECTION
          //System.out.println("Parsing SWITCH SECTION");
          //System.out.println("line: `" + line + "`");

          if (line.equals("")) break;


          String funct = line.split(" THEN ")[0];
          String output = line.split(" THEN ")[1];
          String allFunct = "";

          //Remove Parenthesis
          Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(funct);
          while (m.find()) {
            allFunct = allFunct + " " + m.group(1);
          }

          allFunct = allFunct.trim();

          //Extract switch number and append to end of string array.
          String[] functTokens = allFunct.split(" ");
          String[] withLength = new String[functTokens.length + 1];

          for(int j = 0; j < functTokens.length; j++) {
            withLength[j] = functTokens[j];
          }

          //String block = functTokens[0].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0];
          String num = functTokens[0].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[1];

          withLength[withLength.length - 1] = num;

          int paren = 0;
          paren = output.indexOf('(');
          output = output.substring(paren + 1, output.length() - 1);

          switchInputTerms.add(lineNum, withLength);
          switchOutputTerms.add(lineNum, output.split(" "));
          //System.out.println("DEBUG switch: " + lineNum);
          lineNum++;
        }
      }


      // Overwrite

      // Initialize arrays to lineNum value
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

  //----------------------PLC helper functions-----------------------------------------------------


  private boolean switchIsOccupied(int start, char sign, int blocks ) {

    Block currBlock = myZone.get(start);

    if (currBlock == null) {
      //Block not in zone, debug
      return false;
    }

    boolean occupancy = currBlock.isOccupied();

    //define currBlock and

    //TODO: check for off by one error defined in PLC
    for (int i = 0; i < blocks; i++) {
      Block temp;

      if (sign == '+') {
        if (currBlock.getNextBlock1() > currBlock.getNumber()) {
          //Continue in nextBlock1 direction
          currBlock = myZone.get(currBlock.getNextBlock1());

          if (currBlock != null) {
            occupancy = occupancy | currBlock.isOccupied();
          } else {
            return occupancy;
          }
        } else {

          //Continue in previousBlock direction
          currBlock = myZone.get(currBlock.getPreviousBlock());

          if (currBlock != null) {
            occupancy = occupancy | currBlock.isOccupied();
          } else {
            return occupancy;
          }

        }
      } else {
        if (currBlock.getNextBlock1() < currBlock.getNumber()) {
          //Continue in nextBlock1 direction
          currBlock = myZone.get(currBlock.getNextBlock1());

          if (currBlock != null) {
            occupancy = occupancy | currBlock.isOccupied();
          } else {
            return occupancy;
          }
        } else {

          //Continue in previousBlock direction
          currBlock = myZone.get(currBlock.getPreviousBlock());

          if (currBlock != null) {
            occupancy = occupancy | currBlock.isOccupied();
          } else {
            return occupancy;
          }

        }
      }
    }
    return occupancy;
  }

  /** This function checks the relative number of blocks, blocks, and returns true
  * if there is occupancy in that direction (or up until jurisdiction ends,
  * whichever is first).
  *
  * @param sign direction to travel relative to a block
  * @param blocks number of blocks we are applying boolean logic to
  * @return true if occupied block found, false otherwise
  */
  private boolean isOccupied(char sign, int blocks, String item, Block start) {

    if (start.getNumber() == -1) {
      System.out.println("Yard passed into isOccupied");
      return false;
    }
    Boolean goNext = false;
    Block currBlock = start;


    //set goNext and currBlock based on if it's a block or switch

    if (item.equals("block")) {

      //FOCUSING ON SWITCH FOR NOW
      /*int prev = start.getPreviousBlock();
      int next = start.getNextBlock1();

      if (sign == '-') {
        if (prev < start.getNumber()) goNext = false;
        else goNext = true;
      } else if (sign == '+') {
        if (next > start.getNumber()) goNext = true;
        else goNext = false;
      } else {
        System.out.println("Error: invalid sign detected in PLC");
      }

      //Sets the currBlock to the next block in checking direction
      if(goNext) {
        currBlock = myZone.get(next);
      } else {
        currBlock = myZone.get(prev);
      }*/

    } else if (item.contains("pblock") || item.contains("n1block") || item.contains("n2block")) {

      /*//Set goNext to direction that isn't a switch
      Block check = myZone.get(currBlock.getNextBlock1());
      if (check.isSwitch()) {
        goNext = false;
      } else {
        goNext = true;
      }*/

    } else {
      System.out.println("Error: invalid start to expression detected in PLC");
      System.out.println("Detected: " + item);
    }

    Boolean occupied = currBlock.isOccupied(); //FIX Null pointer exception

    /*if (goNext) {
      //Search occupancy in blocks of greater block number
      //OLD Search occupancy in next blocks
      for (int i = 1; i < blocks; i++) {

        //TODO: -----------------------------------------------------------------------------------
//        occupied = occupied | currBlock.isOccupied();
//        if (currBlock.getNextBlock1() > )


        if (currBlock.getNextBlock1() != -1 && myZone.get(currBlock.getNextBlock1()) != null) {

          occupied = occupied | currBlock.isOccupied();
          currBlock = myZone.get(currBlock.getNextBlock1());
        } else {
          return occupied;
        }
      }
      return occupied;
    } else {
      //Search occupancy in previous blocks
      for (int i = 1; i < blocks; i++) {
        if (currBlock.getPreviousBlock() != -1 && myZone.get(currBlock.getPreviousBlock()) != null) {
          occupied = occupied | currBlock.isOccupied();
          currBlock = myZone.get(currBlock.getPreviousBlock());
        } else {
          return occupied;
        }
      }
      return occupied;
    }*/
    //DEBUG: remove after debugging
    return occupied;
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

  //-----------------------------------------------------------------------------------------------

  /** Uses block occupancies read on this tick to safely change switches/crossings/lights.
   *
   */
  public void assertLogic() {

    //-----------------------ONE iteration of Block ASSERTION---------------------------------
    //Block currBlock = myZone.get(trackOffset);
    //Block prevBlock = null;

    //TODO: use loop here to check block iteration for Debugging
    //DEBUG
    /*ArrayList<Integer> activeSectionIds = new ArrayList<>();

    if (this.id == 2) {
      System.out.println("ID 2");
      for (int f = 73; f <= 84; f++) {
        activeSectionIds.add(f);
      }
      int start = 73;
      int end = 84;
      boolean movingNext1 = true;

      Block testCurr = myZone.get(start);
      Block testPrev;

      if (movingNext1) {
        Block temp = testCurr;
        testCurr = myZone.get(testCurr.getNextBlock1());
        testPrev = temp;
      } else {
        Block temp = testCurr;
        testCurr = myZone.get(testCurr.getPreviousBlock());
        testPrev = temp;
      }

      while (activeSectionIds.contains(testCurr.getNumber())) {
        System.out.println("currBlock: " + testCurr.getNumber());
        Block temp = testCurr;
        testCurr = myLine.getNextBlock(temp.getNumber(), testPrev.getNumber());
        testPrev = temp;
      }
    }


    if (this.id == 7) {
      System.out.println("ID 7");
      for (int f = 14; f <= 27; f++) {
        activeSectionIds.add(f);
      }
      int start = 14;
      int end = 27;
      boolean movingNext1 = false;

      Block testCurr = myZone.get(start);
      Block testPrev;

      if (movingNext1) {
        Block temp = testCurr;
        testCurr = myZone.get(testCurr.getNextBlock1());
        testPrev = temp;
      } else {
        Block temp = testCurr;
        testCurr = myZone.get(testCurr.getPreviousBlock());
        testPrev = temp;
      }

      while (activeSectionIds.contains(testCurr.getNumber())) {
        System.out.println("currBlock: " + testCurr.getNumber());
        Block temp = testCurr;
        testCurr = myLine.getNextBlock(temp.getNumber(), testPrev.getNumber());
        testPrev = temp;
      }

    } else if (this.id == 8) {
      System.out.println("ID 8");
      for (int f = 1; f <= 12; f++) {
        activeSectionIds.add(f);
      }
      int start = 7;
      int end = 3;
      boolean movingNext1 = true;

      Block testCurr = myZone.get(start);
      Block testPrev;

      if (movingNext1) {
        Block temp = testCurr;
        testCurr = myZone.get(testCurr.getNextBlock1());
        testPrev = temp;
      } else {
        Block temp = testCurr;
        testCurr = myZone.get(testCurr.getPreviousBlock());
        testPrev = temp;
      }

      while (activeSectionIds.contains(testCurr.getNumber())) {
        System.out.println("currBlock: " + testCurr.getNumber());
        Block temp = testCurr;
        testCurr = myLine.getNextBlock(temp.getNumber(), testPrev.getNumber());
        testPrev = temp;
      }
    }*/

    //System.out.println("Curr Controller: " + this.id);
    //System.out.println("endBlock: " + endBlock);

    //while (myZone.get(currBlock.getNumber()) != null) {
    for (Block currBlock : myZone.values()) {

      //System.out.println("CurrBlock: " + currBlock.getNumber());

      //TODO: wrap in check if block is occupied
      //Iterate & brake up BLOCK statements
      //-------------------------------------------------------------------------------------
      for (int j = 0; j < blockInputTerms.size(); j++) {

        boolean eval = false;
        String[] currTerm = blockInputTerms.get(j);

        if (currTerm[2].contains("Occupied")) {
          String s = currTerm[1].substring(1,2);
          String bstring = currTerm[1].substring(2,currTerm[1].length() - 1);

          int checkBlocks = Integer.parseInt(bstring);
          char sign = s.toCharArray()[0];

          if (currTerm[2].equals("isOccupied")) {
            //eval = isOccupied(sign,checkBlocks,currTerm[0], currBlock);
            blockInputEval.add(j,eval);

          } else if (currTerm[2].equals("notOccupied")) {
            //eval = !isOccupied(sign,checkBlocks,currTerm[0], currBlock);
            blockInputEval.add(j,eval);

          } else {
            //Invalid function name
          }
        } else if (currTerm[2].contains("Broken")) {
          if (currTerm[2].equals("isBroken")) {
            //TODO
          } else if (currTerm[2].equals("notBroken")) {

          } else {
            //Invalid function name
          }
        } else if (currTerm[2].contains("Freezing")) {
          if (currTerm[2].equals("isFreezing")) {

          } else if (currTerm[2].equals("notFreezing")) {

          } else {
            //Invalid function name
          }
        } else if (currTerm[2].contains("moving")) {
          if (currTerm[2].equals("movingTo")) {

          } else if (currTerm[2].equals("movingFrom")) {

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

        for (int j = 0; j < switchInputTerms.size(); j++) {
          switchInputEval.add(j, false);
        }

        //Evaluate each PLC switch statement
        for (int j = 0; j < switchInputTerms.size(); j++) {

          String[] currInputTerm = switchInputTerms.get(j);

          //Guard checking that the plc line block equals the currBlock
          //If not, go to next switch term
          if (currBlock.getNumber() != Integer.parseInt(currInputTerm[9])) {
            //Switch is not the correct number
            switchInputEval.set(j, false);
            continue;
          }

            //READ line at j from Switch section and evaluate

          //---------------------------------------------------------

          boolean t1 = false;
          boolean t2 = false;

          // previousBlock term [0-2]
          if (!currInputTerm[0].contains("pblock")) System.out.println("Invalid PLC detected");

            String s = currInputTerm[1].substring(1, 2);
            String bstring = currInputTerm[1].substring(2, currInputTerm[1].length() - 1);

            int checkBlocks = Integer.parseInt(bstring);
            char sign = s.toCharArray()[0];

            //if ()

            if (currInputTerm[2].contains("Occupied")) {
              //OCCUPIED

              if (currInputTerm[2].equals("isOccupied")) {
                  // ADD first term eval based on line number

                  switchInputEval.set(j, switchIsOccupied(currSwitch.getPreviousBlock(), sign, checkBlocks));
                  t1 = switchInputEval.get(j);
                  //switchInputEval.add(j, isOccupied(sign, checkBlocks, currInputTerm[0], myLine.getBlock(currSwitch.getPreviousBlock())));
              } else if (currInputTerm[2].equals("notOccupied")) {
                switchInputEval.set(j, !switchIsOccupied(currSwitch.getPreviousBlock(), sign, checkBlocks));
                //switchInputEval.add(j, !isOccupied(sign, checkBlocks, currInputTerm[0], myLine.getBlock(currSwitch.getPreviousBlock())));
              } else {
                //Invalid function name
              }
            } else if (currInputTerm[2].contains("Broken")) {

              //BROKEN
              if (currInputTerm[2].equals("isBroken")) {

              } else if (currInputTerm[2].equals("notBroken")) {

              } else {
                //Invalid function name
              }
            } else if (currInputTerm[2].contains("Freezing")) {

              //FREEZING
              if (currInputTerm[2].equals("isFreezing")) {

              } else if (currInputTerm[2].equals("notFreezing")) {

              } else {
                //Invalid function name
              }
            } else if (currInputTerm[2].contains("moving")) {

              //MOVING
              if (currInputTerm[2].equals("movingTo")) {

              } else if (currInputTerm[2].equals("movingFrom")) {

              } else {
                //Invalid function name
              }
            } else if (currInputTerm[2].equals("ignore")) {
                switchInputEval.set(j, true);
            } else {
              //Unknown input
              System.out.println("Error: unknown input function detected in PLC");
              switchInputEval.set(j, false);
            }

          //---------------------------------------------------------


          // NextBlock1 term [3-5]
          if (!currInputTerm[3].contains("n1block")) System.out.println("Invalid PLC detected");

            if (currInputTerm[5].contains("Occupied")) {
              //OCCUPIED
              s = currInputTerm[4].substring(1, 2);
              bstring = currInputTerm[4].substring(2, currInputTerm[4].length() - 1);

              checkBlocks = Integer.parseInt(bstring);
              sign = s.toCharArray()[0];

              if (currInputTerm[5].equals("isOccupied")) {
                // AND second term eval based on line number w first term eval
                switchInputEval.set(j,t1 & switchIsOccupied(currSwitch.getNextBlock1(), sign, checkBlocks));
                t2 = t1 & switchInputEval.get(j);
                //switchInputEval.set(j,t1 & isOccupied(sign, checkBlocks, currInputTerm[3], myLine.getBlock(currSwitch.getNextBlock1())));
              } else if (currInputTerm[5].equals("notOccupied")) {
                switchInputEval.set(j, t1 & !switchIsOccupied(currSwitch.getNextBlock1(), sign, checkBlocks));
                t2 = t1 & switchInputEval.get(j);
                // switchInputEval.set(j,t1 & !isOccupied(sign, checkBlocks, currInputTerm[3], myLine.getBlock(currSwitch.getNextBlock1())));
              } else {
                //Invalid function name
              }
            } else if (currInputTerm[5].contains("Broken")) {

              //BROKEN
              if (currInputTerm[5].equals("isBroken")) {

              } else if (currInputTerm[5].equals("notBroken")) {

              } else {
                //Invalid function name
              }
            } else if (currInputTerm[5].contains("Freezing")) {

              //FREEZING
              if (currInputTerm[5].equals("isFreezing")) {

              } else if (currInputTerm[5].equals("notFreezing")) {

              } else {
                //Invalid function name
              }
            } else if (currInputTerm[5].contains("moving")) {

              //MOVING
              if (currInputTerm[5].equals("movingTo")) {

              } else if (currInputTerm[5].equals("movingFrom")) {

              } else {
                //Invalid function name
              }
            } else if (currInputTerm[5].equals("ignore")) {
              switchInputEval.set(j,t1 & true);
            } else {
              //Unknown input
              System.out.println("Error: unknown input function detected in PLC");
              switchInputEval.set(j,t1 & false);
            }

          //---------------------------------------------------------

          // NextBlock2 term [6-8]
          if (!currInputTerm[6].contains("n2block")) System.out.println("Invalid PLC detected");

            //Boolean t2 = switchInputEval.get(j);

            if (currInputTerm[8].contains("Occupied")) {
              //OCCUPIED
              s = currInputTerm[7].substring(1, 2);
              bstring = currInputTerm[7].substring(2, currInputTerm[4].length() - 1);

              checkBlocks = Integer.parseInt(bstring);
              sign = s.toCharArray()[0];

              if (currInputTerm[8].equals("isOccupied")) {
                // AND second term eval based on line number w first term eval
                switchInputEval.set(j,t2 & switchIsOccupied(currSwitch.getNextBlock2(), sign, checkBlocks));
                //switchInputEval.set(j,t2 & isOccupied(sign, checkBlocks, currInputTerm[6], myLine.getBlock(currSwitch.getNextBlock2())));
              } else if (currInputTerm[8].equals("notOccupied")) {
                switchInputEval.set(j,t2 & !switchIsOccupied(currSwitch.getNextBlock2(), sign, checkBlocks));
                //switchInputEval.set(j,t2 & !isOccupied(sign, checkBlocks, currInputTerm[6], myLine.getBlock(currSwitch.getNextBlock2())));
              } else {
                //Invalid function name
              }
            } else if (currInputTerm[8].contains("Broken")) {

              //BROKEN
              if (currInputTerm[8].equals("isBroken")) {

              } else if (currInputTerm[8].equals("notBroken")) {

              } else {
                //Invalid function name
              }
            } else if (currInputTerm[8].contains("Freezing")) {

              //FREEZING
              if (currInputTerm[8].equals("isFreezing")) {

              } else if (currInputTerm[8].equals("notFreezing")) {

              } else {
                //Invalid function name
              }
            } else if (currInputTerm[8].contains("moving")) {

              //MOVING
              if (currInputTerm[8].equals("movingTo")) {

              } else if (currInputTerm[8].equals("movingFrom")) {

              } else {
                //Invalid function name
              }
            } else if (currInputTerm[8].equals("ignore")) {
              switchInputEval.set(j,t2 & true);
            } else {
              //Unknown input
              System.out.println("Error: unknown input function detected in PLC");
              switchInputEval.set(j,t2 & false);
            }
        } // J iteration through switchInputTerms

        //System.out.println("Ctrlrl id: " + this.id);
        for (int k = 0; k < switchInputEval.size(); k++) {

          //System.out.println("Term: " + switchOutputTerms.get(k)[2]);

          if (switchInputEval.get(k)) {
            //evaluate switchOutputTerm at k

            if(switchOutputTerms.get(k)[2].equals("switchN1")) {
              //put switch into state N1 (+)

              String block = switchOutputTerms.get(k)[1];

              block = block.substring(1, block.length() - 1);

              int switchNum = Integer.parseInt(block);

              if(currBlock.getNumber() == switchNum) {
                //Looking at correct switch, assert value
                Switch update = (Switch) currBlock;
                update.setStatus(update.getNextBlock1());
              } else {
                System.out.println("Inconsistent switch state found");
              }

            } else if (switchOutputTerms.get(k)[2].equals("switchN2")) {
              //put switch into state N2 (-)

              String block = switchOutputTerms.get(k)[1];

              block = block.substring(1, block.length() - 1);

              int switchNum = Integer.parseInt(block);

              if(currBlock.getNumber() == switchNum) {
                //Looking at correct switch, assert value
                Switch update = (Switch) currBlock;
                update.setStatus(update.getNextBlock2());
              } else {
                System.out.println("Inconsistent switch state found");
              }

            } else {
              System.out.println("Invalid switch state detected");
            }

            //TODO: send CTC signals based on above input


          } else {
            //No check required
            //TODO: send CTC signals here?
          }

        }


        for (Boolean eval : switchInputEval) {

        }




      } //--------------------END IS SWITCH---------------------------------------------


      //DEBUG: is it smart to ignore block logic on a switch? time will tell
      if (currBlock.isSwitch()) {
        //apply switch logic



        //******************************************************************


        //******************************************************************

      } else {
        //apply block logic
      }

//      if (prevBlock == null) {
//        //initialize first prev block
//        if (myLine.getBlock(currBlock.getNextBlock1()) != null) {
//          prevBlock = currBlock;
//          currBlock = myLine.getBlock(currBlock.getNextBlock1());
//          //prevBlock = myLine.getBlock(currBlock.getNextBlock1());
//        } else if (myLine.getBlock(currBlock.getPreviousBlock()) != null) {
//          prevBlock = currBlock;
//          currBlock = myLine.getBlock(currBlock.getPreviousBlock());
//          //prevBlock = myLine.getBlock(currBlock.getPreviousBlock());
//        } else {
//          //DEBUG nb1 and prev found to be null
//          System.out.println("U WOT");
//        }
//      } else {
//        prevBlock = currBlock;
//        //System.out.println("curr block: " + currBlock.getNumber());
//        currBlock = myLine.getNextBlock(currBlock.getNumber(), prevBlock.getNumber());
//        if (currBlock == null) {
//          //DEBUG
//          break;
//        }
//      }

//      if (prevBlock == null) {
//        //initialize first prev block
//        if (myZone.get(currBlock.getNextBlock1()) != null) {
//          prevBlock = currBlock;
//          currBlock = myZone.get(currBlock.getNextBlock1());
//          //prevBlock = myLine.getBlock(currBlock.getNextBlock1());
//        } else if (myZone.get(currBlock.getPreviousBlock()) != null) {
//          prevBlock = currBlock;
//          currBlock = myZone.get(currBlock.getPreviousBlock());
//          //prevBlock = myLine.getBlock(currBlock.getPreviousBlock());
//        } else {
//          //DEBUG nb1 and prev found to be null
//          System.out.println("U WOT");
//        }
//      } else {
//        Block temp = currBlock;
//        //System.out.println("curr block: " + currBlock.getNumber());
//        currBlock = myLine.getNextBlock(temp.getNumber(), prevBlock.getNumber());
//        prevBlock = temp;
//        if (myZone.get(currBlock.getNumber()) == null) {
//          //DEBUG
//          System.out.println("Termination condition found?");
//          break;
//        }
//      }


    } //-------------- END CURRBLOCK ITERATION

    for (Block b : myZone.values()) {


    }

    //TODO these will be voted upon or updated prior to asserting on the track based on above logic
    for(Integer index : myZone.keySet()) {
      myLine.getBlock(index).setAuthority(ctcAuthCurrent.get(index));
      myLine.getBlock(index).setSetPointSpeed(Math.abs(index));
    }
  }
}
