package Opt.Util.Tokens;

public class Constant extends Value {
    public int value;
    public boolean boolVal;
    public boolean isBoolVal;

    public Constant(int value) {
        this.value = value;
        this.isBoolVal = false;
    }
    public Constant(boolean boolVal) {
        this.boolVal = boolVal;
        this.isBoolVal = true;
    }

    @Override
    public String printOut() {
        if (this.isBoolVal) {
            if (boolVal) return "true";
            else return "false";
        } else {
            return Integer.toString(this.value);
        }
    }

    @Override
    public boolean isConst() {
        return true;
    }

    @Override
    public boolean isEq(Value value) {
        if (value.isVar() || value.isInd()) return false;
        Constant constant = (Constant) value;
        if (constant.isBoolVal && this.isBoolVal) {
            return constant.boolVal = this.boolVal;
        } else if (!constant.isBoolVal && !this.isBoolVal) {
            return constant.value == this.value;
        }
        return false;
    }

    @Override
    public Constant toConst() {
        return this;
    }

    @Override
    public boolean isBool() {
        return isBoolVal;
    }

}
