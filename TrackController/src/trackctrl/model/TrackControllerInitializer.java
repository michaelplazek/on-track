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
      Block startBlock;
      Switch startSwitch;

      startBlock = trackEntry.getValue().getStartBlock();
      TrackController startCtrlr = new TrackController(1, startBlock.getNumber(), trackEntry.getValue().getLine());
      startSwitch = (Switch) startBlock;
      addSwitchController(startSwitch, startCtrlr, trackEntry.getValue());

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
    Switch currSwitch;

    //DEBUG 1: checking condition from NRM switch
    if (!isAdded(currBlock) && !track.getBlock(currBlock.getPreviousBlock()).isSwitch()) {
      lms[lineNum].addController(tc);
      //tc.addBlock(currBlock);
    } else {
      return;
    }

    while (true) {
      if (blocksAdded <= 16) {
        if (!(currBlock.isSwitch())) {
          tc.addBlock(currBlock);
          blocksAdded++;
          currBlock = track.getBlock(currBlock.getNextBlock1());
        } else {

          currSwitch = (Switch) currBlock;

          //if(isAdded(currBlock)) {
            //if (currBlock == track.getStartBlock()) {
              //currSwitch = (Switch) currBlock;
              //addSwitchController(currSwitch, tc, track);
            //}
            //return;
          //}

          System.out.println("currBlock.getNumber() " + currBlock.getNumber());
          System.out.println("track.getStartBlock().getPreviousBlock() " + track.getStartBlock().getPreviousBlock());

          //Adds controller to lms and calls function
          //Should do all switch checks here since I add controllers in this function
          //System.out.println("Block detected as switch: " + currBlock.getSection()
          //    + " " + currBlock.getNumber());
          TrackController newSwitchCtrlr = new TrackController(tc.getId()
              + 1, currBlock.getNumber(), currBlock.getLine());
          lms[lineNum].addController(newSwitchCtrlr);
          addSwitchController(currSwitch, newSwitchCtrlr, track);
        }
      } else {

        if (!(currBlock.isSwitch())) {
          //call self with new tc instance and keep searching

          if (!isAdded(currBlock)) {
            TrackController newSegmentCtrlr = new TrackController(tc.getId()
                + 1, currBlock.getNumber(), currBlock.getLine());
            //System.out.println(currBlock.getNumber() + " -- ADDING AGAIN");
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
    if (create.getNextBlock1() < 0) {
      //SWITCH FROM YARD
      System.out.println("Yard found");

      if(!isAdded(track.getBlock(create.getPreviousBlock()))) {

        TrackController newSegmentCtrlr =
            new TrackController(tc.getId() + 1, create.getNumber(), create.getLine());
        addSectionController(track.getBlock(create.getPreviousBlock()), newSegmentCtrlr, track);
      } else if (!isAdded((track.getBlock(create.getNextBlock2())))) {
        TrackController newSegmentCtrlr =
            new TrackController(tc.getId() + 1, create.getNumber(), create.getLine());
        addSectionController(track.getBlock(create.getNextBlock2()), newSegmentCtrlr, track);
      }
    } else {
      System.out.println("In the ELSE");
      //Create Controller for switch and direct neighboring blocks
      //Call addSegmentController on each divergent path starting with
      //the switch nextBlock1 and nextBlock2
      tc.addBlock(create);
      tc.addBlock(track.getBlock(create.getNextBlock1()));

      Block segN = track.getBlock(create.getNextBlock2());
      Block segP = track.getBlock(create.getPreviousBlock());


      if(segN != null) {
        tc.addBlock(segN);
        //segN = track.getBlock(segN.getNextBlock1());
        TrackController segNCtrlr =
            new TrackController(tc.getId()+1, segN.getNumber(), segN.getLine());
        System.out.println("segN: " + segN.getSection() + " " + segN.getNumber());
        System.out.println("segN.isSwitch?: " + segN.isSwitch());
        addSectionController(segN, segNCtrlr, track);
      }

      if(segP != null) {
        tc.addBlock(segP);
        //segP = track.getBlock(segP.getPreviousBlock());
        TrackController segPCtrlr =
            new TrackController(tc.getId()+2, segP.getNumber(), segP.getLine());
        System.out.println("segP: " + segP.getSection() + " " + segP.getNumber());
        System.out.println("segP.isSwitch?: " + segP.isSwitch());
        addSectionController(segP, segPCtrlr, track);
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
