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

public class Parser {

    static SyntaxTree root;
    static SyntaxTreeNode n;
    static SyntaxTreeNode start;
    static Token tk; //Current Token
    static int index = 0;
    static long line;
    static Token expectedToken = new Token(); //Look Ahead Token
    LexAnal lex;
    static ParseTree tree;
    static List<Token> tklist = new ArrayList();
    static List<Long> lineNumber = new ArrayList();
    static String filename;
    public Parser() throws Exception {
        
        this.root = null;
        this.n = null;
        this.start = null;
    }
    public Parser(String s) throws Exception {
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
        System.out.println(tree);
        //System.out.println("--------------------------------------USING PRINT FOR PARSE TREE----------------------------------------------");
        //System.out.println(tree.print(tree.root.firstChild));
        int ctr = 0;
        /*while(ctr < tklist.size()){
         System.out.println(tklist.get(ctr).getLexeme());
         ctr++;
         }*/
    }
    public static void main(String[] args) throws Exception {
        Parser parse = new Parser();
        LexAnal scan = new LexAnal("SSCNO2.txt");
        
        tklist.addAll(scan.buildList());
        lineNumber.addAll(scan.buildError());
        parse = new Parser();
        System.out.println(scan);
        System.out.println("DONE");
        parse.PROGRAM();
        System.out.println("DONE");
        System.out.println(scan);
        System.out.println("----------------------------------------PARSE TREE-------------------------------------");
        System.out.println(tree);
        //System.out.println("--------------------------------------USING PRINT FOR PARSE TREE----------------------------------------------");
        //System.out.println(tree.print(tree.root.firstChild));
        int ctr = 0;
        /*while(ctr < tklist.size()){
         System.out.println(tklist.get(ctr).getLexeme());
         ctr++;
         }*/
    }

    public static void ADVANCE() throws Exception {
        tk = tklist.get(index);
        line = lineNumber.get(index);
        index++;
    }

    public static ParseNode makeMe(Token t) {
        return new ParseNode(t, null, null);
    }

    public static Token ERROR(TokenType expectedToken, Token t, Long line) {
        String ERROR_RED = "\u001B[31m";
        String ERROR_RESET = "\u001B[0m";
        System.out.println(ERROR_RED + "PARSING NOT SUCCESSFUL: Token Expected: " + expectedToken + " . Token Found at line number (" + line + "):" + t.getTokenType() + " Lexeme of: " + t.getLexeme());
        System.exit(0);
        return new Token(t.getLexeme(), TokenType.error);
        
    }

