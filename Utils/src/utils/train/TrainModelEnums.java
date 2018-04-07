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

  public enum Failure {
    FAILED("FAILED"),
    WORKING("WORKING");

    private String status;

    private Failure(String status) {
      this.status = status;
    }

    @Override
    public String toString() {
      return status;
    }

  }

  public enum OnOffStatus {
    ON("ON"),
    OFF("OFF"),
    FAILED("FAILED");

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
