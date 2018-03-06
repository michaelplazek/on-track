package trackctrl.view;

import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller {

  //Toggles
  @FXML
  private ToggleButton modeToggle;
  @FXML
  private ToggleButton lightToggle;
  @FXML
  private ToggleButton crossingToggle;
  @FXML
  private ToggleButton switchToggle;

  //Buttons
  @FXML
  private Button importLogic;

  //Radio Buttons
  @FXML
  private RadioButton manualRad;
  @FXML
  private RadioButton automaticRad;
  @FXML
  private RadioButton redRad;
  @FXML
  private RadioButton greenRad;
  @FXML
  private RadioButton closedRad;
  @FXML
  private RadioButton openRad;
  @FXML
  private RadioButton alterRad;
  @FXML
  private RadioButton stayRad;

  //Choice Boxes
  @FXML
  private ChoiceBox sectionChoice;

  //Spinner
  @FXML
  private Spinner blockSpin;

  //Circles (used as lights)
  @FXML
  private Circle lightGreen;
  @FXML
  private Circle lightRed;
  @FXML
  private Circle blockStatus;
  @FXML
  private Circle blockOccupancy;
  @FXML
  private Circle crossLeft;
  @FXML
  private Circle crossRight;

  //Images
  @FXML
  private ImageView inactiveSwitch;
  @FXML
  private ImageView mainSwitch;
  @FXML
  private ImageView forkSwitch;

  //Labels
  @FXML
  private Label switchFrom;
  @FXML
  private Label switchMain;
  @FXML
  private Label switchFork;

  //Temporary Globals TODO check with team on best practice for globals in controllers
  ToggleGroup opGroup = new ToggleGroup();
  ToggleGroup lightGroup = new ToggleGroup();
  ToggleGroup crossingGroup = new ToggleGroup();
  ToggleGroup switchGroup = new ToggleGroup();

  /**
   * FAKE DATA FOR THE UI DEMO.
   */
  private void populateDropDowns() {
    ObservableList<String> sectionsA = FXCollections.observableArrayList(
             "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M");

    ObservableList<String> sectionsN = FXCollections.observableArrayList(
            "Select section", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
    sectionChoice.setValue("");
    sectionChoice.setItems(sectionsN);
  }

  private void handleOpGroup(ActionEvent event) {

    if (opGroup.getSelectedToggle().equals(automaticRad)) {
      //Check if we are in automatic mode

      //Assert that all radios are disabled when in automatic mode
      disableRadios();
    } else {
      //Manual mode: all other radios should be enabled
      enableRadios();
    }
  }

  private void handleLightGroup(ActionEvent event) {

    if (lightGroup.getSelectedToggle().equals(greenRad)) {
      //Check that track is indeed functional

      //If functional, assert green value in the GUI
      setGreen();
    } else {
      //Assert red value in GUI
      setRed();
    }
  }

  private void handleCrossingGroup(ActionEvent event) {

    if (crossingGroup.getSelectedToggle().equals(closedRad)) {
      //Check that crossing signal is still functional

      //If functional, assert flashing red light
      setClosed();
    } else {
      setOpen();
    }
  }

  private void handleSwitchGroup(ActionEvent event) {

    if (switchGroup.getSelectedToggle().equals(stayRad)) {
      //Check that switch is functional

      //if functional, show image for unaltered path and set labels
      setSwitchStay();
    } else {
      setSwitchAlter();
    }
  }

  private void handleImportLogic(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose a PLC  file");
    fileChooser.getExtensionFilters()
        .addAll(new FileChooser.ExtensionFilter("PLC Files", ".plc", ".csv"));

    File inFile = fileChooser.showOpenDialog((Stage) importLogic.getScene().getWindow());
  }

  private void setSpinnerBounds() {
    SpinnerValueFactory myBlocks = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 150);
    blockSpin.setValueFactory(myBlocks);
  }

  private void groupRadioButtons() {

    /**
     * RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
     *
     */

    //Operational Mode
    //DEFAULT: Automatic
    manualRad.setToggleGroup(opGroup);
    automaticRad.setToggleGroup(opGroup);
    automaticRad.setSelected(true);
    automaticRad.setOnAction(this::handleOpGroup);
    manualRad.setOnAction(this::handleOpGroup);

    //Set other radios to disabled mode
    disableRadios();


    //Lights
    redRad.setToggleGroup(lightGroup);
    greenRad.setToggleGroup(lightGroup);
    greenRad.setSelected(true);
    greenRad.setOnAction(this::handleLightGroup);
    redRad.setOnAction(this::handleLightGroup);


    //Crossing
    closedRad.setToggleGroup(crossingGroup);
    openRad.setToggleGroup(crossingGroup);
    openRad.setSelected(true);
    openRad.setOnAction(this::handleCrossingGroup);
    closedRad.setOnAction(this::handleCrossingGroup);


    //Track Switch
    alterRad.setToggleGroup(switchGroup);
    stayRad.setToggleGroup(switchGroup);
    stayRad.setOnAction(this::handleSwitchGroup);
    alterRad.setOnAction(this::handleSwitchGroup);
  }

  private void disableRadios() {
    redRad.setDisable(true);
    greenRad.setDisable(true);
    closedRad.setDisable(true);
    openRad.setDisable(true);
    stayRad.setDisable(true);
    alterRad.setDisable(true);
  }

  private void enableRadios() {
    redRad.setDisable(false);
    greenRad.setDisable(false);
    closedRad.setDisable(false);
    openRad.setDisable(false);
    stayRad.setDisable(false);
    alterRad.setDisable(false);
  }

  private void setGreen() {
    /**
     * Set our lights to Green
     * Green: ON
     * Red:   OFF
     */
    lightGreen.setFill(Paint.valueOf("Green"));
    lightRed.setFill(Paint.valueOf("Gray"));
  }

  private void setRed() {
    /**
     * Set our lights to Red
     * Green: OFF
     * Red:   ON
     */
    lightRed.setFill(Paint.valueOf("Red"));
    lightGreen.setFill(Paint.valueOf("Gray"));
  }

  private void setClosed() {
    /**
     * Set our crossing to the closed position
     */
    crossLeft.setFill(Paint.valueOf("Red"));
    crossRight.setFill(Paint.valueOf("Gray"));
  }

  private void setOpen() {
    /**
     * Set our crossing to the open position (lights off)
     */
    crossLeft.setFill(Paint.valueOf("Gray"));
    crossRight.setFill(Paint.valueOf("Gray"));
  }

  private void setSwitchInactive() {
    inactiveSwitch.setOpacity(100);
    mainSwitch.setOpacity(0);
    forkSwitch.setOpacity(0);

    switchFrom.setText("--");
    switchMain.setText("--");
    switchFork.setText("--");
  }

  private void setSwitchStay() {
    mainSwitch.setOpacity(100);
    inactiveSwitch.setOpacity(0);
    forkSwitch.setOpacity(0);

    //Find values of selected block if it contains a switch
    demoSwitchLabels();
  }

  private void setSwitchAlter() {
    forkSwitch.setOpacity(100);
    inactiveSwitch.setOpacity(0);
    mainSwitch.setOpacity(0);

    //Find values of selected block if it contains a switch
    demoSwitchLabels();
  }

  private void demoSwitchLabels() {
    switchFrom.setText("N85");
    switchMain.setText("O86");
    switchFork.setText("Q100");
  }

  /**
   * This method will be automatically called upon the initialization of the MVC.
   */
  public void initialize() {
    populateDropDowns();
    setSpinnerBounds();
    groupRadioButtons();
    setOpen();
    setSwitchInactive();
    importLogic.setOnAction(this::handleImportLogic);
  }


}
