package Opt.Util.Tokens;

public class Variable extends Value {
    public final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public String printOut() {
        return this.name;
    }

    @Override
    public boolean equals(Object object) {
        boolean result = false;
        if (object instanceof Variable) {
            result = this.name.equals(((Variable) object).name);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean isEq(Value value) {
        if (value.isConst() || value.isInd()) return false;
        return (((Variable) value).name.equals(this.name));
    }

    @Override
    public Variable toVar() {
        return this;
    }

    @Override
    public boolean isVar() {
        return true;
    }

}
