package game;

import static game.Data.*;

public class Boneyard extends TileSet {
    private Code state = Code.VAL;

    protected Boneyard(Tile centerTile, Deck deck) {
        super();
        for (int i = deck.getInt(); i >=0; i--) {
            for (int j = i; j >= 0; j--) {
                if (i == j && i == centerTile.getPips()[0]) {
                    continue;
                }
                super.add(new Tile(i, j));
            }
        }
    }

    protected Tile draw() {
        if (super.size() == 0) {
            state = Code.EMP;
            return Tile.getNullTile();
        }
        return super.remove(getRandInt(super.size()));
    }

    protected Code getState() {
        return state;
    }
}
