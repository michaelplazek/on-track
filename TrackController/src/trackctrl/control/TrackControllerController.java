package trackctrl.control;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mainmenu.Clock;
import mainmenu.ClockInterface;
import trackctrl.model.TrackController;
import trackctrl.model.TrackControllerLineManager;
import trackmodel.model.Block;
import trackmodel.model.Switch;
import trackmodel.model.Track;
import utils.general.Constants;

import javax.swing.*;

public class TrackControllerController implements Initializable {

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
  @FXML
  private Button checkLogic;

  //Radio Buttons
  @FXML
  private RadioButton manualRad;
  @FXML
  private RadioButton automaticRad;
  @FXML
  private RadioButton lightMainLtoR;
  @FXML
  private RadioButton lightForkLtoR;
  @FXML
  private RadioButton lightMainRtoL;
  @FXML
  private RadioButton lightForkRtoL;
  @FXML
  private RadioButton closedRad;
  @FXML
  private RadioButton openRad;
  @FXML
  private RadioButton alterRad;
  @FXML
  private RadioButton stayRad;
  @FXML
  private RadioButton blockBrokenRad;
  @FXML
  private RadioButton blockRepairedRad;

  //Choice Boxes
  @FXML
  private ChoiceBox blockChoice;

  //Circles (used as lights)
  @FXML
  private Circle fromLight0;
  @FXML
  private Circle fromLight1;
  @FXML
  private Circle mainLight0;
  @FXML
  private Circle mainLight1;
  @FXML
  private Circle forkLight0;
  @FXML
  private Circle forkLight1;
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
  @FXML
  private ImageView lightSwitch;
  @FXML
  private ImageView switchMainRtoL;
  @FXML
  private ImageView switchMainLtoR;
  @FXML
  private ImageView switchForkRtoL;
  @FXML
  private ImageView switchForkLtoR;

  //Labels
  @FXML
  private Label switchFrom;
  @FXML
  private Label switchMain;
  @FXML
  private Label switchFork;

  //Temporary Globals
  ToggleGroup opGroup = new ToggleGroup();
  ToggleGroup lightGroup = new ToggleGroup();
  ToggleGroup crossingGroup = new ToggleGroup();
  ToggleGroup switchGroup = new ToggleGroup();
  ToggleGroup repairGroup = new ToggleGroup();


  private TrackController myController;
  private Track myLine;
  private ClockInterface theClock;
  private boolean isManual = false;
  private Block selBlock;

  public TrackControllerController(String ctrlrId) {
    myController = TrackControllerLineManager.getController(ctrlrId);
    myLine = Track.getTrack(myController.getLine());
  }

  /**
   * FAKE DATA FOR THE UI DEMO.
   */
  private void populateDropDowns() {
    ObservableList<String> blockList = FXCollections.observableArrayList(myController.getZone());
    blockChoice.setValue("Select Block");
    blockChoice.setItems(blockList);

    //Action Event for block selection
    blockChoice.getSelectionModel().selectedItemProperty()
        .addListener((observableValue, oldValue, newValue) -> {
          if (!(blockChoice.getSelectionModel()
              .getSelectedItem().equals("Select Block"))) {

            String sel = newValue.toString();

            sel = sel.split(" ")[1];

            selBlock = myController.getBlock(Integer.parseInt(sel));

            //updateControllerUI(Integer.parseInt(sel));
            //TODO this should get connected to the clock
            run();
          }
        });
  }

  //******************************************************************************************

  private void handleOpGroup(ActionEvent event) {

    if (opGroup.getSelectedToggle().equals(automaticRad)) {
      //Check if we are in automatic mode

      //Assert that all radios are disabled when in automatic mode
      isManual = false;
      disableRadios();
    } else {
      //Manual mode: all other radios should be enabled
      isManual = true;
      enableRadios();
      checkRadios();
    }
  }

