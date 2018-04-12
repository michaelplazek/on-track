package trackmodel.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import trackmodel.model.Beacon;
import trackmodel.view.TrackModelUserInterface;

public class Track {

  private static Track instance = null;
  private String line;
  private HashMap<Integer, Block> track;
  private int startBlock;
  private ArrayList<Integer> occupiedBlocks;
  private ArrayList<Integer> closedBlocks;
  private HashMap<Integer, String> trackStations;

  private static HashMap<String, Track> listOfTracks = new HashMap<>();
  private static HashMap<String, ArrayList<Integer>> blockNumbers = new HashMap<>();
  private static HashMap<String, ArrayList<String>> sectionsId = new HashMap<>();

  /**
   * This is the constructor to create a Track.
   * @param line This will hold the value of a line
   */
  public Track(String line) {
    this.track = new HashMap<>();
    this.line = line;
    addTrack(line.toUpperCase(), this);
  }

  public void addStations(HashMap<Integer, String> stations) {
    this.trackStations = stations;
  }

  public static void addTrack(String n, Track t) {
    listOfTracks.put(n.toUpperCase(), t);
    TrackModelUserInterface.getInstance().getController().updateTracks();
  }



  /**
   * This method will allow for the user to initialize basic tracks.
   */
  public static void initialize() {

    File f = null;

    int stationId = 1;

    try {
      f = new File(Track.class.getResource("green.csv").toURI());

      try {

        HashMap<Integer, String> stations = new HashMap<Integer, String>();

        BufferedReader br = new BufferedReader(new FileReader(f));

        String line = br.readLine();
        int i = 0;

        String filename = f.getName();
        int fileNamePeriodPosition = filename.indexOf('.');
        String lineName = filename.substring(0, fileNamePeriodPosition);
        lineName = lineName.toUpperCase();

        Track newTrack = new Track(lineName);

        listOfTracks.put(lineName, newTrack);

        ArrayList<String> sections = new ArrayList<String>();
        ArrayList<Integer> blocks = new ArrayList<Integer>();

        while (line != null) {
          if (i == 0) {
            line = br.readLine();
            i++;
          } else {

            String[] splitLine = line.split(",");
            if (!sections.contains(splitLine[1])) {
              sections.add(splitLine[1]);
            }
            if (!blocks.contains(Integer.parseInt(splitLine[2]))) {
              blocks.add(Integer.parseInt(splitLine[2]));
            }
            Block b;

            ArrayList<String> storedStation = new ArrayList<>(stations.values());

            if (splitLine[6].contains("STATION")) {
              String[] splitLine2 = (splitLine[6]).split(";");
              for (int j = 0; j < splitLine2.length; j++) {
                if (splitLine2[j].equals("STATION")) {
                  if(!storedStation.contains(splitLine2[j + 1])) {
                    stations.put(stationId, splitLine2[j + 1]);
                    stationId++;
                  }
                }
              }
            }

            if (splitLine[6].contains("SWITCH")) {

              // Create a switch for the Track
              final String lineId = splitLine[0];
              final String section = splitLine[1];
              final int number = Integer.parseInt(splitLine[2]);
              final float len = Float.parseFloat(splitLine[3]);
              final float grade = Float.parseFloat(splitLine[4]);
              final int speedLimit = Integer.parseInt(splitLine[5]);
              final String infra = splitLine[6];
              final float elevation = Float.parseFloat(splitLine[7]);
              final float cumEle = Float.parseFloat(splitLine[8]);
              boolean biDirectional;
              if (splitLine[9].equals("")) {
                biDirectional = false;
              } else {
                biDirectional = true;
              }
              final int previous  = Integer.parseInt(splitLine[10]);
              final int next1 = Integer.parseInt(splitLine[11]);
              final int next2 = Integer.parseInt(splitLine[12]);
              boolean rightStation = false;
              if (splitLine.length > 13) {
                if (splitLine[13].equals("")) {
                  rightStation = false;
                } else {
                  rightStation = true;
                }
              }
              boolean leftStation = false;
              if (splitLine.length > 14) {
                if (splitLine[14].equals("")) {
                  leftStation = false;
                } else {
                  leftStation = true;
                }
              }
              Beacon blockBeacon = null;
              if (splitLine.length == 21) {
                float distance = Float.parseFloat(splitLine[16]);
                byte station = Byte.parseByte(splitLine[17]);
                boolean right = Integer.parseInt(splitLine[18]) == 1;
                boolean underground = Integer.parseInt(splitLine[19]) == 1;
                String message = splitLine[20];

                blockBeacon = new Beacon(distance, station,
                              right, number, underground, message);
              } else if (splitLine.length == 20) {
                float distance = Float.parseFloat(splitLine[16]);
                byte station = Byte.parseByte(splitLine[17]);
                boolean right = Integer.parseInt(splitLine[18]) == 1;
                boolean underground = Integer.parseInt(splitLine[19]) == 1;

                blockBeacon = new Beacon(distance, station,
                    right, number, underground, "");
              }
              b = new Switch(lineId, section, number, len, grade, speedLimit,
                  infra, elevation, cumEle, biDirectional, previous, next1,
                  next2, leftStation, rightStation, blockBeacon);

              if (splitLine[6].contains("YARD") && splitLine[6].contains("FROM")) {
                newTrack.setStartBlock(b.getNumber());

                Block yard = new Block(lineId, "", -1, 0, 0, 0, "",
                    0, 0, false, -2, number, false, false, null);

                newTrack.addBlock(yard);
              }

              newTrack.addBlock(b);

            } else {

              //Create a Block for the Track
              final String lineId = splitLine[0];
              final String section = splitLine[1];
              final int number = Integer.parseInt(splitLine[2]);
              final float len = Float.parseFloat(splitLine[3]);
              final float grade = Float.parseFloat(splitLine[4]);
              final int speedLimit = Integer.parseInt(splitLine[5]);
              final String infra = splitLine[6];
              final float elevation = Float.parseFloat(splitLine[7]);
              final float cumEle = Float.parseFloat(splitLine[8]);
              boolean biDirectional;
              if (splitLine[9].equals("")) {
                biDirectional = false;
              } else {
                biDirectional = true;
              }
              final int previous  = Integer.parseInt(splitLine[10]);
              final int next1 = Integer.parseInt(splitLine[11]);
              boolean rightStation = false;
              if (splitLine.length > 13) {
                if (splitLine[13].equals("")) {
                  rightStation = false;
                } else {
                  rightStation = true;
                }
              }
              boolean leftStation = false;
              if (splitLine.length > 14) {
                if (splitLine[14].equals("")) {
                  leftStation = false;
                } else {
                  leftStation = true;
                }
              }
              Beacon blockBeacon = null;
              if (splitLine.length == 21) {
                float distance = Float.parseFloat(splitLine[16]);
                byte station = Byte.parseByte(splitLine[17]);
                boolean right = Integer.parseInt(splitLine[18]) == 1;
                boolean underground = Integer.parseInt(splitLine[19]) == 1;
                String message = splitLine[20];

                blockBeacon = new Beacon(distance, station,
                    right, number, underground, message);
              } else if (splitLine.length == 20) {
                float distance = Float.parseFloat(splitLine[16]);
                byte station = Byte.parseByte(splitLine[17]);
                boolean right = Integer.parseInt(splitLine[18]) == 1;
                boolean underground = Integer.parseInt(splitLine[19]) == 1;

                blockBeacon = new Beacon(distance, station,
                    right, number, underground, "");
              }

              b = new Block(lineId, section, number, len, grade,
                  speedLimit, infra, elevation, cumEle, biDirectional,
                  previous, next1, leftStation, rightStation, blockBeacon);

              newTrack.addBlock(b);
            }

            line = br.readLine();
          }
        }

        sectionsId.put(lineName, sections);
        blockNumbers.put(lineName, blocks);


        newTrack.addStations(stations);

      } catch (FileNotFoundException ex) {
        System.out.println("Unable to find the file.");
      } catch (IOException ex) {
        System.out.println("Error reading file");
      }

    } catch (URISyntaxException ue) {
      System.out.println("URI Error");
    }

    if (f.exists()) {
      System.out.println("File Found");
    } else {
      System.out.println("File Not Found");
    }
  }

