package Opt.Util;

import Opt.RDAnalyzer;
import Opt.Util.Tokens.Variable;
import java.util.LinkedList;

public class Set<T> extends LinkedList<T> {
    Class<T> type;

    @Override
    public boolean add(T t) {
        if (!super.contains(t)) {
            super.add(t);
            return true;
        }
        return false;
    }

    public boolean union(Set<T> ts) {
        boolean changes = false;
        for (T t : ts) {
            changes = changes | this.add(t);
        }
        return changes;
    }

    public boolean difference(Set<T> ts) {
        boolean changes = false;
        for (T t : ts) {
            changes = changes | this.remove(t);
        }
        return changes;
    }

    @Override
    public boolean remove(Object t) {
        if (super.contains(t)) {
            super.remove(t);
            return true;
        }
        return false;
    }


}
