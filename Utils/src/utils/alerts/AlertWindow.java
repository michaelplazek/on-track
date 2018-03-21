package utils.alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertWindow {

  private String title;
  private String header;
  private String content;

  /**
   * Default constructor.
   */
  public AlertWindow() {}

  /**
   * Constructor.
   * @param title the title of window
   * @param header the text in the header of the window
   * @param content the text in the main portion of the window
   */
  public AlertWindow(String title, String header, String content) {

    this.title = title;
    this.header = header;
    this.content = content;
  }

  /**
   * Display the window. It will display until the user click "OK" to close it.
   */
  public void show() {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle(this.title);
    alert.setHeaderText(this.header);
    alert.setContentText(this.content);

    alert.showAndWait();
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setHeader(String header) {
    this.header = header;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
