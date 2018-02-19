package trackmodel.model;

class SwitchBlock extends Block {

  private int destination1;
  private int destination2;
  private int currentDestination;

  public SwitchBlock(String line, String section, int block, double length,
                    double grade, double speedLimit, String stationName, double elevation,
                    double cumulativeElevation, boolean occupied, boolean underground,
                    boolean railCrossing, boolean beacon, boolean trackHeating,
                    boolean station, boolean powerFailure, boolean railFailure,
                    boolean trackCircuitFailure, int destination1, int destination2,
                    int currentDestination) {

    super(line, section, block, length, grade, speedLimit, stationName, elevation,
           cumulativeElevation, occupied, underground, railCrossing, beacon, trackHeating,
           station, powerFailure, railFailure, trackCircuitFailure);

    this.destination1 = destination1;
    this.destination2 = destination2;
    this.currentDestination = currentDestination;



  }

  public SwitchBlock(){}
}