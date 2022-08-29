import java.util.Timer;
import java.util.TimerTask;

public class Sys_Oversight extends SysSuper_Component {
    private final Sys sysID = Sys.OVERSIGHT;

    //status used to display codes on gui
    private static String status = "WORKING";

    //main is called by gui to start timer that changes pictures.
    //I though it would be easier if I just set the pictures to kind of cycle here,
    // but I can also show the ones I receive.
    public static void main(String[] args) {
        //timer to change pictures
        Timer timer = new Timer();
        TimerTask task = new TimeHelper();
        timer.schedule(task, 1000, 5000);
    }

    @Override
    public void run() {
    }

    @Override
    protected synchronized void messageProcessor(Message message) {
        int streamID = 0;
        if (_PresentationDriver.DEBUG)
            //If STREAM code received, send the devices to the CTRL_CLT
            if(message.code() == Code.STREAM){
                StreamData streamData = new StreamData(deviceArray[0],deviceArray[1],deviceArray[2]);
                streamID = newStream(sysID, Sys.CONTROLLER, streamData);
            }
            // when a code is sent to cancel the stream, cancel the stream
            else if(message.code() == Code.CANCEL_STREAM){
                cancelStream(streamID);
            }
            //if the code is DATA, write it to storage. no need to convert it
            else if(message.code() == Code.DATA) {
                writeToStorage(message.data());
            }
            else if(message.code() == Code.ALARM) {
                sendMessage(new Message(sysID, Sys.EXHIBIT, Code.ALARM));
                sendMessage(new Message(sysID, Sys.ATS, Code.SHUTDOWN_SIGNAL));
                status = "ALARM";
            }
            else if(message.code() == Code.ALL_CLEAR) {
                sendMessage(new Message(sysID, Sys.CONTROLLER, Code.ALL_CLEAR));
                sendMessage(new Message(sysID, Sys.EXHIBIT, Code.WARNING));
                status = "WORKING";
            }
            else if(message.code() == Code.STATUS){
                String deviceInfo = "";
                for (Device d: deviceArray
                ) {
                    deviceInfo = deviceInfo + "\n" + (d.name + ": " + d.state.toString());
                }
                
                DataTest dataTest = new DataTest(deviceInfo);
                sendMessage(new Message(sysID, Sys.CONTROLLER, Code.DATA, dataTest));

                if(status.equals("WORKING")){
                    sendMessage(new Message(sysID, Sys.CONTROLLER, Code.ALL_CLEAR));
                }
                else if(status.equals("ALARM")){
                    sendMessage(new Message(sysID, Sys.CONTROLLER, Code.DANGER));
                }
                else{
                    sendMessage(new Message(sysID, Sys.CONTROLLER, Code.WARNING));
                }
            }
            // If the code is none of the above, make it into data and write that to storage
            else {
                //Display incoming codes. It displays all codes that are not streaming or data codes
                //currently once there is a warning ot Alarm it does not go back to working status.
                status = message.code().toString() + "from " + message.source().toString();
                CodeData codeData = new CodeData(message.source(),message.destination(),message.code());
                writeToStorage(codeData);
            }

    }

    // get the current status
    public static String getStatus(){
        return status;
    }

    //puts incoming messages into data format to write to storage
    protected class CodeData extends Data {
        private Sys src;
        private Sys dest;
        private Code code;

        public CodeData(Sys src, Sys dest, Code code) {
            this.src = src;
            this.dest = dest;
            this.code = code;
        }

        //getters if necessary
        public Sys getSrc (){
            return src;
        }
        public Sys getDest (){
            return dest;
        }
        public Code getCode (){
            return code;
        }
    }

    //streaming requires device type. This packages the three devices into one.
    protected class StreamData extends Device {
        private Device video1;
        private Device video2;
        private Device video3;
        public StreamData(Device video1, Device video2, Device video3){
            super("Videos", State.ON);
            this.video1 = video1;
            this.video2 = video2;
            this.video3 = video3;
        }
    }

    @Override
    protected void initializeDeviceArray() {
        deviceArray = new Device[]{
                //provides names of video and state. This does not send the current picture displayed
                // it could be changed to do so if necessary
                new Device("Video Feed 1", State.ON),
                new Device("Video Feed 2", State.ON),
                new Device("Video Feed 3", State.ON)
        };
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

    //This is the best way I could get the pictures to change in the gui, but if we
    //want to do it differently I can change it. For example, I think they should technically be
    // coming from the exhibit, but I made this to make the demo easier.
    static class TimeHelper extends TimerTask {
        public static int i = 0;
        public static String vid ="v1" ;

        public static void setString(int i){
            if (i%2 == 0){
                vid = "v1";
            }
            else if(i%3 ==0){
                vid = "v2";
            }
            else{
                vid = "v3";
            }
        }
        public String getVid(){
            return vid;
        }
        public void run()
        {
            ++i;
            setString(i);
        }
    }
}

