package trackmodel.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import trackmodel.model.Block;
import trackmodel.model.Switch;
import trackmodel.model.Track;
import trackmodel.view.TrackModelUserInterface;

public class TrackModelController {
  //DROPDOWNS
  @FXML
  private ChoiceBox trackSelection;
  @FXML
  private ChoiceBox blockSelection;

  //BLOCK SPINNER
  @FXML
  private Spinner blockNumber;

  //UPLOAD BUTTON
  @FXML private Button uploadButton;

  //Output Labels
  @FXML private Label blockSize;
  @FXML private Label blockGrade;
  @FXML private Label blockElevation;
  @FXML private Label blockCumElevation;
  @FXML private Label blockSpeedLimit;
  @FXML private Label blockSwitch;

  //Indicator Lights
  @FXML private Circle railStatus;
  @FXML private Circle powerStatus;
  @FXML private Circle circuitStatus;
  @FXML private Circle beaconStatus;
  @FXML private Circle crossingStatus;
  @FXML private Circle undergroundStatus;
  @FXML private Circle occupiedStatus;
  @FXML private Circle trackHeating;
  @FXML private Circle stationStatus;

  //FAILURE OBJECTS
  @FXML private CheckMenuItem railFailureSelect;
  @FXML private CheckMenuItem powerFailureSelect;
  @FXML private CheckMenuItem trackFailureSelect;
  @FXML private Button start;
  @FXML private Button end;
  @FXML private MenuButton failures;

  private Block[] blocks;
  private boolean running;
  private static HashMap<String, Track> listOfTracks = new HashMap<>();
  private static HashMap<String, ArrayList<String>> trackSections = new HashMap<>();
  private static HashMap<String, ArrayList<Integer>> trackBlockNum = new HashMap<>();

  /**
   * This method initializes many of the fields.
   */
  public void initialize() {
    uploadButton.setOnAction(this::handleImportTrack);
    start.setOnAction(this::toggleSelectedFailures);
    end.setOnAction(this::toggleSelectedFailures);
  }

  /**
   * Run all instances of tracks.
   */
  public static void runAllInstances() {
    for (String key: listOfTracks.keySet()) {
      listOfTracks.get(key).run();
    }
  }

  public void updateTracks() {
    trackSelection.setItems(FXCollections.observableArrayList(Track.getListOfTracks().keySet()));
  }

  private void run() {
    if (running) {
      update();
    }
  }

  /**
   * Toggle the failures.
   * @param event pass event.
   */
  public void toggleSelectedFailures(ActionEvent event) {
    Button btn = (Button) event.getSource();

    for (MenuItem item : failures.getItems()) {
      if (CheckMenuItem.class.isInstance(item) && CheckMenuItem.class.cast(item).isSelected()) {
        if (item.getId().equals(powerFailureSelect.getId())) {
          if (btn.getId().equals(start.getId())) {
            blocks[(int)blockNumber.getValue() - 1].setPowerStatus(true);
            powerStatus.setFill(Color.GREEN);
          } else if (btn.getId().equals(end.getId())) {
            blocks[(int)blockNumber.getValue() - 1].setPowerStatus(false);
            powerStatus.setFill(Color.WHITE);
          }
        } else if (item.getId().equals(railFailureSelect.getId())) {
          if (btn.getId().equals(start.getId())) {
            blocks[(int)blockNumber.getValue() - 1].setBrokenRailStatus(true);
            railStatus.setFill(Color.GREEN);
          } else if (btn.getId().equals(end.getId())) {
            blocks[(int)blockNumber.getValue() - 1].setBrokenRailStatus(false);
            railStatus.setFill(Color.WHITE);
          }
        } else if (item.getId().equals(trackFailureSelect.getId())) {
          if (btn.getId().equals(start.getId())) {
            blocks[(int)blockNumber.getValue() - 1].setTrackCircuitStatus(true);
            circuitStatus.setFill(Color.GREEN);
          } else if (btn.getId().equals(end.getId())) {
            blocks[(int)blockNumber.getValue() - 1].setTrackCircuitStatus(false);
            circuitStatus.setFill(Color.WHITE);
          }
        }
      }
    }
  }

  private void populateDropdown() {
    ObservableList<String> track = FXCollections.observableArrayList(
        "Select track", "Blue");
    trackSelection.setValue("Select track");
    trackSelection.setItems(track);

    ObservableList<String> block = FXCollections.observableArrayList(
        "Section", "A");
    blockSelection.setValue("Section");
    blockSelection.setItems(block);
  }

