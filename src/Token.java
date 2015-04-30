/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package farmpro;

/**
 *
 * @author Nyker
 */
public class Token{
    public String lexeme;
    public TokenType tokType;
    
    public Token() {
        lexeme = null;
        tokType = null;
    }
    public Token(String str, TokenType t){
        this.lexeme = str;
        this.tokType = t;
    }
    
    public String getLexeme() {
        return lexeme;
    }
    public String getTokenType() {
        return tokType.toString();
    }
}