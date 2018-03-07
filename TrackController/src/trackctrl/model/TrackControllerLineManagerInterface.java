package trackctrl.model;

//import trackmodel.model.Track;
import java.util.ArrayList;
import trackctrl.model.TrackController;

public interface TrackControllerLineManagerInterface {

  public Boolean setAuthority(int block, float authority);

  public Boolean setSuggestedSpeed(int block, float speed);

  public int[][] getOccupancy();

  public int[][] getInfrastructure();

  public Boolean addController(TrackController newCtrl);

  //public Boolean createInstances(Track line);
}
