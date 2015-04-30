/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lyndon
 */
import java.util.*;
import java.io.*;

public class ParserSyn {

    static SyntaxTree root = null;

    static Token tk; //Current Token
    static int index = 0;
    static long line;
    static Token expectedToken = new Token(); //Look Ahead Token
    LexAnal lex;
    static SyntaxTree tree;
    static List<Token> tklist = new ArrayList();
    static List<Long> lineNumber = new ArrayList();
    static String filename;
    public ParserSyn() throws Exception {

        this.root = null;
        this.filename = "";
        //Parser();
    }
    public ParserSyn(String s) throws Exception {
        this.root = null;
        this.filename = s;
        lex = new LexAnal(s);
        Parser();
    }
    public void Parser() throws Exception{
        //ParserSyn parse = new ParserSyn();
        //LexAnal scan = new LexAnal("maliit.txt");
        tklist.addAll(lex.buildList());
        lineNumber.addAll(lex.buildError());
        //parse = new ParserSyn();
        System.out.println(lex);
        System.out.println("DONE");
        PROGRAM();
        System.out.println("DONE");
        System.out.println(lex);
        System.out.println("----------------------------------------PARSE TREE-------------------------------------");
        System.out.println(root);
        //System.out.println("--------------------------------------USING PRINT FOR PARSE TREE----------------------------------------------");
        //System.out.println(tree.print(tree.root.firstChild));
        int ctr = 0;
        /*while(ctr < tklist.size()){
         System.out.println(tklist.get(ctr).getLexeme());
         ctr++;
         }*/
    }
    public static void main(String[] args) throws Exception {
        //ParserSyn parse = new ParserSyn();
        LexAnal scan = new LexAnal("SSCLittle.txt");

        tklist.addAll(scan.buildList());
        lineNumber.addAll(scan.buildError());
        //parse = new ParserSyn();
        System.out.println(scan);
        System.out.println("DONE");
        PROGRAM();
        System.out.println("DONE");
        System.out.println(scan);
        System.out.println("----------------------------------------PARSE TREE-------------------------------------");
        System.out.println(root);
        //System.out.println("--------------------------------------USING PRINT FOR PARSE TREE----------------------------------------------");
        //System.out.println(tree.print(tree.root.firstChild));
        int ctr = 0;
        /*while(ctr < tklist.size()){
         System.out.println(tklist.get(ctr).getLexeme());
         ctr++;
         }*/
    }
    public SyntaxTree getTree(){
        return root;
    }
    public static void ADVANCE() throws Exception {
        tk = tklist.get(index);
        line = lineNumber.get(index);
        index++;
    }

    public static SyntaxTreeNode makeMe(Token t) {
        return new SyntaxTreeNode(t);
    }

    public static SyntaxTreeNode createMe(Token t) {
        return new SyntaxTreeNode(t);
    }

    public static Token ERROR(TokenType expectedToken, Token t, Long line) {
        String ERROR_RED = "\u001B[31m";
        String ERROR_RESET = "\u001B[0m";
        System.out.println(ERROR_RED + "PARSING NOT SUCCESSFUL: Token Expected: " + expectedToken + " . Token Found at line number (" + line + "):" + t.getTokenType() + " Lexeme of: " + t.getLexeme());
        System.exit(0);
        return new Token(t.getLexeme(), TokenType.error);

    }

    public static SyntaxTree PROGRAM() throws Exception {
        System.out.println("PROGRAM");
        SyntaxTreeNode NodeMe = new SyntaxTreeNode(new Token("PRGM_ROOT", TokenType.pgm_root));
        SyntaxTreeNode SUNRISE = new SyntaxTreeNode();;
        SyntaxTreeNode FARMIN = new SyntaxTreeNode();;
        SyntaxTreeNode PGMNAME = new SyntaxTreeNode();;
        SyntaxTreeNode STMT = new SyntaxTreeNode();;
        SyntaxTreeNode FARMOUT = new SyntaxTreeNode();;
        SyntaxTreeNode SUNSET = new SyntaxTreeNode();;
        SyntaxTree STATETREE = new SyntaxTree();;
        SyntaxTree PROGRAM = new SyntaxTree();;
        SyntaxTreeNode TERMINATOR = new SyntaxTreeNode();
        SyntaxTree P = new SyntaxTree();
        Token ERROR = new Token();
        int countError = 0;
        List<SyntaxTreeNode> children = new ArrayList<SyntaxTreeNode>();
        ADVANCE();
        if (tk.getTokenType().equals("line_comment") || tk.getTokenType().equals("block_comments")) {
            ADVANCE();
        }
        if (tk.getLexeme().equals("SUNRISE")) {
            children.add(makeMe(tk));
            //System.out.println(SUNRISE.getToken().getLexeme());
            ADVANCE();
            if (tk.getLexeme().equals("FARMIN")) {
                children.add(makeMe(tk));
                //System.out.println(FARMIN.getToken().getLexeme());
                ADVANCE();
                //System.out.println(tk.getTokenType());
                if (tk.getTokenType().equals("identifier")) {
                    children.add(makeMe(tk));
                    // System.out.println(PGMNAME.getToken().getLexeme());
                    ADVANCE();
                    children.add(PGMSTMT().root);
                    //ADVANCE();
                    /*while(!tk.getTokenType().equals("FARMOUT")){
                     System.out.println("ENTER");
                     STATETREE = PGMSTMT();
                     if(tk.getTokenType().equals("terminator")){
                     System.out.println("TEMP TERMINATOR");
                     TERMINATOR = makeMe(tk);
                     ADVANCE();
                     }
                     }*/

                    //ADVANCE();
                    if (tk.getLexeme().equals("FARMOUT")) {
                        children.add(makeMe(tk));
                        ADVANCE();
                        if (tk.getLexeme().equals("SUNSET")) {
                            System.out.println("SUNSET");
                            children.add(makeMe(tk));
//                            System.out.println(SUNSET.tk.getTokenType());
                        } else {
                            ERROR = ERROR(TokenType.end, tk, line);
                            countError++;
                        }
                    } else {
                        ERROR = ERROR(TokenType.end_main, tk, line);
                        countError++;
                    }
                } else {
                    ERROR = ERROR(TokenType.identifier, tk, line);
                    countError++;
                }
            } else {
                ERROR = ERROR(TokenType.start_main, tk, line);
                countError++;
            }
        } else {
            ERROR = ERROR(TokenType.start, tk, line);
            countError++;
        }
              NodeMe.setChildren(children);
        P.setRoot(NodeMe);
  
        root = P;
        //System.out.println(PROGRAM.printZeTree(PROGRAM.root));
        System.out.println("END PROGRAM");
        return P;
    }

    public static SyntaxTree PGMSTMT() throws Exception { //STMTS
        System.out.println("STMTS");
        SyntaxTreeNode NodeMe = new SyntaxTreeNode(new Token("STMTS", TokenType.stmt));
        SyntaxTreeNode SOFF = new SyntaxTreeNode();
        SyntaxTree STMT = new SyntaxTree();
        SyntaxTree STMTSPRIME = new SyntaxTree();
        SyntaxTree PGM_STMT = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        Token ERROR;
        if (tk.getTokenType().equals("data_type_int") || tk.getTokenType().equals("data_type_dec") || tk.getTokenType().equals("data_type_bool") || tk.getTokenType().equals("data_type_string") || tk.getTokenType().equals("identifier") || tk.getTokenType().equals("input_type") || tk.getTokenType().equals("output_type") || tk.getTokenType().equals("cond_if") || tk.getTokenType().equals("loop_do") || tk.getTokenType().equals("loop_while") || tk.getTokenType().equals("switcher") || tk.getTokenType().equals("increment") || tk.getTokenType().equals("decrement") || tk.getTokenType().equals("line_comment") || tk.getTokenType().equals("block_comment")) {
            children.add(STMT().root);
            if (tk.getTokenType().equals("data_type_int") || tk.getTokenType().equals("data_type_dec") || tk.getTokenType().equals("data_type_bool") || tk.getTokenType().equals("data_type_string") || tk.getTokenType().equals("identifier") || tk.getTokenType().equals("input_type") || tk.getTokenType().equals("output_type") || tk.getTokenType().equals("cond_if") || tk.getTokenType().equals("loop_do") || tk.getTokenType().equals("loop_while") || tk.getTokenType().equals("switcher") || tk.getTokenType().equals("increment") || tk.getTokenType().equals("decrement") || tk.getTokenType().equals("line_comment") || tk.getTokenType().equals("block_comment")) {
                children.add(STMTS_PRIME().root);
            }
            NodeMe.setChildren(children);
            PGM_STMT.setRoot(NodeMe);
            System.out.println("DONE CONNECTING");
        }
        System.out.println("RETURNING TO HEAVEN");
//        System.out.println("PGMSTMT: " + PGM_STMT);
        return PGM_STMT;
    }

    public static SyntaxTree STMTS_PRIME() throws Exception { //STMTS PRIME
        System.out.println("STMTS PRIME");
        SyntaxTreeNode PGM_BODY_NODE = new SyntaxTreeNode(new Token("STMTS_PRIME", TokenType.stmt));
        SyntaxTree STMTS = new SyntaxTree();
        SyntaxTree PGM_BODY = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        if (tk.getTokenType().equals("data_type_int") || tk.getTokenType().equals("data_type_dec") || tk.getTokenType().equals("data_type_bool") || tk.getTokenType().equals("data_type_string") || tk.getTokenType().equals("identifier") || tk.getTokenType().equals("input_type") || tk.getTokenType().equals("output_type") || tk.getTokenType().equals("cond_if") || tk.getTokenType().equals("loop_do") || tk.getTokenType().equals("loop_while") || tk.getTokenType().equals("switcher") || tk.getTokenType().equals("increment") || tk.getTokenType().equals("decrement") || tk.getTokenType().equals("line_comment") || tk.getTokenType().equals("block_comment")) {
            children.add(PGMSTMT().root);
            PGM_BODY_NODE.setChildren(children);
            PGM_BODY.setRoot(PGM_BODY_NODE);
        } else if (tk.getTokenType().equals("end_main")) {
            System.out.println("LE CHECK");
        }
        return PGM_BODY;
    }

