package trackctrl.model;

//import trackmodel.model.Track;
import java.util.ArrayList;
import trackctrl.model.TrackController;

public interface TrackControllerLineManagerInterface {

  public boolean setAuthority(int block, float authority);

  public boolean setSuggestedSpeed(int block, float speed);

  public int[][] getOccupancy();

  public int[][] getInfrastructure();

  public boolean addController(TrackController newCtrl);

  //public Boolean createInstances(Track line);
}
