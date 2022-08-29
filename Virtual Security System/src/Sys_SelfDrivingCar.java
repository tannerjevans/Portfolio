import java.time.LocalDateTime;
import java.util.*;

public class Sys_SelfDrivingCar extends SysSuper_Component {
    private final Sys sysID = Sys.AUTO_CAR; // SysSuper_Component's ID.
    public boolean movable = true;
    public boolean alarmActive = false;

    public static void main(String[] args) {
        Sys_SelfDrivingCar sysSelfDrivingCar = new Sys_SelfDrivingCar();
        sysSelfDrivingCar.run();
    }


    /**
     * Sets boolean that makes the car movable. Used in the isBlocked method
     * to set car to not movable for obstruction.
     *
     * @param isMovable
     * @return
     */
    public boolean setMovable(boolean isMovable) {
        movable = isMovable;
        return movable;
    }


    @Override
    public void run() {

        Track example = new Track(4);
        Car car = new Car(0, "Car0");
        example.startCarPos(car);
        example.driveTrack(example,car);

    }

    public class DataTest extends Data {
        private String text;

        public DataTest( String text) {
            this.text = text;
        }
        //getters and whatever
        public String getString(){
            return text;
        }
    }

    @Override
    protected synchronized void messageProcessor(Message message) {
        if (_PresentationDriver.DEBUG)
            System.out.println(sysID + " rcvd a " + message.code()
                    + " msg from " + message.source());
        if (message.code() == Code.WARNING) {
            setMovable(false);
        }
        if (message.code() == Code.SHUTDOWN_SIGNAL){
            setMovable(false);
        }
        if (message.code() == Code.RESUME_OPERATIONS){
            deviceArray[0].state = State.OFF;
            setMovable(true);
            alarmActive = false;

        }
        if (message.code() == Code.ALARM){
            deviceArray[0].state = State.ON;
            alarmActive = true;
        }
        if (message.code() == Code.ALL_CLEAR){
            deviceArray[0].state = State.OFF;
            alarmActive = false;
            setMovable(true);
        }
        if (message.code() == Code.STATUS){

            String deviceInfo = "";
            if(movable){
                deviceInfo += "Cars have permission to move.\n";
            }
            else{
                deviceInfo += "Cars do NOT have permission to move.\n";
            }

            for (Device d: deviceArray
            ) {
                deviceInfo = deviceInfo + "\n" + (d.name + ": " + d.state.toString());
            }
            
            DataTest dataTest = new DataTest(deviceInfo);
            sendMessage(new Message(sysID, Sys.CONTROLLER, Code.DATA, dataTest));

            if(movable && !alarmActive){
                sendMessage(new Message(sysID, Sys.CONTROLLER, Code.ALL_CLEAR));
            }
            else if(movable && alarmActive){
                sendMessage(new Message(sysID, Sys.CONTROLLER, Code.WARNING));
            }
            else{
                sendMessage(new Message(sysID, Sys.CONTROLLER, Code.DANGER));
            }
        }
    }

    @Override
    protected void initializeDeviceArray() {
        Device[] devices = {
                new Device("Sys_SelfDrivingCar.Car Alarm", State.OFF),
                new Device("Maintenance", State.OFF),
                new Device("Obstruction", State.OFF),
                new Device("TokenSensor", State.ON)
        };
        deviceArray = devices;
    }


    /**
     * Class to instantiate TokenSensor to verify tokens needed for entering car
     * and tokens used to already enter car.
     */
    public class TokenSensor extends Device {
        public boolean carFull = true;
        protected Map<Integer, String> acceptableTokens = new HashMap<>();
        protected Map<Integer, String> usedTokens = new HashMap<>();

        TokenSensor(String name, State state) {
            super(name, state);
        }

        /**
         * Verifies if all tokens have been used to leave.
         *
         * @return Boolean if all acceptable tokens have been used
         */
        public boolean allGuestsInCar() {
            carFull = acceptableTokens.equals(usedTokens);
            return carFull;
        }