    public static ParseTree PROGRAM() throws Exception {
        System.out.println("PROGRAM");
        ParseNode NodeMe = new ParseNode(new Token("PRGM_ROOT", TokenType.pgm_root), null, null);
        ParseNode SUNRISE = new ParseNode();;
        ParseNode FARMIN = new ParseNode();;
        ParseNode PGMNAME = new ParseNode();;
        ParseNode STMT = new ParseNode();;
        ParseNode FARMOUT = new ParseNode();;
        ParseNode SUNSET = new ParseNode();;
        ParseTree STATETREE = new ParseTree();;
        ParseTree PROGRAM = new ParseTree();;
        ParseNode TERMINATOR = new ParseNode();
        Token ERROR = new Token();
        int countError = 0;

        ADVANCE();
        if(tk.getTokenType().equals("line_comment") || tk.getTokenType().equals("block_comments")){
            ADVANCE();
        }
        if (tk.getLexeme().equals("SUNRISE")) {
            SUNRISE = makeMe(tk);
            //System.out.println(SUNRISE.getToken().getLexeme());
            ADVANCE();
            if (tk.getLexeme().equals("FARMIN")) {
                FARMIN = makeMe(tk);
                //System.out.println(FARMIN.getToken().getLexeme());
                ADVANCE();
                //System.out.println(tk.getTokenType());
                if (tk.getTokenType().equals("identifier")) {
                    PGMNAME = makeMe(tk);
                    // System.out.println(PGMNAME.getToken().getLexeme());
                    ADVANCE();
                    STATETREE = PGMSTMT();
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
                        FARMOUT = makeMe(tk);
                        ADVANCE();
                        if (tk.getLexeme().equals("SUNSET")) {
                            System.out.println("SUNSET");
                            SUNSET = makeMe(tk);
                            System.out.println(SUNSET.tk.getTokenType());
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
        FARMOUT.setNextBro(SUNSET);
        SUNRISE.setNextBro(FARMIN);
        FARMIN.setNextBro(PGMNAME);
        PGMNAME.setNextBro(STATETREE.root);
        STATETREE.root.setNextBro(FARMOUT);
        
        NodeMe.setFirstChild(SUNRISE);
        PROGRAM.setRoot(NodeMe);
        tree = PROGRAM;
        //System.out.println(PROGRAM.printZeTree(PROGRAM.root));
        System.out.println("END PROGRAM");
        return PROGRAM;
    }

    public static ParseTree PGMSTMT() throws Exception { //STMTS
        System.out.println("STMTS");
        ParseNode NodeMe = new ParseNode(new Token("STMTS", TokenType.stmt), null, null);
        ParseNode SOFF = new ParseNode();
        ParseTree STMT = new ParseTree();
        ParseTree STMTSPRIME = new ParseTree();
        ParseTree PGM_STMT = new ParseTree();
        Token ERROR;
        if (tk.getTokenType().equals("data_type_int") || tk.getTokenType().equals("data_type_dec") || tk.getTokenType().equals("data_type_bool") || tk.getTokenType().equals("data_type_string") || tk.getTokenType().equals("identifier") || tk.getTokenType().equals("input_type") || tk.getTokenType().equals("output_type") || tk.getTokenType().equals("cond_if") || tk.getTokenType().equals("loop_do") || tk.getTokenType().equals("loop_while") || tk.getTokenType().equals("switcher") || tk.getTokenType().equals("increment") || tk.getTokenType().equals("decrement") || tk.getTokenType().equals("line_comment") || tk.getTokenType().equals("block_comment")) {
            STMT = STMT();
            NodeMe.setFirstChild(STMT.root);
            if (tk.getTokenType().equals("data_type_int") || tk.getTokenType().equals("data_type_dec") || tk.getTokenType().equals("data_type_bool") || tk.getTokenType().equals("data_type_string") || tk.getTokenType().equals("identifier") || tk.getTokenType().equals("input_type") || tk.getTokenType().equals("output_type") || tk.getTokenType().equals("cond_if") || tk.getTokenType().equals("loop_do") || tk.getTokenType().equals("loop_while") || tk.getTokenType().equals("switcher") || tk.getTokenType().equals("increment") || tk.getTokenType().equals("decrement") || tk.getTokenType().equals("line_comment") || tk.getTokenType().equals("block_comment")) {
                STMTSPRIME = STMTS_PRIME();
                STMT.root.setNextBro(STMTSPRIME.root);
            }
            
            PGM_STMT.setRoot(NodeMe);
            System.out.println("DONE CONNECTING");
        }
        System.out.println("RETURNING TO HEAVEN");
        System.out.println("PGMSTMT: " + PGM_STMT);
        return PGM_STMT;
    }

    public static ParseTree STMTS_PRIME() throws Exception { //STMTS PRIME
        System.out.println("STMTS PRIME");
        ParseNode PGM_BODY_NODE = new ParseNode(new Token("STMTS_PRIME", TokenType.stmt), null, null);
        ParseTree STMTS = new ParseTree();
        ParseTree PGM_BODY = new ParseTree(new ParseNode(new Token("EMPTY", TokenType.EMPTY), null, null));

        if (tk.getTokenType().equals("data_type_int") || tk.getTokenType().equals("data_type_dec") || tk.getTokenType().equals("data_type_bool") || tk.getTokenType().equals("data_type_string") || tk.getTokenType().equals("identifier") || tk.getTokenType().equals("input_type") || tk.getTokenType().equals("output_type") || tk.getTokenType().equals("cond_if") || tk.getTokenType().equals("loop_do") || tk.getTokenType().equals("loop_while") || tk.getTokenType().equals("switcher") || tk.getTokenType().equals("increment") || tk.getTokenType().equals("decrement") || tk.getTokenType().equals("line_comment") || tk.getTokenType().equals("block_comment")) {
            STMTS = PGMSTMT();
            PGM_BODY_NODE.setFirstChild(STMTS.root);
            PGM_BODY.setRoot(PGM_BODY_NODE);
        }
        else if(tk.getTokenType().equals("end_main")){
            System.out.println("LE CHECK");
        }
        return PGM_BODY;
    }

    public static ParseTree STMT() throws Exception { //STMT
        System.out.println("STMTS");
        ParseNode STMTA = new ParseNode(new Token("STMT", TokenType.stmt), null, null);
        ParseNode terminator = new ParseNode();
        ParseNode identifier = new ParseNode();
        ParseNode increment = new ParseNode();
        ParseNode decrement = new ParseNode();
        ParseTree tree_decl = new ParseTree();
        ParseTree tree_assign = new ParseTree();
        ParseTree tree_input = new ParseTree();
        ParseTree tree_output = new ParseTree();
        ParseTree tree_cond = new ParseTree();
        ParseTree tree_loop = new ParseTree();
        ParseTree tree_switch = new ParseTree();
        ParseTree STMT = new ParseTree();
        Token ERROR;
        Outerloop:
        switch (tk.getTokenType()) {
            case "data_type_int":
            case "data_type_string":
            case "data_type_dec":
            case "data_type_bool":
                System.out.println(tk.getTokenType());
                tree_decl = DECLARATION();
                if (tk.getTokenType().equals("terminator")) {
                    terminator = makeMe(tk);
                    ADVANCE();
                }
                STMTA.setFirstChild(tree_decl.root);
                tree_decl.root.setNextBro(terminator);
                STMT.setRoot(STMTA);
                break;
            case "identifier":
                tree_assign = ASSIGN();
                if (tk.getTokenType().equals("terminator")) {
                    terminator = makeMe(tk);
                    ADVANCE();
                }
                STMTA.setFirstChild(tree_assign.root);
                tree_assign.root.setNextBro(terminator);
                STMT.setRoot(STMTA);
                break;
            case "increment":
                increment = makeMe(tk);
                ADVANCE();
                if (tk.getTokenType().equals("identifier")) {
                    identifier = makeMe(tk);
                    ADVANCE();
                    if (tk.getTokenType().equals("terminator")) {
                        terminator = makeMe(tk);
                        ADVANCE();
                    }
                }
                STMTA.setFirstChild(increment);
                increment.setNextBro(identifier);
                identifier.setNextBro(terminator);
                STMT.setRoot(increment);
                System.out.println(STMT);
                break;
            case "decrement":
                decrement = makeMe(tk);
                ADVANCE();
                if (tk.getTokenType().equals("identifier")) {
                    identifier = makeMe(tk);
                    ADVANCE();
                    if (tk.getTokenType().equals("terminator")) {
                        terminator = makeMe(tk);
                        ADVANCE();
                    }
                }
                STMTA.setFirstChild(decrement);
                decrement.setNextBro(identifier);
                identifier.setNextBro(terminator);
                STMT.setRoot(decrement);
                break;
            case "input_type":
                tree_input = INPUT();
                if (tk.getTokenType().equals("terminator")) {
                    terminator = makeMe(tk);
                    ADVANCE();
                }
                STMTA.setFirstChild(tree_input.root);
                tree_input.root.setNextBro(terminator);
                STMT.setRoot(STMTA);
                break;
            case "output_type":
                tree_output = OUTPUT();
                if (tk.getTokenType().equals("terminator")) {
                    terminator = makeMe(tk);
                    ADVANCE();
                }
                STMTA.setFirstChild(tree_output.root);
                tree_output.root.setNextBro(terminator);
                STMT.setRoot(STMTA);
                break;
            case "cond_if":
                tree_cond = CONDSTMT();
                STMTA.setFirstChild(tree_cond.root);
                
                STMT.setRoot(STMTA);
                break;
            case "loop_do":
            case "loop_while":
                tree_loop = LOOP();
                STMTA.setFirstChild(tree_loop.root);
                
                STMT.setRoot(STMTA);
                break;
            case "switcher":
                tree_switch = SWITCHA();
                STMTA.setFirstChild(tree_switch.root);
                
                STMT.setRoot(STMTA);
                break;
            case "line_comment":
            case "block_comment":
                ADVANCE();
                if(tk.getTokenType().equals("line_comments") || tk.getTokenType().equals("block_comments")){
                    break Outerloop;
                }
                System.out.println(tk.getTokenType());
                break;
            default:
                break;
        }
        return STMT;
    }

    public static ParseTree DECLARATION() throws Exception { //DECLARATION TREE
        ParseNode DECLARATION = new ParseNode(new Token("DECL", TokenType.decl), null, null);
        ParseNode EOL = new ParseNode(null,null,null);
        ParseNode ID = new ParseNode();
        ParseNode DEC_ASSIGN = new ParseNode();;
        ParseTree DEC_EXPR = new ParseTree();;
        ParseTree DECL = new ParseTree();
        Token ERROR;
        System.out.println("ENTER");
        switch (tk.getTokenType()) {
            case "data_type_int":
                DECLARATION = makeMe(tk);
                ADVANCE();
                System.out.println(tk.getTokenType());
                if (tk.getTokenType().equals("identifier")) {
                    //System.out.println(tk.getLexeme());
                    ID = makeMe(tk);
                    ADVANCE();
                    DECLARATION.setNextBro(ID);
                    EOL.setNextBro(ID);
                    DEC_EXPR = DECLARATIONA();
                    ID.setNextBro(DEC_EXPR.root);
                    DECL.setRoot(DECLARATION);
                    System.out.println("hello: " + DECL.root.nextBro.tk.getLexeme());
                    
                } else {
                    ERROR = ERROR(TokenType.identifier, tk, line);
                }
                break;
            case "data_type_bool":
                //System.out.println("DATA_TYPE_BOOL");
                DECLARATION = makeMe(tk);
                ADVANCE();
                if (tk.getTokenType().equals("identifier")) {
                    ID = makeMe(tk);
                    ADVANCE();
                    DEC_EXPR = DECLARATIONB();
                    DECLARATION.setFirstChild(DEC_EXPR.root);
                    DECL.setRoot(DEC_EXPR.root);
                } else {
                    ERROR = ERROR(TokenType.identifier, tk, line);
                }
                break;
            case "data_type_dec":
                DECLARATION = makeMe(tk);
                ADVANCE();
                if (tk.getTokenType().equals("identifier")) {
                    ID = makeMe(tk);
                    ADVANCE();
                    DEC_EXPR = DECLARATIONA();
                    DECL.setRoot(DEC_EXPR.root);
                } else {
                    ERROR = ERROR(TokenType.identifier, tk, line);
                }
                break;
            case "data_type_string":
                System.out.println("DATA_TYPE_STRING");
                DECLARATION = makeMe(tk);
                ADVANCE();
                if (tk.getTokenType().equals("identifier")) {
                    ID = makeMe(tk);
                    ADVANCE();
                    DEC_EXPR = DECLARATIONC();
                    DECL.setRoot(DEC_EXPR.root);
                } else {
                    ERROR = ERROR(TokenType.identifier, tk, line);
                }

                break;
            default:
                System.out.println("ENTER THIS");
                break;
        }
        System.out.println("PLANTINT: OUTPUT FOR DECL: \n" + DECL);
        return DECL;
    }

    public static ParseTree DECLARATIONA() throws Exception {
        ParseNode DECLARATIONA = new ParseNode(new Token("DECLA", TokenType.decla), null, null);
        ParseNode assignOP = new ParseNode();
        ParseNode value = new ParseNode();
        ParseNode terminator = new ParseNode();
        ParseTree ArithValue = new ParseTree(new ParseNode(new Token("ArithExpr", TokenType.ArithEXPR), null, null));
        ParseTree DECLAA = new ParseTree(); //TREE FOR THIS METHOD
        Token ERROR, tkLook;
        if (tk.getLexeme().equals("IS")) {
            assignOP = makeMe(tk);
            ADVANCE();
            tkLook = tklist.get(index);
            System.out.println("DECLARATIONA");
            System.out.println(tk.getTokenType());
            System.out.println(tkLook.getTokenType());
            if ((tk.getTokenType().equals("lpar")) && (tkLook.getTokenType().equals("int_const") || tkLook.getTokenType().equals("dec_const") || tkLook.getTokenType().equals("lpar"))) {
                ArithValue = ARITHEXPR();
                assignOP.setNextBro(ArithValue.root);
                DECLAA.setRoot(assignOP);
            } else if ((tk.getTokenType().equals("int_const") || tk.getTokenType().equals("dec_const") || tk.getTokenType().equals("lpar") || tk.getTokenType().equals("identifier")) && (tkLook.getTokenType().equals("arithmd") || tkLook.getTokenType().equals("arithas"))) {
                ArithValue = ARITHEXPR();
                assignOP.setNextBro(ArithValue.root);
                DECLAA.setRoot(assignOP);
            } else if ((tk.getTokenType().equals("int_const") || tk.getTokenType().equals("dec_const") || tk.getTokenType().equals("identifier")) && tkLook.getTokenType().equals("terminator")) {
                value = makeMe(tk);
                ADVANCE();
                if (tk.getTokenType().equals("terminator")) {
                    terminator = makeMe(tk);
                    ADVANCE();
                }
                assignOP.setNextBro(value);

                DECLARATIONA.setFirstChild(assignOP);
                DECLAA.setRoot(DECLARATIONA);

            }

        } else if (tk.getTokenType().equals("terminator")) {
            
        }
        System.out.println("DECLAA: " + DECLAA);
        return DECLAA;
    }

    public static ParseTree DECLARATIONB() throws Exception { //FOR BOOLEAN
        ParseNode DECLARATIONA = new ParseNode(new Token("DECLB", TokenType.decla), null, null);
        ParseNode assignOP = new ParseNode();
        ParseNode value = new ParseNode();
        ParseTree ArithValue = new ParseTree(new ParseNode(new Token("BOOLEXPR", TokenType.BoolEXPR), null, null));
        ParseTree DECLAA = new ParseTree(); //TREE FOR THIS METHOD
        Token ERROR, tkLook;
        if (tk.getLexeme().equals("IS")) {
            assignOP = makeMe(tk);
            ADVANCE();
            tkLook = tklist.get(index);
            System.out.println("DECLB");
            if (tk.getTokenType().equals("identifier") || tk.getTokenType().equals("lpar") || tk.getTokenType().equals("logn_optr")) {
                ArithValue = BOOLEXPR();

                DECLARATIONA.setFirstChild(assignOP);
                assignOP.setNextBro(ArithValue.root);
            } else if (tk.getTokenType().equals("bool_const") && tkLook.getTokenType().equals("terminator")) {
                System.out.println("ENTERED");
                value = makeMe(tk);
                ADVANCE();
                DECLARATIONA.setFirstChild(value);
                assignOP.setNextBro(value);
            } else {
                ERROR = ERROR(TokenType.BoolEXPR, tk, line);
            }
        }

        DECLARATIONA.setFirstChild(assignOP);
        DECLAA.setRoot(DECLARATIONA);
        System.out.println("DECLAA: " + DECLAA);
        return DECLAA;
    }

    public static ParseTree DECLARATIONC() throws Exception { //FOR STRINGS
        ParseNode DECLARATIONA = new ParseNode(new Token("DECLC", TokenType.decla), null, null);
        ParseNode assignOP = new ParseNode();
        ParseNode value = new ParseNode();
        ParseNode terminator = new ParseNode();
        ParseTree ArithValue = new ParseTree(new ParseNode(new Token("ArithExpr", TokenType.ArithEXPR), null, null));
        ParseTree DECLAA = new ParseTree(); //TREE FOR THIS METHOD
        Token ERROR, tkLook;
        if (tk.getLexeme().equals("IS")) {
            assignOP = makeMe(tk);
            ADVANCE();
            if (tk.getTokenType().equals("string_const")) {
                value = makeMe(tk);
                ADVANCE();
                if (tk.getTokenType().equals("terminator")) {
                    terminator = makeMe(tk);
                    ADVANCE();
                } else {
                    ERROR = ERROR(TokenType.terminator, tk, line);
                }
            } else {
                ERROR = ERROR(TokenType.string_const, tk, line);
            }
        }
        assignOP.setNextBro(value);
        DECLARATIONA.setFirstChild(assignOP);
        DECLAA.setRoot(DECLARATIONA);
        return DECLAA;
    }

    public static ParseTree ARITHEXPR() throws Exception {
        ParseNode AREXPRNODE = new ParseNode();
        ParseTree A_TERM = new ParseTree();
        ParseTree ARITHPRIME = new ParseTree();
        ParseTree ARITHEXPR = new ParseTree();
        Token ERROR;
        Token tkLook;
        System.out.println("ARITH EXPR");
        if (tk.getTokenType().equals("identifier") || tk.getTokenType().equals("int_const") || tk.getTokenType().equals("dec_const") || tk.getTokenType().equals("lpar")) {
            A_TERM = A_TERM();
            if (tk.getTokenType().equals("arithas")) {
                ARITHPRIME = ARITH_EXPR_PRIME();
                A_TERM.root.setNextBro(ARITHPRIME.root);
            }
            ARITHEXPR.setRoot(A_TERM.root);
        }
        return ARITHEXPR;
    }

    public static ParseTree A_TERM() throws Exception {
        ParseNode ATERM = new ParseNode();
        ParseTree A_FACTOR = new ParseTree();
        ParseTree A_TERM_PRIME = new ParseTree();
        ParseTree ATERMTREE = new ParseTree();
        System.out.println("A_TERM");
        if (tk.getTokenType().equals("identifier") || tk.getTokenType().equals("int_const") || tk.getTokenType().equals("dec_const") || tk.getTokenType().equals("lpar")) {
            A_FACTOR = A_FACTOR();
            if (tk.getTokenType().equals("arithmd")) {
                A_TERM_PRIME = A_TERM_PRIME();
                A_FACTOR.root.setNextBro(A_TERM_PRIME.root);
            }

            ATERMTREE.setRoot(A_FACTOR.root);
        }
        System.out.println("MADAFAKA: " + ATERMTREE);
        return ATERMTREE;
    }

    public static ParseTree ARITH_EXPR_PRIME() throws Exception {
        ParseNode ARITH_PRIME = new ParseNode(new Token("ARITH_EXPR_PRIME", TokenType.ArithEXPR), null, null);
        ParseNode ARITH_AS = new ParseNode();
        ParseTree A_TERM = new ParseTree();
        ParseTree ARITH_EXPR_PRIME = new ParseTree();
        ParseTree PRIME = new ParseTree();
        Token ERROR;
        if (tk.getTokenType().equals("arithas")) {
            ARITH_AS = makeMe(tk);
            ADVANCE();
            A_TERM = A_TERM();
            ARITH_EXPR_PRIME = ARITH_EXPR_PRIME();
            ARITH_AS.setNextBro(A_TERM.root);
            A_TERM.root.setNextBro(ARITH_EXPR_PRIME.root);
            PRIME.setRoot(ARITH_AS);
        } else if (tk.getTokenType().equals("terminator")) { // arith_expr_prime is nullable, hence must anticipate its follow set, those calling for arith_expr see the follow of arith_expr in that production
            // do nothing since arith_expr would later be called; for example, HARVEST 3+4; <output> would surely call arith_expr, by that time once [output_type] [int_const] [arithas] [int_const] is recognized, [terminator] would surely be called so do nothing
        }
        return PRIME;
    }

    public static ParseTree A_TERM_PRIME() throws Exception {
        ParseNode A_T_PRIME = new ParseNode(new Token("ARITH_EXPR_PRIME", TokenType.ArithEXPR), null, null);
        ParseNode ARITH_MD = new ParseNode();
        ParseTree A_FACTOR = new ParseTree();
        ParseTree A_TERM_PRIME = new ParseTree();
        ParseTree PRIME = new ParseTree();
        Token ERROR;
        if (tk.getTokenType().equals("arithmd")) {
            System.out.println("ARITHMD");
            ARITH_MD = makeMe(tk);
            ADVANCE();
            A_FACTOR = A_FACTOR();
            A_TERM_PRIME = A_TERM_PRIME();
            ARITH_MD.setNextBro(A_FACTOR.root);
            A_FACTOR.root.setNextBro(A_TERM_PRIME.root);
            PRIME.setRoot(ARITH_MD);
        } else if (tk.getTokenType().equals("terminator")) { // arith_expr_prime is nullable, hence must anticipate its follow set, those calling for arith_expr see the follow of arith_expr in that production
            // do nothing since arith_expr would later be called; for example, HARVEST 3+4; <output> would surely call arith_expr, by that time once [output_type] [int_const] [arithas] [int_const] is recognized, [terminator] would surely be called so do nothing
        }
        return PRIME;
    }

    public static ParseTree A_FACTOR() throws Exception {
        ParseNode A_FACTOR = new ParseNode(new Token("A_FACTOR", TokenType.ArithEXPR), null, null);
        ParseNode ID = new ParseNode();
        ParseNode INT_CONST = new ParseNode();
        ParseNode DEC_CONST = new ParseNode();
        ParseNode LPAR = new ParseNode();
        ParseTree ARITH_EXPR = new ParseTree();
        ParseNode RPAR = new ParseNode();
        ParseTree A_FACT = new ParseTree();
        Token ERROR;
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
                LPAR = makeMe(tk);
                ADVANCE();
                ARITH_EXPR = ARITHEXPR();
                System.out.println("MADAFAKA 1 : " + ARITH_EXPR);
                System.out.println(tk.getTokenType());
                if (tk.getTokenType().equals("rpar")) {
                    System.out.println("CREATING NODE");
                    RPAR = makeMe(tk);
                    ADVANCE();
                }
                if (RPAR.tk != null) {
                    System.out.println("CONNECTING");
                    ParseNode EXPR = new ParseNode(new Token("EXPR", TokenType.EXPR), null, null);
                    EXPR.setFirstChild(ARITH_EXPR.root);
                    System.out.println();
                    LPAR.setNextBro(EXPR);
                    EXPR.setNextBro(RPAR);
                    A_FACTOR.setFirstChild(LPAR);
                    A_FACT.setRoot(A_FACTOR);
                }
                break;
            default:
                break;
        }

        return A_FACT;
    }

    public static ParseTree RELEXPR() throws Exception {
        ParseNode RELEXPR = new ParseNode(new Token("REL_EXPR", TokenType.RelExpr), null, null);
        ParseTree R_TERM = new ParseTree();
        ParseTree REL_EXPR_PRIME = new ParseTree();
        ParseTree REL_EXPR = new ParseTree();
        Token ERROR;
        Token tkLook;
        System.out.println("RELEXPR");
        if (tk.getTokenType().equals("identifier") || tk.getTokenType().equals("bool_const") || tk.getTokenType().equals("int_const") || tk.getTokenType().equals("dec_const")) {

            R_TERM = R_TERM();
            System.out.println("R_TERM: " + R_TERM);
            //RELEXPR.setFirstChild(R_TERM.root);
            //REL_EXPR.setRoot(RELEXPR);

            if (tk.getTokenType().equals("relop") || tk.getTokenType().equals("int_const") || tk.getTokenType().equals("dec_const")) {
                REL_EXPR_PRIME = REL_EXPR_PRIME();
                RELEXPR.setFirstChild(R_TERM.root);
                R_TERM.root.setNextBro(REL_EXPR_PRIME.root);
                REL_EXPR.setRoot(RELEXPR);
            }
        }
        return REL_EXPR;
    }
    /*public static ParseTree R_REAL()throws Exception{
        
     }*/

    public static ParseTree R_ALPHA() throws Exception {
        ParseNode R_BETA = new ParseNode(new Token("EXPR", TokenType.EXPR), null, null);
        ParseNode LPAR = new ParseNode();
        ParseNode RPAR = new ParseNode();
        ParseTree ARITH_EXPR = new ParseTree();
        ParseTree RALPHA = new ParseTree();
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
                    ParseNode EXPR = new ParseNode(new Token("EXPR", TokenType.EXPR), null, null);
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

    public static ParseTree R_BETA() throws Exception {
        ParseNode R_BETA = new ParseNode();
        ParseTree ARITH_EXPR = new ParseTree();
        ParseTree RBETA = new ParseTree();
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
    }

    public static ParseTree R_TERM() throws Exception {
        ParseNode RTERM = new ParseNode(new Token("RTERM", TokenType.RelExpr), null, null);
        ParseNode ID = new ParseNode();
        ParseNode BOOL_CONST = new ParseNode();
        ParseTree R_TERM_A = new ParseTree();
        ParseTree R_TERM = new ParseTree(new ParseNode(new Token("EMPTY", TokenType.EMPTY), null, null));
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
                R_TERM_A = R_TERM_A();
                RTERM.setFirstChild(R_TERM_A.root);
                R_TERM.setRoot(RTERM);
                break;
        }
        System.out.println("HELLO: " + R_TERM);
        return R_TERM;
    }

    public static ParseTree R_TERM_A() throws Exception {
        ParseNode R_TERM_A = new ParseNode(new Token("R_TERM_A", TokenType.RelExpr), null, null);
        ParseNode ID = new ParseNode();
        ParseNode INT_CONST = new ParseNode();
        ParseNode DEC_CONST = new ParseNode();
        ParseNode STRING_CONST = new ParseNode();
        ParseNode LPAR = new ParseNode();
        ParseTree REL_EXPR = new ParseTree();
        ParseTree ARITH_EXPR = new ParseTree();
        ParseTree R_TERM = new ParseTree();
        ParseNode RPAR = new ParseNode();
        ParseTree RTERMER = new ParseTree();
        Token ERROR;
        System.out.println("R_TERM_A");
        switch (tk.getTokenType()) {
            case "int_const":
            case "dec_const":
                ARITH_EXPR = ARITHEXPR();
                System.out.println(ARITH_EXPR);
                R_TERM_A.setFirstChild(ARITH_EXPR.root);
                RTERMER.setRoot(ARITH_EXPR.root);
                break;
            case "string_const":
                STRING_CONST = makeMe(tk);
                ADVANCE();
                RTERMER.setRoot(STRING_CONST);
                break;
            case "lpar":
                LPAR = makeMe(tk);
                ADVANCE();
                REL_EXPR = RELEXPR();
                if (tk.getTokenType().equals("rpar")) {
                    RPAR = makeMe(tk);
                    ADVANCE();
                }
                if (RPAR.tk != null) {
                    System.out.println("CONNECTING");
                    ParseNode EXPR = new ParseNode(new Token("EXPR", TokenType.EXPR), null, null);
                    EXPR.setFirstChild(REL_EXPR.root);
                    System.out.println();
                    LPAR.setNextBro(EXPR);
                    EXPR.setNextBro(RPAR);
                    R_TERM_A.setFirstChild(LPAR);
                    RTERMER.setRoot(R_TERM_A);
                }
                break;
            default:
                break;
        }
        System.out.println("RTERMER: " + RTERMER);
        return RTERMER;
    }
    static boolean x = !(true);

    public static ParseTree REL_EXPR_PRIME() throws Exception {
        ParseNode REL_PRIME = new ParseNode(new Token("REL_EXPR_PRIME", TokenType.RelExpr), null, null);
        ParseNode RELOP = new ParseNode();
        ParseTree REL_EXPR_PRIME = new ParseTree();
        ParseTree R_TERM_A = new ParseTree();
        ParseTree PRIME = new ParseTree();
        Token ERROR;
        System.out.println("RELEXPRPRIME");
        if (tk.getTokenType().equals("relop")) {
            RELOP = makeMe(tk);
            ADVANCE();
            System.out.println("HI");
            if (tk.getTokenType().equals("int_const") || tk.getTokenType().equals("dec_const") || tk.getTokenType().equals("string_const") || tk.getTokenType().equals("lpar")) {
                System.out.println("PASOK");
                R_TERM_A = R_TERM_A();
                REL_EXPR_PRIME = REL_EXPR_PRIME();
                System.out.println(REL_EXPR_PRIME);
                PRIME.setRoot(REL_PRIME);
                REL_PRIME.setFirstChild(RELOP);
                RELOP.setNextBro(R_TERM_A.root);
                System.out.println("ROOT" + R_TERM_A.root.getToken().getTokenType());
                R_TERM_A.root.setNextBro(REL_EXPR_PRIME.root);
            }
            else if(tk.getTokenType().equals("identifier")){
                System.out.println("ID");
                R_TERM_A = R_TERM();
                System.out.println("R_TERM");
                REL_EXPR_PRIME = REL_EXPR_PRIME();
                
                PRIME.setRoot(REL_PRIME);
                REL_PRIME.setFirstChild(RELOP);
                RELOP.setNextBro(R_TERM_A.root);
                R_TERM_A.root.setNextBro(REL_EXPR_PRIME.root);
                System.out.println("DONE");
            }

        } else if (tk.getTokenType().equals("terminator")) {
            //EMPTY
        }
        System.out.println("PRIME: " + PRIME);
        return PRIME;
    }

    public static ParseTree BOOLEXPR() throws Exception {
        ParseNode BOOLEXPR = new ParseNode(new Token("BOOL_EXPR", TokenType.BoolEXPR), null, null);
        ParseNode LOGNOPTR = new ParseNode();
        ParseTree B_TERM = new ParseTree();
        ParseTree BOOLEXPRER = new ParseTree();
        ParseTree BOOL_EXPR = new ParseTree();
        Token ERROR;
        Token tkLook;
        System.out.println("BOOL");
        if (tk.getTokenType().equals("logn_optr")) {
            LOGNOPTR = makeMe(tk);
            ADVANCE();
            BOOLEXPRER = BOOLEXPR();
            BOOLEXPR.setFirstChild(LOGNOPTR);
            LOGNOPTR.setNextBro(BOOLEXPRER.root);
            BOOL_EXPR.setRoot(LOGNOPTR);
        } 
        else if (tk.getTokenType().equals("identifier") || tk.getTokenType().equals("bool_const") || tk.getTokenType().equals("lpar") || tk.getTokenType().equals("int_const") || tk.getTokenType().equals("dec_const")) {
            System.out.println("lpar");
            B_TERM = B_TERM();

            BOOL_EXPR.setRoot(B_TERM.root);
        }
        return BOOL_EXPR;
    }

    public static ParseTree B_TERM() throws Exception {
        ParseNode BTERM = new ParseNode(new Token("BTERM", TokenType.BoolEXPR), null, null);
        ParseTree B_FACTOR = new ParseTree();
        ParseTree B_TERM_PRIME = new ParseTree();
        ParseTree BTERMER = new ParseTree();
        if (tk.getTokenType().equals("identifier") || tk.getTokenType().equals("bool_const") || tk.getTokenType().equals("lpar") || tk.getTokenType().equals("int_const") || tk.getTokenType().equals("dec_const")) {
            B_FACTOR = B_FACTOR();
            if (tk.getTokenType().equals("logao_optr")) {
                B_TERM_PRIME = B_TERM_PRIME();
                BTERM.setFirstChild(B_FACTOR.root);
                B_FACTOR.root.setNextBro(B_TERM_PRIME.root);
                BTERMER.setRoot(BTERM);
            } else {
                BTERM.setFirstChild(B_FACTOR.root);
                BTERMER.setRoot(BTERM);
            }

        }
        return BTERMER;
    }

    public static ParseTree B_TERM_PRIME() throws Exception {
        ParseNode ARITH_PRIME = new ParseNode(new Token("B_TERM_PRIME", TokenType.BoolEXPR), null, null);
        ParseNode LOGAO_OPTR = new ParseNode();
        ParseTree B_FACTOR = new ParseTree();
        ParseTree B_TERM_PRIME = new ParseTree();
        ParseTree PRIME = new ParseTree();
        Token ERROR;
        if (tk.getTokenType().equals("logao_optr")) {
            LOGAO_OPTR = makeMe(tk);
            ADVANCE();
            if (tk.getTokenType().equals("identifier") || tk.getTokenType().equals("bool_const") || tk.getTokenType().equals("lpar")) {
                B_FACTOR = B_FACTOR();
                B_TERM_PRIME = B_TERM_PRIME();
                ARITH_PRIME.setFirstChild(LOGAO_OPTR);
                LOGAO_OPTR.setNextBro(B_FACTOR.root);
                B_FACTOR.root.setNextBro(B_TERM_PRIME.root);
                PRIME.setRoot(ARITH_PRIME);
            }

        } else if (tk.getTokenType().equals("terminator")) {
            //EMPTY
        }
        return PRIME;
    }

    public static ParseTree B_FACTOR() throws Exception {
        ParseNode B_FACTOR = new ParseNode(new Token("B_FACTOR", TokenType.BoolEXPR), null, null);
        ParseNode ID = new ParseNode();
        ParseNode BOOL_CONST = new ParseNode();
        ParseNode LPAR = new ParseNode();
        ParseTree BOOL_EXPR = new ParseTree(new ParseNode(new Token("EMPTY", TokenType.EMPTY), null, null));
        ParseTree REL_EXPR = new ParseTree();
        ParseNode RPAR = new ParseNode();
        ParseTree B_FACT = new ParseTree();
        Token ERROR;
        System.out.println("B_FACTOR");
        switch (tk.getTokenType()) {
            case "identifier":
                REL_EXPR = RELEXPR();
                System.out.println("CREATED ID");
                B_FACTOR.setFirstChild(REL_EXPR.root);
                B_FACT.setRoot(B_FACTOR);
                System.out.println("B_FACTOR ID");
                break;
            case "bool_const":
                REL_EXPR = RELEXPR();
                B_FACTOR.setFirstChild(REL_EXPR.root);
                B_FACT.setRoot(B_FACTOR);
                break;
            case "lpar":
                LPAR = makeMe(tk);
                ADVANCE();
                System.out.println("LPAR CREATED");
                BOOL_EXPR = BOOLEXPR();
//                System.out.println(BOOL_EXPR);
                System.out.println("END");
                if (tk.getTokenType().equals("rpar")) {
                    RPAR = makeMe(tk);
                    ADVANCE();
                    System.out.println("RPAR");
                }
                if (RPAR.tk != null) {
                    System.out.println("CONNECTING");
                    ParseNode EXPR = new ParseNode(new Token("EXPR", TokenType.EXPR), null, null);
                    EXPR.setFirstChild(BOOL_EXPR.root);
                    System.out.println();
                    LPAR.setNextBro(EXPR);
                    System.out.println("LPAR CONNECTED TO EXPR");
                    EXPR.setNextBro(RPAR);
                    System.out.println("EXPR CONNECTED TO RPAR");
                    B_FACTOR.setFirstChild(LPAR);
                    System.out.println("LPAR CONNECTED TO B_FACTOR");
                    B_FACT.setRoot(B_FACTOR);
                    System.out.println("ROOT SET");
                }
                break;
            default:
                System.out.println("REL_EXPR");
                REL_EXPR = RELEXPR();
                B_FACT.setRoot(REL_EXPR.root);
                break;
        }
        System.out.println("B_FACT");
        return B_FACT;
    }

    public static ParseTree COMMENTS() throws Exception {
        ParseNode COMMENTS = new ParseNode(new Token("COMMENTS", TokenType.COMMENTS), null, null);
        ParseNode commline = new ParseNode();
        ParseNode commblock = new ParseNode();
        ParseTree COMMENT = new ParseTree();
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
    }

    public static ParseTree OUTPUT() throws Exception {
        ParseNode OUTPUT = new ParseNode(new Token("OUTPUT ROOT", TokenType.OUTPUT), null, null);
        ParseNode HARVEST = new ParseNode();
        ParseTree OUTPUTPRIME = new ParseTree();
        ParseTree OUTPUTA = new ParseTree();
        Token ERROR;
        if (tk.getTokenType().equals("output_type")) {
            HARVEST = makeMe(tk);
            ADVANCE();
            OUTPUTPRIME = OUTPUTPRIME();
            OUTPUT.setFirstChild(OUTPUTPRIME.root);
            OUTPUTA.setRoot(OUTPUT);
        }
        System.out.println("OUTPUT");
        return OUTPUTA;
    }

    public static ParseTree OUTPUTPRIME() throws Exception {
        ParseNode STRING_CO = new ParseNode();
        ParseNode ID = new ParseNode();
        ParseNode TERMINATOR = new ParseNode();
        ParseTree bool_expr = new ParseTree();
        ParseTree arith_expr = new ParseTree();
        ParseTree OUTPUTPRIME = new ParseTree();
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

    public static ParseTree INPUT() throws Exception {
        ParseNode INPUTTYPE = new ParseNode();
        ParseNode ID = new ParseNode();
        ParseTree INPUT = new ParseTree();
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
        INPUTTYPE.setNextBro(ID);
        INPUT.setRoot(INPUTTYPE);
        return INPUT;
    }

    public static ParseTree ASSIGN() throws Exception {
        ParseNode assign = new ParseNode(new Token("assign", TokenType.assign), null, null);
        ParseNode identifier = new ParseNode();
        ParseNode assign_op = new ParseNode();
        ParseTree assign_prime = new ParseTree(new ParseNode(new Token("assign_prime", TokenType.assign_prime), null, null));
        ParseNode terminator = new ParseNode();
        ParseTree ass = new ParseTree();
        Token ERROR, tkLook;
        System.out.println("ASSIGN");
        if (tk.getTokenType().equals("identifier")) {
            identifier = makeMe(tk);
            ADVANCE();
            if (tk.getTokenType().equals("assign_op")) {
                assign_op = makeMe(tk);
                ADVANCE();
                assign_prime = ASSIGNPRIME();
            }
        } else {
            ERROR = ERROR(TokenType.string_const, tk, line);
        }
        assign.setFirstChild(identifier);
        identifier.setNextBro(assign_op);
        assign_op.setNextBro(assign_prime.root);
        assign_prime.root.setNextBro(terminator);
        ass.setRoot(assign);
        return ass;
    }

    public static ParseTree ASSIGNPRIME() throws Exception {
        ParseNode assign_prime = new ParseNode(new Token("assign_prime", TokenType.assign_prime), null, null);
        ParseTree arith_expr = new ParseTree(new ParseNode(new Token("arith_expr", TokenType.ArithEXPR), null, null));
        ParseTree bool_expr = new ParseTree(new ParseNode(new Token("bool_expr", TokenType.BoolEXPR), null, null));
        ParseNode string_const = new ParseNode();
        ParseTree assign_primez = new ParseTree();
        Token ERROR, tkLook;
        // CHECK 
        switch (tk.getTokenType()) {
            case "identifier":
            case "int_const":
            case "dec_const":
                arith_expr = ARITHEXPR();
                assign_prime.setFirstChild(arith_expr.root);
                assign_primez.setRoot(assign_prime);
                break;
            case "logn_optr":
            case "logao_optr":
            case "bool_const":
                bool_expr = BOOLEXPR();
                assign_prime.setFirstChild(bool_expr.root);
                assign_primez.setRoot(assign_prime);
                break;
            case "string_const":
                assign_prime.setFirstChild(string_const);
                assign_primez.setRoot(assign_prime);
                break;
        }
        return assign_primez;
    }

    public static ParseTree CONDSTMT() throws Exception {
        ParseNode COND_STMT = new ParseNode(new Token("COND_STMT", TokenType.cond_stmt), null, null);
        ParseTree COND_STMT_A = new ParseTree();
        ParseTree COND_STMT_PRIME = new ParseTree();
        ParseTree CONDSTMT = new ParseTree();
        Token ERROR;
        if (tk.getTokenType().equals("cond_if")) {
            COND_STMT_A = CONDSTMTA();
            COND_STMT_PRIME = CONDSTMTPRIME();
            CONDSTMT.setRoot(COND_STMT);
            COND_STMT.setNextBro(COND_STMT_A.root);
            COND_STMT_A.root.setNextBro(COND_STMT_PRIME.root);
        }
        return CONDSTMT;
    }

    public static ParseTree CONDSTMTPRIME() throws Exception {
        ParseNode COND_STMT = new ParseNode(new Token("COND_STMT", TokenType.cond_stmt), null, null);
        ParseTree COND_STMT_A = new ParseTree();
        ParseTree COND_STMT_PRIME = new ParseTree(new ParseNode(new Token("COND_STMT_PRIME", TokenType.cond_stmt), null, null));
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

    public static ParseTree CONDSTMTA() throws Exception {
        ParseNode NodeMe = new ParseNode(new Token("COND_STMT_A_ROOT", TokenType.cond_stmt_a), null, null);
        ParseNode CONDIF = new ParseNode();
        ParseNode LPAR = new ParseNode();
        ParseNode RPAR = new ParseNode();
        ParseNode CONDTHEN = new ParseNode();
        ParseNode CONDENDIF = new ParseNode();
        ParseTree STATEA = new ParseTree();
        ParseTree STATEB = new ParseTree();
        ParseTree ULO = new ParseTree();
        Token ERROR;
        int countError = 0;
        System.out.println("CURRENT: " + tk.getTokenType());
        if (tk.getTokenType().equals("cond_if")) {
            CONDIF = makeMe(tk);
            ADVANCE();
            if (tk.getTokenType().equals("lpar")) {
                LPAR = makeMe(tk);
                ADVANCE();
                STATEA = BOOLEXPR();
                if (tk.getTokenType().equals("rpar")) {
                    RPAR = makeMe(tk);
                    ADVANCE();
                    if (tk.getTokenType().equals("cond_then")) {
                        CONDTHEN = makeMe(tk);
                        ADVANCE();
                        STATEB = IFTERMS();
                        System.out.println("STATEB: " + STATEB);
                        if (tk.getTokenType().equals("cond_endif")) {
                            CONDENDIF = makeMe(tk);
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

        CONDIF.setNextBro(LPAR);
        LPAR.setNextBro(STATEA.root);
        STATEA.root.setNextBro(RPAR);
        RPAR.setNextBro(CONDTHEN);
        CONDTHEN.setNextBro(STATEB.root);
        STATEB.root.setNextBro(CONDENDIF);
        NodeMe.setFirstChild(CONDIF);
        ULO.setRoot(NodeMe);
        return ULO;
    }

    public static ParseTree IFTERMS() throws Exception {
        ParseNode IF_TERMS = new ParseNode(new Token("IF TERMS", TokenType.IF_TERMS), null, null);
        ParseTree STMTS = new ParseTree();
        ParseTree IF_TERMS_PRIME = new ParseTree();
        ParseTree IFTERMS = new ParseTree();
        if (tk.getTokenType().equals("data_type_int") || tk.getTokenType().equals("data_type_dec") || tk.getTokenType().equals("data_type_bool") || tk.getTokenType().equals("data_type_string") || tk.getTokenType().equals("identifier") || tk.getTokenType().equals("input_type") || tk.getTokenType().equals("output_type") || tk.getTokenType().equals("cond_if") || tk.getTokenType().equals("loop_do") || tk.getTokenType().equals("loop_while") || tk.getTokenType().equals("switcher") || tk.getTokenType().equals("increment") || tk.getTokenType().equals("decrement") || tk.getTokenType().equals("line_comment") || tk.getTokenType().equals("block_comment")) {
            STMTS = PGMSTMT();
            
            switch (tk.getTokenType()) {
                case "cond_elseif":
                case "cond_else":
                    IF_TERMS_PRIME = IFTERMSPRIME();
                    IFTERMS.setRoot(IF_TERMS);
                    IF_TERMS.setFirstChild(STMTS.root);
                    STMTS.root.setNextBro(IF_TERMS_PRIME.root);
                    break;
                default:
                    break;
            }
        }
        return IFTERMS;
    }

    public static ParseTree IFTERMSPRIME() throws Exception {
        ParseNode IF_TERMS = new ParseNode(new Token("IF TERMS", TokenType.IF_TERMS), null, null);
        ParseTree OPTIONAL_ELSEIF = new ParseTree();
        ParseTree OPTIONAL_ELSE = new ParseTree();
        ParseTree IF_TERMS_PRIME = new ParseTree();

        switch (tk.getTokenType()) {
            case "cond_elseif":
                OPTIONAL_ELSEIF = OPTIONALELSEIF();
                IF_TERMS_PRIME.setRoot(OPTIONAL_ELSEIF.root);
                break;
            case "cond_else":
                OPTIONAL_ELSE = OPTIONALELSE();
                IF_TERMS_PRIME.setRoot(OPTIONAL_ELSE.root);
                break;
            default:
                break;
        }
        return IF_TERMS_PRIME;

    }

    public static ParseTree OPTIONALELSEIF() throws Exception {
        ParseNode NodeMe = new ParseNode(new Token("OPTIONAL_ELSEIF_ROOT", TokenType.optional_elseif), null, null);
        ParseTree OPT_ELSEIF_A = new ParseTree();
        ParseTree OPT_ELSEIF_AP = new ParseTree();
        ParseTree Finally = new ParseTree();
        Token ERROR;
        int countError = 0;
        if (tk.getTokenType().equals("cond_elseif")) {
            OPT_ELSEIF_A = OPTIONALELSEIFA();
            OPT_ELSEIF_AP = OPTIONALELSEIFAPRIME();
            OPT_ELSEIF_A.root.setNextBro(OPT_ELSEIF_AP.root);
            NodeMe.setFirstChild(OPT_ELSEIF_A.root);
            Finally.setRoot(NodeMe);
        }

        return Finally;
    }

    public static ParseTree OPTIONALELSEIFAPRIME() throws Exception {
        ParseNode NodeMe = new ParseNode(new Token("OPTIONAL_ELSE_IF_APRIME_ROOT", TokenType.optional_else_if_aprime), null, null);
        ParseTree OPT_ELSEIF = new ParseTree();
        ParseTree OPT_ELSEIF_P = new ParseTree();
        ParseTree OPT_ELSE = new ParseTree();
        ParseTree Finally = new ParseTree();
        Token ERROR;
        int countError = 0;

        switch (tk.getTokenType()) {
            case "cond_elseif": {
                OPT_ELSEIF = OPTIONALELSEIF();
                OPT_ELSEIF_P = OPTIONALELSEIFPRIME();
                OPT_ELSEIF.root.setNextBro(OPT_ELSEIF_P.root);
                NodeMe.setFirstChild(OPT_ELSEIF.root);
                Finally.setRoot(NodeMe);
                break;
            }
            case "cond_else": {
                OPT_ELSE = OPTIONALELSE();
                OPT_ELSEIF_P = OPTIONALELSEIFPRIME();
                OPT_ELSE.root.setNextBro(OPT_ELSEIF_P.root);
                NodeMe.setFirstChild(OPT_ELSE.root);
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

    public static ParseTree OPTIONALELSEIFPRIME() throws Exception {
        ParseNode NodeMe = new ParseNode(new Token("OPTINAL_ELSEIF_PRIME", TokenType.optional_else_prime), null, null);
        ParseTree OPT_ELSEIF = new ParseTree();
        ParseTree OPT_ELSEIF_P = new ParseTree();
        ParseTree Finally = new ParseTree(new ParseNode(new Token("EMPTY", TokenType.EMPTY), null, null));
        Token ERROR;
        int countError = 0;

        if (tk.getTokenType().equals("cond_elseif")) {
            OPT_ELSEIF = OPTIONALELSEIF();
            // not sure of condition. kasi last token ni elseif prime ung hinahanap :))
            OPT_ELSEIF_P = OPTIONALELSEIFPRIME();
            NodeMe.setFirstChild(OPT_ELSEIF.root);
            OPT_ELSEIF.root.setNextBro(OPT_ELSEIF_P.root);
            Finally.setRoot(NodeMe);
        } else if (tk.getTokenType().equals("terminator")) {
        }
        return Finally;
    }

    public static ParseTree OPTIONALELSEIFA() throws Exception {
        ParseNode NodeMe = new ParseNode(new Token("OPTIONAL_ELSE_IF_A_ROOT", TokenType.optional_else_if_a), null, null);
        ParseNode CONDELSEIF = new ParseNode();
        ParseNode LPAR = new ParseNode();
        ParseNode RPAR = new ParseNode();
        ParseNode CONDTHEN = new ParseNode();
        ParseTree STATEA = new ParseTree();
        ParseTree STATEB = new ParseTree();
        ParseTree ULO = new ParseTree();
        Token ERROR;
        int countError = 0;

        if (tk.getTokenType().equals("cond_elseif")) {
            CONDELSEIF = makeMe(tk);
            ADVANCE();
            if (tk.getTokenType().equals("lpar")) {
                LPAR = makeMe(tk);
                ADVANCE();
                STATEA = BOOLEXPR();
                if(tk.getTokenType().equals("rpar")){
                    RPAR = makeMe(tk);
                    ADVANCE();
                    if (tk.getTokenType().equals("cond_then")) {
                        CONDTHEN = makeMe(tk);
                        ADVANCE();
                        STATEB = PGMSTMT();
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

        CONDELSEIF.setNextBro(LPAR);
        LPAR.setNextBro(STATEA.root);
        STATEA.root.setNextBro(RPAR);
        RPAR.setNextBro(CONDTHEN);
        CONDTHEN.setNextBro(STATEB.root);
        NodeMe.setFirstChild(CONDELSEIF);
        ULO.setRoot(NodeMe);

        return ULO;
    }

    public static ParseTree OPTIONALELSE() throws Exception {
        ParseNode NodeMe = new ParseNode(new Token("OPTIONAL_ELSE_ROOT", TokenType.optional_else), null, null);
        ParseNode CONDELSE = new ParseNode();
        ParseTree STATEA = new ParseTree();
        ParseTree ULO = new ParseTree();
        Token ERROR;
        int countError = 0;
        System.out.println("CHECKING ELSE");
        if (tk.getTokenType().equals("cond_else")) {
            CONDELSE = makeMe(tk);
            ADVANCE();
            STATEA = PGMSTMT();
        } else {
            ERROR = ERROR(TokenType.cond_else, tk, line);
            countError++;
        }
        CONDELSE.setNextBro(STATEA.root);
        NodeMe.setFirstChild(CONDELSE);
        ULO.setRoot(NodeMe);

        return ULO;
    }

    public static ParseTree LOOP() throws Exception {
        ParseNode NodeMe = new ParseNode(new Token("LOOP_ROOT", TokenType.loop_do), null, null);
        ParseTree LOOPA = new ParseTree();
        ParseTree LOOP_P = new ParseTree();
        ParseTree LOOPB = new ParseTree();
        ParseTree Finally = new ParseTree();

        if (tk.getTokenType().equals("loop_do")) {
            LOOPA = LOOPA();
            if (tk.getTokenType().equals("loop_do") || tk.getTokenType().equals("loop_while")) {
                LOOP_P = LOOPPRIME();
            }
            Finally.setRoot(NodeMe);
            NodeMe.setFirstChild(LOOPA.root);
            LOOPA.root.setNextBro(LOOP_P.root);

        } else if (tk.getTokenType().equals("loop_while")) {
            LOOPB = LOOPB();
            if (tk.getTokenType().equals("loop_do") || tk.getTokenType().equals("loop_while")) {
                LOOP_P = LOOPPRIME();
                LOOPB.root.setNextBro(LOOP_P.root);
                NodeMe.setFirstChild(LOOPB.root);
                Finally.setRoot(NodeMe);
            }
        }
        return Finally;
    }

    public static ParseTree LOOPPRIME() throws Exception {
        ParseNode NodeMe = new ParseNode(new Token("LOOP_PRIME_ROOT", TokenType.loop_prime), null, null);
        ParseTree LOOP = new ParseTree();
        ParseTree Finally = new ParseTree();

        if (tk.getTokenType().equals("loop_do") || tk.getTokenType().equals("loop_while")) {
            LOOP = LOOP();
            NodeMe.setFirstChild(LOOP.root);
            Finally.setRoot(NodeMe);
        } else if (tk.getTokenType().equals("terminator")) {
        }
        return Finally;
    }

    public static ParseTree LOOPA() throws Exception {
        ParseNode NodeMe = new ParseNode(new Token("LOOP_A_ROOT", TokenType.loop_a), null, null);
        ParseNode LOOPDO = new ParseNode();
        ParseNode LCURL = new ParseNode();
        ParseNode RCURL = new ParseNode();
        ParseNode LOOPWHILE = new ParseNode();
        ParseNode LPAR = new ParseNode();
        ParseNode RPAR = new ParseNode();
        ParseNode TERMINATOR = new ParseNode();
        ParseTree STATEA = new ParseTree();
        ParseTree STATEB = new ParseTree();
        ParseTree ULO = new ParseTree();
        Token ERROR;
        int countError = 0;

        if (tk.getTokenType().equals("loop_do")) {
            LOOPDO = makeMe(tk);
            ADVANCE();
            if (tk.getTokenType().equals("lcurl")) {
                LCURL = makeMe(tk);
                ADVANCE();
                STATEA = PGMSTMT();
                System.out.println("STATEMENTS: ");
                //System.out.println("ROOT: " + STATEA);
                if (tk.getTokenType().equals("rcurl")) {
                    RCURL = makeMe(tk);
                    ADVANCE();
                    if (tk.getTokenType().equals("loop_while")) {
                        LOOPWHILE = makeMe(tk);
                        ADVANCE();
                        if (tk.getTokenType().equals("lpar")) {
                            System.out.println("BOOL CHECKING");
                            STATEB = BOOLEXPR();
                            System.out.println("HELLO WORLD I AM DONE");
                            if (tk.getTokenType().equals("terminator")) {
                                TERMINATOR = makeMe(tk);
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
        System.out.println("CONNECTING IN LOOPA");
        LOOPDO.setNextBro(LCURL);
        System.out.println("LCURL CONNECTED");
        LCURL.setNextBro(STATEA.root);
        System.out.println("LCURL CONNECTED");
        STATEA.root.setNextBro(RCURL);
        
        System.out.println("LCURL CONNECTED");
        RCURL.setNextBro(LOOPWHILE);
        System.out.println("LCURL CONNECTED");
        LOOPWHILE.setNextBro(STATEB.root);
        System.out.println("LCURL CONNECTED");
        
        System.out.println("LCURL CONNECTED");
        STATEB.root.setNextBro(TERMINATOR);
        System.out.println("LCURL CONNECTED");
        NodeMe.setFirstChild(LOOPDO);
        ULO.setRoot(NodeMe);
        //System.out.println("RETURNING LOOPA: " + ULO);
        return ULO;
    }

    public static ParseTree LOOPB() throws Exception {
        ParseNode NodeMe = new ParseNode(new Token("LOOP_B_ROOT", TokenType.loop_b), null, null);
        ParseNode LCURL = new ParseNode();
        ParseNode RCURL = new ParseNode();
        ParseNode LOOPWHILE = new ParseNode();
        ParseNode LPAR = new ParseNode();
        ParseNode RPAR = new ParseNode();
        ParseNode TERMINATOR = new ParseNode();
        ParseTree STATEA = new ParseTree();
        ParseTree STATEB = new ParseTree();
        ParseTree ULO = new ParseTree();
        Token ERROR;
        int countError = 0;

        if (tk.getTokenType().equals("loop_while")) {
            LOOPWHILE = makeMe(tk);
            ADVANCE();
            if (tk.getTokenType().equals("lpar")) {
                LPAR = makeMe(tk);
                ADVANCE();
                STATEA = BOOLEXPR();
                if (tk.getTokenType().equals("rpar")) {
                    RPAR = makeMe(tk);
                    ADVANCE();
                    if (tk.getTokenType().equals("lcurl")) {
                        LCURL = makeMe(tk);
                        ADVANCE();
                        STATEB = PGMSTMT();
                        if (tk.getTokenType().equals("rcurl")) {
                            RCURL = makeMe(tk);
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

        LOOPWHILE.setNextBro(LPAR);
        LPAR.setNextBro(STATEA.root);
        STATEA.root.setNextBro(RPAR);
        RPAR.setNextBro(LCURL);
        LCURL.setNextBro(STATEB.root);
        STATEB.root.setNextBro(RCURL);
        NodeMe.setFirstChild(LOOPWHILE);
        ULO.setRoot(NodeMe);

        return ULO;
    }

    public static ParseTree SWITCHA() throws Exception {
        ParseNode NodeMe = new ParseNode(new Token("SWITCH_A_ROOT", TokenType.switch_a), null, null);
        ParseNode SWITCHER = new ParseNode();
        ParseNode LPAR = new ParseNode();
        ParseNode IDENTIFIER = new ParseNode();
        ParseNode RPAR = new ParseNode();
        ParseNode LCURL = new ParseNode();
        ParseNode RCURL = new ParseNode();
        ParseTree STATEA = new ParseTree();
        ParseTree ULO = new ParseTree();
        Token ERROR;
        int countError = 0;

        if (tk.getTokenType().equals("switcher")) {
            SWITCHER = makeMe(tk);
            ADVANCE();
            if (tk.getTokenType().equals("lpar")) {
                LPAR = makeMe(tk);
                ADVANCE();
                if (tk.getTokenType().equals("identifier")) {
                    IDENTIFIER = makeMe(tk);
                    ADVANCE();
                    if (tk.getTokenType().equals("rpar")) {
                        RPAR = makeMe(tk);
                        ADVANCE();
                        if (tk.getTokenType().equals("lcurl")) {
                            LCURL = makeMe(tk);
                            ADVANCE();
                            STATEA = CASEA();
                            if (tk.getTokenType().equals("rcurl")) {
                                RCURL = makeMe(tk);
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

        SWITCHER.setNextBro(LPAR);
        LPAR.setNextBro(IDENTIFIER);
        IDENTIFIER.setNextBro(RPAR);
        RPAR.setNextBro(LCURL);
        LCURL.setNextBro(STATEA.root);
        STATEA.root.setNextBro(RCURL);
        NodeMe.setFirstChild(SWITCHER);
        ULO.setRoot(NodeMe);

        return ULO;
    }

    public static ParseTree CASEA() throws Exception {
        ParseNode NodeMe = new ParseNode(new Token("SWITCH_A_ROOT", TokenType.switch_a), null, null);
        ParseNode CASER = new ParseNode();
        ParseNode COLON = new ParseNode();
        ParseNode LCURL = new ParseNode();
        ParseNode RCURL = new ParseNode();
        ParseTree STATEA = new ParseTree();
        ParseTree STATEB = new ParseTree();
        ParseTree STATEC = new ParseTree();
        ParseTree ULO = new ParseTree();
        Token ERROR;
        int countError = 0;

        if (tk.getTokenType().equals("caser")) {
            CASER = makeMe(tk);
            ADVANCE();
            STATEA = CONSTANTS();
            if (tk.getTokenType().equals("colon")) {
                COLON = makeMe(tk);
                ADVANCE();
                if (tk.getTokenType().equals("lcurl")) {
                    LCURL = makeMe(tk);
                    ADVANCE();
                    STATEB = CASE_LOOB();
                    if (tk.getTokenType().equals("rcurl")) {
                        RCURL = makeMe(tk);
                        ADVANCE();
                        STATEC = CASEAPRIME();
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

        CASER.setNextBro(STATEA.root);
        STATEA.root.setNextBro(COLON);
        COLON.setNextBro(LCURL);
        STATEB.root.setNextBro(RCURL);
        RCURL.setNextBro(STATEC.root);
        NodeMe.setFirstChild(CASER);
        ULO.setRoot(NodeMe);
        return ULO;
    }

    public static ParseTree CASE_LOOB() throws Exception {
        ParseNode CASELOOB = new ParseNode(new Token("CASE", TokenType.casecase), null, null);
        ParseTree STMTS = new ParseTree();
        ParseNode BREAKER = new ParseNode();
        ParseNode TERMINATOR = new ParseNode();
        ParseTree CASE_LOOB = new ParseTree();
        Token ERROR;
        if (tk.getTokenType().equals("data_type_int") || tk.getTokenType().equals("data_type_dec") || tk.getTokenType().equals("data_type_bool") || tk.getTokenType().equals("data_type_string") || tk.getTokenType().equals("identifier") || tk.getTokenType().equals("input_type") || tk.getTokenType().equals("output_type") || tk.getTokenType().equals("cond_if") || tk.getTokenType().equals("loop_do") || tk.getTokenType().equals("loop_while") || tk.getTokenType().equals("switcher") || tk.getTokenType().equals("increment") || tk.getTokenType().equals("decrement") || tk.getTokenType().equals("line_comment") || tk.getTokenType().equals("block_comment")) {
            STMTS = PGMSTMT();
            if (tk.getTokenType().equals("breaker")) {
                BREAKER = makeMe(tk);
                ADVANCE();
                if (tk.getTokenType().equals("terminator")) {
                    TERMINATOR = makeMe(tk);
                    ADVANCE();
                    CASE_LOOB.setRoot(CASELOOB);
                    CASELOOB.setFirstChild(STMTS.root);
                    STMTS.root.setNextBro(BREAKER);
                    BREAKER.setNextBro(TERMINATOR);
                } else {
                    ERROR = ERROR(TokenType.terminator, tk, line);
                    TERMINATOR = new ParseNode(new Token(";", TokenType.terminator), null, null);
                }
            }
        }
        return CASE_LOOB;
    }

    public static ParseTree CASEAPRIME() throws Exception {
        ParseNode CASE_A_PRIME = new ParseNode(new Token("CASE_A_PRIME", TokenType.CASE_A_PRIME), null, null);
        ParseTree CASE_A = new ParseTree();
        ParseNode DEFAULTER = new ParseNode();
        ParseNode COLON = new ParseNode();
        ParseNode LCURL = new ParseNode();
        ParseTree STMTS = new ParseTree();
        ParseNode RCURL = new ParseNode();
        ParseTree CASEAPRIME = new ParseTree();
        switch (tk.getTokenType()) {
            case "caser":
                CASE_A = CASEA();
                CASEAPRIME.setRoot(CASE_A.root);
                break;
            case "defaulter":
                DEFAULTER = makeMe(tk);
                ADVANCE();
                if (tk.getTokenType().equals("colon")) {
                    COLON = makeMe(tk);
                    ADVANCE();
                    if (tk.getTokenType().equals("lcurl")) {
                        LCURL = makeMe(tk);
                        ADVANCE();
                        STMTS = PGMSTMT();
                        if (tk.getTokenType().equals("rcurl")) {
                            RCURL = makeMe(tk);
                            ADVANCE();
                            CASE_A_PRIME.setFirstChild(DEFAULTER);
                            DEFAULTER.setNextBro(COLON);
                            COLON.setNextBro(LCURL);
                            LCURL.setNextBro(STMTS.root);
                            STMTS.root.setNextBro(RCURL);
                        }
                    }
                }
                break;
        }
        return CASEAPRIME;
    }

    public static ParseTree CONSTANTS() throws Exception {
        ParseNode CONSTANT = new ParseNode(new Token("CONSTANT", TokenType.constant), null, null);
        ParseNode int_const = new ParseNode();
        ParseNode dec_const = new ParseNode();
        ParseNode string_const = new ParseNode();
        ParseTree CONSTANTS = new ParseTree();
        Token ERROR;
        switch (tk.getTokenType()) {
            case "int_const":
                int_const = makeMe(tk);
                ADVANCE();
                CONSTANTS.setRoot(int_const);
                break;
            case "dec_const":
                dec_const = makeMe(tk);
                ADVANCE();
                CONSTANTS.setRoot(dec_const);
                break;
            case "string_const":
                string_const = makeMe(tk);
                ADVANCE();
                CONSTANTS.setRoot(string_const);
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
