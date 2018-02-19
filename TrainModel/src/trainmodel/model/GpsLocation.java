package trainmodel.model;

/**
 * Created by jeremyzang on 2/19/18.
 */
public class GpsLocation {

  private double lattitude;
  private double longitude;

  public GpsLocation(double lattitude, double longitude) {
    this.lattitude = lattitude;
    this.longitude = longitude;
  }

  public double getLattitude() {
    return lattitude;
  }

  public void setLattitude(double lattitude) {
    this.lattitude = lattitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

}
