package trackmodel.model;

public class TrackBlock {

    private String line;
    private String section;
    private String block;
    private double length;
    private double grade;
    private double speedLimit;
    private String stationName;
    private double elevation;
    private double cumulativeElevation;
    private boolean occupied;
    private boolean underground;
    private boolean railCrossing;
    private boolean beacon;
    private boolean trackHeating;
    private boolean station;

    private boolean railFailure;
    private boolean powerFailure;
    private boolean trackCircuitFailure;

    public TrackBlock(String l, String s, String b, double le, double g, double sL, String sN, double e, double cE, boolean o, boolean u, boolean rC, boolean be, boolean tH, boolean st, boolean pF, boolean rF, boolean tCF){
        this.line = l;
        this.section = s;
        this.block = b;
        this.length = le;
        this.grade = g;
        this.speedLimit = sL;
        this.stationName = sN;
        this.elevation = e;
        this.cumulativeElevation = cE;
        this.occupied = o;
        this.underground = u;
        this.railCrossing = rC;
        this.beacon = be;
        this.trackHeating = tH;
        this.station = st;
        this.railFailure = rF;
        this.powerFailure = pF;
        this.trackCircuitFailure = tCF;
    }

    public String getLine() {
        return line;
    }

    public String getSection() {
        return section;
    }

    public String getBlock() {
        return block;
    }

    public double getLength(){
        return length;
    }

    public double getGrade(){
        return grade;
    }

    public double getSpeedLimit() {
        return speedLimit;
    }

    public String getStationName() {
        return stationName;
    }

    public double getElevation() {
        return elevation;
    }

    public double getCummulativeElevation(){
        return cumulativeElevation;
    }

    public boolean getOccupied() {
        return occupied;
    }

    public boolean getUnderground() {
        return underground;
    }

    public boolean getRailCrossing() {
        return railCrossing;
    }

    public boolean getBeacon() {
        return beacon;
    }

    public boolean getTrackHeating() {
        return trackHeating;
    }

    public boolean getStation() {
        return station;
    }

    public boolean getRailFailure() {
        return railFailure;
    }

    public void setRailFailure(boolean rF) {
        this.railFailure = rF;
    }

    public boolean getPowerFailure() {
        return powerFailure;
    }

    public void setPowerFailure(boolean pF) {
        this.powerFailure = pF;
    }

    public boolean getTrackCircuitFailure() {
        return trackCircuitFailure;
    }

    public void setTrackCircuitFailure(boolean tCF) {
        this.trackCircuitFailure = tCF;
    }
}
