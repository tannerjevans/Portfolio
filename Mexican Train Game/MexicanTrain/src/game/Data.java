/* Refer to the MexTrainGame class for notes on code commenting. */

package game;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 Contains data and methods related to the <code>game</code> package, the
 majority of which are not <code>public</code>.
 <p>
 It is one of only two classes which contain public members, the other being
 <code>MexTrainGame</code>.
 <p>
 Elements are rebased to the <code>Data</code> class under any of the
 following conditions:
 <ul>
 <li>The element is large and interferes with readability of its home class.
 (Protected)
 <li>The element requires complex implementation, but there is no need for
 understanding the implementation to use it. (Protected)
 <li>There is no immediately most-sensible home for the element. (Protected)
 <li>The element provides package-wide support. (Protected)
 <li>The element represents information relating to the broad concept of the
 Mexican Train game (metadata, game rules and standards, etc.).
 (Protected or Private)
 <li>The element is an enum or method which helps external classes utilize
 the package and <b>does not</b> alter <code>game</code> data. (Public)
 </ul>
 <p>
 Some <code>enum</code>s and <code>method</code>s are made public to
 enable GUI drivers to pass game data back and forth in formats that are
 native to the game. This reduces the chance of errors and simplifies code.
 An effort has been made to provide well-formed and easy-to-use data types to
 prevent functional but senseless code (arrays of ints which correspond to a
 large variety of disparate options, for instance).
 <p>
 <b>Public Members:</b>
 <ul>
 <li>{@link game.Data.Deck the Deck enum}
 <li>{@link game.Data.PlayerType the PlayerType enum}
 </ul>
 See {@link MexTrainGame the MexTrainGame class} for more
 information on game creation and package use.
 <p>
 @author  Tanner J. Evans
 @version %I%, %G%
 @since   11.0.8 */
public abstract class Data {

/*-------------------------------PUBLIC MEMBERS-------------------------------*/
    /*
     These members were based in Data to provide clean, easy-to-implement, and
     error-resistant modes of communication to external classes (mainly the
     two GUIs to be designed for the package). All public members of the Data
     class are enums, since these can not altered externally, and it enables
     driver classes to convert external data to internal data in sensible
     ways. It also forces input errors towards compilation time to prevent an
     abundance of error checking or run-time errors from unexpected values.
     */

    /**
     An <code>enum</code> encoding the supported deck types. The
     structure is designed to accomodate the addition of deck types
     dynamically; each new deck type need only be added to the constant(value)
     list.
     However, changes to available deck types <b>will require</b> a rekey of
     <code>dealNum</code> and its indexing design.
     */
    public enum Deck {
        NINE(9),
        TWELVE(12);

        private final int value;

        Deck(int value) {
            this.value = value;
        }

        /**
         A method for obtaining the <code>Deck</code> <code>enum</code> that
         corresponds to a provided integer value.

         @param value The integer value of the <code>Deck enum</code>.
         @return      A <code>Deck enum</code> of corresponding value.
         */
        public static Deck getType(int value) {
            for (Deck deck : Deck.values()) {
                if (deck.getInt() == value) {
                    return deck;
                }
            }
            throw new EnumConstantNotPresentException(Deck.class,
                                                      Integer.toString(value));
        }

        /**
         A method for obtaining the integer value of a <code>Deck</code>
         constant.

         @return  The integer value corresponding to the <code>Deck</code>.
         */
        public int getInt() {
            return value;
        }
    }

    /**
     An <code>enum</code> used to encode the two player types.
     */
    public enum PlayerType {
        PC, NPC
    }

    public enum State {
        IN_PROGRESS, COMPLETE
    }

    /**
     A class for collecting data to be passed into the game.
     Prior to calling the game constructor, external classes must create and
     populate a PlayerData object. If the external class fails to populate at
     least two players of any type, the game increases the number of NPCs
     populated until there are two players. The PlayerData object rejects all
     attempts at population that would result in greater than 8 players.
     Public methods return booleans to indicate success or failure of the
     method invocation.
     <p>
     See method documentation for use of the public methods.
     <p>
     @author  Tanner J. Evans
     @version %I%, %G%
     @since   11.0.8 */
    public static class PlayerData {
        private int         numNPCs = 0;
        private Set<String> pcNames = new HashSet<>();
        private boolean     lock    = false;

