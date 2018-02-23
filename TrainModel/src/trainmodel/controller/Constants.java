package trainmodel.controller;

import javafx.scene.paint.Paint;

/**
 * Created by jeremyzang on 1/28/18.
 */
public class Constants {

  public static final String GREEN = "0x23c51bff";
  public static final String RED = Paint.valueOf("Red").toString();

  public static final String OFF = "OFF";
  public static final String ON = "ON";

  public static final String LABEL_STATUS_UNAVAILABLE = "N/A";

  public static double FRICTION_COEFFICIENT_STEEL = 0.6;
  public static final double kgToLbsConversion = 2.20462262185; //1kg = 2.20462262185 lbs;
  public static final double lbsTokgConversion = 0.45359237; //1lb = 0.45359237 kgs;

  public static final double passengerAvgMassKg = 150 * lbsTokgConversion; //assume each person is avg 150lbs
  public static final double passengerAvgMassLbs = 150;
}
