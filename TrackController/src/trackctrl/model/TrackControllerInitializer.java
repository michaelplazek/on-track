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

  /**
   * Searches the Track Model instances and populates TrackControllerLineManagers
   * and TrackControllers.
   */
  public void parseTrack() {

    boolean ctrlrInit = true;
    HashMap<String,Track> track = Track.getListOfTracks();
    TrackControllerLineManager[] lms = new TrackControllerLineManager[track.size()];

    // Create TrackControllerLineManagers
    int i = 0;
    for (Map.Entry<String, Track> trackEntry : track.entrySet()) {
      // for each String -> Track pair

      lms[i] = new TrackControllerLineManager(trackEntry.getKey());
      int j = 0;
      Switch currSwitch;
      Block currBlock;
      currSwitch = (Switch) trackEntry.getValue().getStartBlock();
      currBlock = currSwitch;
      TrackController curr_ctrlr = new TrackController(0, currBlock.getNumber());

      addSectionController(currBlock, curr_ctrlr, trackEntry.getValue());

//          while (currSwitch.getNextBlock1() != -1 && currSwitch.getNextBlock2() != -1) {
//
//            //Traverse Track until next block is yard
//            //Fix: change loop to curr.nextBlock and remember to start from one block away from start
//            if (j == 15) {
//              //create new track controller and add to that
//
//
//            } else if (currBlock.isSwitch() && j != 0) {
//              //Encountered new switch, reset j and start new track ctrlr
//
//            }
//
//          }
//
//          for (String block : trackEntry.getValue().getBlockList()) { //FIX: this should be a while loop traversing from beginning of track
//
//            if(ctrlrInit) {
//
//              // j==new id foro a ctrlrl i==current block offset in track
//              TrackController tc = new TrackController(j, i);
//              lms[i].addController(tc);
//            }
//
//            //add each block to a track controller
//            //lms[i].ge
//
//            j++;
//          }
          i++;
        }
//
////    lms[0] = new TrackControllerLineManager("Green");
////    lms[1] = new TrackControllerLineManager("Red");
////
////    //DUMMY DATA
////    TrackController[] gtc = new TrackController[10];
////    TrackController[] rtc = new TrackController[10];
////
////    for (int i = 0; i < 10; i++) {
////      gtc[i] = new TrackController(i + 1, 0);
////      lms[0].addController(gtc[i]);
////      rtc[i] = new TrackController(i + 1, 0);
////      lms[1].addController(rtc[i]);
////    }




  }

  private void addSectionController(Block start, TrackController tc, Track track) {

    int blocksAdded = 0;
    Block currBlock = start;
    Switch currSwitch;

    if (start.isSwitch()) {
      tc.addBlock(currBlock);
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
          //Switch detected, call switch adding function
          //Create new track controller for switch
          //DEBUG: 
          TrackController newSwitch = new TrackController(tc.getId() + 1, currBlock.getNumber());
          currSwitch = (Switch) currBlock;
          addSwitchController(currSwitch, newSwitch, track);
          break;
        }
      } else {

        if (!(currBlock.isSwitch())) {
          //call self with new tc instance and keep searching
          TrackController newSegment = new TrackController(tc.getId() + 1, currBlock.getNumber());
          addSectionController(currBlock, newSegment, track);
        } else {
          //Switch detected, call switch adding function
          TrackController newSwitch = new TrackController(tc.getId() + 1, currBlock.getNumber());
          currSwitch = (Switch) currBlock;
          addSwitchController(currSwitch, newSwitch, track);
          break;
        }

      }

    }

  }

  private void addSwitchController(Switch create, TrackController tc, Track track) {

    //Check for ending switch
    if (create.getNextBlock1() == -1 || create.getNextBlock2() == -1) {
      //Yard found
    } else {
      //Create Controller for switch and direct neighboring blocks
      //Call addSegmentController on each divergent path starting with
      //the switch nextBlock1 and nextBlock2
      tc.addBlock(create);
      tc.addBlock(track.getBlock(create.getPreviousBlock()));

      Block seg1 = track.getBlock(create.getNextBlock1());
      Block seg2 = track.getBlock(create.getNextBlock2());
      tc.addBlock(seg1);
      tc.addBlock(seg2);

      TrackController seg1Ctrlr = new TrackController(tc.getId()+1, seg1.getNumber());
      TrackController seg2Ctrlr = new TrackController(tc.getId()+2, seg2.getNumber());

      addSectionController(seg1, seg1Ctrlr, track);
      addSectionController(seg2, seg2Ctrlr, track);
    }
  }

}
