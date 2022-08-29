package game;

import static game.Data.*;

public class Player {

/*---------------------------/   PRIVATE FIELDS    \--------------------------*/
    private final String          name;
    private final PlayerType type;

    private Train train;
    private Hand  hand;
    private int   score = 0;


/*---------------------------/     CONSTRUCTOR     \--------------------------*/
    protected Player(String name, PlayerType type, Tile trainBase) {
        this.name  = name;
        this.type  = type;
        this.train = new Train(trainBase);
        this.hand  = new Hand();
    }


/*---------------------------/    FIELD GETTERS    \--------------------------*/
    protected String getName() {
    return name;
}

    protected PlayerType getType() {
        return type;
    }

    protected Train getTrain() {
        return train;
    }

    protected Hand getHand() {
        return hand;
    }

    protected int getScore() {
        return score;
    }


/*---------------------------/   PACKAGE METHODS   \--------------------------*/
    /* Housekeeping for starting a new round. */
    protected void reset(Tile trainBase, int points) {
        this.train  = new Train(trainBase);
        this.hand   = new Hand();
        this.score += points;
    }

    /* Add domino to hand. */
    protected Code dealTile(Tile tile) {
        return hand.addTile(tile);
    }

    protected String trainStateStr() {
        return train.getState().toStr();
    }

    protected String trainStr() {
        return train.toStr();
    }

    protected void openTrain() {
        train.open();
    }

    protected void closeTrain() {
        train.close();
    }

    protected Code play(Tile tile) {
        return hand.rem(tile);
    }

    protected Code toOwnTrain(Tile tile) {
        int[] pipsNew  = tile.getPips();
        int toMatch    = train.getMatchPip();
        if (pipsNew[0] == toMatch) {
            train.add(tile);
            train.close();
            return Code.FIN;
        }
        else if (pipsNew[1] == toMatch) {
            tile.flipPips();
            train.add(tile);
            train.close();
            return Code.FIN;
        }
        return Code.PMM;
    }

}
