package vn.com.insee.corporate.util.insee;

public class INSEEMessage {
    private int tree;
    private int reduce;

    public INSEEMessage(int tree, int reduce) {
        this.tree = tree;
        this.reduce = reduce;
    }

    public int getTree() {
        return tree;
    }

    public void setTree(int tree) {
        this.tree = tree;
    }

    public int getReduce() {
        return reduce;
    }

    public void setReduce(int reduce) {
        this.reduce = reduce;
    }
}
