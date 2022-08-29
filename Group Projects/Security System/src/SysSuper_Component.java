import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author Tanner J. Evans
 *   The general superclass for most park components. Provides various
 * functionality to support a unified implementation of park systems.
 */

public abstract class SysSuper_Component implements Runnable {
    /**
     * CONSTRUCTOR
     */

    public SysSuper_Component() {
        MessageHandler messageHandler = new MessageHandler();
        Thread handlerThread = new Thread(messageHandler);
        handlerThread.start();
        initializeDeviceArray();
    }


    /**
     * MESSAGING STUFF
     */

    /**
     * MessageQueue object
     * Provides thread-safe addition and removal of messages.
     */
    protected static class MessageQueue extends LinkedBlockingQueue<Message> {
        public void put(Message message) {
            try {
                super.put(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    protected final MessageQueue QUEUE = new MessageQueue();

    /**
     * messageProcessor function
     * Function which the MessageHandler thread will call when
     * messages are received, passing it the message that was placed
     * on the MessageQueue. Must be overridden by each component.
     * @param message - the message to be processed
     */
    protected abstract void messageProcessor(Message message);

    /**
     * MessageHandler object
     * Runs on its own thread, dequeueing new messages from the
     * MessageQueue and passes them to the messageProcessor method.
     */
    private class MessageHandler implements Runnable {
        @Override
        public void run() {
            do {
                try {
                    messageProcessor(QUEUE.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    /**
     * Sys enum
     * Enumerates the types of components in the project. These
     * enums are used for message routing, among other things.
     */
    protected enum Sys {
        EXAMPLE(0),
        DEVICE(1),
        CONTROLLER(3),
        ATS(4),
        OVERSIGHT(5),
        TOKEN(6),
        ALARM(7),
        EXHIBIT(8),
        AUTO_CAR(9),
        DEFAULT(10),
        SELF(11),
        CAR(12);
        private final int VAL;
        Sys(int VAL) {
            this.VAL = VAL;
        }
        public int getVAL() {
            return VAL;
        }
    }

    /**
     * Code enum
     * Enumerates the types of messages that are sent and received
     * throughout the project. These enums are used for message
     * processing.
     */
    protected enum Code {
        TEST,
        TIMER_INDIC,
        HEARTBEAT,
        DATA,
        DEVICE,
        STREAM,
        CANCEL_STREAM,
        ERROR,
        BREAKAGE,
        SENSOR_CHANGE,
        WARNING,
        ALARM,
        NEW_TOKEN,
        GET_DATA,
        DELETE_TOKEN,
        RECEIVE,
        DANGER,
        OBSTRUCTION,
        DELETE_PERSON,
        SHUTDOWN_SIGNAL,
        RESUME_OPERATIONS,
        ALL_CLEAR, 
        STATUS


        // almost certainly many to be added
    }

    /**
     * Data object
     * The object that Messages accept when passing information
     * back and forth. This class can be subclassed as needed, and
     * any subclass of Data can be sent as the Data object of a
     * Message. This enables the passing of complicated sensor data
     * and other information without needing to parse it into a new
     * format.
     */
    protected static class Data {
        // to be subclassed as necessary
    }

    /**
     * Message object
     * Provides a framework for sending and receiving messages to and
     * from other components. All Messages require Sys enums detailing
     * the source component and destination component, as well as a
     * Code enum detailing the type of message being sent. A secondary
     * constructor is available for creating Messages with some Data
     * object embedded.
     */
    protected static class Message {
        private final Sys SOURCE;
        private final Sys DESTINATION;
        private final Code CODE;
        private Data data;
        public Message(Sys src, Sys dest, Code code) {
            this.SOURCE = src;
            this.DESTINATION = dest;
            this.CODE = code;
        }
        public Message(Sys src, Sys dest, Code code, Data data) {
            this.SOURCE = src;
            this.DESTINATION = dest;
            this.CODE = code;
            this.data = data;
        }
        public Sys source() {
            return SOURCE;
        }
        public Sys destination() {
            return DESTINATION;
        }
        public Code code() {
            return CODE;
        }
        public Data data() {
            return data;
        }
    }

    /**
     * sendMessage function
     * Routes Messages through the IF_MessageBroker. This is intended to
     * standardize messaging in a way that will be easily modifiable
     * should the IF_MessageBroker implementation change.
     * @param message
     */
    protected static void sendMessage(Message message) {
        if (_PresentationDriver.DEBUG)
            System.out.println(message.source() + " sent a " + message.CODE + " msg to "
                                   + message.destination());
        IF_MessageBroker.routeMessage(message);
    }

    /**
     * STREAM STUFF
     * Streams are implemented basically as heartbeats that send
     * data with them.
     */

    /**
     * Stream object
     * Can be constructed to send Device information to some Sys
     * destination at regular intervals. The Stream constructor
     * takes a Sys source, Sys destination, and a Device, and sends
     * a Message containing the Device (which is subclassed from
     * Data) to the destination every 3 seconds.
     */
    protected class Stream implements Runnable {
        private final int ID;
        private final Sys SOURCE;
        private final Sys DESTINATION;
        private final Code CODE = Code.STREAM;
        private final Device DEVICE;
        public Stream (Sys source, Sys destination, Device device) {
            ID = getNewID();
            this.SOURCE = source;
            this.DESTINATION = destination;
            this.DEVICE = device;
        }

        public int getID() { return ID; }

        @Override
        public void run() {
            Message message = new Message(SOURCE, DESTINATION, Code.STREAM, DEVICE);
            sendMessage(message);
        }
    }

    /**
     * newStream function
     * Accepts a Sys source, Sys destination, and Device and starts a
     * new Stream to the destination provided.
     * @param source
     * @param destination
     * @param device
     * @return
     */
    protected int newStream(Sys source, Sys destination, Device device) {
        Stream stream = new Stream(source, destination, device);
        int streamID = stream.getID();
        TIMERS.put(streamID, SCHEDULER.scheduleAtFixedRate(
            stream, 0, 3, TimeUnit.SECONDS));
        return streamID;
    }

    /**
     * cancelStream function
     * @param streamID
     */
    protected static void cancelStream(int streamID) {
        TIMERS.remove(streamID).cancel(true);
    }


    /**
     * TIMER STUFF
     * Timers provide a simple way for component coders to initialize
     * new timers, without needing to set up new timer boilerplate.
     */

    private static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(10);
    private static final Map<Integer, ScheduledFuture<?>> TIMERS = new HashMap<>();
    private static int timerID = 0;
    private static int heartbeatID;

    /**
     * Timer object
     * Provides a hidden way for components to set up timers. Is
     * constructed with a Runnable provided by the component which
     * has requested it, and runs the code in the run() method of that
     * Runnable whenever the timer triggers.
     */
    private class Timer implements Callable<Object> {
        private final int ID;
        private final Runnable RUNNABLE;

        public Timer(Runnable r) {
            ID = getNewID();
            this.RUNNABLE = r;
        }

        public int getID() { return ID; }

        @Override
        public Object call() {
            RUNNABLE.run();
            return null;
        }
    }

    /**
     * Heartbeat object
     * Provides a simple way to start a repeating heartbeat timer
     * to some destination.
     */
    private class Heartbeat implements Runnable {
        private final int ID;
        private final Sys SOURCE;
        public Heartbeat(Sys source) {
            ID = getNewID();
            this.SOURCE = source;
        }
        public int getID() { return ID; }

        @Override
        public void run() {
            Message message = new Message(SOURCE, Sys.CONTROLLER, Code.HEARTBEAT);
            sendMessage(message);
        }
    }

    /**
     * getNewID function
     * Used to ensure that getting timer IDs is thread safe.
     * @return - the unique Timer ID
     */
    private synchronized int getNewID() {
        timerID++;
        return timerID;
    }

    /**
     * newTimer function
     * Provides an easy way to start a new Timer.
     * @param seconds - length of timer in seconds
     * @param r - the Runnable to run when the timer expires
     * @return - the unique Timer ID
     */
    protected int newTimer(int seconds, Runnable r) {
        Timer timer = new Timer(r);
        int newTimerID = timer.getID();
        TIMERS.put(newTimerID, SCHEDULER.schedule(timer, seconds, TimeUnit.SECONDS));
        if (_PresentationDriver.DEBUG) System.out.println("Timer " + newTimerID + " started.");
        return newTimerID;
    }

    /**
     * cancelTimer function
     * @param timerID - the ID of the Timer to be cancelled
     */
    protected void cancelTimer(int timerID) {
        if (_PresentationDriver.DEBUG) System.out.println("Timer " + timerID + " cancelled.");
        TIMERS.remove(timerID).cancel(true);
    }

    /**
     * startHeartbeat function
     * Provides an easy way to start a new Heartbeat. Heartbeats
     * are sent to the Controller Server and recur every three
     * seconds by default.
     * @param source - the source of the Heartbeat
     */
    protected void startHeartbeat(Sys source) {
        if (TIMERS.containsKey(heartbeatID)) return;
        Heartbeat heartbeat = new Heartbeat(source);
        heartbeatID = heartbeat.getID();
        TIMERS.put(heartbeatID, SCHEDULER.scheduleAtFixedRate(
            heartbeat, 0, 3, TimeUnit.SECONDS));
        if (_PresentationDriver.DEBUG) System.out.println("Heartbeat " + heartbeatID + " started.");
    }

    /**
     * cancelHeartbeat function
     */
    protected void cancelHeartbeat() {
        TIMERS.remove(heartbeatID).cancel(true);
    }


    /**
     * DATA STORAGE + RETRIEVAL
     */

    /**
     * StorageData object
     * Provides a neat package for storing data with date and time
     * information.
     */
    protected static class StorageData extends Data {
        protected final LocalDateTime DATE_TIME;
        protected final Data DATA;
        public StorageData(LocalDateTime dateTime, Data data) {
            DATE_TIME = dateTime;
            DATA = data;
        }
    }

    /**
     * writeToStorage function
     * @param data - the data to write to storage
     */
    protected void writeToStorage(Data data) {
        LocalDateTime dateTime = LocalDateTime.now();
        StorageData storageData = new StorageData(dateTime, data);
        IF_Storage.addData(storageData);
    }

    /**
     * getStorageData function
     * @return - all data stored for this component
     */
    protected StorageData[] getStorageData() {
        LocalDateTime start = LocalDateTime.MIN;
        LocalDateTime end = LocalDateTime.MAX;
        return IF_Storage.getData(start, end);
    }

    /**
     * getStorageData function
     * @param start - beginning of time frame of data
     * @param end - end of time frame of data
     * @return - all data within time frame
     */
    protected StorageData[] getStorageData(LocalDateTime start, LocalDateTime end) {
        return IF_Storage.getData(start, end);
    }

    /**
     * clearOldStorage function
     * Deletes stored data older than 7 days.
     */
    protected void clearOldStorage() {
        IF_Storage.deleteOlderThanDays(7);
    }


    /**
     * INPUT AND OUTPUT DEVICES
     */

    /**
     * deviceArray Device[]
     * Provides a standardized means of keeping track of Devices.
     * Also provides a means for the demo _PresentationDriver to more easily
     * simulate various park events.
     */
    protected Device[] deviceArray;

    /**
     * initializeDeviceArray function
     * Provides a default means for initializing the deviceArray
     * to empty. Components which utilize Devices must override this
     * function to initialize the Devices to which they will be
     * connected.
     */
    protected void initializeDeviceArray() {
        deviceArray = new Device[]{};
    }

    /**
     * State enum
     * Enumerates the possible states of Devices.
     */
    protected enum State {
        OFF,
        ON
        // Probably more
    }

    /**
     * Device object
     * Provides a framework for simulating various park devices.
     */
    protected class Device extends Data {
        protected final String name;
        protected State prevState;
        protected State state;
        public Device(String name, State state) {
            this.name = name;
            this.state = state;
            prevState = state;
        }

        /**
         * Provides a means for the demo _PresentationDriver to simulate park events in the form
         * of sensor state changes.
         * @param state
         */
        protected void DRIVER_ONLY_changeState(State state) {
            prevState = this.state;
            this.state = state;
            QUEUE.add(new Message(Sys.DEVICE, Sys.DEVICE, Code.DEVICE, this));
        }
    }





}
