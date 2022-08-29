package Opt.Util.Tokens;

public abstract class Value {


    public static Value parseValue(String string) {
        string = string.replace("(", "").replace(")", "").trim();
        if (string.matches("\\[.*][0-9]+")) return new Indirect(string);
        else if (string.matches("\\[[0-9]*]")) {
            String newString = string.replace("[", "").replace("]", "");
            return new Constant(Integer.parseInt(newString));
        } else if (string.matches("\\[true]")) {
            String newString = string.replace("[", "").replace("]", "");
            return new Constant(true);
        } else if (string.matches("\\[false]")) {
            return new Constant(false);
        } else {
            return new Variable(string.replace("[", "").replace("]", ""));
        }
    }

    public static Variable castVar(Value value) {
        return (Variable) value;
    }

    public static Constant castConst(Value value) {
        return (Constant) value;
    }

    public abstract String printOut();
    public boolean isVar() { return false; }
    public boolean isConst() { return false; }
    public boolean isBool() { return false; }
    public abstract boolean isEq(Value value);
    public boolean isInd() { return false; }
    public Constant toConst() {return null;}
    public Variable toVar() {return null;}
    public Indirect toInd() {return null;}
}
