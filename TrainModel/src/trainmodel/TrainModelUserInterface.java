package trainmodel;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import trainmodel.view.TrainModelController;

import java.io.IOException;


public class TrainModelUserInterface {

  public static void openTrainModel(String trainID) {
    FXMLLoader loader = new FXMLLoader(TrainModelUserInterface.class.getResource("trainModelView.fxml"));
    loader.setController(new TrainModelController(trainID));
    try {
      Parent root = loader.load();
      Stage stage = new Stage();
      stage.setTitle(trainID);
      stage.setScene(new Scene(root, 850, 500));
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println(e);
    }
  }

}