  public static HashMap<String, Track> getListOfTracks() {
    return listOfTracks;
  }

  /**
   * This method will return to the user the track's name.
   * @return A String value is returned with the name of the line
   */
  public String getLine() {
    return line;
  }

  /**
   * This will allow the user to alter the name of a track.
   * @param line The new name for this instance of the track.
   */
  public void setLine(String line) {
    this.line = line;
  }

  /**
   * Return the number of block in a track.
   * @return An integer with the number of blocks in a track
   */
  public int getNumberOfBlocks() {
    return track.size();
  }

  /**
   * This method will allow for the user to add a new block to the track.
   * @param newBlock A block to add to the track/line
   */
  public void addBlock(Block newBlock) {
    track.put(newBlock.getNumber(), newBlock);
  }

  /**
   * This method will allow the user to get a block with a specific number.
   * @param num number of the desired block
   */
  public Block getBlock(int num) {
    return track.get(num);
  }

  /**
   * This method creates an ArrayList of Strings for the Block names.
   * @return ArrayList of strings for block names
   */
  public ArrayList<String> getBlockList() {
    ArrayList<Block> list =  new ArrayList<>(track.values());
    ArrayList<String> names = new ArrayList<>();

    for (int i = 0; i < list.size(); i++) {
      names.add(list.get(i).getSection() + list.get(i).getNumber());
    }

    return names;
  }

