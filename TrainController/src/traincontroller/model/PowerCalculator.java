package traincontroller.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import mainmenu.Clock;
import mainmenu.ClockInterface;
import trackmodel.model.Beacon;
import trackmodel.model.Block;
import trackmodel.model.Switch;
import trackmodel.model.Track;
import traincontroller.enums.Mode;
import trainmodel.model.TrainModelInterface;
import utils.train.DoorStatus;
import utils.train.Failure;
import utils.train.OnOffStatus;
import utils.train.TrainData;
import utils.unitconversion.UnitConversions;

public class PowerCalculator {
  private static ClockInterface clock = Clock.getInstance();

  private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

  /**
   * Updates and runs the train controller.
   * @param tc pass train controller to update
   */
  static void run(TrainController tc) {
    if (clock.getChangeInTime() != 0) {
      updateEstimates(tc);
      updateFailures(tc);
      executeAction(tc);
      updateTemperature(tc);
      updateLights(tc);
      updateModelValues(tc);
    }
  }

  static void updateEstimates(TrainController tc) {
    TrainModelInterface tm = tc.getTrainModel();
    double delta = clock.getChangeInTime() / 1000.0;
    double currentSpeed = tm.getCurrentSpeed();
    double distanceTraveled = currentSpeed * delta;
    double distanceIntoCurrentBlock = tc.getDistanceIntoCurrentBlock() + distanceTraveled;
    Block currentBlock = tc.getCurrentBlock();
    if (currentBlock != null) {
      if (distanceIntoCurrentBlock >= currentBlock.getSize()) {
        distanceIntoCurrentBlock -= currentBlock.getSize();
        Track track = Track.getTrack(tc.getLine());
        Block temp = tc.getCurrentBlock();
        tc.setCurrentBlock(nextBlock(temp, tc.getLastBlock(), track));
        tc.setLastBlock(temp);
      }
      tc.setDistanceIntoCurrentBlock(distanceIntoCurrentBlock);
      Beacon current = tc.getBeacon();
      if (current != null) {
        tc.setDistanceToStation(tc.getDistanceToStation() - distanceTraveled);
      }
      if (tc.getTrainModel().getServiceBrakeStatus() == OnOffStatus.ON) {
        double lastSpeed = tc.getCurrentSpeed();
        double acceleration = Math.abs(currentSpeed - lastSpeed) / delta;
        if (acceleration != 0 && delta != 0) {
          tc.setWeight(TrainController.FORCE_BRAKE_TRAIN_EMPTY / acceleration);
        }
      }
    }
  }

  static void updateLights(TrainController tc) {
    try {
      Date date = sdf.parse(clock.getFormattedTime());
      Date dawn = sdf.parse("06:00:00");
      Date dusk = sdf.parse("18:59:59");
      if (tc.isUnderground() || date.compareTo(dawn) <= 0 || date.compareTo(dusk) >= 0) {
        tc.setLightStatus(OnOffStatus.ON);
      } else if (tc.isAutomatic()) {
        tc.setLightStatus(OnOffStatus.OFF);
      }
    } catch (ParseException e) {
      System.out.println(e);
      tc.setLightStatus(OnOffStatus.ON);
    }
  }

  static Block nextBlock(Block currentBlock, Block previousBlock, Track activeTrack) {
    Block nextBlock;
    if (currentBlock.isSwitch()) {
      Switch sw = (Switch) currentBlock;

      if (activeTrack.getBlock(sw.getNextBlock1()) == previousBlock
          || activeTrack.getBlock(sw.getNextBlock2()) == previousBlock) {
        nextBlock = activeTrack.getBlock(sw.getPreviousBlock());
      } else {
        nextBlock = activeTrack.getBlock(sw.getStatus());
      }
    } else {
      nextBlock = activeTrack.getNextBlock(currentBlock.getNumber(), previousBlock.getNumber());
    }
    return nextBlock;
  }

  static void updateFailures(TrainController tc) {
    TrainModelInterface tm = tc.getTrainModel();
    Failure trackLineStatus = tm.getTrackLineFailureStatus();
    Failure engineStatus = tm.getEngineFailureStatus();
    Failure brakeStatus = tm.getBrakeFailureStatus();

    if (trackLineStatus == Failure.FAILED || engineStatus == Failure.FAILED
        || brakeStatus == Failure.FAILED) {
      tc.setMode(Mode.FAILURE);
    }
  }

