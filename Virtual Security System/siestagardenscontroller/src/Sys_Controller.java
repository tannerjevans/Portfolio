

public class Sys_Controller extends SysSuper_Component {

    private final static Sys sysID = Sys.CONTROLLER;
    private final static Sys sysIDexhibit = Sys.EXHIBIT;
    private final static Sys sysIDats = Sys.ATS;
    private final static Sys sysIDoversight = Sys.OVERSIGHT;
    private final static Sys sysIDsdc = Sys.AUTO_CAR;
    private final static Sys sysIDTOKEN = Sys.TOKEN;
    
    //Log info
    private String exhibitLog = "";
    private String atsLog = "";
    private String carLog = "";
    private String oversightLog = "";
    
    // Either green, red, or yellow.
    String atsStatus, sdcStatus, exhibitStatus, alarmStatus, tokenStatus;

    // get the current status of a system
    public String getStatus(String system){
        String status = "null";

        switch(system){
            case "ats":
                status = atsStatus;
                break;
            case "sdc":
                status = sdcStatus; 
                break;
            case "exhibit":
                status = exhibitStatus;
                break;
            case "alarm":
                status = alarmStatus;
                break;
            case "token":
                status = tokenStatus;
                break;
            default:
                break;
        }

        return status;
    }

    public static Sys getSysID(){
        return sysID;
    }

    public static Sys getSysIDsdc(){
        return sysIDsdc;
    }
    
    public static Sys getSysIDtoken(){
        return sysIDTOKEN;
    }

    public static Sys getSysIDexhibit(){
        return sysIDexhibit;
    }
    public static Sys getSysIDats(){
        return sysIDats;
    }

    public static Sys getSysIDoversight(){
        return sysIDoversight;
    }

    public static Code getStatusCode(){
        return Code.STATUS;
    }
    
    public static Code getAlarmCode(){
        return Code.ALARM;
    }

    public static Code getResumeCode(){
        return Code.RESUME_OPERATIONS;
    }

    public static Code getAllClearCode(){
        return Code.ALL_CLEAR;
    }

    public static Code getShutdownCode(){
        return Code.SHUTDOWN_SIGNAL;
    }

    @Override
    public void run() {
        //Initialize status to red.
        atsStatus = "red";
        sdcStatus = "red";
        exhibitStatus = "red";
        alarmStatus = "red";
        tokenStatus = "red";
    }

    @Override
    protected void messageProcessor(SysSuper_Component.Message message) {
        if(message.code() == Code.ALL_CLEAR){
            updateStatus(message.source(), "green");
        }
        else if(message.code() == Code.WARNING){
            updateStatus(message.source(), "yellow");
        }
        else if(message.code() == Code.DANGER){
            updateStatus(message.source(), "red");
        }
        else if(message.code() == Code.DATA){
            if(message.source() == Sys.EXHIBIT){
                exhibitLog = ((Sys_Exhibit.DataTest) message.data()).getString();
            }
            else if(message.source() == Sys.ATS){
                atsLog = ((Sys_AutomatedTicket.DataTest) message.data()).getString();
            }
            else if(message.source() == Sys.OVERSIGHT){
                oversightLog = ((Sys_Oversight.DataTest) message.data()).getString();
            }
            else if(message.source() == Sys.AUTO_CAR){
                carLog = ((Sys_SelfDrivingCar.DataTest) message.data()).getString();
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

    private void updateStatus(SysSuper_Component.Sys source, String newColor) {
        
        switch (source){
            case EXHIBIT:
                exhibitStatus = newColor;
                break;
            case ATS:
                atsStatus = newColor;
                break;
            case OVERSIGHT:
                alarmStatus = newColor;
                break;
            case AUTO_CAR:
                sdcStatus = newColor;
                break;
            case TOKEN:
                tokenStatus = newColor;
                break;
            default:
                break;
        }
    }

    public String getExhibitLog() {
        return exhibitLog;
    }

    public String getATSlog() {
        return atsLog;
    }

    public String getCarLog() {
        return carLog;
    }

    public String getOversightLog() {
        return oversightLog;
    }
}
