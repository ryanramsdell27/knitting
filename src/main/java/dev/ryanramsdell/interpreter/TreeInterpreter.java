package dev.ryanramsdell.interpreter;

import dev.ryanramsdell.data.KnittingPattern;
import dev.ryanramsdell.jjtree.*;
import dev.ryanramsdell.jjtree.Node;
import dev.ryanramsdell.jjtree.SimpleNode;


public class TreeInterpreter {
    private SimpleNode root;
    private KnittingPattern pattern;

    public TreeInterpreter(SimpleNode root) {
        this.root = root;
    }

    public KnittingPattern interpret() {
        ASTStitch castOn = (ASTStitch) this.root.jjtGetChild(0);
        this.pattern = new KnittingPattern(castOn.getNum());
        for(int i = 1; i < this.root.jjtGetNumChildren(); i++) {
            processNode(this.root.jjtGetChild(i), this.pattern);
        }

        return this.pattern;
    }

    private void processNode(Node node, KnittingPattern pattern) {
        node.getId();
        if (node.getClass().equals(ASTStitch.class)) {
            ASTStitch stitch = (ASTStitch) node;
            for(int i = 0; i < stitch.getNum(); i++) {
                pattern.basicStitch(stitch.getType());
            }
        } else if (node.getClass().equals(ASTRepeat.class)) {
            ASTRepeat repeat = (ASTRepeat) node;
            for(int i = 0; i < repeat.getNum(); i++) {
                for(int j = 0; j < repeat.jjtGetNumChildren(); j++) {
                    processNode(repeat.jjtGetChild(j), pattern);
                }
            }
        } else if (node.getClass().equals(ASTDecrease.class)) {
            ASTDecrease decrease = (ASTDecrease) node;
            pattern.decrease(decrease.getType(), decrease.getNum());
        } else if (node.getClass().equals(ASTIncrease.class)) {
            ASTIncrease increase = (ASTIncrease) node;
            pattern.increase(increase.getType(), increase.getNum());
        }
    }



}
