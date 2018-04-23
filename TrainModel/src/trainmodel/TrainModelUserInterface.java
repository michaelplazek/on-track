package trainmodel;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import trainmodel.controller.TrainModelController;

public class TrainModelUserInterface {

  /**
   * Opens a TrainModel UI window.
   * Called from MainMenu.
   * @param trainId the Id of the train.
   */
  public static void openTrainModel(String trainId) {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(TrainModelUserInterface.class.getResource("view/trainModelView.fxml"));
    loader.setController(new TrainModelController(trainId));
    try {
      Parent root = loader.load();
      Stage stage = new Stage();
      stage.setTitle(trainId);
      Scene scene = new Scene(root, 750, 350);
      scene.getStylesheets().add("utils/ctc_style.css");
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println(e);
    }
  }

}
