package ctc.model;

public interface CentralTrafficControlInterface {

  void initialize();

  CentralTrafficControl getInstance();

  void run();

  void exit();
}
