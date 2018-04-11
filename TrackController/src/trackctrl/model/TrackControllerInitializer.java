package trackctrl.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
  private String path = "Utils/src/utils/general/";
  private final String track = "trackCtrlrList.csv";


  /**
   * Initialized track controllers based on config file in Utils
   */
  public void parseConfig() {

    File trackFile = new File(path + track);

    try {
      BufferedReader br = new BufferedReader(new FileReader(trackFile));

      //Read twice to ignore comments
      String line = br.readLine();
      line = br.readLine();
      line = br.readLine();
      line = br.readLine();
      Integer tracksRemaining = Integer.parseInt(line.split(",")[0]);
      int i;
      lms = new TrackControllerLineManager[tracksRemaining+1];

      while ((line = br.readLine()) != null) {
        String[] splitLine = line.split(",");

        //Fetch Track Instance
        Track track = Track.getTrack(splitLine[0]);
        String hexColor = splitLine[1];
        Integer controllers = Integer.parseInt(splitLine[2]);

        //Create new Line manager based on first line info
        TrackControllerLineManager addManager = new TrackControllerLineManager(track.getLine());
        lms[tracksRemaining] = addManager;

        for (i = 0; i < controllers; i++) {
          line = br.readLine();
          splitLine = line.split(",");

          int id = Integer.parseInt(splitLine[0]);
          int offset = Integer.parseInt(splitLine[1]);
          TrackController tc = new TrackController(id, offset, track.getLine());
          int endBlock = Integer.parseInt(splitLine[2]);

          for (int j = offset; j <= endBlock; j++) {
            tc.addBlock(track.getBlock(j));
          }

          if (splitLine.length > 3) {
            offset = Integer.parseInt(splitLine[3]);
            endBlock = Integer.parseInt(splitLine[4]);
            for (int j = offset; j <= endBlock; j++) {
              tc.addBlock(track.getBlock(j));
            }
          }

          tc.loadOccupancy();

          addManager.addController(tc);
        }
        tracksRemaining--;
        i++;
      }

    } catch (FileNotFoundException ex) {
      System.out.println("Unable to find the file.");
    } catch (IOException ex) {
      System.out.println("Error reading file");
    }

    if (trackFile.exists()) {
      System.out.println("Controller Config File Found");
    } else {
      System.out.println("Controller Config File Not Found");
    }
  }

  public void initializeLogic() {
    for (TrackControllerLineManager lm : lms) {
      String line = lm.getLine();
      line = line.toLowerCase();
      for (TrackController tc : lm.getControllersList()) {
          File plc = new File(path + line + tc.getId() + ".plc");
          tc.importLogic(plc);
      }
    }
  }
}
