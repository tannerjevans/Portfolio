package Opt.Util;

import Opt.Util.Tokens.Variable;

public class Label {
    public int value;
    public int index;
    public boolean endStatement = false;

    public Label(int value) {
        this.value = value;
        this.index = value - 1;
        if (value == 0) endStatement = true;
    }

    @Override
    public boolean equals(Object object) {
        boolean result = false;
        if (object instanceof Label) {
            result = this.value == ((Label) object).value;
        }
        return result;
    }

    @Override
    public int hashCode() {
        return 0;
    }


    public String printOut() {
        if (endStatement) return "End";
        return Integer.toString(value);
    }
}
