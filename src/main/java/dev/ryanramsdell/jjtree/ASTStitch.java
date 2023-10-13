package dev.ryanramsdell.jjtree;

import dev.ryanramsdell.data.StitchType;

public
class ASTStitch extends dev.ryanramsdell.jjtree.SimpleNode {
    private StitchType type;
    private int num;
    public ASTStitch(int id) {
        super(id);
    }

    public ASTStitch(Knit p, int id) {
        super(p, id);
    }

    public void setType(StitchType type) {
        this.type = type;
    }
    public void setNum(int num) {
        this.num = num;
    }

    public StitchType getType() {
        return type;
    }

    public int getNum() {
        return num;
    }

    public String toString() {
        return this.type + " " + this.num;
    }

}