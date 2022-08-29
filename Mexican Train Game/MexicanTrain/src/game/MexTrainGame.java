/* The game package contains JavaDoc comments intended to guide public
   use of the class, wrapped by the JavaDoc tags of /** ... */
/* Further commenting is provided for grading, notation, and internal /
   editor use. Non-JavaDoc comments are placed with any of the other
   commenting paradigms, and will not appear in JavaDoc documentation. Some
   non-public commenting serves to visually categorize classes for clarity. */

package game;

import java.util.Arrays;
import java.util.Stack;

import static cmdgui.MexTrainCMD.*;
import static game.Data.*;

/**
 <code>MexTrainGame</code> is the base class for the Mexican Train Game,
 and is intended to be the main hub by which GUIs and other external classes can
 access the rest of the package.
 <p>
 It is one of only two classes which contain public members, the other being
 <code>Data</code>.
 <p>
 The <code>MexTrainGame</code> class is designed to be constructed into a
 distinct, self-reliant object. It contains the methods required to request
 and update game data, in order to interface cleanly with a
 variety of GUIs. The only other class which is intended to be used by
 extra-package entitites is <code>Data</code>, which contains some public
 <code>enum</code>s which will be expedient for passing game data to and
 from the <code>MexTrainGame</code> class. These were moved to Data from
 any number of classes to which they earlier belonged to establish a central
 data type source, improve readability of the code of other classes, and
 leverage the far shorter name Data to clean up member access.
 <p>
 See {@link game.Data the Data class} for more information on provided
 public members.
 <p>
 @author  Tanner J. Evans
 @version %I%, %G%
 @since   11.0.8
 */
public class MexTrainGame {

/*---------------------------/  PRIVATE GAME DATA  \--------------------------*/

    /*--------- Constants After Instantiation: ---------*/
    private final int NUMPLAYERS;
    private final int HANDSIZE;
    private final Deck    DECK;
    private final Players PLAYERLIST;

    /*----------------- Class Variables: ---------------*/
    private int      currPlayerNum;
    private Train    mexTrain;
    private Tile     centerTile;
    private Boneyard boneyard;
    private State    gameState  = State.IN_PROGRESS;
    private State    roundState = State.IN_PROGRESS;
    private int      endRoundCounter = 0;
    private Stack<Integer> doubleStack = new Stack<Integer>();


/*---------------------------/     CONSTRUCTOR     \--------------------------*/
    /**
     The default and only constructor for the <code>MexTrainGame</code>
     <code>class</code>. Instantiates all game variables and game elements.

     @param deck        The type of deck used for the game. This object
        must be a constant from {@link game.Data.Deck the Deck enum}.
     @param playerData  An array containing data about the players for the
        game. This object must be an instance of
     */
    public MexTrainGame(Deck deck, PlayerData playerData) {
        debug              = getDebug();
        DECK               = deck;
        this.centerTile    = new Tile(DECK.getInt(), DECK.getInt());
        PLAYERLIST         = extractPlayerData(playerData, centerTile);
        NUMPLAYERS         = PLAYERLIST.size();
        HANDSIZE           = calcHandSize(DECK.getInt(), NUMPLAYERS);
        this.currPlayerNum = getRandInt(NUMPLAYERS);
        this.boneyard      = new Boneyard(centerTile, DECK);
        this.mexTrain      = new Train(centerTile);
        this.mexTrain.open();
    }


/*---------------------------/       GETTERS       \--------------------------*/

    public State getGameState() {
        return gameState;
    }

    public String[] getPlayerNames() {
        String[] names = new String[NUMPLAYERS];
        for (int i = 0; i < NUMPLAYERS; i++) {
            names[i] = PLAYERLIST.get(i).getName();
        }
        return names;
    }

    public int[] getPlayerScores() {
        int[] scores = new int[NUMPLAYERS];
        for (int i = 0; i < NUMPLAYERS; i++) {
            scores[i] = PLAYERLIST.get(i).getScore();
        }
        return scores;
    }

    public State getRoundState() {
        if ( endRoundCounter == NUMPLAYERS + 1) {
            roundState = State.COMPLETE;
        }
        return roundState;
    }

    public int getNUMPLAYERS() {
        return NUMPLAYERS;
    }

    public void resetRound () {
        int[] pips = centerTile.getPips();
        if (pips[0] == 0) {
            gameState = State.COMPLETE;
            return;
        }
        Tile newCenter = new Tile(pips[0]-1, pips[0]-1);
        centerTile = newCenter;
        roundState = State.IN_PROGRESS;
        endRoundCounter = 0;
        boneyard = new Boneyard(newCenter, DECK);
        for (Player player : PLAYERLIST) {
            Hand hand = player.getHand();
            int points = 0;
            for (Tile tile : hand) {
                int[] pipPoints = tile.getPips();
                points += pipPoints[0] + pipPoints[1];
            }
            player.reset(newCenter, points);
        }
        mexTrain = new Train (newCenter);
    }

