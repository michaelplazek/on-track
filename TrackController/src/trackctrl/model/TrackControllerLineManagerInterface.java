package trackctrl.model;

//import trackmodel.model.Track;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import utils.general.Authority;

public interface TrackControllerLineManagerInterface {

  public boolean sendTrackSignals(int blockId, Authority speed, float authority);

  public boolean closeBlock(int id);

  public boolean repairBlock(int id);

  public boolean toggleSwitch(int id);

  public boolean getOccupancy(int id);

  public String getLine();

  public boolean addController(TrackController newCtrl);

  public ArrayList<TrackController> getControllersList();

}
