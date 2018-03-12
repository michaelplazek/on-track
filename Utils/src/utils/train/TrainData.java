package utils.train;

/**
 * Train data that will not change as the system runs.
 * Data is in metric.
 * Length - meters
 * Weight - kilograms
 * Power - watts
 * Speed - kilometers per hour
 * Acceleration - meters per second^2
 */
public class TrainData {
  public static final int NUMBER_OF_CARS = 2; //Each train has 2 cars
  private static final double LENGTH_OF_CAR = 32.2; //meters
  public static final double LENGTH_OF_TRAIN = LENGTH_OF_CAR * NUMBER_OF_CARS;
  public static final double EMPTY_WEIGHT = 40900; //kilograms (weight of one car)
  public static final double WIDTH_OF_TRAIN = 2.65; //meters
  public static final double HEIGHT_OF_TRAIN = 3.42; //meters
  public static final int MAX_PASSENGERS = 222;
  public static final double MAX_SPEED = 70; //km/hr
  public static final double SERVICE_BRAKE_ACCELERATION = -1.2; // m/s^2 (@ 2/3 load)
  public static final double EMERGENCY_BRAKE_ACCELERATION = -2.73; // m/s^2 (@ 2/3 load)
  public static final double PASSENGER_WEIGHT = 68.0389; //Weight of one passenger in kg (150lbs)
  public static final int MAX_POWER = 120; //(Max motor power = 120kW)

}