  private void populateSpinner() {
    blockNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
        1, 15, 1, 1));

    blockNumber.valueProperty().addListener(new ChangeListener<Integer>() {
      @Override
      public void changed(ObservableValue<? extends Integer> observable,
                          Integer oldVal, Integer newVal) {
        Block picked = blocks[newVal - 1];

        blockSize.setText(Float.toString(picked.getSize()));
        blockGrade.setText(Float.toString(picked.getGrade()));
        blockElevation.setText(Float.toString(picked.getElevation()));
        blockCumElevation.setText(Float.toString(picked.getCumElevation()));
        blockSpeedLimit.setText(Float.toString(picked.getSpeedLimit()));

        if (picked.getBrokenRailStatus()) {
          railStatus.setFill(Color.GREEN);
        } else {
          railStatus.setFill(Color.WHITE);
        }

        if (picked.getPowerStatus()) {
          powerStatus.setFill(Color.GREEN);
        } else {
          powerStatus.setFill(Color.WHITE);
        }

        if (picked.getTrackCircuitStatus()) {
          circuitStatus.setFill(Color.GREEN);
        } else {
          circuitStatus.setFill(Color.WHITE);
        }
        
        if (picked.hasBeacon()) {
          beaconStatus.setFill(Color.GREEN);
        } else {
          beaconStatus.setFill(Color.WHITE);
        }

        if (picked.isOccupied()) {
          occupiedStatus.setFill(Color.GREEN);
        } else {
          occupiedStatus.setFill(Color.WHITE);
        }

        if (picked.isHeated()) {
          trackHeating.setFill(Color.GREEN);
        } else {
          trackHeating.setFill(Color.WHITE);
        }
      }
    });
  }

  private void makeBlockArray() {

    blocks = new Block[15];

    for (int i = 1; i <= 15; i++) {
      Block b = new Block();

      b.setLine("Blue");
      b.setSection("A");
      b.setNumber(i);
      b.setSize(i * (float)2.5);
      b.setGrade((float).2);
      b.setSpeedLimit(55);
      b.setElevation((float).2);
      b.setCumElevation((float).35);

      if (i % 2 == 0) {
        b.setHeated(true);
      }

      if (i == 2) {
        b.setOccupied(true);
      }

      if (i == 3) {
        b.setInfrastructure("STATION;PIONEER");
      } else if (i == 4) {
        b.setInfrastructure("RAILWAY");
      } else if (i == 9) {
        b.setInfrastructure("UNDERGROUND");
      } else {
        b.setInfrastructure("");
      }

      blocks[i - 1] = b;
    }
  }

  private void handleImportTrack(ActionEvent event) {
    FileChooser fc = new FileChooser();
    fc.setTitle("Choose a track file");
    fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));

    File inFile = fc.showOpenDialog((Stage) uploadButton.getScene().getWindow());

    if (inFile != null) {
      importTrackData(inFile);
    }
  }

  private void update() {

  }

  private void importTrackData(File f) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(f));

      String line = br.readLine();
      int i = 0;

      String filename = f.getName();
      int fileNamePeriodPosition = filename.indexOf('.');
      String lineName = filename.substring(0, fileNamePeriodPosition);
      lineName = lineName.toUpperCase();

      Track newTrack = new Track(lineName);

      listOfTracks.put(lineName, newTrack);

      ArrayList<String> sections = new ArrayList<String>();
      ArrayList<Integer> blocks = new ArrayList<Integer>();

      while (line != null) {
        if (i == 0) {
          //System.out.println("Header Line");
          //System.out.println(line);
          String[] splitLine = line.split(",");
          line = br.readLine();
          i++;
        } else {

          String[] splitLine = line.split(",");

          if (!sections.contains(splitLine[1])) {
            sections.add(splitLine[1]);
          }

          if (!blocks.contains(Integer.parseInt(splitLine[2]))) {
            blocks.add(Integer.parseInt(splitLine[2]));
          }

          Block b;

          if (splitLine[6].contains("SWITCH")) {
            // Create a switch for the Track

            String lineId = splitLine[0];
            //System.out.print(lineId + "\t");
            String section = splitLine[1];
            //System.out.print(section + "\t");
            int number = Integer.parseInt(splitLine[2]);
            //System.out.print(number + "\t");
            float len = Float.parseFloat(splitLine[3]);
            //System.out.print(len + "\t");
            float grade = Float.parseFloat(splitLine[4]);
            //System.out.print(grade + "\t");
            int speedLimit = Integer.parseInt(splitLine[5]);
            //System.out.print(speedLimit + "\t");
            String infra = splitLine[6];
            //System.out.print(infra + "\t");
            float elevation = Float.parseFloat(splitLine[7]);
            //System.out.print(elevation + "\t");
            float cumEle = Float.parseFloat(splitLine[8]);
            //System.out.print(cumEle + "\t");
            boolean biDirectional;
            if (splitLine[9].equals("")) {
              biDirectional = false;
            } else {
              biDirectional = true;
            }
            //System.out.print(biDirectional + "\t");
            int previous  = Integer.parseInt(splitLine[10]);
            //System.out.print(previous + "\t");
            int next1 = Integer.parseInt(splitLine[11]);
            //System.out.print(next1 + "\t");


            int next2 = Integer.parseInt(splitLine[12]);
            //System.out.print(next2);

            boolean rightStation = false;
            if (splitLine.length > 13) {
              if (splitLine[13].equals("")) {
                rightStation = false;
              } else {
                rightStation = true;
              }
            }
            //System.out.print(rightStation + "\t");

            boolean leftStation = false;
            if (splitLine.length > 14) {
              if (splitLine[14].equals("")) {
                leftStation = false;
              } else {
                leftStation = true;
              }
            }
            //System.out.print(leftStation + "\t");

            b = new Switch(lineId, section, number, len, grade, speedLimit,
                infra, elevation, cumEle, biDirectional, previous, next1,
                next2, leftStation, rightStation);

            if (splitLine[6].contains("YARD") && splitLine[6].contains("FROM")) {
              newTrack.setStartBlock(number);
            }

            newTrack.addBlock(b);

          } else {
            //Create a Block for the Track

            //System.out.println(splitLine.length);

            String lineId = splitLine[0];
            //System.out.print(lineId + "\t");
            String section = splitLine[1];
            //System.out.print(section + "\t");
            int number = Integer.parseInt(splitLine[2]);
            //System.out.print(number + "\t");
            float len = Float.parseFloat(splitLine[3]);
            //System.out.print(len + "\t");
            float grade = Float.parseFloat(splitLine[4]);
            //System.out.print(grade + "\t");
            int speedLimit = Integer.parseInt(splitLine[5]);
            //System.out.print(speedLimit + "\t");
            String infra = splitLine[6];
            //System.out.print(infra + "\t");
            float elevation = Float.parseFloat(splitLine[7]);
            //System.out.print(elevation + "\t");
            float cumEle = Float.parseFloat(splitLine[8]);
            //System.out.print(cumEle + "\t");
            boolean biDirectional;
            if (splitLine[9].equals("")) {
              biDirectional = false;
            } else {
              biDirectional = true;
            }
            //System.out.print(biDirectional + "\t");
            int previous  = Integer.parseInt(splitLine[10]);
            //System.out.print(previous + "\t");
            int next1 = Integer.parseInt(splitLine[11]);
            //System.out.print(next1 + "\t");

            boolean rightStation = false;
            if (splitLine.length > 13) {
              if (splitLine[13].equals("")) {
                rightStation = false;
              } else {
                rightStation = true;
              }
            }
            //System.out.print(rightStation + "\t");

            boolean leftStation = false;
            if (splitLine.length > 14) {
              if (splitLine[14].equals("")) {
                leftStation = false;
              } else {
                leftStation = true;
              }
            }
            //System.out.print(leftStation + "\t");

            b = new Block(lineId, section, number, len, grade,
                speedLimit, infra, elevation, cumEle, biDirectional,
                previous, next1, leftStation, rightStation);

            newTrack.addBlock(b);
          }

          //System.out.println();
          line = br.readLine();
        }
      }

      trackSections.put(lineName, sections);
      trackBlockNum.put(lineName, blocks);

      //System.out.println(newTrack.getNumberOfBlocks());
    } catch (FileNotFoundException ex) {
      System.out.println("Unable to find the file.");
    } catch (IOException ex) {
      System.out.println("Error reading file");
    }
  }

  /**
   * This method will allow for others to get a Track based on Track Name
   * @param trackName The name of the track the user wants
   * @return A Track object shall be returned or null if invalid trackName
   */
  public Track getTrack(String trackName) {

    Track val = listOfTracks.get(trackName.toUpperCase());

    if (val != null) {
      return val;
    } else {
      return null;
    }
  }
}