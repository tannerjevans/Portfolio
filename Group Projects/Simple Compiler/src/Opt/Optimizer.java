package Opt;

import Opt.Util.ConditionedCFG;
import DataStructureGeneration.*;
import Opt.Util.Tokens.Variable;
import Opt.Util.Set;

import java.util.*;

public class Optimizer {
    private ConditionedCFG conditionedCFG;
    private final List<String> VARS;
    private String[] vars;

    public Optimizer(Map<Integer, CFG> cfg, List<String> vars) {
        vars.remove("true");
        vars.remove( "false");
        this.VARS = vars;
        Conditioner conditioner = new Conditioner(cfg);
        conditionedCFG = conditioner.conditionedCFG;
        conditionedCFG.printOut("Conditioned CFG:");
        this.vars = vars.toArray(new String[0]);
    }

    public void optimizeDefault() {
        foldConstants();
        conditionedCFG.printOut("After Constant Folding:");
        eliminateDeadCode();
    }

    public void foldConstants() {
        RDAnalyzer rd = new RDAnalyzer(conditionedCFG, vars);
        conditionedCFG = rd.fold();
    }

    public void eliminateDeadCode() {
        Set<Variable> liveVarsAtEnd = new Set<>();
        String outputVar = Data.outputVariable;
        if (!VARS.contains(outputVar)) {
            System.out.println("The provided output variable does not exist in code. Live Variable Analysis not " +
                                   "allowed. Continuing without dead code elimination. Fix and try again for full " +
                                   "optimization.");
        } else {
            liveVarsAtEnd.add(new Variable(outputVar));
            LivenessAnalyzer livenessAnalyzer = new LivenessAnalyzer(conditionedCFG, liveVarsAtEnd);
            conditionedCFG = livenessAnalyzer.getConditionedCFG();
            conditionedCFG.printOut("After Dead Code Elimination:");
        }
    }

    public Map<Integer, CFG> getOptimizedCFG() {
        return conditionedCFG.toOutputForm();
    }

}