   private void handleLightGroup(ActionEvent event) {

    if (lightGroup.getSelectedToggle().equals(lightMainLtoR)) {
      //TODO actually call track controller functions for this
      setMainLightsLtoR();
    } else if (lightGroup.getSelectedToggle().equals(lightForkLtoR)) {
      setForkLightsLtoR();
    } else if (lightGroup.getSelectedToggle().equals(lightMainRtoL)) {
      setMainLightsRtoL();
    } else if (lightGroup.getSelectedToggle().equals(lightForkRtoL)) {
      setForkLightsRtoL();
    } else {
      //Do nothing
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
    String blockString = (String) blockChoice.getSelectionModel().getSelectedItem();
    blockString = blockString.split(" ")[1];
    Block update = myLine.getBlock(Integer.parseInt(blockString));

    if (switchGroup.getSelectedToggle().equals(stayRad) && update.isSwitch()) {
      Switch s = (Switch) update;
      s.toggle();
      updateSwitchState(update);
    } else {
      setSwitchAlter();
    }
  }

  //TODO
  private void handleRepairGroup(ActionEvent event) {
    if (repairGroup.getSelectedToggle().equals(blockBrokenRad)) {
      //attempt to close a block for maintainence
      selBlock.setClosedForMaintenance(true);
    } else {
      selBlock.setClosedForMaintenance(false);
    }

    updateBlockStatus(selBlock);
  }

  //TODO
  private void handleImportLogic(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose a PLC  file");
    fileChooser.getExtensionFilters()
        .addAll(new FileChooser.ExtensionFilter("PLC Files", ".plc", ".csv"));

    File inFile = fileChooser.showOpenDialog((Stage) importLogic.getScene().getWindow());

    myController.importLogic(inFile);
  }

  //TODO
  private void handleCheckLogic(ActionEvent event) {
    if (!(myController.checkLogic())) {
      //Display error
    }
  }

  //******************************************************************************************

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
    lightMainLtoR.setToggleGroup(lightGroup);
    lightForkLtoR.setToggleGroup(lightGroup);
    lightMainRtoL.setToggleGroup(lightGroup);
    lightForkRtoL.setToggleGroup(lightGroup);
    lightMainLtoR.setSelected(true);
    lightMainLtoR.setOnAction(this::handleLightGroup);
    lightForkLtoR.setOnAction(this::handleLightGroup);
    lightMainRtoL.setOnAction(this::handleLightGroup);
    lightForkRtoL.setOnAction(this::handleLightGroup);


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

    //Track block maintanence
    blockBrokenRad.setToggleGroup(repairGroup);
    blockRepairedRad.setToggleGroup(repairGroup);
    blockBrokenRad.setOnAction(this::handleRepairGroup);
    blockRepairedRad.setOnAction(this::handleRepairGroup);

  }

  //------------------HELPER FUNCTIONS ----------------------------------------


  private void resetLightSwitch() {
    switchMainRtoL.setOpacity(0);
    switchMainLtoR.setOpacity(0);
    switchForkRtoL.setOpacity(0);
    switchForkLtoR.setOpacity(0);
  }

  private void disableRadios() {
    lightMainLtoR.setDisable(true);
    lightForkLtoR.setDisable(true);
    lightMainRtoL.setDisable(true);
    lightForkRtoL.setDisable(true);
    closedRad.setDisable(true);
    openRad.setDisable(true);
    stayRad.setDisable(true);
    alterRad.setDisable(true);
    blockBrokenRad.setDisable(true);
    blockRepairedRad.setDisable(true);
  }

  private void enableRadios() {
    lightMainLtoR.setDisable(false);
    lightForkLtoR.setDisable(false);
    lightMainRtoL.setDisable(false);
    lightForkRtoL.setDisable(false);
    closedRad.setDisable(false);
    openRad.setDisable(false);
    stayRad.setDisable(false);
    alterRad.setDisable(false);
    blockBrokenRad.setDisable(false);
    blockRepairedRad.setDisable(false);
  }

  private void setMainLightsLtoR() {
    mainLight0.setFill(Paint.valueOf(Constants.GREEN));
    mainLight1.setFill(Paint.valueOf("Red"));
    fromLight0.setFill(Paint.valueOf(Constants.GREEN));
    fromLight1.setFill(Paint.valueOf("Red"));

    //set images
    resetLightSwitch();
    lightSwitch.setOpacity(0);
    switchMainLtoR.setOpacity(100);
    forkSwitch.setOpacity(0);
    inactiveSwitch.setOpacity(0);
    mainSwitch.setOpacity(100);
  }

  private void setMainLightsRtoL() {

    mainLight0.setFill(Paint.valueOf(Constants.RED));
    mainLight1.setFill(Paint.valueOf(Constants.GREEN));
    fromLight0.setFill(Paint.valueOf(Constants.GREEN));
    fromLight1.setFill(Paint.valueOf(Constants.RED));

    //set images
    resetLightSwitch();
    lightSwitch.setOpacity(0);
    switchMainRtoL.setOpacity(100);
    forkSwitch.setOpacity(0);
    inactiveSwitch.setOpacity(0);
    mainSwitch.setOpacity(100);
  }

  private void setForkLightsLtoR() {

    // Find correct light status based on boolean logic
    fromLight0.setFill(Paint.valueOf(Constants.GREEN));
    fromLight1.setFill(Paint.valueOf(Constants.RED));
    forkLight0.setFill(Paint.valueOf(Constants.GREEN));
    forkLight1.setFill(Paint.valueOf(Constants.RED));

    resetLightSwitch();
    lightSwitch.setOpacity(0);
    switchForkLtoR.setOpacity(100);
    forkSwitch.setOpacity(100);
    inactiveSwitch.setOpacity(0);
    mainSwitch.setOpacity(0);
  }

