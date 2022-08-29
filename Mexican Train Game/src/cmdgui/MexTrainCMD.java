package cmdgui;

import static game.Data.*;
import static cmdgui.Prints.*;
import game.MexTrainGame;
import java.util.Scanner;

/**
 This is the command line GUI for the MexTrainGame class. To run, compile and
 run with a JDK/JRE.

 @author  Tanner J. Evans
 @version %I%, %G%
 @since   11.0.8
 */
public class MexTrainCMD {
/*---------------------------/        DEBUG        \--------------------------*/
    private static final boolean debug  = false;
    private static final boolean divert = false;
    public  static boolean getDebug() { return debug; }
    /* Debug divStream positioned at end of file to reduce clutter.*/


/*---------------------------/        MAIN         \--------------------------*/
    public static void main(String[] args) {
        PlayerData playerData;
        Deck       deck;
        Scanner    input = divert ?
                        new Scanner(divStream).useDelimiter(", ") :
                        new Scanner(System.in);

        welcome(input);
        deck       = gatherDeckData(input);
        playerData = gatherPlayerData(input);
        MexTrainGame game = new MexTrainGame(deck, playerData);

        /*-----------------------/   MAIN GAME LOOP    \----------------------*/
        while ( game.getGameState() == State.IN_PROGRESS) {
            game.deal();

            /*---------------------/     ROUND LOOP      \--------------------*/
            while ( game.getRoundState() == State.IN_PROGRESS) {
                printBoard(game);
                PlayerType playerType = game.getPlayerType();
                if (playerType == PlayerType.NPC) {
                    game.playNPC();
                } else {
                    boolean drawFlag = false;

                    while (true) {
                        int[] move = getMovePC(game, input);
                        if ( !(move[0] == 0) && !(move[1] == 0) ) {
                            if (game.playPC(move)) {
                                break;
                            }
                            System.out.println("Move invalid. Try again.");
                        } else {
                            if (drawFlag) {
                                game.passPlayer();
                                break;
                            }
                            game.forceCurrentPlayerDraw();
                            drawFlag = true;
                        }
                    }
                }
            }
            game.resetRound();
            printRoundResults(game);
        }
        System.out.println("Game Over!");
    }


/*---------------------------/ SUPPORTING METHODS  \--------------------------*/

    protected static void welcome(Scanner input) {
        boolean   unanswered = true;
        int       fails      = 0;

        System.out.println(INTRO);
        do {
            checkFails(fails);
            System.out.println(CONT);
            String ans = input.next();
            if (ans.equals("n") || ans.equals("N")) {
                System.out.println("Ending application.");
                System.exit(0);
            } else if (!ans.equals("y") && !ans.equals("Y")) {
                System.out.println(BADIN);
                fails++;
            } else {
                unanswered = false;
            }
        } while (unanswered);
    }

    protected static Deck gatherDeckData(Scanner input) {
        Deck    deck       = null;
        boolean unanswered = true;
        int     fails      = 0;

        do {
            checkFails(fails);
            System.out.println(DECKCHECK);
            int ans;
            try {
                ans = input.nextInt();
                deck       = Deck.getType(ans);
                unanswered = false;
            } catch (Exception err) {
                System.out.println(BADIN);
                fails++;
            }
        } while (unanswered);

        return deck;
    }

