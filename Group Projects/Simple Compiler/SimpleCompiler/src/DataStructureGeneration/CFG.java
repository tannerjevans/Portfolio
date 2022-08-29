package DataStructureGeneration;

import java.util.ArrayList;

public class CFG {
        Integer identifier;
        public ArrayList<String> basicBlock;
        public ArrayList<String> nodeNames;
        public ArrayList<Integer> edges;

        public CFG(Integer identifier, ArrayList<String> basicBlock, ArrayList<Integer> edges, ArrayList<String> nodeNames){
            this.identifier = identifier;
            this.basicBlock = basicBlock;
            this.nodeNames = nodeNames;
            this.edges = edges;

        }

}
