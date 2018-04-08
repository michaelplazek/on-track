package trackctrl.model;

import java.util.HashMap;
import java.util.Map;
import trackctrl.model.TrackController;
import trackctrl.model.TrackControllerLineManager;
import trackctrl.view.TrackControllerUserInterface;
import trackmodel.model.Block;
import trackmodel.model.Switch;
import trackmodel.model.Track;

public class TrackControllerInitializer {

  private int lineNum = 0;
  private TrackControllerLineManager[] lms;

  /**
   * Searches the Track Model instances and populates TrackControllerLineManagers
   * and TrackControllers.
   */
  public void parseTrack() {

    boolean ctrlrInit = true;
    HashMap<String,Track> track = Track.getListOfTracks();
    lms = new TrackControllerLineManager[track.size()];

    // Create TrackControllerLineManagers
    for (Map.Entry<String, Track> trackEntry : track.entrySet()) {

      lms[lineNum] = new TrackControllerLineManager(trackEntry.getKey());
      Block currBlock;
      //currBlock = trackEntry.getValue().getStartBlock();
      currBlock =
          trackEntry.getValue().getBlock(trackEntry.getValue().getStartBlock().getNextBlock1());
      TrackController currCtrlr =
          new TrackController(0, currBlock.getNumber(), currBlock.getLine());
      lms[lineNum].addController(currCtrlr);
      addSectionController(currBlock, currCtrlr, trackEntry.getValue());
      lineNum++;
    }
  }


  /** This function creates a TrackController for a particular
   * section or multiple sections, and continues creating until
   * a switch is Encountered.
   * @param start - This is the first block of the track from yard
   * @param tc - This is the TrackController we are adding the new Blocks to
   * @param track - This is the Track object we are currently parsing
   */
  private void addSectionController(Block start, TrackController tc, Track track) {

    int blocksAdded = 0;
    Block currBlock = start;
    //tc.addBlock(currBlock);
    Switch currSwitch;

    if (!isAdded(currBlock)) {

      lms[lineNum].addController(tc);

    } else {
      return;
    }

    //Check if first block passed in was a switch (starting case)
    //DEBUG: May be irrelevent if passing block 63 in instead of 62 (switch)
    //NOT SURE WHAT THIS DOES NOW
    if (start.isSwitch()) {
      //DEBUG: why add this?
      //tc.addBlock(currBlock);
      currSwitch = (Switch) currBlock;
      if (currBlock.getNextBlock1() != -1) {
        //fetch next block from switch
        tc.addBlock(currBlock);
        blocksAdded++;
        currBlock = track.getBlock(currBlock.getNextBlock1());
      } else if (currSwitch.getNextBlock2() != -1) {
        tc.addBlock(currBlock);
        blocksAdded++;
        currBlock = track.getBlock(currSwitch.getNextBlock2());
      }
    }

    while (true) {
      if (blocksAdded <= 16) {
        if (!(currBlock.isSwitch())) {
          tc.addBlock(currBlock);
          blocksAdded++;
          currBlock = track.getBlock(currBlock.getNextBlock1());
        } else {

          currSwitch = (Switch) currBlock;
          //Switch detected, call switch adding function
          //Create new track controller for switch
          //DEBUG:

          //TODO
          //Check if TrackController for switch has already been created

          if (isAdded(currBlock)) {
            break;
          }

          //System.out.println("currBlock.getNumber() " + currBlock.getNumber());
          //System.out.println("track.getStartBlock().getPreviousBlock() " +
          // track.getStartBlock().getPreviousBlock());

          //DEBUG: Check for end of track in this function or switch function
          if (currBlock.getNumber() == track.getStartBlock().getPreviousBlock()) {
            return;
          } else if ((currSwitch.getNextBlock1() < 0
              || currSwitch.getNextBlock2() < 0
              || currSwitch.getPreviousBlock() < 0)
              && (isAdded(track.getBlock(currSwitch.getNextBlock1()))
              || isAdded(track.getBlock(currSwitch.getNextBlock2())))) {
            //Yard found
            System.out.println("Yard found");
            return;
          }

          //Adds controller to lms and calls function
          //Should do all switch checks here since I add controllers in this function
          System.out.println("Block detected as switch: " + currBlock.getSection()
              + " " + currBlock.getNumber());
          TrackController newSwitchCtrlr = new TrackController(tc.getId()
              + 1, currBlock.getNumber(), currBlock.getLine());
          lms[lineNum].addController(newSwitchCtrlr);
          currSwitch = (Switch) currBlock;
          addSwitchController(currSwitch, newSwitchCtrlr, track);
          break;
        }
      } else {

        if (!(currBlock.isSwitch())) {
          //call self with new tc instance and keep searching

          if (!isAdded(currBlock)) {
            TrackController newSegmentCtrlr = new TrackController(tc.getId()
                + 1, currBlock.getNumber(), currBlock.getLine());
            System.out.println(currBlock.getNumber() + " -- ADDING AGAIN");
            addSectionController(currBlock, newSegmentCtrlr, track);
          } else {
            break;
          }

        } else {
          //Switch detected, call switch adding function
          TrackController newSwitchCtrlr = new TrackController(tc.getId()
              + 1, currBlock.getNumber(), currBlock.getLine());
          lms[lineNum].addController(newSwitchCtrlr);
          currSwitch = (Switch) currBlock;
          addSwitchController(currSwitch, newSwitchCtrlr, track);
          break;
        }

      }

    }

  }


