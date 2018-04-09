package utils.train;

/**
 * Created by jeremyzang on 4/7/18.
 */
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