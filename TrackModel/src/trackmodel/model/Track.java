package trackmodel.model;

import java.util.ArrayList;

public class Track {

  private String trackName;
  public ArrayList<Block> track;

  /**
   * This is the constructor to create a Track.
   * @param line This will hold the value of a line
   */
  public Track(String line) {
    track = new ArrayList<Block>();
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
    try {
      track.add(newBlock.getNumber(), newBlock);
    } catch (Exception e) {
      int space = newBlock.getNumber() - track.size();
      for (int i = 0; i < space; i++) {
        track.add(null);
      }
      track.add(newBlock.getNumber(), newBlock);
    }
  }

  /**
   * This method will allow the user to get a block with a specific number.
   * @param num number of the desired block
   */
  public Block getBlock(int num) {
    if (num >= 0 && num < track.size()) {
      Block temp = track.get(num);

      if (temp != null) {
        if (temp.getNumber() == num) {
          return temp;
        }
      }
    }

    for (int i = 0; i < track.size(); i++) {
      Block temp = track.get(i);
      if (temp != null) {
        if (temp.getNumber() == num) {
          return temp;
        }
      }
    }

    return null;
  }

  /**
   * This method will return a string to identify the block with.
   * @param num If the number of a block
   * @return A String will be returned that has the section and block number
   */
  public String getBlockString(int num) {
    if (num >= 0 && num < track.size()) {
      Block temp = track.get(num);

      if (temp != null) {
        if (temp.getNumber() == num) {
          String blockId = temp.getSection() + Integer.toString(num);
          return blockId;
        }
      }
    }

    for (int i = 0; i < track.size(); i++) {
      Block temp = track.get(i);
      if (temp != null) {
        if (temp.getNumber() == num) {
          String blockId = temp.getSection() + Integer.toString(num);
          return blockId;
        }
      }
    }

    return null;
  }

}
