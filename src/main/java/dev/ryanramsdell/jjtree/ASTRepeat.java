package dev.ryanramsdell.jjtree;

public
class ASTRepeat extends dev.ryanramsdell.jjtree.SimpleNode {
    private int num;
    public ASTRepeat(int id) {
        super(id);
    }

    public ASTRepeat(Knit p, int id) {
        super(p, id);
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public String toString() {
        return "Repeat " + this.num;
    }

}