    protected static PlayerData gatherPlayerData(Scanner scanner) {
        PlayerData playerData = new PlayerData();
        int        numNPCs    = 0;
        int        numPCs     = 0;
        boolean    unanswered = true;
        int        fails      = 0;

        System.out.println(NUMPLAYERSNOTICE);
        do {
            System.out.println(NPCCHECK);
            while ( !scanner.hasNextInt() ) {
                if ( scanner.hasNextLine() ) scanner.nextLine();
                fails++;
                checkFails(fails);
                System.out.println(BADIN);
                System.out.println(NPCCHECK);
            }
            int ans = scanner.nextInt();
            if ( ans < 0 || ans > 8 ) {
                System.out.println(RANGEERR);
                System.out.println(NUMPLAYERSNOTICE);
                fails++;
                checkFails(fails);
                continue;
            }
            numNPCs = ans;
            unanswered = false;
        } while ( unanswered );

        fails      = 0;
        unanswered = true;
        do {
            System.out.println(PCCHECK);
            while ( !scanner.hasNextInt() ) {
                if ( scanner.hasNextLine() ) scanner.nextLine();
                fails++;
                checkFails(fails);
                System.out.println(BADIN);
                System.out.println(PCCHECK);
            }
            int ans = scanner.nextInt();
            if ( ans < 0 || ans + numNPCs > 8 ) {
                System.out.println(RANGEERR);
                System.out.println(NUMPLAYERSNOTICE);
                fails++;
                checkFails(fails);
                continue;
            }
            numPCs = ans;
            unanswered = false;
        } while ( unanswered );


        for ( int i = 0 ; i < numPCs ; i++ ) {
            fails      = 0;
            unanswered = true;
            do {
                System.out.printf(PCNAME, i+1);
                String ans = scanner.next();
                if ( scanner.hasNextLine() ) scanner.nextLine();
                if ( ans.length() > 23 ) {
                    System.out.println(RANGEERR);
                    fails++;
                    checkFails(fails);
                    continue;
                }
                if ( !playerData.addPC(ans) ) {
                    System.out.println(DUPNAME);
                    fails++;
                    checkFails(fails);
                    continue;
                }
                unanswered = false;
            } while ( unanswered );
        }

        playerData.setNumNPCs(numNPCs);
        System.out.println(SPACER);
        return playerData;
    }

    private static void checkFails(int fails) {
        if ( fails > 2 ) {
            System.out.println(ATTLIM);
            System.exit(0);
        }
    }

    private static void printBoard(MexTrainGame game) {
        System.out.print(TRAINTITLE);
        int numPlayers = game.getNUMPLAYERS();
        for ( int i = 0; i <= numPlayers; i++ ) {
            if ( i == numPlayers ) {
                System.out.print(game.playerName(i) + " (current player): ");
            } else {
                System.out.print(game.playerName(i) + ": ");
            }
            System.out.println(game.trainState(i));
            System.out.printf("%s:", i+1);
            System.out.println(game.trainString(i));
        }
        if (game.getPlayerType() == PlayerType.PC) {
            System.out.println("Your hand:");
            System.out.println(game.getPlayerHandStr());
            System.out.println();
        } else {
            System.out.println("NPC turn complete.");
        }
    }

    private static int[] getMovePC(MexTrainGame game, Scanner scanner) {
        boolean unanswered = true;
        String answer = "";
        int tile  = 0;
        int train = 0;

        System.out.println(GETMOVE);
        do {
            String ans = scanner.next();
            if ( scanner.hasNextLine() ) scanner.nextLine();
            if ( ans.equals("pass") || ans.equals("play") ) {
                answer = ans;
                unanswered = false;
            } else {
                System.out.println(BADIN);
            }
        } while ( unanswered );


        if (answer.equals("play")) {

            unanswered = true;
            do {
                System.out.println(
                        "Which tile would you like to play?"
                                  );
                while ( !scanner.hasNextInt() ) {
                    System.out.println(BADIN);
                }
                int ans = scanner.nextInt();
                if ( ans < 1 || ans > game.currentPlayerHandSize() ) {
                    System.out.println(BADIN);
                    continue;
                }
                tile = ans;
                unanswered = false;
            } while ( unanswered );

            unanswered = true;
            do {
                System.out.println(
                        "Which train would you like to play it on?"
                                  );
                while ( !scanner.hasNextInt() ) {
                    System.out.println(BADIN);
                }
                int ans = scanner.nextInt();
                if ( ans < 1 || ans > game.getNUMPLAYERS() + 1 ) {
                    System.out.println(BADIN);
                    continue;
                }
                train = ans;
                unanswered = false;
            } while ( unanswered );
        }

        int[] result = { tile, train };
        return result;
    }

    private static void printRoundResults(MexTrainGame game) {
        String[] names = game.getPlayerNames();
        int[] scores = game.getPlayerScores();
        System.out.println();
        System.out.println(
        "------------------------ ROUND OVER ------------------------" );
        System.out.println();
        System.out.println("Player Scores:");
        for (int i = 0; i < game.getNUMPLAYERS(); i++) {
            System.out.printf("%-30s: %4d\n", names[i], scores[i]);
        }
    }

    private static final String  divStream =
            "y, 12, 3, 1, Tanner, ";
}