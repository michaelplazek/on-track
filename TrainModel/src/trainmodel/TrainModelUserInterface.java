package trainmodel;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import trainmodel.view.TrainModelController;



public class TrainModelUserInterface extends Application {

  private TrainModelController controller;
  private Parent root;
  private Scene scene;

  //Will be used after Multiple instances issues are worked out.
//  private TrainModelUserInterface() {
//    FXMLLoader loader = new FXMLLoader(getClass().getResource("trainModelView.fxml"));
//
//    try {
//      this.root = loader.load();
//      this.controller = loader.getController();
//      this.scene = new Scene(root);
//    } catch (IOException e) {
//      System.out.println(e);
//    }
//  }

  /**
   * Called by Main Menu to load the window for the TrainModel UI.
   * TODO: Handle case of multiple instances of TrainModelUserInterface and TrainModelController
   */
  public void load() {
    Stage stage = new Stage();
    stage.setScene(this.scene);
    stage.show();
  }

  public TrainModelController getController() {
    return this.controller;
  }

  /**
   * The bellow methods will be used for testing TrainModel individually. Will be deleted in future development
   * once the use case with multiple instances of TrainModelController are worked out.
   *
   */

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("view/trainModelView.fxml"));
    Parent root = loader.load();
    primaryStage.setTitle("Train 1");
    primaryStage.setScene(new Scene(root, 850, 500));
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }

}