        /**
         Sets the number of NPCs in the PlayerList.
         @param  numNPCs The total number of NPCs that will be in the game.
                            Valid calls overwrite.
         @return boolean = <p>
                           { true if successful <p>
                           { false if unsuccessful
         */
        public boolean setNumNPCs(int numNPCs) {
            boolean success = numNPCs >= 0 &&
                              (numNPCs + pcNames.size()) <= 8 &&
                              !lock;
            if (success) {
                this.numNPCs = numNPCs;
            }
            return success;
        }

        /**
         Adds a new PC player to the list of PC players, and must be called
         for each addition. Duplicate names are rejected.
         @param  name      The name of the PC to be added.
         @return boolean = <p>
                           { true if successful <p>
                           { false if unsuccessful
         */
        public boolean addPC(String name) {
            boolean success = (numNPCs + pcNames.size() + 1) <= 8 &&
                              pcNames.add(name) &&
                              !lock;
            return success;
        }

        protected int getNumNPCs() {
            lock = true;
            return numNPCs;
        }

        protected String[] getPCnames() {
            lock = true;
            return pcNames.toArray(new String[0]);
        }
    }


/*----------------------------NON-PUBLIC MEMBERS------------------------------*/
    /*
     These members were based in Data to improve readability of object
     classes, provide centralized configuration, and isolate
     implementations which seemed likely to undergo revision. To the
     greatest extent possible, the goal has been to ensure that once
     complete, only the code within this section of the package will need
     to be modified to alter functionality, rules, and settings.
     */


    /* MetaData */

    protected static final String[] npcPlayerNames = {
            "Jehosephat", "Mahjong Master", "Credibly Cretinous", "f1n4l_f0rm",
            "trainz4dayz", "Conqueror", "KriegerinDerSpielsteine",
            "Herr Gordon", "FrauMau", "the_pain_train_72", "ergo",
            "Stormageddon", "OkayOmens", "ogtylestyle", "helpI'mAnNpcLetMeOut",
            "blueSun52", "TheAviatar", "ThiefOfJoy", "DoubleTrouble",
            "sloppyJose", "lkng4ad8plz", "cowerds", "(^o^)",
            "knightofthedamned", "gitRdone"
    };

    protected static final int[][] dealNum = {
            //2   3   4   5   6   7   8      number of players
            {15, 13, 10,  8,  7,  6,  6}, // double 9s deck
            {16, 16, 15, 14, 12, 10,  9}  // double 12s deck
    };

    protected enum SetType { TRAIN, HAND }

    /* Return codes for various operations. */
    protected enum Code {
        VAL, // = valid
        NUL, // = null
        FIN, // = finished successfully
        CLO, // = train close
        EMP, // = boneyard empty
        PMM, // = pip mismatch
        USD, // = unsatisfied double
        DNE, // = does not exist
        OTH, // = other
        DUB  // = double added
    }

    protected static enum TrainState {
        OPEN,
        CLOSED;

        protected String toStr() {
            return this.equals(TrainState.OPEN) ? "(open)" : "";
        }
    }

    /* Package Utility Methods and Classes */

    protected static boolean debug;

    protected static int calcHandSize(int deck, int players) {
        deck = (deck/3) - 3;
        players = players - 2;
        return Data.dealNum[deck][players];
    }

    protected static int getRandInt(int exUpBound) {
        return ThreadLocalRandom.current().nextInt(exUpBound);
    }

    protected static String[] getRandNPC(int numNPCs) {
        String[] names = new String[numNPCs];
        Arrays.fill(names, "");
        int index = getRandInt(npcPlayerNames.length);
        names[0] = npcPlayerNames[index];
        for (int i = 1; i < numNPCs; i++) {
            while (names[i].equals("")) {
                index = getRandInt(npcPlayerNames.length);
                String name = npcPlayerNames[index];
                names[i] = name;
                for (int j = i - 1; j >= 0; j--) {
                    if (names[j].equals(name)) {
                        names[i] = "";
                        break;
                    }
                }
            }
        }
        return names;
    }

    protected static Players extractPlayerData(PlayerData playerData,
                                               Tile trainBase) {
        Players players = new Players();
        String[] npcNames = getRandNPC(playerData.getNumNPCs());
        for (String npcName : npcNames) {
            players.add(new Player(npcName, PlayerType.NPC, trainBase));
        }
        for (String pcName : playerData.getPCnames()) {
            players.add(new Player(pcName, PlayerType.PC, trainBase));
        }
        return players;
    }

    // The actual data structure that houses player data. Extends LinkedList to
    // simplify code.
    protected static class Players extends LinkedList<Player> {
    }
}