    public static SyntaxTree STMT() throws Exception { //STMT
        System.out.println("STMTS");
        SyntaxTreeNode STMTA = new SyntaxTreeNode(new Token("STMT", TokenType.stmt));
        SyntaxTreeNode terminator = new SyntaxTreeNode();
        SyntaxTreeNode identifier = new SyntaxTreeNode();
        SyntaxTreeNode increment = new SyntaxTreeNode();
        SyntaxTreeNode decrement = new SyntaxTreeNode();
        SyntaxTree tree_decl = new SyntaxTree();
        SyntaxTree tree_assign = new SyntaxTree();
        SyntaxTree tree_input = new SyntaxTree();
        SyntaxTree tree_output = new SyntaxTree();
        SyntaxTree tree_cond = new SyntaxTree();
        SyntaxTree tree_loop = new SyntaxTree();
        SyntaxTree tree_switch = new SyntaxTree();
        SyntaxTree STMT = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        Token ERROR;
        Outerloop:
        switch (tk.getTokenType()) {
            case "data_type_int":
            case "data_type_string":
            case "data_type_dec":
            case "data_type_bool":
                System.out.println(tk.getTokenType());
                children.add(DECLARATION().root);
                if (tk.getTokenType().equals("terminator")) {
                    children.add(makeMe(tk));
                    ADVANCE();
                }
                STMTA.setChildren(children);
                STMT.setRoot(STMTA);
                break;
            case "identifier":
                children.add(ASSIGN().root);
                if (tk.getTokenType().equals("terminator")) {
                    children.add(makeMe(tk));
                    ADVANCE();
                }
                STMTA.setChildren(children);
                STMT.setRoot(STMTA);
                break;
            case "increment":
                children.add(makeMe(tk));
                ADVANCE();
                if (tk.getTokenType().equals("identifier")) {
                    children.add(makeMe(tk));
                    ADVANCE();
                    if (tk.getTokenType().equals("terminator")) {
                        children.add(makeMe(tk));
                        ADVANCE();
                    }
                }
                STMTA.setChildren(children);
                STMT.setRoot(STMTA);
                break;
            case "decrement":
                children.add(makeMe(tk));
                ADVANCE();
                if (tk.getTokenType().equals("identifier")) {
                    children.add(makeMe(tk));
                    ADVANCE();
                    if (tk.getTokenType().equals("terminator")) {
                        children.add(makeMe(tk));
                        ADVANCE();
                    }
                }
                STMTA.setChildren(children);
                STMT.setRoot(STMTA);
                break;
            case "input_type":
                children.add(INPUT().root);
                if (tk.getTokenType().equals("terminator")) {
                    children.add(makeMe(tk));
                    ADVANCE();
                }
                STMTA.setChildren(children);
                STMT.setRoot(STMTA);
                break;
            case "output_type":
                children.add(OUTPUT().root);
                if (tk.getTokenType().equals("terminator")) {
                    children.add(makeMe(tk));
                    ADVANCE();
                }
                STMTA.setChildren(children);
                STMT.setRoot(STMTA);
                break;
            case "cond_if":
                children.add(CONDSTMT().root);
                STMTA.setChildren(children);

                STMT.setRoot(STMTA);
                break;
            case "loop_do":
            case "loop_while":
                children.add(LOOP().root);
                STMTA.setChildren(children);

                STMT.setRoot(STMTA);
                break;
            case "switcher":
                children.add(SWITCHA().root);
                STMTA.setChildren(children);

                STMT.setRoot(STMTA);
                break;
            case "line_comment":
            case "block_comment":
                ADVANCE();
                if (tk.getTokenType().equals("line_comments") || tk.getTokenType().equals("block_comments")) {
                    break Outerloop;
                }
                System.out.println(tk.getTokenType());
                break;
            default:
                break;
        }
        return STMT;
    }

    public static SyntaxTree DECLARATION() throws Exception { //DECLARATION TREE
        SyntaxTreeNode DECLARATION = new SyntaxTreeNode(new Token("DECL", TokenType.decl));
        //SyntaxTreeNode EOL = new SyntaxTreeNode(null,null,null);
        SyntaxTreeNode ID = new SyntaxTreeNode();
        SyntaxTreeNode DEC_ASSIGN = new SyntaxTreeNode();;
        SyntaxTree DEC_EXPR = new SyntaxTree();;
        SyntaxTree DECL = new SyntaxTree();
        Token ERROR;
        List<SyntaxTreeNode> children = new ArrayList();
        System.out.println("ENTER");
        switch (tk.getTokenType()) {
            case "data_type_int":
                children.add(makeMe(tk));
                ADVANCE();
                //System.out.println(tk.getTokenType());
                if (tk.getTokenType().equals("identifier")) {
                    //System.out.println(tk.getLexeme());
                    children.add(makeMe(tk));
                    ADVANCE();
                    children.add(DECLARATIONA().root);
                    DECLARATION.setChildren(children);
                    DECL.setRoot(DECLARATION);
                    //System.out.println("hello: " + DECL.root.nextBro.tk.getLexeme());

                } else {
                    ERROR = ERROR(TokenType.identifier, tk, line);
                }
                return DECL;
            case "data_type_bool":
                //System.out.println("DATA_TYPE_BOOL");
                children.add(makeMe(tk));
                ADVANCE();
                if (tk.getTokenType().equals("identifier")) {
                    children.add(makeMe(tk));
                    ADVANCE();
                    children.add(DECLARATIONB().root);
                    DECLARATION.setChildren(children);
                    DECL.setRoot(DECLARATION);
                } else {
                    ERROR = ERROR(TokenType.identifier, tk, line);
                }
                return DECL;
            case "data_type_dec":
                children.add(makeMe(tk));
                ADVANCE();
                if (tk.getTokenType().equals("identifier")) {
                    children.add(makeMe(tk));
                    ADVANCE();
                    children.add(DECLARATIONA().root);
                    DECLARATION.setChildren(children);
                    DECL.setRoot(DECLARATION);
                } else {
                    ERROR = ERROR(TokenType.identifier, tk, line);
                }
                return DECL;
            case "data_type_string":
                //System.out.println("DATA_TYPE_STRING");
                children.add(makeMe(tk));
                ADVANCE();
                if (tk.getTokenType().equals("identifier")) {
                    children.add(makeMe(tk));
                    ADVANCE();
                    children.add(DECLARATIONC().root);
                    DECLARATION.setChildren(children);
                    DECL.setRoot(DECLARATION);
                } else {
                    ERROR = ERROR(TokenType.identifier, tk, line);
                }
                return DECL;
                //break;
            default:
                System.out.println("ENTER THIS");
                break;
        }
//        System.out.println("PLANTINT: OUTPUT FOR DECL: \n" + DECL);
        return null;
    }

    public static SyntaxTree DECLARATIONA() throws Exception {
        SyntaxTreeNode DECLARATIONA = new SyntaxTreeNode(new Token("DECLA", TokenType.decla));
        SyntaxTreeNode assignOP = new SyntaxTreeNode();
        SyntaxTreeNode value = new SyntaxTreeNode();
        SyntaxTreeNode terminator = new SyntaxTreeNode();
        SyntaxTree ArithValue = new SyntaxTree();
        SyntaxTree DECLAA = new SyntaxTree(); //TREE FOR THIS METHOD
        Token ERROR, tkLook;
        List<SyntaxTreeNode> children = new ArrayList();
        if (tk.getLexeme().equals("IS")) {
            children.add(makeMe(tk));
            ADVANCE();
            tkLook = tklist.get(index);
            System.out.println("DECLARATIONA");
            System.out.println(tk.getTokenType());
            System.out.println(tkLook.getTokenType());
            if ((tk.getTokenType().equals("lpar")) && (tkLook.getTokenType().equals("int_const") || tkLook.getTokenType().equals("dec_const") || tkLook.getTokenType().equals("lpar"))) {
                children.add(ARITHEXPR().root);
                DECLARATIONA.setChildren(children);
                DECLAA.setRoot(DECLARATIONA);
            } else if ((tk.getTokenType().equals("int_const") || tk.getTokenType().equals("dec_const") || tk.getTokenType().equals("lpar") || tk.getTokenType().equals("identifier")) && (tkLook.getTokenType().equals("arithmd") || tkLook.getTokenType().equals("arithas"))) {
                children.add(ARITHEXPR().root);
                DECLARATIONA.setChildren(children);
                DECLAA.setRoot(DECLARATIONA);
            } else if ((tk.getTokenType().equals("int_const") || tk.getTokenType().equals("dec_const") || tk.getTokenType().equals("identifier")) && tkLook.getTokenType().equals("terminator")) {
                children.add(makeMe(tk));
                ADVANCE();
                if (tk.getTokenType().equals("terminator")) {
                    children.add(makeMe(tk));
                    ADVANCE();
                }
                DECLARATIONA.setChildren(children);
                DECLAA.setRoot(DECLARATIONA);
            }
        } else if (tk.getTokenType().equals("terminator")) {

        }
        //System.out.println("DECLAA: " + DECLAA);
        return DECLAA;
    }

    public static SyntaxTree DECLARATIONB() throws Exception { //FOR BOOLEAN
        SyntaxTreeNode DECLARATIONA = new SyntaxTreeNode(new Token("DECLB", TokenType.decla));
        SyntaxTreeNode assignOP = new SyntaxTreeNode();
        SyntaxTreeNode value = new SyntaxTreeNode();
        SyntaxTree ArithValue = new SyntaxTree();
        SyntaxTree DECLAA = new SyntaxTree(); //TREE FOR THIS METHOD
        Token ERROR, tkLook;
        List<SyntaxTreeNode> children = new ArrayList();
        if (tk.getLexeme().equals("IS")) {
            children.add(makeMe(tk));
            ADVANCE();
            tkLook = tklist.get(index);
            System.out.println("DECLB");
            if (tk.getTokenType().equals("identifier") || tk.getTokenType().equals("lpar") || tk.getTokenType().equals("logn_optr")) {
                children.add(BOOLEXPR().root);
                
                DECLARATIONA.setChildren(children);
                DECLAA.setRoot(DECLARATIONA);
            } else if (tk.getTokenType().equals("bool_const") && tkLook.getTokenType().equals("terminator")) {
                System.out.println("ENTERED");
                children.add(makeMe(tk));
                ADVANCE();
                DECLARATIONA.setChildren(children);
                DECLAA.setRoot(DECLARATIONA);
            } else {
                ERROR = ERROR(TokenType.BoolEXPR, tk, line);
            }
        }

        return DECLAA;
    }

