package utils.train;

/**
 * Created by jeremyzang on 2/11/18.
 * This class contains the Enums used for the TrainModel.
 */
public final class TrainModelEnums {

  public enum DoorStatus {
    OPEN("OPEN"),
    CLOSED("CLOSED");

    private String status;

    private DoorStatus(String status) {
      this.status = status;
    }

    @Override
    public String toString() {
      return status;
    }

  }

  public enum TrackLineStatus {
    CONNECTED("CONNECTED"),
    DISCONNECTED("DISCONNECTED");

    private String status;

    private TrackLineStatus(String status) {
      this.status = status;
    }

    @Override
    public String toString() {
      return status;
    }

  }

  public enum AntennaStatus {
    CONNECTED("CONNECTED"),
    DISCONNECTED("DISCONNECTED");

    private String status;

    private AntennaStatus(String status) {
      this.status = status;
    }

    @Override
    public String toString() {
      return status;
    }

  }

  public enum OnOffStatus {
    ON("ON"),
    OFF("OFF");

    private String status;

    private OnOffStatus(String status) {
      this.status = status;
    }

    @Override
    public String toString() {
      return status;
    }
  }

}
