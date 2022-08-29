
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Marcos Lopez
 */
public class Sys_TokenMonitoring extends SysSuper_Component {
    private final Sys sysID = Sys.TOKEN;
    private final List<Token> tokenList = new ArrayList<>();
    private boolean isRunning = true;

    /*
    public static void main(String[] args){
        Sys_TokenMonitoring tokenMonitoringSystem = new Sys_TokenMonitoring();
        tokenMonitoringSystem.run();
    }
     */

    /**
     * @author Marcos Lopez
     * Processes Messages received from Message Broker.
     * Codes: | message.data() input: | output:
     * NEWTOKEN| TempData(String name) (for now)| Adds a token to Monitoring System as well as Pylon System with owner name and unique ID.
     * GETDATA| NULL | Prints out all info for all tokens
     * DELETETOKEN| TempData(int ID) (for now)| Deletes token with given ID from System.
     * DATA| NULL| test section (will be modified or removed)
     */
    @Override
    protected synchronized void messageProcessor(Message message) {
        if (_PresentationDriver.DEBUG) {
            System.out.println(sysID + " received a " + message.code()
                    + " message from " + message.source());

        }
        if(message.code() == Code.NEW_TOKEN){
            if(message.data() != null) {
                TempData td =  (TempData) message.data();
                tokenList.add(new Token(td.getOwner()));
                for (Device device: deviceArray
                     ) {
                   Pylon pylon = (Pylon)device;
                   pylon.updateList();
                }
                //tokenList.add();
                System.out.println("Token Added");
            }
        }
        if(message.code() == Code.GET_DATA){
            System.out.println("GET DATA REACHED");
                for (Token token: tokenList
                     ) {

                    System.out.println("Token ID: " + token.getId() + ", Token Owner: " + token.getOwner() + " Emergency: "  + token.EmergencyState +   ", LOCATION X: " + token.getLocation()[0] + " Y: " + token.getLocation()[1] );
                }


          // sendMessage(new Message(sysID, message.source(), Code.DATA, new TempData()));
        }
        if(message.code() == Code.STATUS){
            if(isRunning){
                sendMessage(new Message(sysID, Sys.CONTROLLER, Code.ALL_CLEAR));
            }
            else if(!isRunning){
                sendMessage(new Message(sysID, Sys.CONTROLLER, Code.DANGER));
            }
            else{
                sendMessage(new Message(sysID, Sys.CONTROLLER, Code.WARNING));
            }
        }
        if(message.code() == Code.DELETE_TOKEN){
            TempData data = (TempData)message.data();
            Token temp = new Token("TEMP");
            for (Token t:tokenList
                 ) {
                if(t.getId() == data.id){
                    temp = t;
                }
            }
            tokenList.remove(temp);

            for (Device device: deviceArray
            ) {
                Pylon pylon = (Pylon)device;
                pylon.updateList();
            }
        }
        if(message.code() == Code.DATA){
            //System.out.println(message.data().toString());
            tokenList.get(0).setEmergencyState(true);
        }
    }

    /**
     * @author Marcos Lopez
     * starts a stream to the CTRL_SVR and then all commented sections are test info will be removed.
     */
    @Override
    public void run() {
        tokenList.add(new Token("Test"));
        newStream(sysID, Sys.CONTROLLER, deviceArray[0]);

        //QUEUE.put(new Message(sysID,sysID,Code.GET_DATA));
        //QUEUE.put(new Message(sysID,sysID,Code.DATA));
        //cancelStream(streamID);
        //QUEUE.put(new Message());

    }

    /**
     * @author Marcos Lopez
     * creates a device array full of pylons
     */
    @Override
    protected void initializeDeviceArray() {
        deviceArray = new Device[]{
                new Pylon("Pylon 1", State.ON),
                new Pylon("Pylon 2", State.ON),
                new Pylon("Pylon 3", State.ON)
        };
        }

    /**
     * @author  Marcos Lopez
     * Pylon Object which holds
     * a triangulated location for a token
     * A List of Tokens
     * A String Name
     * A State
     */

    protected class Pylon extends Device {
        private final double[] TriangulatedLocation = new double[2];
        private List<Token> listOfTokens;
        public Pylon(String name, State state) {
            super(name, state);
            listOfTokens = tokenList;
        }

        /**
         * sets the list to the Overarching List for which will be sent apart of a stream.
         */
        public void updateList(){
            listOfTokens = tokenList;
        }

        /**
         * Sets up a random coordinate for location information. Not well designed but looks like it is.
         * @return a double array with randomized X and y coordinates.
         */
        public double[] getTriangulatedLocation() {
            Random random = new Random();
            double temp = random.nextDouble();
            temp += random.nextDouble();
            temp = temp / 100;

            TriangulatedLocation[0] = 27.26 + temp;
            temp = random.nextDouble();
            temp += random.nextDouble();
            temp += random.nextDouble();
            temp = temp / 100;
            TriangulatedLocation[1] = -82.54 + temp;
            return TriangulatedLocation;
        }

        public List<Token> getListOfTokens() {
            return listOfTokens;
        }
    }

    /**
     * @author Marcos Lopez
     * Token Object for use with various components.
     */
    protected class Token extends Data{
        private final int id;
        private final String Owner;
        private double[] location;
        private boolean EmergencyState;

        /**
         * Creates a new Token Object
         * @param owner name of visitor
         */
        Token(String owner){
            if(tokenList.size() == 0){
                id = 1;
            }else {
                this.id = tokenList.get(tokenList.size() - 1).getId() + 1;
            }
            Pylon p = (Pylon)deviceArray[0];
            this.location = p.getTriangulatedLocation();
            this.Owner = owner;
            this.EmergencyState = false;
        }

        /**
         * @return x and y coordinates in [0] and [1] respectively.
         */
        public double[] getLocation() {
            Pylon p = (Pylon)deviceArray[0];
            this.location = p.getTriangulatedLocation();
            return this.location;
        }

        public int getId(){
            return this.id;
        }
        public String getOwner(){
            return this.Owner;
        }

        /**
         * changes emergency state to given state either true or false
         * if state is true then TokenSystem will send a code to server
         * and SGC to alert them of a visitor in danger. Message.Data() is the token of the visitor.
         * @param state true or false boolean
         *
         */
        public void setEmergencyState(boolean state){
            EmergencyState = state;
            if(state) {
                sendMessage(new Message(sysID, Sys.CONTROLLER, Code.DANGER, this));
               // sendMessage(new Message(sysID, Sys.OVERSIGHT, Code.DANGER, new TempData(this.Owner, this.location)));
            }
        }
        public boolean isEmergencyState(){
            return EmergencyState;
        }

    }

    /**
     * temp stand-in for data extending object everyone will use.
     */
    protected class TempData extends Data{
        private int id;
        private String owner;
        double[] location;

        TempData(String owner){
            this.owner = owner;
        }
        TempData(String owner, double[] loc){
            this.owner = owner;
            location = loc;
        }
        TempData(int ID){
            this.id = ID;
        }
        public String getOwner() {
            return owner;
        }
    }

}
