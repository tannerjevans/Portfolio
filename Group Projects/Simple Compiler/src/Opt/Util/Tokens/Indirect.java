package Opt.Util.Tokens;

public class Indirect extends Value {
    public int index;
    public String indExprName;

    public Indirect(String string) {
        indExprName = string;
    }

    @Override
    public String printOut() {
        return "";
    }

    @Override
    public boolean isEq(Value value) {
        if (value instanceof Indirect) {
            if (((Indirect) value).indExprName.equals(this.indExprName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isInd() {
        return true;
    }

    @Override
    public Indirect toInd() {
        return this;
    }
}
