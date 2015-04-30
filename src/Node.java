/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package farmpro;

import java.util.LinkedList;

/**
 *
 * @author Nyker
 */
public class Node {
    public Token content;
    public Node parent;
    public LinkedList<Node> children = null;
    
    public Node(Token passedContent) {
        this.content = passedContent;
        this.parent = null;
        this.children = new LinkedList<>();
    }
    public Node(Token passedContent, Node magulang) {
        this.content = passedContent;
        this.parent = magulang;
        this.children = new LinkedList<>();
    }
    
    public void addChild(Node anak) {
        children.add(anak);
        anak.parent = this;
    }
    
    public Token getContent() {
        return content;
    }
    public String getContentString() {
        return content.getTokenType();
    }
    public Node getParent() {
        return parent;
    }
    public Token getParentToken() {
        return parent.content;
    }
    public String getParentString() {
        return parent.content.getTokenType();
    }    
}
