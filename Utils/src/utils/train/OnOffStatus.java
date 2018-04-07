package utils.train;

/**
 * Created by jeremyzang on 4/7/18.
 */
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
