/**
 * Author: Robin Acosta 3/8/2021
 * The speaker interface is a class that maps sound constants to the specific
 * processes needed to instruct the speaker to output the intended sound.
 */

public class SpeakerInterface {

    /**
     * Takes a Sound and prints out the associated sound.
     * @param sound to be played
     */
    protected static void output(Sound sound){
        switch (sound){
            case POS:
                System.out.println("Speaker plays positive sound");
                break;
            case NEG:
                System.out.println("Speaker plays negative sound");
                break;
            case NTR:
                System.out.println("Speaker plays neutral sound");
                break;
            default:
                System.out.println("No sound is played");
        }
    }
}