package mbo.model;

public interface MovingBlockOverlayInterface {

  void initialize();

  void run();

  // getters
  String getScheduleName();

  String getDesiredThroughput();

  // setters
  void setScheduleName(String scheduleName);

  void setDesiredThroughput(String desiredThroughput);

}
