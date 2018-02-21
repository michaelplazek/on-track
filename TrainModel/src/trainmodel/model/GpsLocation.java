package trainmodel.model;

/**
 * Created by jeremyzang on 2/19/18.
 * This file as of 2/21/18 located in TrainModel module for testing. Later development will require this file be
 * located in the Utils package for every module to share.
 */
public class GpsLocation {

  private double latitude;
  private double longitude;

  public GpsLocation(double lattitude, double longitude) {
    this.latitude = lattitude;
    this.longitude = longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

}
