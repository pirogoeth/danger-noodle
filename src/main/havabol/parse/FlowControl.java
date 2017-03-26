package havabol.parse;

import havabol.Token;
import havabol.classify.*;

import java.util.*;

public class FlowControl implements ParseElement {

    private IfControl ifBlock;
    private WhileControl whileBlock;
    private ForControl forBlock;
    private SelectControl selectBlock;

    public FlowControl(IfControl ifBlock) {
        this.ifBlock = ifBlock;
    }

    public FlowControl(WhileControl whileBlock) {
        this.whileBlock = whileBlock;
    }

    public FlowControl(ForControl forBlock) {
        this.forBlock = forBlock;
    }

    public FlowControl(SelectControl selectBlock) {
        this.selectBlock = selectBlock;
    }

    public boolean isValid() {
        return ( this.ifBlock != null && this.ifBlock.isValid() ) ||
               ( this.whileBlock != null && this.whileBlock.isValid() ) ||
               ( this.forBlock != null && this.forBlock.isValid() ) ||
               ( this.selectBlock != null && this.selectBlock.isValid() );
    }

    public String debug() {
        StringBuilder sb = new StringBuilder();

        sb.append("FlowControl ~>\n");
        if ( this.ifBlock != null ) {
            sb.append("  " + this.ifBlock.debug());
        } else if ( this.whileBlock != null ) {
            sb.append(this.whileBlock.debug());
        } else if ( this.forBlock != null ) {
            sb.append("  " + this.forBlock.debug());
        } else if ( this.selectBlock != null ) {
            sb.append("  " + this.selectBlock.debug());
        }

        return sb.toString();
    }

}
