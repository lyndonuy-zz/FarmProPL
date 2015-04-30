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

public class Interpreter {
    static Map<String, Attributes> GlobalMap = new HashMap();
    static SyntaxTree tree;
    static String s = "";
    public static void main(String[] args) throws Exception{
        Parser parse = new Parser("SSCLittle.txt");
        ParserSyn parser = new ParserSyn("SSCLittle.txt");
        System.out.println("-----------------------LEXICAL ANALYZER------------------------------");
        System.out.println(parser.lex);
        System.out.println("-----------------------PARSED TREES----------------------------------");
        System.out.println(parse.tree);
        tree = parser.root;
        GlobalMap = parser.lex.getSymbolTable();
        //Interpreter();
        mainInterpreter();
        //SyntaxTree root = parser.getTree();
        //System.out.println(root);
        //parser.main();
    }
    public static List<SyntaxTreeNode> build(SyntaxTree.kTreeTraversalOrder order) {
        List<SyntaxTreeNode> l = null;
        if (tree.root != null) {
            l = build(tree.root, order);
        }
        return l;
    }

    private static List<SyntaxTreeNode> build(SyntaxTreeNode node, SyntaxTree.kTreeTraversalOrder o) {
        List<SyntaxTreeNode> traverse = new ArrayList<SyntaxTreeNode>();
        if (o == SyntaxTree.kTreeTraversalOrder.PRE_ORDER) {
            buildPreOrder(node, traverse);
        } else if (o == SyntaxTree.kTreeTraversalOrder.POST_ORDER) {
//            buildPostOrder(node, traverse);
        }
        return traverse;
    }

    private static void buildPreOrder(SyntaxTreeNode node, List<SyntaxTreeNode> traverse) {
        traverse.add(node);
        for (SyntaxTreeNode t : node.getChildren()) {
            if(t != null){
                //System.out.println("TRAVERSE: " + traverse.toString());
                buildPreOrder(t, traverse);
            }
            else{
                
            }
        }
    }
    static List<SyntaxTreeNode> hehehe = new ArrayList();
    static int i = 0;
    public static void mainInterpreter(){
        
        //System.out.println(build(SyntaxTree.kTreeTraversalOrder.PRE_ORDER));
        //List<SyntaxTreeNode> hehehe = build(SyntaxTree.kTreeTraversalOrder.PRE_ORDER);
        hehehe = build(SyntaxTree.kTreeTraversalOrder.PRE_ORDER);
        for(; i < hehehe.size(); i++){
            System.out.println("FOR LOOP");
            if(hehehe.get(i).tk.getTokenType().equals("start")){
                System.out.println("START");
            }
            else if(hehehe.get(i).tk.getTokenType().equals("start_main")){
                i += 1;
                System.out.println("MAIN");
                if(hehehe.get(i).tk.getTokenType().equals("identifier")){
                    System.out.println("ID");
                }
            }
            else if(hehehe.get(i).tk.getTokenType().equals("stmt")){
                System.out.println("STMT");
                STMT();
            }
            else if(hehehe.get(i).tk.getTokenType().equals("end_main")){
                
            }
            else if(hehehe.get(i).tk.getTokenType().equals("end")){
                
            }
        }
    }
    static int id;
    public static void STMT(){
        for(; i < hehehe.size() && !hehehe.get(i).equals("end_main"); i++){
            if(hehehe.get(i).tk.getTokenType().equals("stmt")){
                
            }
            else if(hehehe.get(i).tk.getTokenType().equals("OUTPUT")){
                System.out.println("HEHE");
                i += 1;
                SOP();
            }
            else if(hehehe.get(i).tk.getTokenType().equals("INPUT")){
                
            }
            else if(hehehe.get(i).tk.getTokenType().equals("ArithEXPR")){
                
            }
            else if(hehehe.get(i).tk.getTokenType().equals("decl")){
                i += 1;
                System.out.println("decl");
                if (hehehe.get(i).tk.getTokenType().equals("data_type_int")) {
                    i += 1;
                    if (hehehe.get(i).tk.getTokenType().equals("identifier")) {
                        id = i;
                        i += 1;
                        System.out.println();
                        if (hehehe.get(i).tk.getTokenType().equals("decla")) {
                            i += 1;
                            if (hehehe.get(i).tk.getTokenType().equals("assign_op")) {
                                i += 1;
                                ARITH_EXPR();
                                if (hehehe.get(i).tk.getTokenType().equals("terminator")) {
                                    
                                }
                            }
                        }
                    }
                }
                
            }
        }
    }
    public static void AttributeChange(double a){
        for (Map.Entry<String, Attributes> entry : GlobalMap.entrySet()) {
            String key = entry.getKey();
            Attributes attribute = entry.getValue();
            System.out.println("Attribute Change");
            if (key.equals(hehehe.get(id).tk.getLexeme())) {
                attribute.value = String.valueOf(a);
            }
        }
    }
    public static void ARITH_EXPR(){
        String str = "";
        while(hehehe.get(i).tk.getTokenType().equals("ArithEXPR")){
            i += 1;
            //System.out.println("null");
            if(hehehe.get(i).tk.getTokenType().equals("int_const") || hehehe.get(i).tk.getTokenType().equals("dec_const") || hehehe.get(i).tk.getTokenType().equals("identifier") || hehehe.get(i).tk.getTokenType().equals("arithmd") || hehehe.get(i).tk.getTokenType().equals("arithas")){
                System.out.println("ARITH");
                System.out.println("VALUE of s as program gets from List: " + s);
                if(str.equals("")){
                    str = hehehe.get(i).tk.getLexeme();
                }
                else
                    str = str + " " + hehehe.get(i).tk.getLexeme();
                //str = hehehe.get(i).tk.getLexeme() + " " + str;
                i += 1;
            }
            else{
                
            }
        }
//        String[] evaluate = s.split(" ");
        double a, c;
        String b;
        String[] cd = str.split(" ");
        for(int i = 0; i < cd.length; i++){
            System.out.println(cd[i]);
        }
        System.out.println(str);
        b = InfixToPrefix(str);
        System.out.println(b);
        a = evalPrefix(b);
        System.out.println(a);
        AttributeChange(a);
    }
        public static String InfixToPrefix(String s){
        Stack<String> st = new <String> Stack(); //optr
        Stack<String> st1 = new <String> Stack(); //rev
        String delimiter = " ";
        String fi = "";
        String[] c = s.split(delimiter) ;
        for(int i = c.length - 1; i >= 0; i--){
            if (c[i].compareTo("+") != 0 && c[i].compareTo("-") != 0 && c[i].compareTo("*") != 0 && c[i].compareTo("/") != 0 && c[i].compareTo("^") != 0 && c[i].compareTo("(") != 0 && c[i].compareTo(")") != 0) { 
                st1.push(c[i]);
            }
            else{
                if(c[i].compareTo("(") == 0){
                    while(!st.isEmpty() && st.top().compareTo(")") != 0){
                        st1.push(st.pop());
                    }
                    st.pop();
                }
                else st.push(c[i]);
                while(!st.isEmpty() && PriorityPost(c[i], "a") < PriorityPost(st.top(), "a")){
                    st1.push(st.pop());
                }
            }
        }
        while(!st.isEmpty()){
            st1.push(st.pop());
        }
        while(!st1.isEmpty()){
            fi = fi + st1.pop() + " ";
        }
        return fi;
    }   
   
    
        public static int PriorityPost(String s, String t){
        if(t.compareTo("a") == 0){
            if(s.compareTo("+") == 0 || s.compareTo("-") == 0){
                return 1;
            }
            if(s.compareTo("%") == 0){
                return 3;
            }
            if(s.compareTo("*") == 0 || s.compareTo("/") == 0){
                return 5;
            }
            if(s.compareTo("^") == 0){
                return 8;
            }
            else{
                return 9;
            }
        }
        else{
            if(s.compareTo("+") == 0 || s.compareTo("-") == 0){
                return 2;
            }
            if(s.compareTo("%") == 0){
                return 4;
            }
            if(s.compareTo("*") == 0 || s.compareTo("/") == 0){
                return 6;
            }
            if(s.compareTo("^") == 0){
                return 7;
            }
            else{
                return 0;
            }
        }
    }
    public static int PriorityPre(String s, String t){
        if(t.compareTo("a") == 0){
            if(s.compareTo("+") == 0 || s.compareTo("-") == 0){
                return 1;
            }
            if(s.compareTo("*") == 0 || s.compareTo("/") == 0){
                return 3;
            }
            if(s.compareTo("^") == 0){
                return 6;
            }
            if(s.compareTo("(") == 0){
                return 0;
            }
            else{
                return 9;
            }
        }
        else{
            if(s.compareTo("+") == 0 || s.compareTo("-") == 0){
                return 2;
            }
            if(s.compareTo("*") == 0 || s.compareTo("/") == 0){
                return 4;
            }
            if(s.compareTo("^") == 0){
                return 5;
            }
            if(s.compareTo("(") == 0){
                return 9;
            }
            else{
                return 0;
            }
        }
    }