    public static SyntaxTree DECLARATIONC() throws Exception { //FOR STRINGS
        SyntaxTreeNode DECLARATIONA = new SyntaxTreeNode(new Token("DECLC", TokenType.decla));
        SyntaxTreeNode assignOP = new SyntaxTreeNode();
        SyntaxTreeNode value = new SyntaxTreeNode();
        SyntaxTreeNode terminator = new SyntaxTreeNode();
        SyntaxTree ArithValue = new SyntaxTree();
        SyntaxTree DECLAA = new SyntaxTree(); //TREE FOR THIS METHOD
        Token ERROR, tkLook;
        List<SyntaxTreeNode> children = new ArrayList();
        if (tk.getLexeme().equals("IS")) {
            children.add(makeMe(tk));
            ADVANCE();
            if (tk.getTokenType().equals("string_const")) {
                children.add(makeMe(tk));
                ADVANCE();
                if (tk.getTokenType().equals("terminator")) {
                    children.add(makeMe(tk));
                    ADVANCE();
                } else {
                    ERROR = ERROR(TokenType.terminator, tk, line);
                }
            } else {
                ERROR = ERROR(TokenType.string_const, tk, line);
            }
        }
        DECLARATIONA.setChildren(children);
        DECLAA.setRoot(DECLARATIONA);
        return DECLAA;
    }

    public static SyntaxTree ARITHEXPR() throws Exception {
        SyntaxTreeNode AREXPRNODE = new SyntaxTreeNode(new Token("ARITH_PRIME", TokenType.ArithEXPR));
        SyntaxTree A_TERM = new SyntaxTree();
        SyntaxTree ARITHPRIME = new SyntaxTree();
        SyntaxTree ARITHEXPR = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        Token ERROR;
        Token tkLook;
        System.out.println("ARITH EXPR");
        if (tk.getTokenType().equals("identifier") || tk.getTokenType().equals("int_const") || tk.getTokenType().equals("dec_const") || tk.getTokenType().equals("lpar")) {
            //A_TERM = A_TERM();
            children.add(A_TERM().root);
            if (tk.getTokenType().equals("arithas")) {
                //ARITHPRIME = ARITH_EXPR_PRIME();
                children.add(ARITH_EXPR_PRIME().root);
                //A_TERM.root.setNextBro(ARITHPRIME.root);
            }
            AREXPRNODE.setChildren(children);
            ARITHEXPR.setRoot(AREXPRNODE);
        }
        return ARITHEXPR;
    }

    public static SyntaxTree A_TERM() throws Exception {
        SyntaxTreeNode ATERM = new SyntaxTreeNode(new Token("ARITH_TERM", TokenType.ArithEXPR));
        SyntaxTree A_FACTOR = new SyntaxTree();
        SyntaxTree A_TERM_PRIME = new SyntaxTree();
        SyntaxTree ATERMTREE = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        System.out.println("A_TERM");
        if (tk.getTokenType().equals("identifier") || tk.getTokenType().equals("int_const") || tk.getTokenType().equals("dec_const") || tk.getTokenType().equals("lpar")) {
            //A_FACTOR = A_FACTOR();
            children.add(A_FACTOR().root);
            if (tk.getTokenType().equals("arithmd")) {
                //A_TERM_PRIME = A_TERM_PRIME();
                children.add(A_TERM_PRIME().root);
                //A_FACTOR.root.setNextBro(A_TERM_PRIME.root);
            }
            ATERM.setChildren(children);
            ATERMTREE.setRoot(ATERM);
        }
        //System.out.println("MADAFAKA: " + ATERMTREE);
        return ATERMTREE;
    }

    public static SyntaxTree ARITH_EXPR_PRIME() throws Exception {
        SyntaxTreeNode ARITH_PRIME = new SyntaxTreeNode(new Token("ARITH_EXPR_PRIME", TokenType.ArithEXPR));
        SyntaxTreeNode ARITH_AS = new SyntaxTreeNode();
        SyntaxTree A_TERM = new SyntaxTree();
        SyntaxTree ARITH_EXPR_PRIME = new SyntaxTree();
        SyntaxTree PRIME = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        Token ERROR;
        if (tk.getTokenType().equals("arithas")) {
            children.add(makeMe(tk));
            ADVANCE();
            //A_TERM = A_TERM();
            children.add(A_TERM().root);
            //ARITH_EXPR_PRIME = ARITH_EXPR_PRIME();
            children.add(ARITH_EXPR_PRIME().root);
            //ARITH_AS.setNextBro(A_TERM.root);
            //A_TERM.root.setNextBro(ARITH_EXPR_PRIME.root);
            ARITH_PRIME.setChildren(children);
            PRIME.setRoot(ARITH_PRIME);
        } else if (tk.getTokenType().equals("terminator")) { // arith_expr_prime is nullable, hence must anticipate its follow set, those calling for arith_expr see the follow of arith_expr in that production
            // do nothing since arith_expr would later be called; for example, HARVEST 3+4; <output> would surely call arith_expr, by that time once [output_type] [int_const] [arithas] [int_const] is recognized, [terminator] would surely be called so do nothing
        }
        return PRIME;
    }

    public static SyntaxTree A_TERM_PRIME() throws Exception {
        SyntaxTreeNode A_T_PRIME = new SyntaxTreeNode(new Token("ARITH_EXPR_PRIME", TokenType.ArithEXPR));
        SyntaxTreeNode ARITH_MD = new SyntaxTreeNode();
        SyntaxTree A_FACTOR = new SyntaxTree();
        SyntaxTree A_TERM_PRIME = new SyntaxTree();
        SyntaxTree PRIME = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        Token ERROR;
        if (tk.getTokenType().equals("arithmd")) {
            //System.out.println("ARITHMD");
            children.add(makeMe(tk));
            ADVANCE();
            //A_FACTOR = A_FACTOR();
            children.add(A_FACTOR().root);
            //A_TERM_PRIME = A_TERM_PRIME();
            children.add(A_TERM_PRIME().root);
            //ARITH_MD.setNextBro(A_FACTOR.root);
            //A_FACTOR.root.setNextBro(A_TERM_PRIME.root);
            A_T_PRIME.setChildren(children);
            PRIME.setRoot(A_T_PRIME);
        } else if (tk.getTokenType().equals("terminator")) { // arith_expr_prime is nullable, hence must anticipate its follow set, those calling for arith_expr see the follow of arith_expr in that production
            // do nothing since arith_expr would later be called; for example, HARVEST 3+4; <output> would surely call arith_expr, by that time once [output_type] [int_const] [arithas] [int_const] is recognized, [terminator] would surely be called so do nothing
        }
        return PRIME;
    }

    public static SyntaxTree A_FACTOR() throws Exception {
        SyntaxTreeNode A_FACTOR = new SyntaxTreeNode(new Token("A_FACTOR", TokenType.ArithEXPR));
        SyntaxTreeNode ID = new SyntaxTreeNode();
        SyntaxTreeNode INT_CONST = new SyntaxTreeNode();
        SyntaxTreeNode DEC_CONST = new SyntaxTreeNode();
        SyntaxTreeNode LPAR = new SyntaxTreeNode();
        SyntaxTree ARITH_EXPR = new SyntaxTree();
        SyntaxTreeNode RPAR = new SyntaxTreeNode();
        SyntaxTree A_FACT = new SyntaxTree();
        Token ERROR;
        List<SyntaxTreeNode> children = new ArrayList();
        System.out.println("A_FACTOR");
        switch (tk.getTokenType()) {
            case "identifier":
                System.out.println("identifier");
                ID = makeMe(tk);
                ADVANCE();
                A_FACT.setRoot(ID);
                break;
            case "int_const":
                System.out.println("int_const fact");
                INT_CONST = makeMe(tk);
                ADVANCE();
                A_FACT.setRoot(INT_CONST);
                break;
            case "dec_const":
                DEC_CONST = makeMe(tk);
                ADVANCE();
                A_FACT.setRoot(DEC_CONST);
                break;
            case "lpar":
                System.out.println("LPAR");
                children.add(makeMe(tk));
                ADVANCE();
                //ARITH_EXPR = ARITHEXPR();
                children.add(ARITHEXPR().root);
                //System.out.println("MADAFAKA 1 : " + ARITH_EXPR);
                System.out.println(tk.getTokenType());
                if (tk.getTokenType().equals("rpar")) {
                    //System.out.println("CREATING NODE");
                    children.add(makeMe(tk));
                    ADVANCE();
                }
                if (RPAR.tk != null) {
                    System.out.println("CONNECTING");
                    /*SyntaxTreeNode EXPR = new SyntaxTreeNode(new Token("EXPR", TokenType.EXPR), null, null);
                     EXPR.setFirstChild(ARITH_EXPR.root);
                     System.out.println();
                     LPAR.setNextBro(EXPR);
                     EXPR.setNextBro(RPAR);
                     A_FACTOR.setFirstChild(LPAR);
                     A_FACT.setRoot(A_FACTOR);*/
                    A_FACTOR.setChildren(children);
                    A_FACT.setRoot(A_FACTOR);
                }
                break;
            default:
                break;
        }

        return A_FACT;
    }

    public static SyntaxTree RELEXPR() throws Exception {
        SyntaxTreeNode RELEXPR = new SyntaxTreeNode(new Token("REL_EXPR", TokenType.RelExpr));
        SyntaxTree R_TERM = new SyntaxTree();
        SyntaxTree REL_EXPR_PRIME = new SyntaxTree();
        SyntaxTree REL_EXPR = new SyntaxTree();
        Token ERROR;
        Token tkLook;
        List<SyntaxTreeNode> children = new ArrayList();
        System.out.println("RELEXPR");
        if (tk.getTokenType().equals("identifier") || tk.getTokenType().equals("bool_const") || tk.getTokenType().equals("int_const") || tk.getTokenType().equals("dec_const")) {

            //R_TERM = R_TERM();
            children.add(R_TERM().root);
            //System.out.println("R_TERM: " + R_TERM);
            //RELEXPR.setFirstChild(R_TERM.root);
            RELEXPR.setChildren(children);
            REL_EXPR.setRoot(RELEXPR);
            if (tk.getTokenType().equals("relop") || tk.getTokenType().equals("int_const") || tk.getTokenType().equals("dec_const")) {
                //REL_EXPR_PRIME = REL_EXPR_PRIME();
                children.add(REL_EXPR_PRIME().root);
                //RELEXPR.setFirstChild(R_TERM.root);
                //R_TERM.root.setNextBro(REL_EXPR_PRIME.root);
                //REL_EXPR.setRoot(RELEXPR);
            }
        }
        return REL_EXPR;
    }
    /*public static SyntaxTree R_REAL()throws Exception{
        
     }*/

