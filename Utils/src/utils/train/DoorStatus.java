package utils.train;

/**
 * Created by jeremyzang on 4/7/18.
 */
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
