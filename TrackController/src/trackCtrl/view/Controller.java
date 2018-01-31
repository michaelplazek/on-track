package trackCtrl.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.shape.Circle;
import javafx.scene.image.ImageView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;

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
    private Button importPLC;

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
    private ChoiceBox sectionCB;

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
    private ImageView staySwitch;
    @FXML
    private ImageView alterSwitch;

    //Labels
    @FXML
    private Label switchFrom;
    @FXML
    private Label switchStay;
    @FXML
    private Label switchAlter;

    //Temporary Globals TODO check with team on best practice for globals in controllers
    ToggleGroup opTG = new ToggleGroup();
    ToggleGroup lightTG = new ToggleGroup();
    ToggleGroup crossingTG = new ToggleGroup();
    ToggleGroup switchTG = new ToggleGroup();

    /**
     * FAKE DATA FOR THE UI DEMO.
     */
    private void populateDropDowns() {
        ObservableList<String> sectionsA = FXCollections.observableArrayList(
                 "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M");

        ObservableList<String> sectionsN = FXCollections.observableArrayList(
                "Select section", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
        sectionCB.setValue("");
        sectionCB.setItems(sectionsN);
    }

    private void handleOpTG(ActionEvent event) {

        if (opTG.getSelectedToggle().equals(automaticRad)) {
            //Check if we are in automatic mode

            //Assert that all radios are disabled when in automatic mode
            disableRadios();
        }
        else {
            //Manual mode: all other radios should be enabled
            enableRadios();
        }
    }

    private void handleLightTG(ActionEvent event) {

        if (lightTG.getSelectedToggle().equals(greenRad)) {
            //Check that track is indeed functional

            //If functional, assert green value in the GUI
            setGreen();
        }
        else {
            //Assert red value in GUI
            setRed();
        }
    }

    private void handleCrossingTG(ActionEvent event) {

        if (crossingTG.getSelectedToggle().equals(closedRad)) {
            //Check that crossing signal is still functional

            //If functional, assert flashing red light
            setClosed();
        }
        else {
            setOpen();
        }
    }

    private void handleSwitchTG(ActionEvent event) {

        if (switchTG.getSelectedToggle().equals(stayRad)) {
            //Check that switch is functional

            //if functional, show image for unaltered path and set labels
            setSwitchStay();
        }
        else {
            setSwitchAlter();
        }
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
        manualRad.setToggleGroup(opTG);
        automaticRad.setToggleGroup(opTG);
        automaticRad.setSelected(true);
        automaticRad.setOnAction(this::handleOpTG);
        manualRad.setOnAction(this::handleOpTG);

        //Set other radios to disabled mode
        disableRadios();


        //Lights
        redRad.setToggleGroup(lightTG);
        greenRad.setToggleGroup(lightTG);
        greenRad.setSelected(true);
        greenRad.setOnAction(this::handleLightTG);
        redRad.setOnAction(this::handleLightTG);


        //Crossing
        closedRad.setToggleGroup(crossingTG);
        openRad.setToggleGroup(crossingTG);
        openRad.setSelected(true);
        openRad.setOnAction(this::handleCrossingTG);
        closedRad.setOnAction(this::handleCrossingTG);


        //Track Switch
        alterRad.setToggleGroup(switchTG);
        stayRad.setToggleGroup(switchTG);
        stayRad.setOnAction(this::handleSwitchTG);
        alterRad.setOnAction(this::handleSwitchTG);
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
        staySwitch.setOpacity(0);
        alterSwitch.setOpacity(0);

        switchFrom.setText("--");
        switchStay.setText("--");
        switchAlter.setText("--");
    }

    private void setSwitchStay() {
        staySwitch.setOpacity(100);
        inactiveSwitch.setOpacity(0);
        alterSwitch.setOpacity(0);

        //Find values of selected block if it contains a switch
        demoSwitchLabels();
    }

    private void setSwitchAlter() {
        alterSwitch.setOpacity(100);
        inactiveSwitch.setOpacity(0);
        staySwitch.setOpacity(0);

        //Find values of selected block if it contains a switch
        demoSwitchLabels();
    }

    private void demoSwitchLabels() {
        switchFrom.setText("N85");
        switchStay.setText("O86");
        switchAlter.setText("Q100");
    }

    /**
     * This method will be automatically called upon the initialization of the MVC.
     */
    public void initialize() {
        populateDropDowns();
        setSpinnerBounds();
        groupRadioButtons();
        setSwitchInactive();
    }


}
