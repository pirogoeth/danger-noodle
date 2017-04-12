package havabol.parse;

import havabol.Token;
import havabol.classify.*;
import static havabol.util.Text.*;

import java.util.*;

public class FlowControl implements ParseElement {

    public enum Type {
        IF,
        FOR,
        WHILE,
        SELECT;
    }

    private Type flowType;

    private IfControl ifBlock;
    private WhileControl whileBlock;
    private ForControl forBlock;
    private SelectControl selectBlock;

    public FlowControl(IfControl ifBlock) {
        this.flowType = Type.IF;
        this.ifBlock = ifBlock;
    }

    public FlowControl(WhileControl whileBlock) {
        this.flowType = Type.WHILE;
        this.whileBlock = whileBlock;
    }

    public FlowControl(ForControl forBlock) {
        this.flowType = Type.FOR;
        this.forBlock = forBlock;
    }

    public FlowControl(SelectControl selectBlock) {
        this.flowType = Type.SELECT;
        this.selectBlock = selectBlock;
    }

    public Type getFlowType() {
        return this.flowType;
    }

    public IfControl getIf() {
        return this.ifBlock;
    }

    public WhileControl getWhile() {
        return this.whileBlock;
    }

    public ForControl getFor() {
        return this.forBlock;
    }

    public boolean isValid() {
        return ( this.ifBlock != null && this.ifBlock.isValid() ) ||
               ( this.whileBlock != null && this.whileBlock.isValid() ) ||
               ( this.forBlock != null && this.forBlock.isValid() ) ||
               ( this.selectBlock != null && this.selectBlock.isValid() );
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "FlowControl ~>\n"));
        if ( this.ifBlock != null ) {
            sb.append(this.ifBlock.debug(indent + 2));
        } else if ( this.whileBlock != null ) {
            sb.append(this.whileBlock.debug(indent + 2));
        } else if ( this.forBlock != null ) {
            sb.append(this.forBlock.debug(indent + 2));
        } else if ( this.selectBlock != null ) {
            sb.append(this.selectBlock.debug(indent + 2));
        }

        return sb.toString();
    }

}
