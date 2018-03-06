package trackctrl.model;

//import trackmodel.model.Track;
import trackctrl.model.TrackController;

public interface TrackControllerLineManagerInterface {

  public Boolean setAuthority(int block, float authority);

  public Boolean setSuggestedSpeed(int block, float speed);

  public int[][] getOccupancy();

  public int[][] getInfrastructure();

  public TrackControllerLineManager getInstance(String line);

  //public Boolean createInstances(Track line);
}
