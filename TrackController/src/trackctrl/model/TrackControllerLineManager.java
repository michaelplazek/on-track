package trackctrl.model;

import java.util.ArrayList;
import trackctrl.model.TrackController;

public class TrackControllerLineManager implements TrackControllerLineManagerInterface {

  private String line;
  private ArrayList<TrackController> lineControllers;
  private int[][] occupancy, states;

  private static ArrayList<TrackControllerLineManager> lines;

  public TrackControllerLineManager(String line) {
    this.line = line;

    if (!(lines.contains(this))) {
      lines.add(this);
    }
  }

  @Override
  public Boolean setAuthority(int block, float authority) {
    return null;
  }

  @Override
  public Boolean setSuggestedSpeed(int block, float speed) {
    return null;
  }

  @Override
  public int[][] getOccupancy() {
    return new int[0][];
  }

  @Override
  public int[][] getInfrastructure() {
    return new int[0][];
  }

  @Override
  public TrackControllerLineManager getInstance(String line) {

    for (TrackControllerLineManager aLine : lines) {

      if (aLine.line.equals(line)) {
        //line found, return instance
        return aLine;
      }
    }

    return null;
  }
}