    /*public static SyntaxTree R_ALPHA() throws Exception {
     SyntaxTreeNode R_BETA = new SyntaxTreeNode(new Token("EXPR", TokenType.EXPR), null, null);
     SyntaxTreeNode LPAR = new SyntaxTreeNode();
     SyntaxTreeNode RPAR = new SyntaxTreeNode();
     SyntaxTree ARITH_EXPR = new SyntaxTree();
     SyntaxTree RALPHA = new SyntaxTree();
     Token ERROR;
     switch (tk.getTokenType()) {
     case "int_const":
     case "dec_const":
     case "string_const":
     ARITH_EXPR = R_BETA();
     RALPHA.setRoot(ARITH_EXPR.root);
     break;
     case "lpar":
     LPAR = makeMe(tk);
     ADVANCE();
     ARITH_EXPR = R_BETA();
     if (tk.getTokenType().equals("rpar")) {
     RPAR = makeMe(tk);
     }
     if (RPAR.tk != null) {
     System.out.println("CONNECTING");
     SyntaxTreeNode EXPR = new SyntaxTreeNode(new Token("EXPR", TokenType.EXPR), null, null);
     EXPR.setFirstChild(ARITH_EXPR.root);
     System.out.println();
     LPAR.setNextBro(EXPR);
     EXPR.setNextBro(RPAR);
     R_BETA.setFirstChild(LPAR);
     RALPHA.setRoot(R_BETA);
     }
     break;
     }
     return RALPHA;
     }

     public static SyntaxTree R_BETA() throws Exception {
     SyntaxTreeNode R_BETA = new SyntaxTreeNode();
     SyntaxTree ARITH_EXPR = new SyntaxTree();
     SyntaxTree RBETA = new SyntaxTree();
     Token ERROR;
     switch (tk.getTokenType()) {
     case "int_const":
     case "dec_const":
     ARITH_EXPR = ARITHEXPR();
     RBETA.setRoot(ARITH_EXPR.root);
     break;
     case "string_const":
     R_BETA = makeMe(tk);
     RBETA.setRoot(R_BETA);
     break;
     }
     return RBETA;
     }*/
    public static SyntaxTree R_TERM() throws Exception {
        SyntaxTreeNode RTERM = new SyntaxTreeNode(new Token("RTERM", TokenType.RelExpr));
        SyntaxTreeNode ID = new SyntaxTreeNode();
        SyntaxTreeNode BOOL_CONST = new SyntaxTreeNode();
        SyntaxTree R_TERM_A = new SyntaxTree();
        SyntaxTree R_TERM = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        System.out.println("RTERM");
        switch (tk.getTokenType()) {
            case "identifier":
                ID = makeMe(tk);
                ADVANCE();
                R_TERM.setRoot(ID);
                break;
            case "bool_const":
                BOOL_CONST = makeMe(tk);
                ADVANCE();
                R_TERM.setRoot(BOOL_CONST);
                break;
            default:
                //R_TERM_A = R_TERM_A();
                children.add(R_TERM_A().root);
                RTERM.setChildren(children);
                R_TERM.setRoot(RTERM);
                break;
        }
        //System.out.println("HELLO: " + R_TERM);
        return R_TERM;
    }

    public static SyntaxTree R_TERM_A() throws Exception {
        SyntaxTreeNode R_TERM_A = new SyntaxTreeNode(new Token("R_TERM_A", TokenType.RelExpr));
        SyntaxTreeNode ID = new SyntaxTreeNode();
        SyntaxTreeNode INT_CONST = new SyntaxTreeNode();
        SyntaxTreeNode DEC_CONST = new SyntaxTreeNode();
        SyntaxTreeNode STRING_CONST = new SyntaxTreeNode();
        SyntaxTreeNode LPAR = new SyntaxTreeNode();
        SyntaxTree REL_EXPR = new SyntaxTree();
        SyntaxTree ARITH_EXPR = new SyntaxTree();
        SyntaxTree R_TERM = new SyntaxTree();
        SyntaxTreeNode RPAR = new SyntaxTreeNode();
        SyntaxTree RTERMER = new SyntaxTree();
        Token ERROR;
        List<SyntaxTreeNode> children = new ArrayList();
        System.out.println("R_TERM_A");
        switch (tk.getTokenType()) {
            case "int_const":
            case "dec_const":
                //ARITH_EXPR = ARITHEXPR();
                children.add(ARITHEXPR().root);
                //System.out.println(ARITH_EXPR);
                R_TERM_A.setChildren(children);
                RTERMER.setRoot(R_TERM_A);
                break;
            case "string_const":
                STRING_CONST = makeMe(tk);
                ADVANCE();
                RTERMER.setRoot(STRING_CONST);
                break;
            case "lpar":
                children.add(makeMe(tk));
                ADVANCE();
                //REL_EXPR = RELEXPR();
                children.add(RELEXPR().root);
                if (tk.getTokenType().equals("rpar")) {
                    children.add(makeMe(tk));
                    ADVANCE();
                }
                if (RPAR.tk != null) {
                    System.out.println("CONNECTING");
                    /*SyntaxTreeNode EXPR = new SyntaxTreeNode(new Token("EXPR", TokenType.EXPR), null, null);
                     EXPR.setFirstChild(REL_EXPR.root);
                     System.out.println();
                     LPAR.setNextBro(EXPR);
                     EXPR.setNextBro(RPAR);
                     R_TERM_A.setFirstChild(LPAR);
                     RTERMER.setRoot(R_TERM_A);*/
                    R_TERM_A.setChildren(children);
                    RTERMER.setRoot(R_TERM_A);
                }
                break;
            default:
                break;
        }
        //System.out.println("RTERMER: " + RTERMER);
        return RTERMER;
    }
    //static boolean x = !(true);

    public static SyntaxTree REL_EXPR_PRIME() throws Exception {
        SyntaxTreeNode REL_PRIME = new SyntaxTreeNode(new Token("REL_EXPR_PRIME", TokenType.RelExpr));
        SyntaxTreeNode RELOP = new SyntaxTreeNode();
        SyntaxTree REL_EXPR_PRIME = new SyntaxTree();
        SyntaxTree R_TERM_A = new SyntaxTree();
        SyntaxTree PRIME = new SyntaxTree();
        Token ERROR;
        List<SyntaxTreeNode> children = new ArrayList();
        System.out.println("RELEXPRPRIME");
        if (tk.getTokenType().equals("relop")) {
            children.add(makeMe(tk));
            ADVANCE();
            //System.out.println("HI");
            if (tk.getTokenType().equals("int_const") || tk.getTokenType().equals("dec_const") || tk.getTokenType().equals("string_const") || tk.getTokenType().equals("lpar")) {
                //System.out.println("PASOK");
                //R_TERM_A = R_TERM_A();
                children.add(R_TERM_A().root);
                //REL_EXPR_PRIME = REL_EXPR_PRIME();
                children.add(REL_EXPR_PRIME().root);
                //System.out.println(REL_EXPR_PRIME);
                PRIME.setRoot(REL_PRIME);
                REL_PRIME.setChildren(children);
            } else if (tk.getTokenType().equals("identifier")) {
                System.out.println("ID");
                //R_TERM_A = R_TERM();
                children.add(R_TERM().root);
                System.out.println("R_TERM");
                //REL_EXPR_PRIME = REL_EXPR_PRIME();
                children.add(REL_EXPR_PRIME().root);
                PRIME.setRoot(REL_PRIME);
                REL_PRIME.setChildren(children);
                /*RELOP.setNextBro(R_TERM_A.root);
                 R_TERM_A.root.setNextBro(REL_EXPR_PRIME.root);
                 System.out.println("DONE");*/
            }

        } else if (tk.getTokenType().equals("terminator")) {
            //EMPTY
        }
        //System.out.println("PRIME: " + PRIME);
        return PRIME;
    }

    public static SyntaxTree BOOLEXPR() throws Exception {
        SyntaxTreeNode BOOLEXPR = new SyntaxTreeNode(new Token("BOOL_EXPR", TokenType.BoolEXPR));
        SyntaxTreeNode LOGNOPTR = new SyntaxTreeNode();
        SyntaxTree B_TERM = new SyntaxTree();
        SyntaxTree BOOLEXPRER = new SyntaxTree();
        SyntaxTree BOOL_EXPR = new SyntaxTree();
        Token ERROR;
        Token tkLook;
        List<SyntaxTreeNode> children = new ArrayList();
        System.out.println("BOOL");
        if (tk.getTokenType().equals("logn_optr")) {
            children.add(makeMe(tk));
            ADVANCE();
            //BOOLEXPRER = BOOLEXPR();
            children.add(BOOLEXPR().root);
            BOOLEXPR.setChildren(children);
            //LOGNOPTR.setNextBro(BOOLEXPRER.root);
            BOOL_EXPR.setRoot(BOOLEXPR);
        } else if (tk.getTokenType().equals("identifier") || tk.getTokenType().equals("bool_const") || tk.getTokenType().equals("lpar") || tk.getTokenType().equals("int_const") || tk.getTokenType().equals("dec_const")) {
            System.out.println("lpar");
            //B_TERM = B_TERM();
            children.add(B_TERM().root);
            BOOLEXPR.setChildren(children);
            BOOL_EXPR.setRoot(BOOLEXPR);
        }
        return BOOL_EXPR;
    }

