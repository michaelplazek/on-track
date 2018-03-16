package traincontroller.model;

public interface TrainControllerInterface {

  void setAntennaSignal(float speed, float authority);

  void setBeaconSignal(Byte[] signal);

  void setTrackCircuitSignal(Byte[] signal);

  void activateEmergencyBrake();

}