  /** This function creates a TrackController for an encountered Switch.
   * @param create - This is the Switch object to get a TrackController
   * @param tc - This is the TrackController to add the Switch to
   * @param track - This is the Track object we are currently parsing
   */
  private void addSwitchController(Switch create, TrackController tc, Track track) {
    //Check for ending switch
    if ((create.getNextBlock1() < 0
        || create.getNextBlock2() < 0
        || create.getPreviousBlock() < 0)
        &&  (isAdded(track.getBlock(create.getNextBlock1()))
        || isAdded(track.getBlock(create.getNextBlock2())))) {
      //Yard found
      System.out.println("Yard found");
    } else {
      System.out.println("In the ELSE");
      //Create Controller for switch and direct neighboring blocks
      //Call addSegmentController on each divergent path starting with
      //the switch nextBlock1 and nextBlock2
      tc.addBlock(create);
      tc.addBlock(track.getBlock(create.getPreviousBlock()));

      Block seg1 = track.getBlock(create.getNextBlock1());
      Block seg2 = track.getBlock(create.getNextBlock2());



      if (seg1 != null) {
        tc.addBlock(seg1);
        seg1 = track.getBlock(seg1.getNextBlock1());
        TrackController seg1Ctrlr =
            new TrackController(tc.getId() + 1, seg1.getNumber(), seg1.getLine());
        addSectionController(seg1, seg1Ctrlr, track);
        System.out.println("Seg1: " + seg1.getSection() + " " + seg1.getNumber());
        System.out.println("Seg1.isSwitch?: " + seg1.isSwitch());
      }

      if (seg2 != null) {
        tc.addBlock(seg2);
        seg2 = track.getBlock(seg2.getNextBlock1());
        TrackController seg2Ctrlr =
            new TrackController(tc.getId() + 2, seg2.getNumber(), seg2.getLine());
        addSectionController(seg2, seg2Ctrlr, track);
        System.out.println("Seg2: " + seg2.getSection() + " " + seg2.getNumber());
        System.out.println("Seg2.isSwitch?: " + seg2.isSwitch());
      }
    }
  }

  private boolean isAdded(Block checkBlock) {

    if (checkBlock != null) {
      for (TrackController check : lms[lineNum].getControllersList()) {

        if (check.hasBlock(checkBlock.getNumber())) {
          //Block already parsed, break.
          return true;
        }
      }
    }
    return false;
  }

}
