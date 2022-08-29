package game;

import static game.Data.*;

import java.util.LinkedList;

public abstract class TileSet extends LinkedList<Tile> {
    protected Code addTile(Tile tile) {
        super.add(tile);
        return Code.FIN;
    }
}

