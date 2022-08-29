/**
 * 	For this project, this will function as our command-line interface for input. It would be cool to have an ASCII
 * 	art keypad or something. It's just going to be a runnable that boots up and accepts one button at a time, and
 * 	only accepts supported data.
 */
 
/**
 * Author: Thomas Bowidowicz - 3/9/2021
 * KeypadInterface class - CGK Project UNM CS460 SPR'21
 * This class serves as the command line interface for the program.
 * Users enter the password into the terminal, the integers are
 * converted to the key enum values, and then sent to the CommDriver class.
 */

import java.util.Scanner;
import java.lang.Runnable;

public class KeypadInterface implements Runnable {
    private String keyOne = "";
    private int i = 0;
    private Key key;

    /**
     * Run method
     * @param - none
     * @return - none
     * This method overrides the run method in the Runnable class.
     * It contains the logic that runs the command interface for the
     * keypad interactions. It uses a scanner to read in the integer
     * values from the commandline and then converts them to the
     * corresponding enum values and calls the input() method in the
     * CommDriver class with those keys as a parameter. Pressing "x"
     * followed by enter will exit the loop.
     *
     * I added a counter to and if statements to the while loop to
     * help with reprinting the keypadArt and the "Please enter password"
     * phrase.
     */
    public void run() {
        Scanner s = new Scanner(System.in);

        while (!(keyOne.equals("x"))) {
            if (i == 0) {
                keypadArt();
                System.out.println("Please enter password: ");
                i++;
            }
            else {
                keyOne = s.nextLine();
                CommGateDriver.input(convert(keyOne));
                if (keyOne.equals("clear") || keyOne.equals("Clear") || keyOne.equals("CLEAR")) {
                    i = 0;
                }
                if (keyOne.equals("#")) {
                    //System.out.println("Checking password: ");
                    i = 0;
                }
            }
        }
    }

    /**
     *
     * @param - String s
     * @return - Key key
     * This method takes a String  as a parameter and returns a Key enum value.
     * The switch statement checks the String entered for each of the enum values
     * in the Key enum and creates a key equal to that value. The default case is
     * a String being entered that is not from the Key enum.
     */
    private Key convert(String s) {
        switch (s) {
            case "0":
                key = Key.ZERO;
                break;
            case "1":
                key = Key.ONE;
                break;
            case "2":
                key = Key.TWO;
                break;
            case "3":
                key = Key.THREE;
                break;
            case "4":
                key = Key.FOUR;
                break;
            case "5":
                key = Key.FIVE;
                break;
            case "6":
                key = Key.SIX;
                break;
            case "7":
                key = Key.SEVEN;
                break;
            case "8":
                key = Key.EIGHT;
                break;
            case "9":
                key = Key.NINE;
                break;
            case "#":
                key = Key.POUND;
                break;
            case "*":
                key = Key.STAR;
                break;
            case "Clear":
            case "clear":
            case "CLEAR":
                key = Key.CLEAR;
                break;
            default:
                System.out.println("Invalid input. Key pressed is not on keypad.");
                break;
        }
        return key;
    }

    /**
     * @param - none
     * @return - none
     * The keypadArt method simply prints out a visual representation of the keypad to
     * the terminal.
     */
    private void keypadArt() {
        System.out.println("-----------------");
        System.out.println("|   1   2   3   |");
        System.out.println("|               |");
        System.out.println("|   4   5   6   |");
        System.out.println("|               |");
        System.out.println("|   7   8   9   |");
        System.out.println("|               |");
        System.out.println("|   #   0   *   |");
        System.out.println("|---------------|");
        System.out.println("|     CLEAR     |");
        System.out.println("-----------------");
    }
}
