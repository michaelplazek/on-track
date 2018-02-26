package ctc.view;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CentralTrafficControlUserInterface {

  /**
   * Main method to  be called from the Main Menu to open window.
   * @param args String[] needed to make Java happy
   */
  public static void main(String[] args) {

    try {
      Parent root1 = (Parent) FXMLLoader.load(
          CentralTrafficControlUserInterface.class.getResource("ctc.fxml"));
      Stage stage = new Stage();
      stage.setScene(new Scene(root1));
      stage.show();
    } catch (IOException e) {
      System.out.println(e);
    }
  }
}