  private void setForkLightsRtoL() {

    fromLight0.setFill(Paint.valueOf(Constants.RED));
    fromLight1.setFill(Paint.valueOf(Constants.GREEN));
    forkLight0.setFill(Paint.valueOf(Constants.RED));
    forkLight1.setFill(Paint.valueOf(Constants.GREEN));

    resetLightSwitch();
    lightSwitch.setOpacity(0);
    switchForkRtoL.setOpacity(100);
    forkSwitch.setOpacity(100);
    inactiveSwitch.setOpacity(0);
    mainSwitch.setOpacity(0);
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
  }

  private void setSwitchAlter() {
    forkSwitch.setOpacity(100);
    inactiveSwitch.setOpacity(0);
    mainSwitch.setOpacity(0);
  }

  private void checkDirection() {
    String block = (String) blockChoice.getSelectionModel().getSelectedItem();
    block = block.split(" ")[1];
    int id = Integer.parseInt(block);
    Switch s = (Switch) myLine.getBlock(id);

    Block n1 = myLine.getBlock(s.getNextBlock1());
    Block n2 = myLine.getBlock(s.getNextBlock2());

//    if(!n1.isBiDirectional()) {
//
//    } else if () {
//
//    }
  }

  private void checkRadios() {
    //This check of the radio buttons responsible for disabling radio
    //buttons in manual mode when a block that is not a switch is selected

    if(isManual) {
      disableRadios();
      blockRepairedRad.setDisable(false);
      blockBrokenRad.setDisable(false);
      if(!selBlock.isSwitch()) {
        lightMainLtoR.setDisable(true);
        lightForkLtoR.setDisable(true);
        lightMainRtoL.setDisable(true);
        lightForkRtoL.setDisable(true);
        stayRad.setDisable(true);
        alterRad.setDisable(true);
      } else if (!selBlock.isCrossing() && !selBlock.isSwitch()) {
        closedRad.setDisable(true);
        openRad.setDisable(true);
      } else {
        enableRadios();
      }
    } else {
      disableRadios();
    }


    //This portion of checkRadios will

  }

  //--------------------------------------------------------------------------------

  /**
   * UPDATE UI FUNCTIONS
   */

  private void updateControllerUI(int id) {

    Block update = myController.getBlock(id);

    updateBlockStatus(update);
    updateSwitchState(update);
    checkRadios();
  }

  private void updateBlockStatus(Block update) {
    if(update.isOccupied()) {
      blockOccupancy.setFill(Paint.valueOf(Constants.GREEN));
    } else {
      blockOccupancy.setFill(Paint.valueOf("Gray"));
    }

    if(update.isClosedForMaintenance()) {
      blockStatus.setFill(Paint.valueOf(Constants.RED));
    } else {
      blockStatus.setFill(Paint.valueOf(Constants.GREEN));
    }
  }

  private void updateSwitchState(Block update) {

    if(update.isSwitch()) {
      Switch updateSwitch = (Switch) update;
      int p = updateSwitch.getPreviousBlock();
      int n1 = updateSwitch.getNextBlock1();
      int n2 = updateSwitch.getNextBlock2();

      Block previous = myLine.getBlock(p);
      Block next1 = myLine.getBlock(n1);
      Block next2 = myLine.getBlock(n2);

      switchFrom.setText(previous.getSection() + p);
      switchMain.setText(next1.getSection() + n1);
      switchFork.setText(next2.getSection() + n2);

      if(updateSwitch.getStatus() == n1) {
        setSwitchStay();
        //if() TODO check lights based on switch and set them properly
      } else {
        setSwitchAlter();
      }
    } else {
      //DISABLE switch things
      setSwitchInactive();
      checkRadios();
    }

  }

  /**
   * This function is run on each tick of the clock and will update data
   * concerning the UI.
   */
  public void run() {
    //Update UI based on changes in Selected block
    //Take snapshot of current block and pass int in
    String curr = (String) blockChoice.getSelectionModel().getSelectedItem();
    curr = curr.split(" ")[1];

    updateControllerUI(Integer.parseInt(curr));
  }

  /**
   * This method will be automatically called upon the initialization of the MVC.
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    theClock = Clock.getInstance();

    //Init UI
    populateDropDowns();
    groupRadioButtons();
    setOpen();
    setSwitchInactive();
    resetLightSwitch();
    importLogic.setOnAction(this::handleImportLogic);
    checkLogic.setOnAction(this::handleCheckLogic);
  }
}
