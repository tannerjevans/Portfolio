import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Authors: Tanner Evans & Jacob Varela 3/10/2021
 * This class is the main class of the keypad system. This class will
 * change the program to the appropriate Mode and State based on the
 * input received from the keypad.
 * 
 */
public class CommGateDriver {

    static Database DB;

    /**
     * Timer class to measure time elapsed from last key pressed to enable timeout
     * and send system into idle from recording mode
     * Author Jared Bock 3/9/2021
     */
    static public class MyTimer {
        Timer timer;
        Toolkit toolkit;

        /**
         * Constructor of MyTimer object
         * @param sec
         * length timer will run in seconds.
         */
        public MyTimer(int sec){
            toolkit = Toolkit.getDefaultToolkit();
            timer =new Timer();
            timer.schedule(new ButtonTask(), sec*1000);
        }

        /**
         * Getter of Timer object. To be used in timerCancel method to stop
         * timer.
         * @return Timer timer.
         */
        public Timer getTimer(){
            return timer;
        }

        /**
         * run() method for timer which auto-cancel at the designated time marker
         * in the MyTimer constructor
         */
        class ButtonTask extends TimerTask {
            public void run (){
                System.out.println("Timeout");
                //toolkit.beep();
                input(Key.CLEAR);
                timer.cancel();
            }
        }

        /**
         * Stops timer prior to time out.
         * @param
         */
        public void timerCancel(){
            timer.cancel();
        }
    }

    static Mode currentMode = Mode.READY;
    static State currentState = State.IDLE;
    static Mode changingCode;
    static KeySequence userInput = new KeySequence();
    static MyTimer timer;
    static int TIMEOUT = 15;

    /**
     * Adds a key to the current user-input code.
     * @param key to be added.
     */
    static public synchronized void input(Key key) {
        userInput.add(key);
        processInput();
    }

    /**
     * Resets the timer by cancelling the currently running timer
     * and then creating a new timer.
     */
    static private void resetTimer() {
        if (timer != null) timer.timerCancel();
        timer = new MyTimer(TIMEOUT);
    }

    /**
     * Clears out the userInput by assigning a new instance of
     * KeySequence to the variable.
     */
    static private void resetInput() {
        userInput = new KeySequence();
    }

    /**
     * Reassignes the program's mode and state to the specified params. 
     * @param mode The new mode the program will change to.
     * @param state The new state the program will change to.
     */
    static private void transition(Mode mode, State state) {
        currentMode = mode;
        currentState = state;
    }

    /**
     * Reassignes the program's mode and state to the specified params. 
     * @param mode The new mode the program will change to.
     * @param state The new state the program will change to.
     * @param code Specified which user passcode we are attempting to update.
     */
    static private void transition(Mode mode, State state, Mode code) {
        currentMode = mode;
        currentState = state;
        changingCode = code;
    }

    /**
     * The processInput() method performs the various state change actions. It utilizes the
     * currentMode, currentState, and changingCode variables to account for state.
     *
     */
    static private void processInput() {
        Key input = userInput.get(userInput.length()-1);
        KeySequence test;
        switch (currentMode) {
            case READY:
                switch (currentState) {
                    case IDLE:
                        if (input.isNum()) {
                            resetTimer();
                            transition(Mode.READY, State.RECORDING);
                        } else {
                            resetInput();
                        }
                        break;
                    case RECORDING:
                        if (input.isNum()) {
                            resetTimer();
                        } else if (input.equals(Key.POUND)) {
                            timer.timerCancel();
                            transition(Mode.USER, State.VALIDATING);
                            input(Key.POUND);
                        } else if (input.equals(Key.STAR)) {
                            timer.timerCancel();
                            transition(Mode.ADMIN, State.VALIDATING);
                            input(Key.STAR);
                        } else {
                            timer.timerCancel();
                            resetInput();
                            transition(Mode.READY, State.IDLE);
                        }
                        break;
                }
                break;
            case USER:
                test = userInput.trim();
                if (test.validate() && DB.checkCode(test.toNum())) {
                    GateInterface.openGate();
                    SpeakerInterface.output(Sound.POS);
                } else {
                    SpeakerInterface.output(Sound.NEG);
                }
                resetInput();
                transition(Mode.READY, State.IDLE);
                break;
            case ADMIN:
                switch (currentState) {
                    case VALIDATING:
                        test = userInput.trim();
                        if (test.validate() && DB.checkAdminCode(test.toNum())) {
                            resetTimer();
                            SpeakerInterface.output(Sound.NTR);
                            transition(Mode.ADMIN, State.WAITING);
                        } else {
                            SpeakerInterface.output(Sound.NEG);
                            transition(Mode.READY, State.IDLE);
                        }
                        resetInput();
                        break;
                    case WAITING:
                        resetTimer();
                        if (input.isNum()) {
                            ;
                        } else if (input.equals(Key.POUND)) {
                            resetInput();
                            transition(Mode.ADMIN, State.RECORDING, Mode.ADMIN);
                        } else if (input.equals(Key.STAR)) {
                            resetInput();
                            transition(Mode.ADMIN, State.RECORDING, Mode.USER);
                        } else {
                            timer.timerCancel();
                            resetInput();
                            transition(Mode.READY, State.IDLE);
                        }
                        break;
                    case RECORDING:
                        resetTimer();
                        if (input.isNum()) {
                            if (userInput.length() == 4) {
                                if (changingCode.equals(Mode.ADMIN)) {
                                    try {
                                        DB.updateAdminCode(userInput.toNum());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if (changingCode.equals(Mode.USER)) {
                                    try {
                                        DB.updateUserCode(userInput.toNum());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                SpeakerInterface.output(Sound.POS);
                                timer.timerCancel();
                                resetInput();
                                transition(Mode.READY, State.IDLE);
                            }
                        } else {
                            resetInput();
                            SpeakerInterface.output(Sound.NTR);
                            transition(Mode.ADMIN, State.WAITING);
                        }
                        break;
                }
                break;
        }
    }

    /**
     * Initializes a new instance of the Database class and Keypad Interface. 
     * Then calls the run() method of keypad interface to startup the keypad and
     * allow us to record user input through stdin. 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
            
            DB = new Database();
            
            KeypadInterface keypad = new KeypadInterface();
            keypad.run();
            
    }
}
