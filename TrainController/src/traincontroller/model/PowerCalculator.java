package traincontroller.model;

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
    if (distanceIntoCurrentBlock >= currentBlock.getSize()) {
      Track track = Track.getTrack(tc.getLine());
      Block temp = tc.getCurrentBlock();
      tc.setCurrentBlock(nextBlock(temp, tc.getLastBlock(), track));
      tc.setLastBlock(temp);
    }
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
    if (currentTemp <= setTemperature - 0.1) {
      tm.setHeaterStatus(OnOffStatus.ON);
    } else if (currentTemp >= setTemperature + 0.1) {
      tm.setAcStatus(OnOffStatus.OFF);
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
  }

  static void executeNormal(TrainController tc) {
    double currentSpeed = tc.getTrainModel().getCurrentSpeed();
    double setSpeed = tc.getSetSpeed();
    double lastIntegral = tc.getIntegral();
    double lastSpeed = tc.getCurrentSpeed();
    double kp = tc.getKp();
    double ki = tc.getKi();
    double integral;
    double speedLimit = getSpeedLimit(tc, getSafeStopDistance(tc));

    if (currentSpeed > setSpeed || currentSpeed > speedLimit) {
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
    double max;
    Track track = Track.getTrack(currentBlock.getLine());
    remainingDistance -= (double)currentBlock.getSize();
    if (remainingDistance <= 0) {
      max = currentBlock.getSpeedLimit();
    } else {
      max = Math.max(currentBlock.getSpeedLimit(), recursiveSpeedLimit(
        nextBlock(currentBlock, lastBlock, track),
        currentBlock, remainingDistance));
      if (currentBlock.isSwitch()) {
        max = Math.max(max, recursiveSpeedLimit(
          track.getNextBlock2(currentBlock.getNumber(), currentBlock.getPreviousBlock()),
          currentBlock, remainingDistance));
      }
    }
    return max;
  }

  static void activateEmergencyBrake(TrainController tc) {
    TrainModelInterface tm = tc.getTrainModel();
    if (tm.getEmergencyBrakeStatus() != OnOffStatus.ON) {
      tc.activateEmergencyBrake();
    }
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
}
