package trackmodel.model;

import java.util.HashMap;

public class Track {

  private String trackName;
  private HashMap<Integer, Block> track;
  private int startBlock;

  /**
   * This is the constructor to create a Track.
   * @param line This will hold the value of a line
   */
  public Track(String line) {
    track = new HashMap<>();
    trackName = line;
  }

  /**
   * This method will return to the user the track's name.
   * @return A String value is returned with the name of the line
   */
  public String getName() {
    return trackName;
  }

  /**
   * This will allow the user to alter the name of a track.
   * @param newName The new name for this instance of the track.
   */
  public void setName(String newName) {
    trackName = newName;
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
    Block temp = track.get(num);
    return temp;
  }

  /**
   * This method will allow for the user to get the next block.
   * @param previousBlock This will return the prior block the train was on
   * @param currentBlock This will be the id of the current block the train is on
   */
  public Block getNextBlock(int previousBlock, int currentBlock) {
    Block temp = track.get(currentBlock);
    if (temp.getPrevious() == previousBlock) {
      return track.get(temp.getNextBlock1());
    } else {
      return track.get(temp.getPrevious());
    }
  }

  /**
   * This method will return the other possible block for the track.
   * @param currentBlock The current block the train is on
   * @return A Block that the train will be going to
   */
  public Block getNextBlock2(int currentBlock) {
    Block temp = track.get(currentBlock);
    if (temp.isSwitchHere()) {
      Switch s = (Switch) temp;
      return track.get(s.getNextBlock2());
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
  public String getBlockString(int num) {
    Block temp = track.get(num);
    if (temp != null) {
      return temp.getSection() + Integer.toString(num);
    } else {
      return "";
    }
  }

}
