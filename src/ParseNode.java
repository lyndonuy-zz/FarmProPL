/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lyndon
 */
public class ParseNode {
    Token tk;
    ParseNode firstChild;
    ParseNode nextBro;
    
    public ParseNode(){
        this(null, null, null);
    }
    public ParseNode(Token t, ParseNode n, ParseNode sib){
        this.tk = t;
        this.firstChild = n;
        this.nextBro = sib;
    }
    public void createNode(Token t, ParseNode n, ParseNode nod){
        this.tk = t;
        this.firstChild = n;
        this.nextBro = nod;
    }
    public void setToken(Token t){
        this.tk = t;
    }
    public void setFirstChild(ParseNode n){
        this.firstChild = n;
    }
    public void setNextBro(ParseNode n){
        this.nextBro = n;
    }
    
    
    public Token getToken(){
        return this.tk; 
    }
}
