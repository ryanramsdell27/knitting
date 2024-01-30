package dev.ryanramsdell.interpreter;

import dev.ryanramsdell.data.KPattern;
import dev.ryanramsdell.enums.StitchType;
import dev.ryanramsdell.jjtree.*;
import dev.ryanramsdell.jjtree.Node;
import dev.ryanramsdell.jjtree.SimpleNode;

import java.lang.reflect.InvocationTargetException;


/**
 * Type parameterized interpreter of jjtree parsed from user input.
 * Iterates through the knitting program, mapping the grammar defined for the parser generator
 * to the API defined in the KnittingPattern interface
 * @param <T> Must implement the KPattern interface.
 */
public class TreeInterpreter<T extends KPattern> {
    private SimpleNode root;
    private final T pattern;


    public TreeInterpreter(SimpleNode root, Class<? extends T> kpClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.root = root;
        this.pattern = kpClass.getConstructor().newInstance();
    }

    /**
     * Pareses the input knitting pattern and interprets each command
     * @return The KPattern object after iterating over the pattern
     */
    public T interpret() {
        ASTStitch castOn = (ASTStitch) this.root.jjtGetChild(0);
        assert(castOn.getType().equals(StitchType.CAST_ON));
        this.pattern.init(castOn.getNum(), true);
        for(int i = 1; i < this.root.jjtGetNumChildren(); i++) {
            processNode(this.root.jjtGetChild(i), this.pattern);
        }

        return this.pattern;
    }

    /**
     * Maps a node from the parser tree into an instruction defined by KPattern API
     * @param node jjtree representation of a single operation in knitting pattern
     * @param pattern KPattern object on which to perform the mapped node operation
     */
    private void processNode(Node node, T pattern) {
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

    /**
     * Return instance of KPattern
     * @return instance of KPattern
     */
    public T getPattern() {
        return pattern;
    }
}