    public static double evaluate(double a, String b, double c){
        char y = b.charAt(0);
        double answer = 0;
        switch(y){
            case '+':
                answer = a + c;
                break;
            case '-':
                answer = a - c;
                break;
            case '*':
                answer = a * c;
                break;
            case '/':
                answer = a / c;
                break;
            case '^':
                answer = Math.pow(a,c);
                break;
        }
        return answer;
    }
    public static double evalPrefix(String s){
        Stack st = new Stack();
        String delimiter = " ";
        String[] c = s.split(delimiter);

        double x, y;

        for (int i = c.length - 1; i >= 0; i--) {
            if (c[i].compareTo("*") != 0 && c[i].compareTo("/") != 0 && c[i].compareTo("-") != 0 && c[i].compareTo("+") != 0 && c[i].compareTo("^") != 0) {
                st.push(Double.parseDouble(c[i]));
            } else {
                y = (Double)st.pop(); 
                x = (Double)st.pop();
                st.push(evaluate(y, c[i], x));
            }

        }
        return (Double)st.pop();
    }
    public static void SOP(){
        //sList<SyntaxTreeNode> c = n.children;
        //System.out.println("SOP");
        //System.out.println(hehehe.get(i).tk.getTokenType());
        if(hehehe.get(i).tk.getTokenType().equals("output_type")){
            //System.out.println("OUTPUT_TYPE");
            i += 1;
            //System.out.println("OUTPUT_TYPE");
            //System.out.println(hehehe.get(i).tk.getTokenType());
            if(hehehe.get(i).tk.getTokenType().equals("string_const")){
                System.out.println(hehehe.get(i).tk.lexeme);
            }
            while(hehehe.get(i).tk.getTokenType().equals("ArithEXPR")){
                i += 1;
                if (hehehe.get(i).tk.getTokenType().equals("identifier")) {
                    for (Map.Entry<String, Attributes> entry : GlobalMap.entrySet()) {
                        String key = entry.getKey();
                        Attributes attribute = entry.getValue();
                        System.out.println("SOP");
                        if (key.equals(hehehe.get(i).tk.getLexeme())) {
                            System.out.println(attribute.getValue());
                        }
                    }
                }
            }
        }
    }
}
