package traincontroller.model;

import java.util.HashMap;

public interface TrainControllerInterface {

  void setAntennaSignal(Byte[] signal);

  void setBeaconSignal(Byte[] signal);

  void setTrackCircuitSignal(Byte[] signal);

  void activateEmergencyBrake();

}
