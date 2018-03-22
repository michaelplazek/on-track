package trackctrl.view;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import trackctrl.control.TrackControllerController;

public class TrackControllerUserInterface {

  /**
   * Opens the Track Controller user interface for the specified controller.
   * @param ctrlrId Id of a TrackController
   */
  public static void openTrackController(String ctrlrId) {
    FXMLLoader loader =
        new FXMLLoader(TrackControllerUserInterface.class.getResource("trackCtrl.fxml"));
    loader.setController(new TrackControllerController(ctrlrId));
    try {
      Parent root = loader.load();
      Stage stage = new Stage();
      stage.setTitle(ctrlrId);
      stage.setScene(new Scene(root, 375, 415));
      stage.setResizable(false);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println(e);
    }
  }
}