  static void executeAction(TrainController tc) {
    Mode mode = tc.getMode();
    if (mode == Mode.FAILURE || mode == Mode.CTC_EMERGENCY_BRAKE)  {
      activateEmergencyBrake(tc);
    } else if (mode == Mode.CTC_BRAKE || mode == Mode.DRIVER_BRAKE) {
      if (tc.isAutomatic() && (tc.getCurrentBlock().getStationName() != null
          || nextBlock(tc.getCurrentBlock(), tc.getLastBlock(),
              Track.getTrack(tc.getLine())).getStationName() != null)
          && tc.getCurrentSpeed() == 0) {
        tc.setWeight(TrainData.EMPTY_WEIGHT * TrainData.NUMBER_OF_CARS
            + TrainData.MAX_PASSENGERS * 2 * 150 * UnitConversions.LBS_TO_KGS);
        TrainModelInterface tm = tc.getTrainModel();
        Beacon beacon = tc.getBeacon();
        if (beacon.isRight() && tm.getRightDoorStatus() != DoorStatus.OPEN) {
          tm.setRightDoorStatus(DoorStatus.OPEN);
        } else if (!beacon.isRight() && tm.getLeftDoorStatus() != DoorStatus.OPEN) {
          tm.setLeftDoorStatus(DoorStatus.OPEN);
        }
      }
      activateServiceBrake(tc);
    } else if (mode == Mode.AT_STATION) {
      executeAtStation(tc);
    } else if (mode == Mode.STATION_BRAKE) {
      executeStopAtStation(tc);
    } else {
      executeNormal(tc);
    }
  }

  static void updateModelValues(TrainController tc) {
    TrainModelInterface tm = tc.getTrainModel();
    tc.setCurrentSpeed(tm.getCurrentSpeed());
    tc.setCurrentTemperature(tm.getCurrentTemp());
  }

  static double getSafeStopDistance(TrainController tc) {
    double acceleration = TrainController.FORCE_BRAKE_TRAIN_EMPTY / tc.getWeight();
    if (acceleration == 0) {
      acceleration = 1.102018; //decceleration of fully loaded train
    }
    double velocity = tc.getTrainModel().getCurrentSpeed();
    double time = velocity / acceleration;
    return velocity * time + .5 * acceleration * time * time;
  }

  static void updateTemperature(TrainController tc) {
    TrainModelInterface tm = tc.getTrainModel();
    double currentTemp = tm.getCurrentTemp();
    double setTemperature = tc.getSetTemperature();
    if (currentTemp <= setTemperature - 0.005) {
      tm.setHeaterStatus(OnOffStatus.ON);
      tm.setAcStatus(OnOffStatus.OFF);
    } else if (currentTemp >= setTemperature + 0.005) {
      tm.setAcStatus(OnOffStatus.ON);
      tm.setHeaterStatus(OnOffStatus.OFF);
    } else {
      tm.setAcStatus(OnOffStatus.OFF);
      tm.setHeaterStatus(OnOffStatus.OFF);
    }
  }

  static void executeAtStation(TrainController tc) {
    activateServiceBrake(tc);
    tc.setPowerCommand(0);
    tc.setWeight(TrainData.EMPTY_WEIGHT * TrainData.NUMBER_OF_CARS
        + TrainData.MAX_PASSENGERS * 2 * 150 * UnitConversions.LBS_TO_KGS);
    TrainModelInterface tm = tc.getTrainModel();
    Beacon beacon = tc.getBeacon();
    if (beacon.isRight() && tm.getRightDoorStatus() != DoorStatus.OPEN) {
      tm.setRightDoorStatus(DoorStatus.OPEN);
    } else if (!beacon.isRight() && tm.getLeftDoorStatus() != DoorStatus.OPEN) {
      tm.setLeftDoorStatus(DoorStatus.OPEN);
    }
  }

  static void executeStopAtStation(TrainController tc) {
    double safeStoppingDistance = getSafeStopDistance(tc);
    if (tc.getBeacon() != null) {
      if (tc.getDistanceToStation() > 0 && tc.getDistanceToStation() - 1 <= safeStoppingDistance) {
        activateServiceBrake(tc);
      } else if (Math.abs(tc.getDistanceToStation()) <= 1 && tc.getCurrentSpeed() == 0) {
        tc.setMode(Mode.AT_STATION);
      } else {
        executeNormal(tc);
      }
    } else {
      executeNormal(tc);
    }
    closeDoors(tc);
  }

