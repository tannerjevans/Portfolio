/**
 * @author Thomas Bowidowicz
 * CS460 - Spring 2021
 *
 * This class serves as the backend logic for the Sys_AutomatedTicket
 * or ATS.
 */

import java.util.ArrayList;

public class Sys_AutomatedTicket extends SysSuper_Component {

    private final Sys sysID = Sys.ATS;

    private String firstName;
    private String lastName;
    private String middleInitial;
    private int age;
    private int creditCardNumber;
    private int expirationDate;
    private int securityNumber;
    private boolean shutdownSignal = false;

    ArrayList<Person> customerList = new ArrayList<>();

    /*
    public static void main(String args[]) {
      Sys_AutomatedTicket ats = new Sys_AutomatedTicket();
      ats.run();
    }
    */

    /**
     * Processes messages recieved from message broker
     * shutdown signal
     * GET DATA - Retrieves the complete customer list at the park and prints it out
     * DELETE_PERSON - removes a person from the customer list
     * SHUTDOWN_SIGNAL - toggles the shutdown signal to true which will cease operations of the kiosks
     * RESUME_OPERATIONS - toggles the shutdown signal to false and will allow the kiosks to resume operations
     *
     * @param message - the message to be processed
     */
    @Override
    protected synchronized void messageProcessor(Message message) {
        if (_PresentationDriver.DEBUG)
            System.out.println(sysID + " received a " + message.code()
                    + " message from " + message.source());

        if(message.code() == Code.GET_DATA){
            System.out.println("GET DATA REACHED");
            System.out.println("Customer List:");
            for (int i = 0; i < customerList.size(); i++) {
                System.out.println("Customer " + i + ":");
                System.out.println("First Name: " + customerList.get(i).firstName);
                System.out.println("Middle Initial: " + customerList.get(i).middleInitial);
                System.out.println("Last Name: " + customerList.get(i).lastName);
                System.out.println("Age: " + customerList.get(i).age);
                System.out.println("Credit Card Number: " + customerList.get(i).creditCardNumber);
                System.out.println("Expiration Date: " + customerList.get(i).expirationDate);
                System.out.println("Security Number: " + customerList.get(i).securityNumber);
                System.out.println();
            }}
            // Need to work on
            if(message.code() == Code.DELETE_PERSON) {
                CustomerId data = (CustomerId)message.data();
                Person temp = new Person("TEMP");
                for (Sys_AutomatedTicket.Person p : customerList
                ) {
                    if(p.getCustomerId() == data.id){
                        temp = p;
                    }
                }
                customerList.remove(temp);

            }

            // Shutdown signal
            if(message.code() == Code.SHUTDOWN_SIGNAL) {
                System.out.println("SHUTDOWN SIGNAL RECEIVED");
                shutdownSignal = true;
            }

            // Resume operations
            if(message.code() == Code.RESUME_OPERATIONS) {
                System.out.println("RESUME OPERATIONS RECEIVED");
                shutdownSignal = false;
            }

            // update controller client of status
        if(message.code() == Code.STATUS){

            String deviceInfo = "";

            if(customerList.size() > 0){
            for (int i = 0; i < customerList.size(); i++) {
                deviceInfo += "Customer " + i + ":\n";
                deviceInfo += "First Name: " + customerList.get(i).firstName+"\n";
                deviceInfo += "Middle Initial: " + customerList.get(i).middleInitial+"\n";
                deviceInfo += "Last Name: " + customerList.get(i).lastName+"\n";
                deviceInfo += "Age: " + customerList.get(i).age +"\n";
                deviceInfo += "Credit Card Number: " + customerList.get(i).creditCardNumber +"\n";
                deviceInfo += "Expiration Date: " + customerList.get(i).expirationDate+"\n";
                deviceInfo += "Security Number: " + customerList.get(i).securityNumber+"\n";
            }}
            else{}
            deviceInfo = "No customers to report.";
            DataTest dataTest = new DataTest(deviceInfo);
            sendMessage(new Message(sysID, Sys.CONTROLLER, Code.DATA, dataTest));

            if(shutdownSignal){
                sendMessage(new Message(sysID, Sys.CONTROLLER, Code.DANGER));
            }
            else{
                sendMessage(new Message(sysID, Sys.CONTROLLER, Code.ALL_CLEAR));
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
    @Override
    public void run() {
        sendMessage(new Message(sysID, Sys.CONTROLLER, Code.ALL_CLEAR));
        printList();
        //GUI_Kiosk.main(null);
    }

    /**
     * This class defines the Person object. This is an object that'll contain all information about a customer
     * for the park including payment information.
     */
    protected class Person extends Data{
        private String firstName;
        private String lastName;
        private String middleInitial;
        private int age;
        private int creditCardNumber;
        private int expirationDate;
        private int securityNumber;
        private int customerId;

        public Person(String firstName) {
            this.firstName = firstName;
        }

        /**
         * This is the constructor for the Person object.
         * @param firstName - String
         * @param lastName - String
         * @param middleInitial - String
         * @param age - int
         * @param creditCardNumber - int
         * @param expirationDate - int
         * @param securityNumber - int
         */
        public Person(String firstName, String lastName, String middleInitial, int age, int creditCardNumber,
                      int expirationDate, int securityNumber) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.middleInitial = middleInitial;
            this.age = age;
            this.creditCardNumber = creditCardNumber;
            this.expirationDate = expirationDate;
            this.securityNumber = securityNumber;

            if(customerList.size() == 0) {
                customerId = 1;
            } else {
                this.customerId = customerList.get(customerList.size() - 1).getCustomerId() + 1;
            }
        }

        // Getters and setters
        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getMiddleInitial() {
            return middleInitial;
        }

        public int getAge() {
            return age;
        }

        public int getCreditCardNumber() {
            return creditCardNumber;
        }

        public int getExpirationDate() {
            return expirationDate;
        }

        public int getSecurityNumber() {
            return securityNumber;
        }

        public int getCustomerId() {
            return customerId;
        }

    }

    /**
     * Method takes in payment information from the GUI_Kiosk and returns a boolean value if it is
     * successfully verified or not. If the class verifies a person's account information, it also
     * creates a new person object and adds it to the person arraylist.
     * @return boolean
     */
    public boolean verifyPayment(String firstName, String lastName, String middleInitial, int age, int creditCardNumber,
                                 int expirationDate, int securityNumber) {
        boolean isVerified = true;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleInitial = middleInitial;
        this.age = age;
        this.creditCardNumber = creditCardNumber;
        this.expirationDate = expirationDate;
        this.securityNumber = securityNumber;

        // Stand in for actual internet verification. Currently, all payment is verified.
        if(isVerified) {
            Person person = new Person(firstName, lastName, middleInitial, age, creditCardNumber, expirationDate,
                    securityNumber);
            addPerson(person);
            System.out.println(customerList.size());
            return true;
        } else {
            return false;
        }
    }

    // Add person to the customer list
    public void addPerson(Person person) {
        Person customer = person;
        customerList.add(customer);
    }

    // Iterate through customer list and print out each person.
    public void printList() {
        System.out.println("Customer List:");
        for (int i = 0; i < customerList.size(); i++) {
            System.out.println("Customer " + i + ":");
            System.out.println("First Name: " + customerList.get(i).firstName);
            System.out.println("Middle Initial: " + customerList.get(i).middleInitial);
            System.out.println("Last Name: " + customerList.get(i).lastName);
            System.out.println("Age: " + customerList.get(i).age);
            System.out.println("Credit Card Number: " + customerList.get(i).creditCardNumber);
            System.out.println("Expiration Date: " + customerList.get(i).expirationDate);
            System.out.println("Security Number: " + customerList.get(i).securityNumber);
            System.out.println();
        }
    }

    public boolean getShutdownSignal() {
        return shutdownSignal;
    }

    @Override
    protected void initializeDeviceArray() {
        Device[] devices = {
                new Device("Kiosk 1", State.ON)
        };
        deviceArray = devices;
    }

    protected class CustomerId extends Data {
        private int id;
    }
}
