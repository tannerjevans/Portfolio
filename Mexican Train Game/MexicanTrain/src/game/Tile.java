package game;

import static game.Data.*;

public class Tile {
    private final int[] pips = {0, 0};
    private Code code = Code.FIN;
    protected Tile(int left, int right) {
        pips[0] = left;
        pips[1] = right;
    }

    private Tile() {
        pips[0] = -1;
        pips[1] = -1;
        code = Code.DNE;
    }

    protected void flipPips() {
        int temp = pips[0];
        pips[0] = pips[1];
        pips[1] = temp;
    }
    protected int[] getPips() {
        return pips;
    }

    protected String tileToStr() {
        String left  = Integer.toString(pips[0], 13).toUpperCase();
        String right = Integer.toString(pips[1], 13).toUpperCase();
        return "["+left+"|"+right+"]";
    }

    protected String pipToStr() {
        String right = Integer.toString(pips[1], 13).toUpperCase();
        return "...|"+right+"]";
    }

    protected Code getCode() {
        return code;
    }

    protected static Tile getNullTile() {
        return new Tile();
    }
}
