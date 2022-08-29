package Opt.Util;

public abstract class Pairs {

    public static class Pair<T, U> {
        final T first;
        final U second;

        private Pair() {
            first = null;
            second = null;
        }

        public Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }
    }

    public static class Position extends Pair<Integer, Integer> {
        final int node;
        final int line;

        public Position(int node, int line) {
            this.node = node;
            this.line = line;
        }

        @Override
        public boolean equals(Object o) {
            Position position = (Position) o;
            return position.line == this.line && position.node == this.node;
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }
}