    public static SyntaxTree B_TERM() throws Exception {
        SyntaxTreeNode BTERM = new SyntaxTreeNode(new Token("BTERM", TokenType.BoolEXPR));
        SyntaxTree B_FACTOR = new SyntaxTree();
        SyntaxTree B_TERM_PRIME = new SyntaxTree();
        SyntaxTree BTERMER = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        if (tk.getTokenType().equals("identifier") || tk.getTokenType().equals("bool_const") || tk.getTokenType().equals("lpar") || tk.getTokenType().equals("int_const") || tk.getTokenType().equals("dec_const")) {
            //B_FACTOR = B_FACTOR();
            children.add(B_FACTOR().root);
            BTERM.setChildren(children);
            //B_FACTOR.root.setNextBro(B_TERM_PRIME.root);
            BTERMER.setRoot(BTERM);
            if (tk.getTokenType().equals("logao_optr")) {
                //B_TERM_PRIME = B_TERM_PRIME();
                children.add(B_TERM_PRIME().root);

            }
        }
        return BTERMER;
    }

    public static SyntaxTree B_TERM_PRIME() throws Exception {
        SyntaxTreeNode ARITH_PRIME = new SyntaxTreeNode(new Token("B_TERM_PRIME", TokenType.BoolEXPR));
        SyntaxTreeNode LOGAO_OPTR = new SyntaxTreeNode();
        SyntaxTree B_FACTOR = new SyntaxTree();
        SyntaxTree B_TERM_PRIME = new SyntaxTree();
        SyntaxTree PRIME = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        Token ERROR;
        if (tk.getTokenType().equals("logao_optr")) {
            children.add(makeMe(tk));
            ADVANCE();
            if (tk.getTokenType().equals("identifier") || tk.getTokenType().equals("bool_const") || tk.getTokenType().equals("lpar")) {
                //B_FACTOR = B_FACTOR();
                children.add(B_FACTOR().root);
                //B_TERM_PRIME = B_TERM_PRIME();
                children.add(B_TERM_PRIME().root);
                ARITH_PRIME.setChildren(children);
                //LOGAO_OPTR.setNextBro(B_FACTOR.root);
                //B_FACTOR.root.setNextBro(B_TERM_PRIME.root);
                PRIME.setRoot(ARITH_PRIME);
            }

        } else if (tk.getTokenType().equals("terminator")) {
            //EMPTY
        }
        return PRIME;
    }

    public static SyntaxTree B_FACTOR() throws Exception {
        SyntaxTreeNode B_FACTOR = new SyntaxTreeNode(new Token("B_FACTOR", TokenType.BoolEXPR));
        SyntaxTreeNode ID = new SyntaxTreeNode();
        SyntaxTreeNode BOOL_CONST = new SyntaxTreeNode();
        SyntaxTreeNode LPAR = new SyntaxTreeNode();
        SyntaxTree BOOL_EXPR = new SyntaxTree();
        SyntaxTree REL_EXPR = new SyntaxTree();
        SyntaxTreeNode RPAR = new SyntaxTreeNode();
        SyntaxTree B_FACT = new SyntaxTree();
        Token ERROR;
        List<SyntaxTreeNode> children = new ArrayList();
        System.out.println("B_FACTOR");
        switch (tk.getTokenType()) {
            case "identifier":
                //REL_EXPR = RELEXPR();
                children.add(RELEXPR().root);
                //System.out.println("CREATED ID");
                B_FACTOR.setChildren(children);
                B_FACT.setRoot(B_FACTOR);
                //System.out.println("B_FACTOR ID");
                break;
            case "bool_const":
                //REL_EXPR = RELEXPR();
                children.add(RELEXPR().root);
                B_FACTOR.setChildren(children);
                B_FACT.setRoot(B_FACTOR);
                break;
            case "lpar":
                children.add(makeMe(tk));
                ADVANCE();
                System.out.println("LPAR CREATED");
                //BOOL_EXPR = BOOLEXPR();
                children.add(BOOLEXPR().root);
//                System.out.println(BOOL_EXPR);
                System.out.println("END");
                if (tk.getTokenType().equals("rpar")) {
                    children.add(makeMe(tk));
                    ADVANCE();
                    System.out.println("RPAR");
                }
                if (RPAR.tk != null) {
                    System.out.println("CONNECTING");
                    /*SyntaxTreeNode EXPR = new SyntaxTreeNode(new Token("EXPR", TokenType.EXPR), null, null);
                     EXPR.setFirstChild(BOOL_EXPR.root);
                     System.out.println();
                     LPAR.setNextBro(EXPR);
                     System.out.println("LPAR CONNECTED TO EXPR");
                     EXPR.setNextBro(RPAR);
                     System.out.println("EXPR CONNECTED TO RPAR");*/
                    B_FACTOR.setChildren(children);
                    System.out.println("LPAR CONNECTED TO B_FACTOR");
                    B_FACT.setRoot(B_FACTOR);
                    System.out.println("ROOT SET");
                }
                break;
            default:
                System.out.println("REL_EXPR");
                children.add(RELEXPR().root);
                B_FACTOR.setChildren(children);
                B_FACT.setRoot(B_FACTOR);
                break;
        }
        //System.out.println("B_FACT");
        return B_FACT;
    }

    /*public static SyntaxTree COMMENTS() throws Exception {
     SyntaxTreeNode COMMENTS = new SyntaxTreeNode(new Token("COMMENTS", TokenType.COMMENTS));
     SyntaxTreeNode commline = new SyntaxTreeNode();
     SyntaxTreeNode commblock = new SyntaxTreeNode();
     SyntaxTree COMMENT = new SyntaxTree();
     Token ERROR;

     switch (tk.getTokenType()) {
     case "line_comment":
     commline = makeMe(tk);
     ADVANCE();
     COMMENTS.setFirstChild(commline);
     break;
     case "block_comment":
     commblock = makeMe(tk);
     ADVANCE();
     COMMENTS.setFirstChild(commblock);
     break;
     default:
     ERROR = ERROR(TokenType.string_const, tk, line);
     break;

     }
     COMMENT.setRoot(COMMENTS);
     return COMMENT;
     }*/
    public static SyntaxTree OUTPUT() throws Exception {
        SyntaxTreeNode OUTPUT = new SyntaxTreeNode(new Token("OUTPUT ROOT", TokenType.OUTPUT));
        SyntaxTreeNode HARVEST = new SyntaxTreeNode();
        SyntaxTree OUTPUTPRIME = new SyntaxTree();
        SyntaxTree OUTPUTA = new SyntaxTree();
        Token ERROR;
        List<SyntaxTreeNode> children = new ArrayList();
        if (tk.getTokenType().equals("output_type")) {
            children.add(makeMe(tk));
            ADVANCE();
            //OUTPUTPRIME = OUTPUTPRIME();
            children.add(OUTPUTPRIME().root);
            OUTPUT.setChildren(children);
            OUTPUTA.setRoot(OUTPUT);
        }
        System.out.println("OUTPUT");
        return OUTPUTA;
    }

    public static SyntaxTree OUTPUTPRIME() throws Exception {
        SyntaxTreeNode STRING_CO = new SyntaxTreeNode();
        SyntaxTreeNode ID = new SyntaxTreeNode();
        SyntaxTreeNode TERMINATOR = new SyntaxTreeNode();
        SyntaxTree bool_expr = new SyntaxTree();
        SyntaxTree arith_expr = new SyntaxTree();
        SyntaxTree OUTPUTPRIME = new SyntaxTree();

        Token ERROR;
        switch (tk.getTokenType()) {
            case "string_const":
                STRING_CO = makeMe(tk);
                ADVANCE();
                OUTPUTPRIME.setRoot(STRING_CO);
                break;
            case "identifier":
            case "int_const":
            case "dec_const":
                arith_expr = ARITHEXPR();
                OUTPUTPRIME.setRoot(arith_expr.root);
                break;
            case "logn_optr":
            case "logao_optr":
            case "bool_const":
                bool_expr = BOOLEXPR();
                OUTPUTPRIME.setRoot(bool_expr.root);
                break;
            default:
                ERROR = ERROR(TokenType.EMPTY, tk, line);
                break;
        }

        return OUTPUTPRIME;
    }

    public static SyntaxTree INPUT() throws Exception {
        SyntaxTreeNode INPUTT = new SyntaxTreeNode(new Token("INPUT", TokenType.input_type));
        SyntaxTreeNode INPUTTYPE = new SyntaxTreeNode();
        SyntaxTreeNode ID = new SyntaxTreeNode();
        SyntaxTree INPUT = new SyntaxTree();
        Token ERROR;
        if (tk.getTokenType().equals("input_type")) {
            INPUTTYPE = makeMe(tk);
            ADVANCE();
            if (tk.getTokenType().equals("identifier")) {
                ID = makeMe(tk);
                ADVANCE();
            } else {
                ERROR = ERROR(TokenType.identifier, tk, line);
            }
        } else {
            ERROR = ERROR(TokenType.input_type, tk, line);
        }
        List<SyntaxTreeNode> children = new ArrayList();
        children.add(INPUTTYPE);
        children.add(ID);
        INPUTT.setChildren(children);
        INPUT.setRoot(INPUTT);
        return INPUT;
    }

    public static SyntaxTree ASSIGN() throws Exception {
        SyntaxTreeNode assign = new SyntaxTreeNode(new Token("assign", TokenType.assign));
        SyntaxTreeNode identifier = new SyntaxTreeNode();
        SyntaxTreeNode assign_op = new SyntaxTreeNode();
        SyntaxTree assign_prime = new SyntaxTree();
        SyntaxTreeNode terminator = new SyntaxTreeNode();
        SyntaxTree ass = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        Token ERROR, tkLook;
        System.out.println("ASSIGN");
        if (tk.getTokenType().equals("identifier")) {
            //identifier = makeMe(tk);
            children.add(makeMe(tk));
            ADVANCE();
            if (tk.getTokenType().equals("assign_op")) {
                //assign_op = makeMe(tk);
                children.add(makeMe(tk));
                ADVANCE();
                //assign_prime = ASSIGNPRIME();
                children.add(ASSIGNPRIME().root);
            }
        } else {
            ERROR = ERROR(TokenType.string_const, tk, line);
        }
        assign.setChildren(children);
        ass.setRoot(assign);
        return ass;
    }

