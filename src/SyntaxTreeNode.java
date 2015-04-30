

//package sequencethesizer;

import java.util.*;
import java.util.regex.*;

public class SyntaxTreeNode {
    protected Token tk;
    //protected int level;
    protected List <SyntaxTreeNode> children;
    
    public SyntaxTreeNode() {
        super();
        children = new ArrayList<SyntaxTreeNode>();
    }

    public SyntaxTreeNode(Token t) {
        this();
        tk(t);
    }

    public void tk(Token t) {
        this.tk = t;
    }

    public int getNumberChildren() {
        return getChildren().size();
    }

    public List<SyntaxTreeNode> getChildren() {
        return this.children;
    }

    public boolean childrenExist() {
        return (getNumberChildren() > 0);
    }

    public void setChildren(List<SyntaxTreeNode> c) {
        this.children = c;
    }

    public void addChild(SyntaxTreeNode c) {
        children.add(c);
    }

    public void addChildIndex(int i, SyntaxTreeNode c) throws IndexOutOfBoundsException {
        children.add(i, c);
    }

    public void removeChildren() {
        this.children = new ArrayList<SyntaxTreeNode>();
    }

    public void removeChildIndex(int i) throws IndexOutOfBoundsException {
        children.remove(i);
    }

    public SyntaxTreeNode getChildIndex(int i) throws IndexOutOfBoundsException {
        return children.get(i);
    }

    public String getData() {
        return "Lexeme: " + this.tk.getLexeme() + " TokenType: " + this.tk.getTokenType();
    }

    public String toString() {
        return getData().toString();
    }

    public boolean checkEqual(SyntaxTreeNode node) {
        return node.getData().equals(getData());
    }

    public int hashCode() {
        return getData().hashCode();
    }

    public String toStringVerbose() {
        String s = getData().toString() + ":[";
        for (SyntaxTreeNode node : getChildren()) {
            s += node.getData().toString() + ", ";
        }
        //Need to apply Pattern because for some reason. 
        Pattern pattern = Pattern.compile(", $", Pattern.DOTALL);
        Matcher match = pattern.matcher(s);

        s = match.replaceFirst("");
        s += "]";
        return s;
    }
}
