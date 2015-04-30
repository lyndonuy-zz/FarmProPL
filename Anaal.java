/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lyndon
 */
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.*;
public class Anaal {
    protected static BufferedReader tokenReader;
    static HashMap<String, Token> reservedWord = new HashMap<String, Token>();
    static HashMap<String, Token> idWord = new HashMap<String, Token>();
    static int state;
    static String label;
    static String Code;
    static String token;
    static char tokenChar;
    static String builder = "", id, trash;
    static int ctr = 0;
    //static boolean TokenCreated = false;
    static ArrayList<String> lineOfCode = new ArrayList();
    static Token k;
    public Token getToken(){
        return null;
    }
    public Anaal() throws Exception{
        this.tokenReader = new BufferedReader(new FileReader("src/emp.txt"));
    }
    public void getLine() throws Exception{
        Code = this.tokenReader.readLine();
    }
    public void getNextChar() throws IOException{
        this.tokenChar = (char) tokenReader.read();
    }
    private void numbers() throws Exception{
        Outerloop:
        while(true){
            getNextChar();
            switch(tokenChar){
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    builder = builder + tokenChar;
                    break;
                default:
                    if(tokenChar == ' ')
                        break Outerloop;
            }
        }
    }
    public static void main(String[] args) throws Exception {

        HashMap<String, Token> rw = new HashMap<>();
        HashMap<String, Token> id = new HashMap<>();
        //tokenReader = new Scanner(new FileReader("src/emp.txt"));
        Token t;
        t = new Token("START", 1);
        rw.put("SUNRISE", new Token("START", 1));
        t = new Token("START_MAIN", 2);
        rw.put("FARMIN", new Token("START_MAIN", 2));
        t = new Token("END_MAIN", 2);
        rw.put("FARMOUT", new Token("END_MAIN", 2));
        rw.put("SUNSET", new Token("END", 1));
        rw.put("IS", new Token("ASSIGN_OP", 3));
        rw.put("ISEQUAL", new Token("RELOP", 4));
        rw.put("::", new Token("LINE_COMMENT", 12));
        rw.put("(:", new Token("BLOCK_COMMENT", 13));
        rw.put(":)", new Token("BLOCK_COMMENT", 13));
        rw.put("(", new Token("LPARA", 5));
        rw.put(")", new Token(")", 6));
        rw.put("+", new Token("ARITHAS_OP", 8));
        rw.put("-", new Token("ARITHAS_OP", 8));
        rw.put("*", new Token("ARITHAS_OP", 7));
        rw.put("/", new Token("ARITHAS_OP", 7));
        rw.put("ISNOTEQUAL", new Token("RELOP", 4));
        rw.put(">", new Token("RELOP", 4));
        rw.put("<", new Token("RELOP", 4));
        rw.put(">=", new Token("RELOP", 4));
        rw.put("<=", new Token("RELOP", 4));
        rw.put("NOT", new Token("log_optr", 4));
        rw.put("AND", new Token("log_optr", 4));
        rw.put("OR", new Token("log_optr", 4));
        rw.put("PLANTBOOL", new Token("data_type_bool", 3));
        rw.put("PLANTINT", new Token("data_type_int", 3));
        rw.put("PLANTDEC", new Token("data_type_dec", 3));
        rw.put("PLANTSTRING", new Token("data_type_string", 3));
        rw.put(";", new Token("TERMINATOR", 3));
        rw.put("HARVEST", new Token("res_word", 9));
        rw.put("FERTILIZEBOOL", new Token("res_word", 9));
        rw.put("FERTILIZEINT", new Token("res_word", 9));
        rw.put("FERTILIZEDEC", new Token("res_word", 9));
        rw.put("FERTILIZESTRING", new Token("res_word", 9));
        rw.put("GROW", new Token("UN_ARITH_OPTR", 8));
        rw.put("TRIM", new Token("UN_ARITH_OPTR", 8));
        rw.put("IF", new Token("COND_STMT", 10));
        rw.put("ELSEIF", new Token("COND_STMT", 4));
        rw.put("ELSE", new Token("COND_STMT", 4));
        rw.put("THEN", new Token("COND_STMT", 15));
        rw.put("ENDIF", new Token("COND_STMT", 4));
        rw.put("PLOW", new Token("LOOP", 4));
        rw.put("WHILE", new Token("LOOP", 4));
        rw.put("CHANGE", new Token("LOOP", 4));
        rw.put("SEASON", new Token("LOOP", 4));
        rw.put("YES", new Token("bool_const", 3));
        rw.put("NO", new Token("bool_const", 3));
        rw.put("\"", new Token("string_format", 11));
        rw.put(".", new Token("string_concat", 11));
        rw.put("@n", new Token("new_line", 11));
        rw.put("@t", new Token("tab_line", 11));
        rw.put("@\"", new Token("double quote", 11));
        rw.put(" ", new Token("WHITESPACE", 16));
        //reservedWord.putAll(rw);
        //Reader();
        int ctr = 0;
        System.out.println(rw);
        Anaal lexan = new Anaal();
        //System.out.println(reservedWord);
        /*while (ctr < lineOfCode.size()) {
            /*System.out.println(lineOfCode.get(ctr));
             checkCount();
            Code = lineOfCode.get(ctr);
            System.out.println("Checking: " + Code);
            System.out.println("Length is: " + Code.length());
            //checkCount();
            state = 1;
            for (int i = 0; i < Code.length(); i++) {
                //ctr = i;
                //checkState(Code.charAt(i));
            }
        }*/
    }
}