    public static SyntaxTree ASSIGNPRIME() throws Exception {
        SyntaxTreeNode assign_prime = new SyntaxTreeNode(new Token("assign_prime", TokenType.assign_prime));
        SyntaxTree arith_expr = new SyntaxTree();
        SyntaxTree bool_expr = new SyntaxTree();
        SyntaxTreeNode string_const = new SyntaxTreeNode();
        SyntaxTree assign_primez = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        Token ERROR, tkLook;
        // CHECK 
        switch (tk.getTokenType()) {
            case "identifier":
            case "int_const":
            case "dec_const":
                //arith_expr = ARITHEXPR();
                children.add(ARITHEXPR().root);
                //assign_prime.setFirstChild(arith_expr.root);
                assign_prime.setChildren(children);
                assign_primez.setRoot(assign_prime);
                break;
            case "logn_optr":
            case "logao_optr":
            case "bool_const":
            case "relop":
                //bool_expr = BOOLEXPR();
                children.add(BOOLEXPR().root);
                assign_prime.setChildren(children);
                assign_primez.setRoot(assign_prime);
                break;
            case "string_const":
                children.add(makeMe(tk));
                ADVANCE();
                assign_prime.setChildren(children);
                assign_primez.setRoot(assign_prime);
                break;
        }
        return assign_primez;
    }

    public static SyntaxTree CONDSTMT() throws Exception {
        SyntaxTreeNode COND_STMT = new SyntaxTreeNode(new Token("COND_STMT", TokenType.cond_stmt));
        SyntaxTree COND_STMT_A = new SyntaxTree();
        SyntaxTree COND_STMT_PRIME = new SyntaxTree();
        SyntaxTree CONDSTMT = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        Token ERROR;
        if (tk.getTokenType().equals("cond_if")) {
            //COND_STMT_A = CONDSTMTA();
            children.add(CONDSTMTA().root);
            //COND_STMT_PRIME = CONDSTMTPRIME();
            children.add(CONDSTMTPRIME().root);
            CONDSTMT.setRoot(COND_STMT);
            COND_STMT.setChildren(children);
            //COND_STMT.setNextBro(COND_STMT_A.root);
            //COND_STMT_A.root.setNextBro(COND_STMT_PRIME.root);
        }
        return CONDSTMT;
    }

    public static SyntaxTree CONDSTMTPRIME() throws Exception {
        SyntaxTreeNode COND_STMT = new SyntaxTreeNode(new Token("COND_STMT", TokenType.cond_stmt));
        SyntaxTree COND_STMT_A = new SyntaxTree();
        SyntaxTree COND_STMT_PRIME = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        Token ERROR;
        switch (tk.getTokenType()) {
            case "cond_if":
                COND_STMT_A = CONDSTMT();
                COND_STMT_PRIME.setRoot(COND_STMT_A.root);
                return COND_STMT_PRIME;

            default:
                break;
        }
        return COND_STMT_PRIME;
    }

    public static SyntaxTree CONDSTMTA() throws Exception {
        SyntaxTreeNode NodeMe = new SyntaxTreeNode(new Token("COND_STMT_A_ROOT", TokenType.cond_stmt_a));
        SyntaxTreeNode CONDIF = new SyntaxTreeNode();
        SyntaxTreeNode LPAR = new SyntaxTreeNode();
        SyntaxTreeNode RPAR = new SyntaxTreeNode();
        SyntaxTreeNode CONDTHEN = new SyntaxTreeNode();
        SyntaxTreeNode CONDENDIF = new SyntaxTreeNode();
        SyntaxTree STATEA = new SyntaxTree();
        SyntaxTree STATEB = new SyntaxTree();
        SyntaxTree ULO = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        Token ERROR;
        int countError = 0;
        //System.out.println("CURRENT: " + tk.getTokenType());
        if (tk.getTokenType().equals("cond_if")) {
            children.add(makeMe(tk));
            ADVANCE();
            if (tk.getTokenType().equals("lpar")) {
                children.add(makeMe(tk));
                ADVANCE();
                //STATEA = BOOLEXPR();
                children.add(BOOLEXPR().root);
                if (tk.getTokenType().equals("rpar")) {
                    children.add(makeMe(tk));
                    ADVANCE();
                    if (tk.getTokenType().equals("cond_then")) {
                        children.add(makeMe(tk));
                        ADVANCE();
                        //STATEB = IFTERMS();
                        children.add(IFTERMS().root);
                        System.out.println("STATEB: " + STATEB);
                        if (tk.getTokenType().equals("cond_endif")) {
                            children.add(makeMe(tk));
                            ADVANCE();
                        } else {
                            ERROR = ERROR(TokenType.cond_endif, tk, line);
                            countError++;
                        }
                    } else {
                        ERROR = ERROR(TokenType.cond_then, tk, line);
                        countError++;
                    }
                } else {
                    ERROR = ERROR(TokenType.rpar, tk, line);
                    countError++;
                }
            } else {
                ERROR = ERROR(TokenType.lpar, tk, line);
                countError++;
            }
        } else {
            ERROR = ERROR(TokenType.cond_if, tk, line);
            countError++;
        }
        NodeMe.setChildren(children);
        ULO.setRoot(NodeMe);
        return ULO;
    }

    public static SyntaxTree IFTERMS() throws Exception {
        SyntaxTreeNode IF_TERMS = new SyntaxTreeNode(new Token("IF TERMS", TokenType.IF_TERMS));
        SyntaxTree STMTS = new SyntaxTree();
        SyntaxTree IF_TERMS_PRIME = new SyntaxTree();
        SyntaxTree IFTERMS = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        if (tk.getTokenType().equals("data_type_int") || tk.getTokenType().equals("data_type_dec") || tk.getTokenType().equals("data_type_bool") || tk.getTokenType().equals("data_type_string") || tk.getTokenType().equals("identifier") || tk.getTokenType().equals("input_type") || tk.getTokenType().equals("output_type") || tk.getTokenType().equals("cond_if") || tk.getTokenType().equals("loop_do") || tk.getTokenType().equals("loop_while") || tk.getTokenType().equals("switcher") || tk.getTokenType().equals("increment") || tk.getTokenType().equals("decrement") || tk.getTokenType().equals("line_comment") || tk.getTokenType().equals("block_comment")) {
            //STMTS = PGMSTMT();
            children.add(PGMSTMT().root);
            switch (tk.getTokenType()) {
                case "cond_elseif":
                case "cond_else":
                    //IF_TERMS_PRIME = IFTERMSPRIME();
                    children.add(IFTERMSPRIME().root);
                    IF_TERMS.setChildren(children);
                    IFTERMS.setRoot(IF_TERMS);
                    //IF_TERMS.setChildren(children);
                    break;
                default:
                    break;
            }
        }
        return IFTERMS;
    }

    public static SyntaxTree IFTERMSPRIME() throws Exception {
        SyntaxTreeNode IF_TERMS = new SyntaxTreeNode(new Token("IF TERMS", TokenType.IF_TERMS));
        SyntaxTree OPTIONAL_ELSEIF = new SyntaxTree();
        SyntaxTree OPTIONAL_ELSE = new SyntaxTree();
        SyntaxTree IF_TERMS_PRIME = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        switch (tk.getTokenType()) {
            case "cond_elseif":
                // OPTIONAL_ELSEIF = OPTIONALELSEIF();
                children.add(OPTIONALELSEIF().root);
                IF_TERMS.setChildren(children);
                IF_TERMS_PRIME.setRoot(IF_TERMS);
                break;
            case "cond_else":
                // OPTIONAL_ELSE = OPTIONALELSE();
                children.add(OPTIONALELSE().root);
                IF_TERMS.setChildren(children);
                IF_TERMS_PRIME.setRoot(IF_TERMS);
                break;
            default:
                break;
        }
        return IF_TERMS_PRIME;

    }

    public static SyntaxTree OPTIONALELSEIF() throws Exception {
        SyntaxTreeNode NodeMe = new SyntaxTreeNode(new Token("OPTIONAL_ELSEIF_ROOT", TokenType.optional_elseif));
        SyntaxTree OPT_ELSEIF_A = new SyntaxTree();
        SyntaxTree OPT_ELSEIF_AP = new SyntaxTree();
        SyntaxTree Finally = new SyntaxTree();
        Token ERROR;
        List<SyntaxTreeNode> children = new ArrayList();
        int countError = 0;
        if (tk.getTokenType().equals("cond_elseif")) {
            //OPT_ELSEIF_A = OPTIONALELSEIFA();
            children.add(OPTIONALELSEIFA().root);
            //OPT_ELSEIF_AP = OPTIONALELSEIFAPRIME();
            children.add(OPTIONALELSEIFA().root);
            NodeMe.setChildren(children);
            Finally.setRoot(NodeMe);
        }
        return Finally;
    }

    public static SyntaxTree OPTIONALELSEIFAPRIME() throws Exception {
        SyntaxTreeNode NodeMe = new SyntaxTreeNode(new Token("OPTIONAL_ELSE_IF_APRIME_ROOT", TokenType.optional_else_if_aprime));
        SyntaxTree OPT_ELSEIF = new SyntaxTree();
        SyntaxTree OPT_ELSEIF_P = new SyntaxTree();
        SyntaxTree OPT_ELSE = new SyntaxTree();
        SyntaxTree Finally = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        Token ERROR;
        int countError = 0;

        switch (tk.getTokenType()) {
            case "cond_elseif": {
                //OPT_ELSEIF = OPTIONALELSEIF();
                children.add(OPTIONALELSEIF().root);
                //OPT_ELSEIF_P = OPTIONALELSEIFPRIME();
                children.add(OPTIONALELSEIFPRIME().root);
                //OPT_ELSEIF.root.setNextBro(OPT_ELSEIF_P.root);
                NodeMe.setChildren(children);
                Finally.setRoot(NodeMe);
                break;
            }
            case "cond_else": {
                //OPT_ELSE = OPTIONALELSE();
                children.add(OPTIONALELSE().root);
                //OPT_ELSEIF_P = OPTIONALELSEIFPRIME();
                children.add(OPTIONALELSEIFPRIME().root);
                //OPT_ELSE.root.setNextBro(OPT_ELSEIF_P.root);
                //NodeMe.setFirstChild(OPT_ELSE.root);
                NodeMe.setChildren(children);
                Finally.setRoot(NodeMe);
                break;
            }
            default: {
                ERROR = ERROR(TokenType.cond_if, tk, line);
                countError++;
            }
        }
        return Finally;
    }

