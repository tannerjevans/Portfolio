package Opt.Util.Tokens;

import Opt.Util.Statement;
import Opt.Util.VarSet;

public class ReturnStatement extends Statement {
    public Variable returnVar;

    public ReturnStatement(Variable returnVar) {
        super();
        this.returnVar = returnVar;
    }

    @Override
    public VarSet getGen() {
        VarSet gen = new VarSet();

        gen.add(returnVar);

        return gen;
    }

    @Override
    public VarSet getKill() {
        VarSet kill = new VarSet();


        return kill;
    }

    @Override
    public String[] printOut() {
        String[] response = new String[4];
        response[0] = "RETURN";
        response[1] = "";
        response[2] = returnVar.name;
        response[3] = "";
        return response;
    }

}
