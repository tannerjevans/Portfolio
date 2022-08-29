import java.time.LocalDateTime;

public class ComponentExample extends Component {
    private final Sys sysID = Sys.EXAMPLE; // SysSuper_Component's ID.

    /**
     * For purposes of presentation, I have built a driver which can instantiate and run
     * all our components, and dispatch presentation commands. I feel like we should include
     * a main in each component, though, as a nod to the idea that they are supposedly going
     * to be run on different systems.
     *
     * You can have a main which creates an object of the class within which it resides. Then
     * either main or the driver call run().
     *
     * @author Tanner J. Evans
     */
    /*
    public static void main(String[] args) {
        ComponentExample componentExample = new ComponentExample();
        ComponentExample.run();
    }
     */

    /**
     * run() provides a thread of execution independent of messages, and messageProcessor()
     * provides a thread of execution in response to messages. You may have to use flags
     * or something to communicate between these threads of execution. You need to provide
     * concrete implementations for both, although it is entirely possible to isolate
     * logic to the messageProcessor() and use Timers and dropping messages onto your
     * own queue to implement stuff that might go in run otherwise.
     * Message receipt is synchronized with a blocking
     * queue, and messageProcessor should probably be synchronized.
     *
     * So far, having an informative print out in your messageProcessor() function, tied
     * to the DEBUG boolean in _PresentationDriver, seems really useful.
     *
     * Right now, sensors trigger an internal message that still passes through the
     * MessageProcessor, so your sensor event handling will go there, too.
     */
    @Override
    public void run() {
        /** Timers:
         * Timers take a time in seconds and a Runnable class. Whatever
         * you put in the run() method of the Runnable class will run when
         * the timer expires.
         * You can start a heartbeat by calling startHeartbeat() with your
         * component id, and you can cancel it with endHeartbeat().
         * Heartbeats are sent to the server every three seconds.
         */
        newTimer(5, new TimerRunnable());
        int newTimerID = newTimer(2, new TimerRunnable());
        cancelTimer(newTimerID);
        startHeartbeat(sysID);
        cancelHeartbeat();
        /** Messages:
         * To send a one-time message, create a Message, and pass it to the
         * sendMessage() function. There are two constructors for Message,
         * one with and one without Data. As seen below, you can extend Data
         * into your own Data subclass in order to customize what kind of info
         * you can send. Messages need a source and destination ID,
         * as well as a Code enum, which you can feel free to add to as necessary.
         */
        Message message1 = new Message(sysID, Sys.CTRL_SVR, Code.TEST);
        sendMessage(message1);
        DataTest dataTest = new DataTest(2, "Test");
        Message message2 = new Message(sysID, Sys.CTRL_SVR, Code.DATA, dataTest);
        sendMessage(message2);
        /**
         * Streams:
         * Streams take a source and destination Sys enum, and a device. They
         * will send the device to the destination every three seconds until
         * you cancel it.
         */
        int streamID = newStream(sysID, Sys.CTRL_CLT, deviceArray[0]);
        cancelStream(streamID);

        /**
         * Storage:
         * For now, storage is not persistent.
         * writeToStorage(Data data) writes your data to storage with current time.
         * getStorageData() returns all stored data.
         * getStorageData(LocalDateTime start, LocalDateTime end) returns all
         * stored data with time between the start and end, inclusive.
         * clearOldStores() deletes all data with timestamp older than 7 days.
         * When you write a data object to storage, it is wrapped in a
         * StorageData object. Functions which return stored data return
         * arrays of StorageData items, which have a DATE_TIME value and a DATA
         * value, which is the Data object you originally passed.
         *
         * You can easily set a timer's run() method to store a device at regular
         * intervals.
         */
        writeToStorage(deviceArray[0]);
        StorageData[] storedData = getStorageData();
        StorageData[] storedDataInTimeframe =
            getStorageData(LocalDateTime.now().minusHours(5),
                LocalDateTime.now().minusMinutes(3));
        clearOldStorage();

    }

    @Override
    protected synchronized void messageProcessor(Message message) {
        if (Driver.DEBUG)
            System.out.println(sysID + " rcvd a " + message.code()
                                   + " msg from " + message.source());
    }

    /**
     * If you deal with devices, you can override this. Otherwise, the default
     * implementation is fine. Note that you can subclass Device into some custom
     * device if a name and a single state is insufficient. The default implementation
     * is basically just for sensors, and please note that the
     * DRIVER_ONLY_changeState() method
     * in Device is intended for use by the _PresentationDriver class to simulate device state
     * changes occurring not as a result of system operation, which is why it
     * sends a message to the SysSuper_Component. If you need to build a device which
     * you want to send commands to, subclass Device and build a method for doing
     * that specifically.
     *
     * Add to States as necessary.
     *
     * Note that Device extends Data, so you can pass it as Data in a message.
     * Changing the state of a Device triggers it to drop a message onto the
     * queue to that effect, passing itself as the data, so you don't have
     * to worry too much about how to implement that.
     */
    @Override
    protected void initializeDeviceArray() {
        Device[] devices = {
            new Device("Break Sensor 1", State.OFF),
            new Device("Break Sensor 2", State.OFF)
        };
        deviceArray = devices;
    }

    /**
     * You can build Runnable timer classes to do whatever you want.
     */
    private class TimerRunnable implements Runnable {
        @Override
        public void run() {
            DataTest dataTest = new DataTest(2, "Timer for something ran.");
            QUEUE.put(new Message(Sys.SELF, Sys.SELF, Code.TIMER_INDIC, dataTest));
            return;
        }
    }

    /**
     * You can subclass Data to get a container for any form of data
     * you may need to pass through messages. Just make sure to provide
     * getters. We'll probably shift these to the superclass when done,
     * I just can't predict exactly what you guys will want to use.
     */
    protected class DataTest extends Data {
        private int number;
        private String text;
        public DataTest(int number, String text) {
            this.number = number;
            this.text = text;
        }
        //getters and whatever
    }
}
