package trackctrl.model;

import java.util.ArrayList;
import trackctrl.model.TrackController;

public class TrackControllerLineManager implements TrackControllerLineManagerInterface {

  public String line;
  private ArrayList<TrackController> lineControllers = null;
  private int[][] occupancy;
  private int[][] states;

  private static ArrayList<TrackControllerLineManager> lines = null;

  public TrackControllerLineManager(String line) {
    this.line = line;
    if (lines != null) {
      if (!(lines.contains(this))) {
        lines.add(this);
      }
    } else {
      lines = new ArrayList<TrackControllerLineManager>();
      lines.add(this);
    }

  }

  @Override
  public Boolean setAuthority(int block, float authority) {
    if (lineControllers != null) {
      for (TrackController tc : lineControllers) {
        if (tc.hasBlock(block)) {
          return tc.setAuthority(block, authority);
        }
      }
    }
    return false;
  }

  @Override
  public Boolean setSuggestedSpeed(int block, float speed) {
    if (lineControllers != null) {
      for (TrackController tc : lineControllers) {
        if (tc.hasBlock(block)) {
          return tc.setSuggestedSpeed(block, speed);
        }
      }
    }
    return false;
  }

  //TODO
  @Override
  public int[][] getOccupancy() {
    return new int[0][];
  }

  //TODO
  @Override
  public int[][] getInfrastructure() {
    return new int[0][];
  }

  @Override
  public Boolean addController(TrackController newCtrl) {
    if (lineControllers != null) {
      return lineControllers.add(newCtrl);
    } else {
      lineControllers = new ArrayList<TrackController>();
      return lineControllers.add(newCtrl);
    }
  }

  public static TrackControllerLineManager getInstance(String line) {

    //TODO: make sure no duplicate controllers exist?
    for (TrackControllerLineManager aline : lines) {
      if (aline.line.equals(line)) {
        //line found, return instance
        return aline;
      }
    }
    return null;
  }

  public static ArrayList<TrackControllerLineManager> getLines() {
    if (lines != null) {
      return lines;
    } else {
      return lines = new ArrayList<TrackControllerLineManager>();
    }
  }

}
