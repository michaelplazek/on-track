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
  public static final double LENGTH_OF_CAR = 32.2; //meters
  public static final double LENGTH_OF_TRAIN = LENGTH_OF_CAR * NUMBER_OF_CARS;
  public static final double EMPTY_WEIGHT = 40900; //kilograms (weight of one car)
  public static final double WIDTH_OF_TRAIN = 2.65; //meters
  public static final double HEIGHT_OF_TRAIN = 3.42; //meters
  public static final int MAX_PASSENGERS = 222;
  public static final double MAX_SPEED = 70; //km/hr
  public static final double SERVICE_BRAKE_ACCELERATION = -1.509; // m/s^2 (when train empty)
  public static final double EMERGENCY_BRAKE_ACCELERATION = -3.433; // m/s^2 (when train empty)
  public static final double PASSENGER_WEIGHT = 68.0389; //Weight of one passenger in kg (150lbs)
  public static final int MAX_POWER = 120; //(Max motor power = 120kW)
  public static final double KG_TO_LBS_CONVERSION = 2.20462262185; //1kg = 2.20462262185 lbs;
  public static final double LBS_TO_KG_CONVERSION = 0.45359237; //1lb = 0.45359237 kgs;
  public static final double PASSENGER_AVG_MASS_KG
      = 150 * LBS_TO_KG_CONVERSION; //assume each person is avg 150lbs
  public static final double PASSENGER_AVG_MASS_LBS = 150;
  public static final double TEMPERATURE_RATE_OF_CHANGE = 0.00001666666;
  //1 degree per minute = 1/60000 degree per millisecond.
}
