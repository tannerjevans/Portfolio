package game;

import static game.Data.*;

public class Train extends TileSet {
    private TrainState state = TrainState.CLOSED;

    protected Train(Tile tile) {
        super();
        int[] pips = tile.getPips();
        Tile newTile = new Tile(0, pips[1]);
        super.add(tile);
    }

    @Override
    protected Code addTile(Tile tile) {
        if (state == TrainState.CLOSED) return Code.CLO;
        int[] pipsNew  = tile.getPips();
        int toMatch    = getMatchPip();
        if (pipsNew[0] == toMatch) {
            super.add(tile);
            if (pipsNew[1] == toMatch) {
                return Code.DUB;
            }
            return Code.FIN;
        }
        else if (pipsNew[1] == toMatch) {
            tile.flipPips();
            super.add(tile);
            if (pipsNew[1] == toMatch) {
                return Code.DUB;
            }
            return Code.FIN;
        }
        return Code.PMM;
    }

    protected int getMatchPip() {
        int[] last = super.getLast().getPips();
        return last[1];
    }

    protected TrainState getState() {
        return state;
    }

    protected String toStr() {
        int size = super.size();
        int start;
        String result = "";
        if (size <= 7) {
            start = 1;
            result += super.get(0).pipToStr();
        } else {
            start = size - 7;
            result += super.get(start).pipToStr();
        }
        for (int i = start; i < size; i++) {
            result = result + ", " + super.get(i).tileToStr();
        }
        return result;
    }

    protected void open() {
        state = TrainState.OPEN;
    }

    protected void close() {
        state = TrainState.CLOSED;
    }
}