package Opt.Util;

public abstract class Operator {

    public static OpEnum getOpEnum(String string) {
        OpEnum result;
        result = ArithOp.get(string);
        if (result == null) result = AssignOp.get(string);
        if (result == null) result = CompareOp.get(string);
        return result;
    }

    public interface OpEnum {
        String getOperator();
    }

    public enum ArithOp implements OpEnum {
        ADD("+"),
        SUB("-"),
        MUL("*"),
        DIV("/");
        final String operator;

        ArithOp(String operator) {
            this.operator = operator;
        }

        public String getOperator() {
            return this.operator;
        }

        public static ArithOp get(String string) {
            for (ArithOp op : ArithOp.values()) if (op.operator.equals(string)) return op;
            return null;
        }
    }

    public enum AssignOp implements OpEnum {
        SET_EQ(":=");
        final String operator;

        AssignOp(String operator) {
            this.operator = operator;
        }

        public String getOperator() {
            return this.operator;
        }

        public static AssignOp get(String string) {
            for (AssignOp op : AssignOp.values()) if (op.operator.equals(string)) return op;
            return null;
        }
    }

    public enum CompareOp implements OpEnum {
        W_EQ("w="),
        W_LEQ("w<="),
        W_GEQ("w>="),
        W_LT("w<"),
        W_GT("w>"),
        I_EQ("i="),
        I_LEQ("i<="),
        I_GEQ("i>="),
        I_LT("i<"),
        I_GT("i>");
        final String operator;

        CompareOp(String operator) {
            this.operator = operator;
        }

        public String getOperator() {
            return this.operator;
        }

        public static CompareOp get(String string) {
            for (CompareOp op : CompareOp.values()) if (op.operator.equals(string)) return op;
            return null;
        }

    }

    public enum BoolOp implements OpEnum {
        NOT("not"),
        AND("and"),
        OR("or");

        final String operator;

        BoolOp(String operator) {
            this.operator = operator;
        }

        public String getOperator() {
            return this.operator;
        }

        public static BoolOp get(String string) {
            for (BoolOp op : BoolOp.values()) if (op.operator.equals(string)) return op;
            return null;
        }
    }

    public enum AnnotOp implements OpEnum {
        ANNOT_OP;

        @Override
        public String getOperator() {
            return "";
        }
    }
}
