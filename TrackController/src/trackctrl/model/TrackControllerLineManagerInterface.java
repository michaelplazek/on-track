package trackctrl.model;

//import trackmodel.model.Track;
import java.util.ArrayList;

import javafx.collections.ObservableList;

public interface TrackControllerLineManagerInterface {

  public boolean setAuthority(int block, float authority);

  public boolean setSuggestedSpeed(int block, float speed);

  public boolean setSwitchOverride(int block, boolean state);

  public boolean closeBlock(int id);

  public boolean repairBlock(int id);

  public boolean toggleSwitch(int id);

  public int[][] getOccupancy();

  public int[][] getInfrastructure();

  public String getLine();

  public boolean addController(TrackController newCtrl);

  public ArrayList<TrackController> getControllersList();

}