    public void deal() {
        for (int j = 0; j < HANDSIZE; j++) {
            for ( int i = 0 ; i < NUMPLAYERS ; i++) {
                Tile tile = boneyard.draw();
                PLAYERLIST.get(i).dealTile(tile);
                if (debug) System.out.print(tile.tileToStr());
            }
            if (debug) System.out.println();
        }
    }

    protected int findMaxChainVal(Hand oldHand, int toMatch) {
        Hand hand = cloneHand(oldHand);
        int maxVal = 0;
        for (int i = 0; i < hand.size(); i++) {
            int[] pips = hand.get(i).getPips();
            int newMatch = -1;
            if (pips[0] == toMatch) {
                newMatch = pips[1];
            } else if (pips[1] == toMatch) {
                newMatch = pips[0];
            }
            if (newMatch >= 0) {
                int hereVal = pips[0] + pips[1];
                Hand newHand = cloneHand(hand);
                newHand.remove(i);
                int newVal = hereVal + findMaxChainVal(newHand, newMatch);
                if (newVal > maxVal) {
                    maxVal = newVal;
                }
            }
        }
        return maxVal;
    }

    protected Hand cloneHand(Hand original) {
        Hand handClone = new Hand();
        for (Tile tile : original) {
            int[] pips = tile.getPips();
            Tile newTile = new Tile(pips[0], pips[1]);
            handClone.add(newTile);
        }
        return handClone;
    }

    public void playNPC() {
        boolean drawFlag = false;

        while ( !doubleStack.isEmpty() ) {
            Train train;
            int index = doubleStack.peek();
            if (index == 0) {
                train = mexTrain;
            } else {
                train = PLAYERLIST.get(index-1).getTrain();
            }
            Player player = PLAYERLIST.get(currPlayerNum);
            Hand   hand   = player.getHand();
            Code   played = Code.NUL;

            int match = train.getMatchPip();
            boolean matchFound = false;
            int     matchIndex = 0;
            int     matchValue = 0;

            for ( int i = 0 ; i < hand.size() ; i++ ) {
                int[] pips = hand.get(i).getPips();
                if ( pips[0] == match || pips[1] == match ) {
                    int sum = pips[0] + pips[1];
                    if ( sum > matchValue ) {
                        matchValue = sum;
                        matchIndex = i;
                        matchFound = true;
                    }
                }
            }

            if ( matchFound ) {
                Tile tile = hand.get(matchIndex);
                played = train.addTile(hand.get(matchIndex));
                if ( played == Code.DUB ) {
                    hand.rem(tile);
                    endRoundCounter = 0;
                    return;
                }
                if ( played == Code.FIN ) {
                    hand.rem(tile);
                    doubleStack.pop();
                    endRoundCounter = 0;
                    currPlayerNum   = getNext(1);
                    return;
                }
            }

            if ( drawFlag ) {
                currPlayerNum = getNext(1);
                player.openTrain();
                return;
            }

            if ( !boneyard.isEmpty() ) {
                player.dealTile(boneyard.draw());
            } else {
                endRoundCounter++;
            }
            drawFlag = true;
        }

        while (true) {
            Player player = PLAYERLIST.get(currPlayerNum);
            Hand   hand   = player.getHand();
            Code   played = Code.NUL;

            int   match                = player.getTrain().getMatchPip();
            int   personalTrainMatches = 0;
            int[] matchLocations       = new int[hand.size()];
            Arrays.fill(matchLocations, 0);
            for ( int i = 0 ; i < hand.size() ; i++ ) {
                int[] pips = hand.get(i).getPips();
                if ( pips[0] == match || pips[1] == match ) {
                    matchLocations[personalTrainMatches] = i;
                    personalTrainMatches++;
                }
            }

            if (personalTrainMatches > 0) {
                int maxVal = 0;
                int maxValTileIndex = matchLocations[0];
                for (int i = 0; i < personalTrainMatches; i++) {
                    Hand newHand = cloneHand(hand);
                    Tile matchTile = newHand.remove(matchLocations[i]);
                    int[] pips = matchTile.getPips();
                    int newMatch;
                    int hereVal = pips[0] + pips[1];
                    if (pips[0] != match) {
                        newMatch = pips[0];
                    } else {
                        newMatch = pips[1];
                    }
                    int thisChainMaxVal = hereVal + findMaxChainVal(hand,
                                                                    newMatch);
                    System.out.println(thisChainMaxVal);
                    if (thisChainMaxVal > maxVal) {
                        maxVal = thisChainMaxVal;
                        maxValTileIndex = matchLocations[i];
                    }
                }

                System.out.println(maxVal);
                played = player.toOwnTrain(hand.get(maxValTileIndex));
                if (played == Code.FIN) {
                    hand.remove(maxValTileIndex);
                    endRoundCounter = 0;
                    currPlayerNum   = getNext(1);
                    return;
                }
            } else {
                match = mexTrain.getMatchPip();
                boolean matchFound = false;
                int     trainMatch = 0;
                int     matchIndex = 0;
                int     matchValue = 0;

                for ( int i = 0 ; i < hand.size() ; i++ ) {
                    int[] pips = hand.get(i).getPips();
                    if ( pips[0] == match || pips[1] == match ) {
                        int sum = pips[0] + pips[1];
                        if ( sum > matchValue ) {
                            matchValue = sum;
                            matchIndex = i;
                            matchFound = true;
                            trainMatch = 0;
                        }
                    }
                }

                for ( int i = 0 ; i < NUMPLAYERS ; i++ ) {
                    Train train = PLAYERLIST.get(i).getTrain();
                    if ( train.getState() == TrainState.OPEN ) {
                        match = train.getMatchPip();
                        for ( int j = 0 ; j < hand.size() ; j++ ) {
                            int[] pips = hand.get(j).getPips();
                            if ( pips[0] == match || pips[1] == match ) {
                                int sum = pips[0] + pips[1];
                                if ( sum > matchValue ) {
                                    matchValue = sum;
                                    matchIndex = j;
                                    matchFound = true;
                                    trainMatch = i + 1;
                                }
                            }
                        }
                    }
                }

                if ( matchFound ) {
                    Tile tile = hand.get(matchIndex);
                    if ( trainMatch == 0 ) {
                        played = mexTrain.addTile(tile);
                    } else {
                        Train train = PLAYERLIST.get(trainMatch-1).getTrain();
                        played = train.addTile(hand.get(matchIndex));
                    }
                    if ( played == Code.DUB ) {
                        hand.rem(tile);
                        endRoundCounter = 0;
                        doubleStack.push(trainMatch);
                        return;
                    }
                    if ( played == Code.FIN ) {
                        hand.rem(tile);
                        endRoundCounter = 0;
                        currPlayerNum   = getNext(1);
                        return;
                    }
                }
            }
            if ( drawFlag ) {
                currPlayerNum = getNext(1);
                player.openTrain();
                return;
            }

            if ( !boneyard.isEmpty() ) {
                player.dealTile(boneyard.draw());
            } else {
                endRoundCounter++;
            }
            drawFlag = true;
        }
    }

