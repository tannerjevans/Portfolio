package cmdgui;

public abstract class Prints {
    protected static final String INTRO =
          "-------------------- Mexican Train Game --------------------\n" +
          "\n" +
          "The rules for this game can be found at:                    \n" +
          "    https://www.mexicantrainrulesandstrategies.com/         \n" +
          "\n" +
          "A couple of important details about this command-line       \n" +
          "version of the game:                                        \n" +
          " ~ If you select a double-twelve deck, pip values of 10, 11,\n" +
          "   and 12 are represented by A, B, and C. You may select    \n" +
          "   them with either their integer or character values.      \n";

    protected static final String CONT =
            "Would you like to continue? (Y/n)" ;

    protected static final String SPACER =
            "\n\n\n\n\n\n\n\n\n";

    protected static final String TRY =
            "Please try again.";

    protected static final String DECKCHECK =
            "Would you like to play with a double-9s deck or a double-\n" +
            "    12s deck? (enter 9 or 12)";

    protected static final String NUMPLAYERSNOTICE =
            "You must select between 2 and 8 players.";

    protected static final String PCCHECK =
            "How many humans will be playing?\n" +
            "(enter a number between 0 and 8)";

    protected static final String NPCCHECK =
            "How many non-humans will be playing?\n" +
            "(enter a number between 0 and 8)";

    protected static final String PCNAME =
            "Please provide a name for player %d: \n";

    protected static final String DUPNAME =
            "Duplicate names are not permitted.\n" +
            TRY;

    protected static final String BADIN =
            "Unrecognized input.\n" +
            TRY;

    protected static final String ERRORESCAPE =
            "Unrecognized input. Try again, or enter QUIT to quit.";

    protected static final String RANGEERR =
            "The number of players must be between 2 and 8, inclusive.\n" +
            TRY;

    protected static final String ATTLIM =
            "Attempt limit reached. Ending program.";

    protected static final String TRAINTITLE =
            "\n-------------------------  Trains  " +
            "-------------------------\n\n";

    protected static final String GETMOVE =
            "Would you like to play a tile or draw/pass?\n" +
            "(type play or pass)";
}
