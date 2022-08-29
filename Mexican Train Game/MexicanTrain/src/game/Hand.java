package game;

import static game.Data.*;
import static java.lang.Math.abs;

public class Hand extends TileSet {
    protected Code rem(Tile tile) {
        if (super.contains(tile)) {
            super.remove(tile);
            return Code.FIN;
        }
        return Code.DNE;
    }

    protected String toStr() {
        String result = "";
        int j = 0;
        for (int i = 0; i < super.size(); i++) {
            if (i%6 == 0 && i != 0) {
                result += "\n";
                if (j == 0) result += "  ";
                j = abs(1-j);
            }
            result += String.format("%2d", i+1) + ":";
            result += super.get(i).tileToStr() + " ";
        }
        return result;
    }
}