    public static SyntaxTree OPTIONALELSEIFPRIME() throws Exception {
        SyntaxTreeNode NodeMe = new SyntaxTreeNode(new Token("OPTINAL_ELSEIF_PRIME", TokenType.optional_else_prime));
        SyntaxTree OPT_ELSEIF = new SyntaxTree();
        SyntaxTree OPT_ELSEIF_P = new SyntaxTree();
        SyntaxTree Finally = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        Token ERROR;
        int countError = 0;

        if (tk.getTokenType().equals("cond_elseif")) {
            //OPT_ELSEIF = OPTIONALELSEIF();
            children.add(OPTIONALELSEIF().root);
            // not sure of condition. kasi last token ni elseif prime ung hinahanap :))
            //OPT_ELSEIF_P = OPTIONALELSEIFPRIME();
            children.add(OPTIONALELSEIFPRIME().root);
            NodeMe.setChildren(children);
            //OPT_ELSEIF.root.setNextBro(OPT_ELSEIF_P.root);
            Finally.setRoot(NodeMe);
        } else if (tk.getTokenType().equals("terminator")) {
        }
        return Finally;
    }

    public static SyntaxTree OPTIONALELSEIFA() throws Exception {
        SyntaxTreeNode NodeMe = new SyntaxTreeNode(new Token("OPTIONAL_ELSE_IF_A_ROOT", TokenType.optional_else_if_a));
        SyntaxTreeNode CONDELSEIF = new SyntaxTreeNode();
        SyntaxTreeNode LPAR = new SyntaxTreeNode();
        SyntaxTreeNode RPAR = new SyntaxTreeNode();
        SyntaxTreeNode CONDTHEN = new SyntaxTreeNode();
        SyntaxTree STATEA = new SyntaxTree();
        SyntaxTree STATEB = new SyntaxTree();
        SyntaxTree ULO = new SyntaxTree();
        Token ERROR;
        List<SyntaxTreeNode> children = new ArrayList();
        int countError = 0;

        if (tk.getTokenType().equals("cond_elseif")) {
            children.add(makeMe(tk));
            // CONDELSEIF = makeMe(tk);
            ADVANCE();
            if (tk.getTokenType().equals("lpar")) {
                children.add(makeMe(tk));
                // LPAR = makeMe(tk);
                ADVANCE();
                // STATEA = BOOLEXPR();
                children.add(BOOLEXPR().root);
                if (tk.getTokenType().equals("rpar")) {
                    children.add(makeMe(tk));
                    // RPAR = makeMe(tk);
                    ADVANCE();
                    if (tk.getTokenType().equals("cond_then")) {
                        children.add(makeMe(tk));
                        // CONDTHEN = makeMe(tk);
                        ADVANCE();
                        // STATEB = PGMSTMT();
                        children.add(PGMSTMT().root);
                    } else {
                        ERROR = ERROR(TokenType.cond_then, tk, line);
                        countError++;
                    }
                }

            } else {
                ERROR = ERROR(TokenType.lpar, tk, line);
                countError++;
            }
        } else {
            ERROR = ERROR(TokenType.cond_elseif, tk, line);
            countError++;
        }
        NodeMe.setChildren(children);
        ULO.setRoot(NodeMe);

        return ULO;
    }

    public static SyntaxTree OPTIONALELSE() throws Exception {
        SyntaxTreeNode NodeMe = new SyntaxTreeNode(new Token("OPTIONAL_ELSE_ROOT", TokenType.optional_else));
        SyntaxTreeNode CONDELSE = new SyntaxTreeNode();
        SyntaxTree STATEA = new SyntaxTree();
        SyntaxTree ULO = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        Token ERROR;
        int countError = 0;
        System.out.println("CHECKING ELSE");
        if (tk.getTokenType().equals("cond_else")) {
            // CONDELSE = makeMe(tk);
            children.add(makeMe(tk));
            ADVANCE();
            // STATEA = PGMSTMT();
            children.add(PGMSTMT().root);
        } else {
            ERROR = ERROR(TokenType.cond_else, tk, line);
            countError++;
        }
        NodeMe.setChildren(children);
        ULO.setRoot(NodeMe);

        return ULO;
    }

    public static SyntaxTree LOOP() throws Exception {
        SyntaxTreeNode NodeMe = new SyntaxTreeNode(new Token("LOOP_ROOT", TokenType.loop_do));
        SyntaxTree LOOPA = new SyntaxTree();
        SyntaxTree LOOP_P = new SyntaxTree();
        SyntaxTree LOOPB = new SyntaxTree();
        SyntaxTree Finally = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        if (tk.getTokenType().equals("loop_do")) {
            // LOOPA = LOOPA();
            children.add(LOOPA().root);
            if (tk.getTokenType().equals("loop_do") || tk.getTokenType().equals("loop_while")) {
                //  LOOP_P = LOOPPRIME();
                children.add(LOOPPRIME().root);
            }
            Finally.setRoot(NodeMe);
            NodeMe.setChildren(children);
            //LOOPA.root.setNextBro(LOOP_P.root);

        } else if (tk.getTokenType().equals("loop_while")) {
            //LOOPB = LOOPB();-
            children.add(LOOPB().root);
            if (tk.getTokenType().equals("loop_do") || tk.getTokenType().equals("loop_while")) {
                // LOOP_P = LOOPPRIME();
                children.add(LOOPPRIME().root);
                //LOOPB.root.setNextBro(LOOP_P.root);
                NodeMe.setChildren(children);
                Finally.setRoot(NodeMe);
            }
        }
        return Finally;
    }

    public static SyntaxTree LOOPPRIME() throws Exception {
        SyntaxTreeNode NodeMe = new SyntaxTreeNode(new Token("LOOP_PRIME_ROOT", TokenType.loop_prime));
        SyntaxTree LOOP = new SyntaxTree();
        SyntaxTree Finally = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        if (tk.getTokenType().equals("loop_do") || tk.getTokenType().equals("loop_while")) {
            // LOOP = LOOP();
            children.add(LOOP().root);
            NodeMe.setChildren(children);
            Finally.setRoot(NodeMe);
        } else if (tk.getTokenType().equals("terminator")) {
        }
        return Finally;
    }

    public static SyntaxTree LOOPA() throws Exception {
        SyntaxTreeNode NodeMe = new SyntaxTreeNode(new Token("LOOP_A_ROOT", TokenType.loop_a));
        SyntaxTreeNode LOOPDO = new SyntaxTreeNode();
        SyntaxTreeNode LCURL = new SyntaxTreeNode();
        SyntaxTreeNode RCURL = new SyntaxTreeNode();
        SyntaxTreeNode LOOPWHILE = new SyntaxTreeNode();
        SyntaxTreeNode LPAR = new SyntaxTreeNode();
        SyntaxTreeNode RPAR = new SyntaxTreeNode();
        SyntaxTreeNode TERMINATOR = new SyntaxTreeNode();
        SyntaxTree STATEA = new SyntaxTree();
        SyntaxTree STATEB = new SyntaxTree();
        SyntaxTree ULO = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        Token ERROR;
        int countError = 0;

        if (tk.getTokenType().equals("loop_do")) {
            // LOOPDO = makeMe(tk);
            children.add(makeMe(tk));
            ADVANCE();
            if (tk.getTokenType().equals("lcurl")) {
                // LCURL = makeMe(tk);
                children.add(makeMe(tk));
                ADVANCE();
                // STATEA = PGMSTMT();
                children.add(PGMSTMT().root);
                System.out.println("STATEMENTS: ");
                //System.out.println("ROOT: " + STATEA);
                if (tk.getTokenType().equals("rcurl")) {
                    // RCURL = makeMe(tk);
                    children.add(makeMe(tk));
                    ADVANCE();
                    if (tk.getTokenType().equals("loop_while")) {
                        // LOOPWHILE = makeMe(tk);
                        children.add(makeMe(tk));
                        ADVANCE();
                        if (tk.getTokenType().equals("lpar")) {
                            System.out.println("BOOL CHECKING");
                            // STATEB = BOOLEXPR();
                            children.add(BOOLEXPR().root);
                            System.out.println("HELLO WORLD I AM DONE");
                            if (tk.getTokenType().equals("terminator")) {
                                // TERMINATOR = makeMe(tk);
                                children.add(makeMe(tk));
                                ADVANCE();
                            } else {
                                ERROR = ERROR(TokenType.terminator, tk, line);
                                countError++;
                            }

                        } else {
                            ERROR = ERROR(TokenType.lpar, tk, line);
                            countError++;
                        }
                    } else {
                        ERROR = ERROR(TokenType.loop_while, tk, line);
                        countError++;
                    }
                } else {
                    ERROR = ERROR(TokenType.rcurl, tk, line);
                    countError++;
                }
            } else {
                ERROR = ERROR(TokenType.lcurl, tk, line);
                countError++;
            }
        } else {
            ERROR = ERROR(TokenType.loop_do, tk, line);
            countError++;
        }

        NodeMe.setChildren(children);
        ULO.setRoot(NodeMe);
        //System.out.println("RETURNING LOOPA: " + ULO);
        return ULO;
    }

    public static SyntaxTree LOOPB() throws Exception {
        SyntaxTreeNode NodeMe = new SyntaxTreeNode(new Token("LOOP_B_ROOT", TokenType.loop_b));
        SyntaxTreeNode LCURL = new SyntaxTreeNode();
        SyntaxTreeNode RCURL = new SyntaxTreeNode();
        SyntaxTreeNode LOOPWHILE = new SyntaxTreeNode();
        SyntaxTreeNode LPAR = new SyntaxTreeNode();
        SyntaxTreeNode RPAR = new SyntaxTreeNode();
        SyntaxTreeNode TERMINATOR = new SyntaxTreeNode();
        SyntaxTree STATEA = new SyntaxTree();
        SyntaxTree STATEB = new SyntaxTree();
        SyntaxTree ULO = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        Token ERROR;
        int countError = 0;

        if (tk.getTokenType().equals("loop_while")) {
            // LOOPWHILE = makeMe(tk);
            children.add(makeMe(tk));
            ADVANCE();
            if (tk.getTokenType().equals("lpar")) {
                // LPAR = makeMe(tk);
                children.add(makeMe(tk));
                ADVANCE();
                // STATEA = BOOLEXPR();
                children.add(BOOLEXPR().root);
                if (tk.getTokenType().equals("rpar")) {
                    // RPAR = makeMe(tk);
                    children.add(makeMe(tk));
                    ADVANCE();
                    if (tk.getTokenType().equals("lcurl")) {
                        // LCURL = makeMe(tk);
                        children.add(makeMe(tk));
                        ADVANCE();
                        // STATEB = PGMSTMT();
                        children.add(PGMSTMT().root);
                        if (tk.getTokenType().equals("rcurl")) {
                            // RCURL = makeMe(tk);
                            children.add(makeMe(tk));
                            ADVANCE();
                        } else {
                            ERROR = ERROR(TokenType.rcurl, tk, line);
                            countError++;
                        }
                    } else {
                        ERROR = ERROR(TokenType.lcurl, tk, line);
                        countError++;
                    }
                } else {
                    ERROR = ERROR(TokenType.rpar, tk, line);
                    countError++;
                }
            } else {
                ERROR = ERROR(TokenType.lpar, tk, line);
                countError++;
            }
        } else {
            ERROR = ERROR(TokenType.loop_while, tk, line);
            countError++;
        }

        NodeMe.setChildren(children);
        ULO.setRoot(NodeMe);

        return ULO;
    }

