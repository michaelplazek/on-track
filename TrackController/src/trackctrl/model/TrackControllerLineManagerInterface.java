package trackctrl.model;

//import trackmodel.model.Track;
import java.util.ArrayList;

import javafx.collections.ObservableList;

public interface TrackControllerLineManagerInterface {

  public boolean sendTrackSignals(int blockId, float speed, float authority);

  public boolean setSwitchOverride(int block, boolean state);

  public boolean closeBlock(int id);

  public boolean repairBlock(int id);

  public boolean toggleSwitch(int id);

  public boolean getOccupancy(int id);

  public boolean getInfrastructure(int id);

  public String getLine();

  public boolean addController(TrackController newCtrl);

  public ArrayList<TrackController> getControllersList();

}