  /**
   * This method will return a list of every block with a station.
   * @return Arraylist of Strings of station names.
   */
  public ArrayList<String> getStationList() {

    ArrayList<Block> list =  new ArrayList<>(track.values());
    ArrayList<String> stations = new ArrayList<>();
    Block block;
    for (int i = 0; i < list.size(); i++) {
      block = list.get(i);
      if (block.isLeftStation() || block.isRightStation()) {
        stations.add(block.getStationName());
      }
    }

    return stations;
  }

  /**
   * This method will allow for the user to get the next block.
   * @param previousBlock This will return the prior block the train was on
   * @param currentBlock This will be the id of the current block the train is on
   */
  public Block getNextBlock(int currentBlock, int previousBlock) {

    Block cur = track.get(currentBlock);
    Block next = track.get(cur.getNextBlock1());

    if (cur.getPreviousBlock() == previousBlock) {

      // track is coming from the opposite direction
      if (!next.isBiDirectional()
          && track.get(next.getNextBlock1()) == cur) {
        return null;
      } else {
        return track.get(cur.getNextBlock1());
      }
    } else {
      return track.get(cur.getPreviousBlock());
    }
  }

  /**
   * This method will return the other possible block for the track.
   * @param currentBlock The current block the train is on
   * @param previousBlock This will return the prior block the train was on
   * @return Returns null if the fork is illegal. Otherwise returns the next block
   */
  public Block getNextBlock2(int currentBlock, int previousBlock) {

    Block temp = track.get(currentBlock);

    if (temp.isSwitch()) {

      Switch s = (Switch) temp;
      if (previousBlock == s.getPreviousBlock()) {

        Block fork = track.get(s.getNextBlock2());
        if (fork.getNumber() == -1) {
          return fork;
        } else if (!fork.isBiDirectional()
            && (fork.getNextBlock1() != s.getNumber())) {
          return fork;
        }
      }
    }

    return null;
  }

  public void setStartBlock(int startBlock) {
    this.startBlock = startBlock;
  }

  public Block getStartBlock() {
    return track.get(startBlock);
  }

  /**
   * This method will return a string to identify the block with.
   * @param num If the number of a block
   * @return A String will be returned that has the section and block number
   */
  public String getBlockName(int num) {
    Block temp = track.get(num);
    if (temp != null) {
      return temp.getSection() + Integer.toString(num);
    } else {
      return null;
    }
  }

  public void run() {

  }

  public void toggleFailure(Block block, String failureType) {
    if (failureType.equals("POWER")) {
      block.setPowerStatus(!block.getPowerStatus());
    } else if (failureType.equals("RAIL")) {
      block.setBrokenRailStatus(!block.getBrokenRailStatus());
    } else if (failureType.equals("CIRCUIT")) {
      block.setTrackCircuitStatus(!block.getBrokenRailStatus());
    }
  }

  public String getOccupiedBlocks() {
    String blocksOccupied = "";

    for (Block b: track.values()) {
      if (b.isOccupied()) {
        if (blocksOccupied.equals("")) {
          blocksOccupied = blocksOccupied + b.getSection() + b.getNumber();
        } else {
          blocksOccupied = blocksOccupied + ", " + b.getSection() + b.getNumber();
        }
      }
    }
    return blocksOccupied;
  }

  public String getClosedBlocks() {
    String blocksClosed = "";

    for (Block b: track.values()) {
      if (b.isClosedForMaintenance() || b.getPowerStatus() ||
          b.getTrackCircuitStatus() || b.getBrokenRailStatus()) {
        if (blocksClosed.equals("")) {
          blocksClosed = blocksClosed + b.getSection() + b.getNumber();
        } else {
          blocksClosed = blocksClosed + ", " + b.getSection() + b.getNumber();
        }
      }
    }
    return blocksClosed;
  }

  /**
   * This method will allow for others to get a Track based on Track Name.
   * @param trackName The name of the track the user wants
   * @return A Track object shall be returned or null if invalid trackName
   */
  public static Track getTrack(String trackName) {

    Track val = listOfTracks.get(trackName.toUpperCase());

    if (val != null) {
      return val;
    } else {
      return null;
    }
  }

  /**
   * This will allow the user to set a block to closed for maintenance.
   * @param blockId The id for a block to close
   * @param status The new status for closed for maintenance
   * @return A boolean value for if the change went through
   */
  public boolean setClosedForMaintenance(int blockId, boolean status) {

    Block b = this.getBlock(blockId);

    b.setClosedForMaintenance(status);

    if (b.isClosedForMaintenance() == status) {
      return true;
    } else {
      return false;
    }
  }


}
