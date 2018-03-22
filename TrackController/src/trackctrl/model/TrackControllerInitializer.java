package trackctrl.model;

import java.util.HashMap;
import java.util.Map;
import trackctrl.model.TrackController;
import trackctrl.model.TrackControllerLineManager;
import trackctrl.view.TrackControllerUserInterface;
import trackmodel.model.Track;

public class TrackControllerInitializer {

  /**
   * Searches the Track Model instances and populates TrackControllerLineManagers
   * and TrackControllers.
   */
  public static void parseTrack() {

    HashMap<String,Track> track = Track.getListOfTracks();
    TrackControllerLineManager[] lms = new TrackControllerLineManager[2];

    //TrackControllerLineManager[] lms = new TrackControllerLineManager[track.size()];

    //Create TrackControllerLineManagers
//    int i = 0;
//    for (Map.Entry<String, Track> entry : track.entrySet()) {
//      i++;
//      lms[i] = new TrackControllerLineManager(entry.getKey());
//      for (String block : entry.getValue().getBlockList()) {
//        //add each block to a track controller
//
//
//      }
//    }

    lms[0] = new TrackControllerLineManager("Green");
    lms[1] = new TrackControllerLineManager("Red");

    //DUMMY DATA
    TrackController[] gtc = new TrackController[10];
    TrackController[] rtc = new TrackController[10];

    for (int i = 0; i < 10; i++) {
      gtc[i] = new TrackController(i + 1, 0);
      lms[0].addController(gtc[i]);
      rtc[i] = new TrackController(i + 1, 0);
      lms[1].addController(rtc[i]);
    }

  }
}