        /**
         * insertToken is responsible for input a **valid** token into the
         * usedTokens Map and deleting it from the acceptable token map.
         *
         * @param token
         */
        public void insertToken(Sys_TokenMonitoring.Token token) {
            if (acceptableTokens.containsKey(token.getId())) {
                usedTokens.put(token.getId(), token.getOwner());
                acceptableTokens.remove(token.getId(), token.getOwner());
            } else {
                System.out.println("Not an acceptable token.");
            }
        }
    }

    /**
     * Class that creates Track object.
     */
    protected class Track {
        private LinkedList<Object> track = new LinkedList<>();

        Track(int nodes) {
            for (int i = 0; i < nodes; i++) {
                track.add(i, null);
            }
        }

        public LinkedList<Object> getTrack() {
            return this.track;
        }

        /**
         * Insert's an obstacle into a given location of our track
         *
         * @param location
         */
        public void insertObstacle(int location) {
            track.set(location, "rock");
        }

        /**
         * Initializes start point for car.
         *
         * @param car
         */
        public void startCarPos(Car car) {
            track.set(0, car);
        }

        /**
         * Changes obstacle held in a given node of track to null to represent
         * obstacle being cleared
         * @param location
         */
        public void removeObstacle(int location) {
            track.set(location, null);
            setMovable(true);

        }

        /**
         * Rudimentary driving logic. Sys_SelfDrivingCar.Car moves up one node in Linked List
         * assuming it isn't occupied or obstructed. Sends OBSTRUCTION message
         * to CTRL_SVR if not empty
         *
         * @param track - "track" car drives on.
         * @param car   - Sys_SelfDrivingCar.Car object.
         */
        public void driveCar(Track track, Car car) {
            int next;
            next = track.getTrack().indexOf(car) + 1;
            if (next == track.getTrack().size()) {
                startCarPos(car);
                track.getTrack().set(next - 1, null);
            }
            //Use QUEUE version if running from Sys_SelfDrivingCar not _PresentationDriver to prevent error.
            else if (track.getTrack().get(next) != null) {
                sendMessage(new Message(sysID, Sys.CONTROLLER,Code.OBSTRUCTION));
                //QUEUE.put(new Message(sysID, Sys.CTRL_SVR, Code.OBSTRUCTION));
                car.setMovable(false);
            } else if (track.getTrack().get(next) == null) {
                track.getTrack().set(next, car);
                track.getTrack().set(next - 1, null);

            }
        }

        /**
         * Drives car to new node every 3 seconds starting from node 0, through the length of the
         * LinkedList, back to node 0.
         * @param track- Track object
         * @param car-Sys_SelfDrivingCar.Car object
         */
        public void driveTrack(Track track, Car car) {
            for (int i = 0; i < track.getTrack().size(); i++) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                track.driveCar(track, car);
            }
        }
    }

    public static class Car extends SysSuper_Component {
        private final Sys sysID = Sys.CAR; // SysSuper_Component's ID.
        private final int carID;
        private String carName;
        private boolean movable =true;
        Map<Integer, String> carMap = new HashMap<>(); {

        }

        public Car(int val, String name) {
            this.carID = val;
            this.carName=name;
            carMap.put(val, carName);
        }

        /**
         * gets location of car object on track
         * @param track
         * @param val
         * @return
         */
        public Object getLocation (Track track, int val) {
            return track.getTrack().get(val);
        }

        /**
         * Set if Sys_SelfDrivingCar.Car object is movable
         * @param isMovable
         * @return
         */
        public boolean setMovable(boolean isMovable){
            movable = isMovable;
            return movable;
        }

        /**
         * Retrieves boolean value assigned to Sys_SelfDrivingCar.Car
         * @return
         */
        public boolean getMovable(){return movable;}

        public static void main(String[] args) {
            Car car = new Car(1,"J");
            car.run();
        }

        @Override
        public void run() {
        }

        @Override
        protected synchronized void messageProcessor(Message message) {
            if (_PresentationDriver.DEBUG)
                System.out.println(sysID + " rcvd a " + message.code()
                        + " msg from " + message.source());
        }

        @Override
        protected void initializeDeviceArray() {
            Device[] devices = {
            };
            deviceArray = devices;
        }

    }
}

