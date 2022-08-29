/**
 * Author : Jared Bock 2/27/2021
 * ENUM to represent keypad keys for CGK Project UNM CS460 SPR'21
 */

public enum Key {
    ZERO(0), ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5),
    SIX(6), SEVEN(7), EIGHT(8), NINE(9),
    POUND(-1), STAR(-1), CLEAR(-1);

    private final int VAL;

    /**
     * Constructor for Key. Allows for values to be assigned to each enum.
     * @param VAL
     * Value assigned to each enum
     */

    Key(int VAL) {
        this.VAL = VAL;
    }

    /**
     * Boolean returning if key is a number using assigned values.
     * @return
     * true if not -1 o/w false
     */

    public boolean isNum() {
        return VAL != -1;
    }

    /**
     * Returns integer value of key or -1 for non-numerical keys.
     * @return
     * Assigned value
     */

    public int toNum() {
        return VAL;

    }
}



