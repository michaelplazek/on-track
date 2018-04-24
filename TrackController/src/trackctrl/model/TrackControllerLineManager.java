package trackctrl.model;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import trackmodel.model.Block;
import trackmodel.model.Switch;
import trackmodel.model.Track;

import utils.general.Authority;

public class TrackControllerLineManager implements TrackControllerLineManagerInterface {

  public String line;

  private static ArrayList<TrackControllerLineManager> lines;
  private ArrayList<TrackController> lineControllers;
  private ArrayList<String> lineControllerIds;

  /**
   * Constructor for a TrackControllerLineManager
   * The line argument should be formatted as a line
   * should be displayed. i.e. "Blue", "Red", etc.
   * @param line a string containing a name for the Track Line, usually a color
   */
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

    lineControllers = new ArrayList<TrackController>();
    lineControllerIds = new ArrayList<String>();
  }

  @Override
  public String getLine() {
    return line;
  }

  @Override
  public boolean sendTrackSignals(int block, Authority authority, float setSpeed) {
    if (lineControllers != null) {
      for (TrackController tc : lineControllers) {
        if (tc.hasBlock(block)) {
          return tc.sendTrackSignals(block, authority, setSpeed);
        }
      }
    }
    return false;
  }

  @Override
  public boolean getOccupancy(int id) {
    if (lineControllers != null) {
      for (TrackController tc : lineControllers) {
        if (tc.hasBlock(id)) {
          return tc.getOccupancy(id);
        }
      }
    }
    //DEBUG: this could cause some problems if not checked properly above
    return false;
  }

  @Override
  public boolean addController(TrackController newCtrl) {
    lineControllerIds.add(line + " " + newCtrl.getId());
    return lineControllers.add(newCtrl);
  }

  @Override
  public ArrayList<TrackController> getControllersList() {
    return this.lineControllers;
  }

  /**
   * Returns an instance of a TrackControllerLineManager based on the
   * line name.
   * @param line a string that should correspond to a line created on initialization
   * @return returns a TrackControllerLineManager object that oversees a particular line
   */
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

  /**
   * Returns a controller object based on its Id number.
   * @param ctrlrId String of the controller ID, either "Green 1"
   * @return TrackController object matching the line and id
   */
  public static TrackController getController(String ctrlrId) {
    String[] temp = ctrlrId.split(" ");
    String line = temp[0];
    String id = null;

    line = temp[0];
    id = temp[1];

    for (TrackControllerLineManager lm : lines) {
      if (lm.line.equals(line)) {
        //line found, get ctrlr
        ArrayList ctrlrs = lm.getControllersList();
        return (TrackController) ctrlrs.get(Integer.parseInt(id) - 1);
      }
    }
    return null;
  }

  /**
   * Sets occupancy of a block in the Track Model to true to allow someone to
   * repair the Track Circuit.
   * @param id block identifier for requested block
   * @return boolean indicating success of operation
   */
  public boolean closeBlock(int id) {
    if (lineControllers != null) {
      for (TrackController tc : lineControllers) {
        if (tc.hasBlock(id)) {
          tc.closeBlock(id);
          break;
        }
      }
    }
    return false;
  }

  /**
   * Sets occupancy of a block in the Track Model to false indicating someone
   * repaired the Track Circuit.
   * @param id block identifier for requested block
   * @return boolean indicating success of operation
   */
  public boolean repairBlock(int id) {
    if (lineControllers != null) {
      for (TrackController tc : lineControllers) {
        if (tc.hasBlock(id)) {
          tc.repairBlock(id);
          break;
        }
      }
    }
    return false;
  }

  /**
   * Toggles the state of a switch on a block indicated by id if one exists.
   * @param id block identifier for requested block
   * @return boolean indicating new switch state
   */
  public boolean toggleSwitch(int id) {
    if (lineControllers != null) {
      for (TrackController tc : lineControllers) {
        if (tc.hasBlock(id)) {
          if (tc.getBlock(id).isSwitch()) {
            return tc.toggleSwitch(id);
          }
        }
      }
    }
    return false;
  }

  /**
   * Gives all Line Managers created via a Java ArrayList.
   * @return returns an ArrayList of the created instances
   */
  public static ArrayList<TrackControllerLineManager> getLines() {
    if (lines != null) {
      return lines;
    } else {
      return lines = new ArrayList<TrackControllerLineManager>();
    }
  }

  /** This iterates through all controllers of the current line and runs the
   * respective run function within the controller.
   */
  public static void runControllers() {
    if (lines != null) {

      for (TrackControllerLineManager lm : lines) {
        if (lm.getControllersList().size() != 0) {
          for (TrackController tc : lm.getControllersList()) {
            tc.run();
          }
        }
      }
    }
  }

  /** Static method used to iterate through static list of lines.
   *
   */
  public static void runTrackControllers() {
    for (TrackControllerLineManager tclm : lines) {
      tclm.runControllers();
    }
  }

  public ObservableList<String> getObservableListOfIds() {
    return FXCollections.observableArrayList(this.lineControllerIds);
  }

  /**
   * Returns total number of lines.
   * @return number of lines created
   */
  public static int getLineCount() {
    return lines.size();
  }

}
