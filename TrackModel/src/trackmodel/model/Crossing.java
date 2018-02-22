package trackmodel.model;

import java.util.List;
import java.util.Objects;

public class Crossing {

  private boolean status;

  public Crossing() {
    status = false;
  }

  public boolean getStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  /**
   * This method will return a string for the crossing.
   * @return A string value will be returned
   */
  public String toString() {
    if (status) {
      return "DOWN";
    } else {
      return "UP";
    }
  }

}
