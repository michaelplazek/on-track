package ctc.model;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import trackmodel.model.Track;

public class TrackMaintenance {

  private static TrackMaintenance instance;
  private String line;

  private ObservableList<String> blockList = FXCollections.observableArrayList();
  private ObservableList<String> trackList = FXCollections.observableArrayList("Select track");
  private ObservableList<String> actionsList = FXCollections.observableArrayList(
      "Select action", "Close block", "Repair block", "Toggle switch");

  /**
   * Private constructor.
   */
  private TrackMaintenance() {

    makeTrackList();
  }

  /**
   * This is the logic to maintain a single instance of a CTC object.
   * @return the single instance of the CTC
   */
  public static TrackMaintenance getInstance() {
    if (instance == null) {
      instance = new TrackMaintenance();
    }
    return instance;
  }

  /**
   * Initializes the class.
   */
  public void initialize() {
    makeTrackList();
  }

  /**
   * Create the list of strings for the block dropdown.
   */
  public void makeBlockList() {

    Track track = Track.getListOfTracks().get(line);

    // clear the list before refreshing
    blockList.clear();

    if (track != null) {
      blockList.addAll(track.getBlockList());
    }
  }

  protected void makeTrackList() {

    HashMap<String,Track> track = Track.getListOfTracks();
    for (Map.Entry<String, Track> entry : track.entrySet()) {
      String key = entry.getKey();
      trackList.add(key);
    }

    if (trackList.size() > 1) {
      line = trackList.get(1);
    }
  }

  public String getLine() {
    return line;
  }

  public void setLine(String line) {
    this.line = line;
  }

  public ObservableList<String> getBlockList() {
    return blockList;
  }

  public void setBlockList(ObservableList<String> blockList) {
    this.blockList = blockList;
  }

  public ObservableList<String> getTrackList() {
    return trackList;
  }

  public void setTrackList(ObservableList<String> trackList) {
    this.trackList = trackList;
  }

  public ObservableList<String> getActionsList() {
    return actionsList;
  }

  public void setActionsList(ObservableList<String> actionsList) {
    this.actionsList = actionsList;
  }
}
