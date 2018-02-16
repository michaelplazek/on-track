package traincontroller.model;

public interface TrainControllerInterface {

  void setAntennaSignal(Byte[] signal);

  void setBeaconSignal(Byte[] signal);

  void setTrackCircuitSignal(Byte[] signal);

  /** CTC Signal to start the train. */
  void start(String trainId);

}
