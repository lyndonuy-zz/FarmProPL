/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lyndon
 */
public class ParseTree {

    ParseNode root;

    public ParseTree() {
        this.root = null;
    }

    public ParseTree(ParseNode r) {
        this.root = r;
    }

    void setRoot(ParseNode r) {
        this.root = r;
    }

    public String printZeTree(ParseNode node) {
        if (node != null && (node.nextBro != null || node.firstChild != null)) {
            return "[ Token Type: " + node.getToken().getTokenType() + " Lexeme: " + node.getToken().getLexeme() + " f = " + printZeTree(node.firstChild) + " s = " + printZeTree(node.nextBro) + " ] ";
        } else {
            return "\\";
        }
    }

    public String toString() {
        if (this.root != null) {
            return "[ Token Type: " + this.root.getToken().getTokenType() + " Lexeme: " + this.root.getToken().getLexeme() + " f = " + printZeTree(this.root.firstChild) + " s = " + printZeTree(this.root.nextBro) + " ]";
        } else {
            return "\\";
        }
    }

    public String print(ParseNode node) {
        String s = "";
        if (node == null) {
            return "\\";
        }
        s = "[ TokenType: " + node.getToken().getTokenType() + " Lexeme: " + node.getToken().getLexeme();
        if (node.firstChild != null) {
            s = s + " f = " + print(node.firstChild);
        }
        if (node.nextBro != null) {
            s = s + " s = " + print(node.nextBro);
        }
        else{
            s = s + "\\";
        }
        return s = s + " ] ";
    }
}
