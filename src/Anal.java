
import java.util.*;
import java.util.regex.*;
import java.io.*;
import jdk.nashorn.internal.parser.TokenKind;

public class Anal {

    protected static Scanner tokenReader;
    static HashMap<String, Token> reservedWord = new HashMap<String, Token>();
    static HashMap<String, Token> idWord = new HashMap<String, Token>();
        static int state;
        static String label;
        static String Code;
        static String token;
        static String builder, id, trash;
        static int ctr = 0;
        static boolean TokenCreated = false;
        static ArrayList<String> lineOfCode = new ArrayList();
        static Token k;

    public Token getToken() {
        return null;
    }

    private static void Reader() throws Exception {
        tokenReader = new Scanner(new FileReader("src/emp.txt"));
        String line = "";
        tokenReader.useDelimiter("\n");
        while (tokenReader.hasNext()) {
            line = tokenReader.next();
            lineOfCode.add(line);
        }
        tokenReader.close();
    }

    /*public Anal(String program) throws Exception{
     tokenReader = new Scanner(new FileReader("emp.txt"));
     }*/
    private static void checker() {
        if (reservedWord.containsKey(builder) && state == 4) {
            //reservedWord.getOrDefault("SUNRISE", k).setCheck(true);
        }
    }

    protected static void checkToken() {
        
    }

    protected static void checkState(char c) {
        switch (c) {
            case '<':
            case '>':
                System.out.println(ctr);
                builder = builder + c;
                checkCount();
                relop(Code.charAt(ctr));
                break;
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
                System.out.println(ctr);
                builder = builder + c;
                checkCount();
                numbers(Code.charAt(ctr)); //ISNOT <=
                break;
            case '+':
            case '-':
            case '*':
            case '/':
                System.out.println(ctr);
                checkCount();
                operator(Code.charAt(ctr));
                break;
/*            case 'I':
                System.out.println(ctr);
                builder = builder + c;
                checkCount();
//                relop2(Code.charAt(ctr));
                break;*/
            case '"':
                System.out.println(ctr);
                builder = builder + c;
                checkCount();
                stringConst(Code.charAt(ctr));
                break ;

            default:
                System.out.println(ctr);
                if(c != ' ')
                    doubleCheckRW(c);
        }
    }

    protected static void checkCount() {
        if (ctr < Code.length()) {
            ctr++;
            System.out.println("Increment OK: " + ctr);
        } else {

        }
    }

    protected static void stringConst(char c) {
        state = 2;
        Outerloop:
        while (true) {
            switch (c) {
                case '"':
                    builder = builder + c;
                    break Outerloop;
                default:
                    builder = builder + c;
                    checkCount();
                    stringConst(Code.charAt(ctr));
            }
        }
        checker();
    }

    protected static void relop2(char c) {
        state = 2;
        Outerloop:
        while (true) {
            switch (c) {
                case 'S':
                    state = 2;
                    checkCount();
                    relop2(Code.charAt(ctr));
                case 'N':
                    state = 2;
                    checkCount();
                    relop2(Code.charAt(ctr));
                case 'O':
                    state = 2;
                    checkCount();
                    relop2(Code.charAt(ctr));
                case 'T':
                    builder = builder + c;
                    state = 4;
                    checkCount();
                    relop2(Code.charAt(ctr));
                case 'E':
                    builder = builder + c;
                    state = 3;
                    checkCount();
                    relop2(Code.charAt(ctr));
                case 'Q':
                    builder = builder + c;
                    state = 3;
                    checkCount();
                    relop2(Code.charAt(ctr));
                case 'U':
                    builder = builder + c;
                    state = 3;
                    checkCount();
                    relop2(Code.charAt(ctr));
                case 'A':
                    builder = builder + c;
                    state = 3;
                    checkCount();
                    relop2(Code.charAt(ctr));
                case 'L':
                    builder = builder + c;
                    state = 4;
                    checkCount();
                    relop2(Code.charAt(ctr));
                default:
                    if (c == ' ') {
                        checker();
                    }
                    break Outerloop;
            }
        }
    }
    /* method to create Identifiers. Not sure how to do it. the method should save whatever the method can create in the 
     id hashmap. I have at least a draft code but I'm not posting it yet since I am not quite confident with the code
     sample source draft code: http://stackoverflow.com/questions/18389135/how-to-verify-if-a-value-in-hashmap-exist
     */

    protected static void createID() {

    }

    //checking for operators
    protected static void operator(char c) {
        state = 2;
        Outerloop:
        while (true) {

        }
    }

    //Checking for numbers in a string..... or an equation.... Idk. 
    protected static void numbers(char c) {
        state = 2;
        Outerloop:
        while (true) {
            switch (c) {
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
                    builder = builder + c;
                    checkCount();
                    c = Code.charAt(ctr); //numbers(Code.charAt(ctr));
                default:
                    if (c == ' ') {
                        break Outerloop;
                    } else {

                    }

            }
        }
    }

    //Done I think..... I mean checking for greater than etc.....
    protected static void relop(char c) {
        state = 2;
        Outerloop:
        while (true) {
            if (c == '=') {
                builder = builder + c;
            } else if (c == ' ') {
                break Outerloop;
            } else {
                builder = "";
            }
            break Outerloop;
        }
    }

    //checking for Reserved words
    protected static void doubleCheckRW(char c) {
        state = 2;
        Outerloop:
        while (true) {
            switch (c) {
                case 'S':
                case 'U':
                case 'N':
                    builder = builder + c;
                    System.out.println("doubleCheck");
                    //System.out.println(c);
                    state = 3;
                    checkCount();
                    c = Code.charAt(ctr);
                    if(c == 'N'){
                        System.out.println(c);
                        break Outerloop;
                    }
                    else break;
            }
        }
        if (state == 3) {
            checkCount();
            c = Code.charAt(ctr);
        }
        System.out.println(builder);
        state = 2;
        SecOuterLoop:
        while (true) {
            switch (c) {
                case 'R':
                    builder = builder + c;
                    state = 3;
                    checkCount();
                    c = Code.charAt(ctr);
                    break;
                case 'I':
                case 'S':
                case 'E':
                    if (state == 3) {
                        state = 4;
                    }
                    builder = builder + c;
                    checkCount();
                    c = Code.charAt(ctr);
                    break;
                case 'T':
                    builder = builder + c;
                    System.out.println(c);
                    checkCount();
                    c = Code.charAt(ctr);
                    state = 4;
                    break;
                default:
                    break SecOuterLoop;
            }
        }
    }

    public static void main(String[] args) throws Exception {

        HashMap<String, Token> rw = new HashMap<>();
        HashMap<String, Token> id = new HashMap<>();
        //tokenReader = new Scanner(new FileReader("src/emp.txt"));
        Token t;
/*        t = new Token("START", 1);
        rw.put("SUNRISE", t);
        t = new Token("START_MAIN", 2);
        rw.put("FARMIN", t);
        t = new Token("END_MAIN", 2);
        rw.put("FARMOUT", t);
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
        reservedWord.putAll(rw);*/
        Reader();
        int ctr = 0;
        System.out.println(rw);
        //System.out.println(reservedWord);
        while (ctr < lineOfCode.size()) {
            /*System.out.println(lineOfCode.get(ctr));
             checkCount();*/
            Code = lineOfCode.get(ctr);
            System.out.println("Checking: " + Code);
            System.out.println("Length is: " + Code.length());
            checkCount();
            state = 1;
            for (int i = 0; i < Code.length(); i++) {
                //ctr = i;
                checkState(Code.charAt(i));
            }
        }
    }
}