    public static SyntaxTree SWITCHA() throws Exception {
        SyntaxTreeNode NodeMe = new SyntaxTreeNode(new Token("SWITCH_A_ROOT", TokenType.switch_a));
        SyntaxTreeNode SWITCHER = new SyntaxTreeNode();
        SyntaxTreeNode LPAR = new SyntaxTreeNode();
        SyntaxTreeNode IDENTIFIER = new SyntaxTreeNode();
        SyntaxTreeNode RPAR = new SyntaxTreeNode();
        SyntaxTreeNode LCURL = new SyntaxTreeNode();
        SyntaxTreeNode RCURL = new SyntaxTreeNode();
        SyntaxTree STATEA = new SyntaxTree();
        SyntaxTree ULO = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        Token ERROR;
        int countError = 0;

        if (tk.getTokenType().equals("switcher")) {
            // SWITCHER = makeMe(tk);
            children.add(makeMe(tk));
            ADVANCE();
            if (tk.getTokenType().equals("lpar")) {
                // LPAR = makeMe(tk);
                children.add(makeMe(tk));
                ADVANCE();
                if (tk.getTokenType().equals("identifier")) {
                    // IDENTIFIER = makeMe(tk);
                    children.add(makeMe(tk));
                    ADVANCE();
                    if (tk.getTokenType().equals("rpar")) {
                        // RPAR = makeMe(tk);
                        children.add(makeMe(tk));
                        ADVANCE();
                        if (tk.getTokenType().equals("lcurl")) {
                            // LCURL = makeMe(tk);
                            children.add(makeMe(tk));
                            ADVANCE();
                            // STATEA = CASEA();
                            children.add(CASEA().root);
                            if (tk.getTokenType().equals("rcurl")) {
                                children.add(makeMe(tk));
                                ADVANCE();
                            } else {
                                ERROR = ERROR(TokenType.rcurl, tk, line);
                                countError++;
                            }
                        } else {
                            ERROR = ERROR(TokenType.lcurl, tk, line);
                            countError++;
                        }
                    } else {
                        ERROR = ERROR(TokenType.rpar, tk, line);
                        countError++;
                    }
                } else {
                    ERROR = ERROR(TokenType.identifier, tk, line);
                    countError++;
                }
            } else {
                ERROR = ERROR(TokenType.lpar, tk, line);
                countError++;
            }
        } else {
            ERROR = ERROR(TokenType.switcher, tk, line);
            countError++;
        }
        NodeMe.setChildren(children);
        ULO.setRoot(NodeMe);

        return ULO;
    }

    public static SyntaxTree CASEA() throws Exception {
        SyntaxTreeNode NodeMe = new SyntaxTreeNode(new Token("SWITCH_A_ROOT", TokenType.switch_a));
        SyntaxTreeNode CASER = new SyntaxTreeNode();
        SyntaxTreeNode COLON = new SyntaxTreeNode();
        SyntaxTreeNode LCURL = new SyntaxTreeNode();
        SyntaxTreeNode RCURL = new SyntaxTreeNode();
        SyntaxTree STATEA = new SyntaxTree();
        SyntaxTree STATEB = new SyntaxTree();
        SyntaxTree STATEC = new SyntaxTree();
        SyntaxTree ULO = new SyntaxTree();
        Token ERROR;
        int countError = 0;
        List<SyntaxTreeNode> children = new ArrayList();
        if (tk.getTokenType().equals("caser")) {
            children.add(makeMe(tk));
            ADVANCE();
            //STATEA = CONSTANTS();
            children.add(CONSTANTS().root);
            if (tk.getTokenType().equals("colon")) {
                children.add(makeMe(tk));
                ADVANCE();
                if (tk.getTokenType().equals("lcurl")) {
                    children.add(makeMe(tk));
                    ADVANCE();
                    //STATEB = CASE_LOOB();
                    children.add(CASE_LOOB().root);
                    if (tk.getTokenType().equals("rcurl")) {
                        children.add(makeMe(tk));
                        ADVANCE();
                        //STATEC = CASEAPRIME();
                        children.add(CASEAPRIME().root);
                    } else {
                        ERROR = ERROR(TokenType.rcurl, tk, line);
                        countError++;
                    }
                } else {
                    ERROR = ERROR(TokenType.lcurl, tk, line);
                    countError++;
                }
            } else {
                ERROR = ERROR(TokenType.colon, tk, line);
                countError++;
            }
        } else {
            ERROR = ERROR(TokenType.caser, tk, line);
            countError++;
        }
        NodeMe.setChildren(children);
        ULO.setRoot(NodeMe);
        return ULO;
    }

    public static SyntaxTree CASE_LOOB() throws Exception {
        SyntaxTreeNode CASELOOB = new SyntaxTreeNode(new Token("CASE", TokenType.casecase));
        SyntaxTree STMTS = new SyntaxTree();
        SyntaxTreeNode BREAKER = new SyntaxTreeNode();
        SyntaxTreeNode TERMINATOR = new SyntaxTreeNode();
        SyntaxTree CASE_LOOB = new SyntaxTree();
        Token ERROR;
        List<SyntaxTreeNode> children = new ArrayList();
        if (tk.getTokenType().equals("data_type_int") || tk.getTokenType().equals("data_type_dec") || tk.getTokenType().equals("data_type_bool") || tk.getTokenType().equals("data_type_string") || tk.getTokenType().equals("identifier") || tk.getTokenType().equals("input_type") || tk.getTokenType().equals("output_type") || tk.getTokenType().equals("cond_if") || tk.getTokenType().equals("loop_do") || tk.getTokenType().equals("loop_while") || tk.getTokenType().equals("switcher") || tk.getTokenType().equals("increment") || tk.getTokenType().equals("decrement") || tk.getTokenType().equals("line_comment") || tk.getTokenType().equals("block_comment")) {
            //STMTS = PGMSTMT();
            children.add(PGMSTMT().root);
            if (tk.getTokenType().equals("breaker")) {
                children.add(makeMe(tk));
                ADVANCE();
                if (tk.getTokenType().equals("terminator")) {
                    children.add(makeMe(tk));
                    ADVANCE();
                    CASELOOB.setChildren(children);
                    CASE_LOOB.setRoot(CASELOOB);
                } else {
                    ERROR = ERROR(TokenType.terminator, tk, line);
                    //TERMINATOR = new SyntaxTreeNode(new Token(";", TokenType.terminator), null, null);
                }
            }
        }
        return CASE_LOOB;
    }

    public static SyntaxTree CASEAPRIME() throws Exception {
        SyntaxTreeNode CASE_A_PRIME = new SyntaxTreeNode(new Token("CASE_A_PRIME", TokenType.CASE_A_PRIME));
        SyntaxTree CASE_A = new SyntaxTree();
        SyntaxTreeNode DEFAULTER = new SyntaxTreeNode();
        SyntaxTreeNode COLON = new SyntaxTreeNode();
        SyntaxTreeNode LCURL = new SyntaxTreeNode();
        SyntaxTree STMTS = new SyntaxTree();
        SyntaxTreeNode RCURL = new SyntaxTreeNode();
        SyntaxTree CASEAPRIME = new SyntaxTree();
        List<SyntaxTreeNode> children = new ArrayList();
        switch (tk.getTokenType()) {
            case "caser":
                //CASE_A = CASEA();
                children.add(CASEA().root);
                CASE_A_PRIME.setChildren(children);
                CASEAPRIME.setRoot(CASE_A_PRIME);
                break;
            case "defaulter":
                children.add(makeMe(tk));
                ADVANCE();
                if (tk.getTokenType().equals("colon")) {
                    children.add(makeMe(tk));
                    ADVANCE();
                    if (tk.getTokenType().equals("lcurl")) {
                        children.add(makeMe(tk));
                        ADVANCE();
                        //STMTS = PGMSTMT();
                        children.add(PGMSTMT().root);
                        if (tk.getTokenType().equals("rcurl")) {
                            children.add(makeMe(tk));
                            ADVANCE();
                            CASE_A_PRIME.setChildren(children);
                            CASEAPRIME.setRoot(CASE_A_PRIME);
                        }
                    }
                }
                break;
        }
        return CASEAPRIME;
    }

    public static SyntaxTree CONSTANTS() throws Exception {
        SyntaxTreeNode CONSTANT = new SyntaxTreeNode(new Token("CONSTANT", TokenType.constant));
        SyntaxTreeNode int_const = new SyntaxTreeNode();
        SyntaxTreeNode dec_const = new SyntaxTreeNode();
        SyntaxTreeNode string_const = new SyntaxTreeNode();
        SyntaxTree CONSTANTS = new SyntaxTree();
        Token ERROR;
        List<SyntaxTreeNode> children = new ArrayList();
        switch (tk.getTokenType()) {
            case "int_const":
                children.add(makeMe(tk));
                ADVANCE();
                CONSTANT.setChildren(children);
                CONSTANTS.setRoot(int_const);
                break;
            case "dec_const":
                children.add(makeMe(tk));
                ADVANCE();
                CONSTANT.setChildren(children);
                CONSTANTS.setRoot(int_const);
                break;
            case "string_const":
                children.add(makeMe(tk));
                ADVANCE();
                CONSTANT.setChildren(children);
                CONSTANTS.setRoot(int_const);
                break;
        }
        return CONSTANTS;
    }

    public static boolean match(Token t) {
        if (t == expectedToken) {
            return true;
        } else {
            return false; //Calling ERROR Later
        }
    }
}