    public boolean playPC(int[] move) {
        boolean played = false;

        Player player = PLAYERLIST.get(currPlayerNum);
        int selectedTile = move[0] - 1;
        int selectedTrain = move[1];
        Tile tile;
        Train train;
        boolean ownTrain = false;
        tile = player.getHand().get(selectedTile);
        int index = getNext(selectedTrain-1);
        if (selectedTrain == 1) {
            train = mexTrain;
        } else {
            ownTrain = (index == currPlayerNum);
            train = PLAYERLIST.get(index).getTrain();
        }

        boolean doublePlay = false;
        int[] pips = tile.getPips();
        if (pips[0] == pips[1]) doublePlay = true;

        if ( !doubleStack.isEmpty() ) {
            if (index+1 != doubleStack.peek()) {
                return played;
            }
        }

        if (ownTrain) {
            if (player.toOwnTrain(tile) == Code.FIN) {
                player.play(tile);
                player.closeTrain();
                if ( !doubleStack.isEmpty() ) {
                    doubleStack.pop();
                }
                if (doublePlay) {
                    doubleStack.push(currPlayerNum+1);
                    endRoundCounter = 0;
                    played = true;
                    return played;
                }
                currPlayerNum = getNext(1);
                endRoundCounter = 0;
                played = true;
            }
        } else {
            if (train.addTile(tile) == Code.FIN) {
                player.play(tile);
                if ( !doubleStack.isEmpty() ) {
                    doubleStack.pop();
                }
                if (doublePlay) {
                    doubleStack.push(index+1);
                    endRoundCounter = 0;
                    played = true;
                    return played;
                }
                currPlayerNum = getNext(1);
                endRoundCounter = 0;
                played = true;
            }
        }
        return played;
    }

    public String playerName(int shift) {
        if (shift == 0) return "Mexican Train";
        int next = getNext(shift);
        return PLAYERLIST.get(next).getName();
    }

    // Cycle through player list
    private int getNext(int shift) {
        return (currPlayerNum + shift) % NUMPLAYERS;
    }

    public PlayerType getPlayerType() {
        return PLAYERLIST.get(currPlayerNum).getType();
    }

    public String getPlayerHandStr() {
        return PLAYERLIST.get(currPlayerNum).getHand().toStr();
    }


    public String trainState(int shift) {
        if (shift == 0) return TrainState.OPEN.toStr();
        int next = getNext(shift);
        return PLAYERLIST.get(next).trainStateStr();
    }

    public String trainString(int shift) {
        if (shift == 0) return mexTrain.toStr();
        int next = getNext(shift);
        return PLAYERLIST.get(next).trainStr();
    }

    public int currentPlayerHandSize() {
        return PLAYERLIST.get(currPlayerNum).getHand().size();
    }

    public void forceCurrentPlayerDraw() {
        if ( boneyard.getState() != Code.EMP ) {
            Tile tile = boneyard.draw();
            PLAYERLIST.get(currPlayerNum).dealTile(tile);
        }
    }

    public void passPlayer() {
        PLAYERLIST.get(currPlayerNum).openTrain();
        currPlayerNum = getNext(1);
        endRoundCounter++;
    }
}