  static void executeNormal(TrainController tc) {
    double currentSpeed = tc.getTrainModel().getCurrentSpeed();
    double setSpeed = tc.isAutomatic() ? tc.getSetSpeed() : tc.getDriverSetSpeed();
    double lastIntegral = tc.getIntegral();
    double lastSpeed = tc.getCurrentSpeed();
    double kp = tc.getKp();
    double ki = tc.getKi();
    double integral;
    double safeStoppingDistance = getSafeStopDistance(tc);
    double speedLimit = getSpeedLimit(tc, safeStoppingDistance) * 1000.0 / 3600.0;
    double endOfRoute = Double.MAX_VALUE;
    if (tc.getBlocksLeft() < 10) {
      endOfRoute = getDistanceLeft(tc);
    }

    if (currentSpeed > setSpeed || currentSpeed > speedLimit
        || endOfRoute <= safeStoppingDistance + 10) {
      activateServiceBrake(tc);
      tc.setPowerCommand(0);
    } else {
      deactivateServiceBrake(tc);

      integral = lastIntegral + clock.getChangeInTime() / 1000.0 / 2
          * ((setSpeed - currentSpeed) + (setSpeed - lastSpeed));

      double power = kp * (setSpeed - currentSpeed) + ki * integral;

      if (power > TrainData.MAX_POWER * 100) {
        power = TrainData.MAX_POWER * 100;
        integral = lastIntegral;
      }

      deactivateServiceBrake(tc);
      tc.setIntegral(integral);
      tc.setPowerCommand(power / 100);
    }
    closeDoors(tc);
  }

  static double getSpeedLimit(TrainController tc, double safeStopDistance) {
    double remainingDistance = safeStopDistance + tc.getDistanceIntoCurrentBlock();
    return recursiveSpeedLimit(tc.getCurrentBlock(), tc.getLastBlock(), remainingDistance);
  }

  private static double recursiveSpeedLimit(Block currentBlock, Block lastBlock,
                                            double remainingDistance) {
    if (currentBlock == null) {
      return 70;
    } else if (Double.isNaN(remainingDistance)) {
      return currentBlock.getSpeedLimit();
    }
    double min;
    Track track = Track.getTrack(currentBlock.getLine());
    remainingDistance -= (double)currentBlock.getSize();
    if (remainingDistance <= 0) {
      min = currentBlock.getSpeedLimit();
    } else {
      min = Math.min(currentBlock.getSpeedLimit(), recursiveSpeedLimit(
        nextBlock(currentBlock, lastBlock, track),
        currentBlock, remainingDistance));
      if (currentBlock.isSwitch()) {
        min = Math.min(min, recursiveSpeedLimit(
          track.getNextBlock2(currentBlock.getNumber(), currentBlock.getPreviousBlock()),
          currentBlock, remainingDistance));
      }
    }
    return min;
  }

  private static double getDistanceLeft(TrainController tc) {
    double min = recursiveDistanceLeft(tc.getCurrentBlock(),
        tc.getLastBlock(), tc.getBlocksLeft()) - tc.getDistanceIntoCurrentBlock();
    return tc.getBlocksLeft() == 0 ? min : min + 10;
  }

  private static double recursiveDistanceLeft(Block currentBlock, Block lastBlock,
                                            double blocksLeft) {
    if (currentBlock == null) {
      return Double.MAX_VALUE;
    }
    double min;
    Track track = Track.getTrack(currentBlock.getLine());
    if (blocksLeft <= 0) {
      min = 0;
    } else {
      min = recursiveDistanceLeft(nextBlock(currentBlock, lastBlock, track),
          currentBlock, blocksLeft - 1);
      if (currentBlock.isSwitch()) {
        min = Math.min(min, recursiveDistanceLeft(
            track.getNextBlock2(currentBlock.getNumber(), currentBlock.getPreviousBlock()),
            currentBlock, blocksLeft - 1));
      }
      min += currentBlock.getSize();
    }
    return min;
  }

  static void activateEmergencyBrake(TrainController tc) {
    TrainModelInterface tm = tc.getTrainModel();
    if (tm.getEmergencyBrakeStatus() != OnOffStatus.ON) {
      tc.activateEmergencyBrake();
    }
    closeDoors(tc);
    tc.setPowerCommand(0);
  }

  static void deactivateServiceBrake(TrainController tc) {
    TrainModelInterface tm = tc.getTrainModel();
    if (tm.getServiceBrakeStatus() != OnOffStatus.OFF) {
      tc.setServiceBrake(OnOffStatus.OFF);
    }
  }

  static void activateServiceBrake(TrainController tc) {
    TrainModelInterface tm = tc.getTrainModel();
    if (tm.getServiceBrakeStatus() != OnOffStatus.ON) {
      tc.setServiceBrake(OnOffStatus.ON);
    }
    tc.setPowerCommand(0);
  }

  static void closeDoors(TrainController tc) {
    if (tc.getLeftDoorStatus() == DoorStatus.OPEN) {
      tc.setLeftDoorStatus(DoorStatus.CLOSED);
    }
    if (tc.getRightDoorStatus() == DoorStatus.OPEN) {
      tc.setRightDoorStatus(DoorStatus.CLOSED);
    }
  }
}
