
public class Sys_Exhibit extends SysSuper_Component {
    private final Sys sysID = Sys.EXHIBIT;
    private boolean received = true;
    private boolean AlarmState = false;
    private boolean warningState = false;

    /*
    public static void main(String[] args) {
        Sys_Exhibit exhibit = new Sys_Exhibit();
        exhibit.run();
    }*/


    @Override
    protected synchronized void messageProcessor(Message message) {
        //if The Gate fails or Device suffers a Break.... Stop Heartbeat and messages all components an alarm signal.
        if (message.code() == Code.BREAKAGE) {
            cancelHeartbeat();
            sendMessage(new Message(sysID, Sys.CONTROLLER,Code.ERROR));
            received = false;

            newTimer(0, new SendAlarmToAllTimer());

        }
        //if Sensor changes but does not fail, sends a warning signal to the server and starts a timer,
        //if exhibit does not recieve a signal from someone it will escalate and send an alarm to all components
        if (message.code() == Code.SENSOR_CHANGE) {
            received = false;
            sendMessage(new Message(sysID, Sys.CONTROLLER,Code.WARNING));
            newTimer(30, new SendAlarmToAllTimer());
        }
        //enters alarm mode when told to.
        if(message.code() == Code.ALARM){
            deviceArray[6].state = State.ON;
            deviceArray[7].state = State.ON;
            AlarmState = true;
            System.out.println("EXHIBIT IN ALARM MODE");
        }
        // prints a readout of devices.
        if(message.code() == Code.GET_DATA){
            for (Device d: deviceArray
            ) {
                System.out.println(d.name + ": " + d.state.toString());
            }
        }
        //acknowledges a signal for a warning. Does not escalate issue.
        if(message.code() == Code.RECEIVE){
            received = true;
        }
        if(message.code() == Code.WARNING){
            AlarmState = false;
            warningState = true;
        }
        //acknowledges a signal for all clear
        if(message.code() == Code.ALL_CLEAR){
            AlarmState = false;
            warningState = false;
            startHeartbeat(Sys.EXHIBIT);
            deviceArray[6].state = State.OFF;
            deviceArray[7].state = State.OFF;
        }

        // update controller client of status
        if(message.code() == Code.STATUS){
            String deviceInfo = "";
            for (Device d: deviceArray
            ) {
                deviceInfo = deviceInfo + "\n" + (d.name + ": " + d.state.toString());
            }
            
            DataTest dataTest = new DataTest(deviceInfo);
            sendMessage(new Message(sysID, Sys.CONTROLLER, Code.DATA, dataTest));

            if(AlarmState){
                sendMessage(new Message(sysID, Sys.CONTROLLER, Code.DANGER));
            }
            else if(!AlarmState && !warningState){
                sendMessage(new Message(sysID, Sys.CONTROLLER, Code.ALL_CLEAR));
            }
            else{
                sendMessage(new Message(sysID, Sys.CONTROLLER, Code.WARNING));
            }
        }

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

    /*
    Starts Heartbeat to server.
     */
    @Override
    public void run() {
        startHeartbeat(Sys.EXHIBIT);
        sendMessage(new Message(sysID, Sys.CONTROLLER, Code.ALL_CLEAR));
        //QUEUE.put(new Message(sysID,sysID,Code.ALARM));
        //QUEUE.put(new Message(sysID,sysID,Code.GETDATA));
        //sendMessage(new Message(sysID,sysID,Code.TEST));
        
        // Periodically update any controller clients.
        newTimer(5, new updateTimer());
        int streamID = newStream(sysID, Sys.CONTROLLER, deviceArray[0]);
    }
    /*
    Creates a set of devices 0 - 3 are all electric fence, 4 & 5 change nothing really, 6 & 7 are the lights and speakers
    during an emergency.
     */
    @Override
    protected void initializeDeviceArray() {
        deviceArray = new Device[]{
                new Device("Electric Fence Sensor 1", State.ON),
                new Device("Electric Fence Sensor 2", State.ON),
                new Device("Electric Fence Sensor 3", State.ON),
                new Device("Electric Fence Sensor 4", State.ON),
                new Device("Camera Sensor 1", State.ON),
                new Device("Gate Sensor 1 ", State.OFF),
                new Device("Alarm Speaker",State.OFF),
                new Device("Alarm Lights",State.OFF)
        };
    }

    private class updateTimer implements Runnable {
        @Override
        public void run() {
            if(AlarmState){
                sendMessage(new Message(sysID, Sys.CONTROLLER, Code.DANGER));
            }
            else{
                sendMessage(new Message(sysID, Sys.CONTROLLER, Code.ALL_CLEAR));
            }

            return;
        }
    }

    private class SendAlarmToAllTimer implements Runnable {
        @Override
        public void run() {
            if(!received) {
                Sys[] components = Sys.values();
                for (Sys component : components) {
                    sendMessage(new Message(sysID, component, Code.ALARM));
                }
            }
            return;
        }
    }
}