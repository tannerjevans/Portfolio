package DataStructureGeneration;

public class ASTNode<type> {
    public type getData() {
        return data;
    }

    public type[] getChildren() {
        return children;
    }

    public type getParent() {
        return parent;
    }

    type data;
    type [] children;
    type parent;

    public ASTNode(type data, type [] children, type parent){
        this.data = data;
        this.children = children;
        this.parent = parent;
    }


}