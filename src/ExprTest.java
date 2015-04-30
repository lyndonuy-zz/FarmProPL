/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user
 */
//Lyndon A. Uy
//3CSD, 3csd-47
//Lab Exercise 7
import java.util.*;
public class ExprTest {
    static Scanner console = new Scanner(System.in);
    public static void main(String[] args) {
        int t;
        double eval;
        String s, b;
        System.out.println("Input a String: ");
        s = console.nextLine();
        System.out.println("Input an Integer: ");
        t = console.nextInt();
        
        switch(t){
            case 1:
                System.out.println("Postfix: ");
                System.out.println(s);
                System.out.println("Infix: ");
                b = postfixToInfix(s); 
                System.out.println(b);
                break;
            case 2:
                System.out.println("Infix: ");
                System.out.println(s);
                System.out.println("Prefix: ");
                b = InfixToPrefix(s);
                System.out.println(b);
                System.out.println(evalPrefix(b));
                break;
            case 3:
                System.out.println("Postfix: ");
                System.out.println(s);
                System.out.println("Infix: ");
                eval = evalPostFix(s); 
                System.out.println(eval);
                break;
            case 4:
                System.out.println("Infix: ");
                System.out.println(s);
                System.out.println("Postfix: ");
                b = InfixToPostfix(s);
                 
                System.out.println(b);
                break;
            case 5:
                System.out.println("Prefix: ");
                System.out.println(s);
                System.out.println("Infix: ");
                b = PrefixToInfix(s);
                System.out.println(b);
                System.out.println(evalPrefix(s));
                break;
            case 6:
                
        }
        System.exit(0);
    }
    public static String postfixToInfix(String s) { 
        Stack<String> st = new Stack();
        String delimiter = " ";
        String fi = "";
        String[] c = s.split(delimiter) ;  
        Object a, b; 
        for (int i = 0; i < c.length; i++) { 
            if (c[i].compareTo("*") != 0 && c[i].compareTo("/") != 0 && c[i].compareTo("-") != 0 && c[i].compareTo("+") != 0 && c[i].compareTo("^") != 0) { 
                st.push(c[i]); 
            } 
            else { 
                b = st.pop(); 
                a = st.pop(); 
                st.push(concat(a, c[i], b)); 
            } 
        }
        while (!st.isEmpty()){
            fi = fi + st.pop();
        }
        return fi; 
    } 
    public static String PrefixToInfix(String s)
    {
        Stack<String> st = new <String> Stack();
        String delimiter = " ";
        String[] c = s.split(delimiter);
        String e = "";

        Object A, B;

        for (int i = c.length - 1; i >= 0; i--) {
            if (c[i].compareTo("*") != 0 && c[i].compareTo("/") != 0 && c[i].compareTo("-") != 0 && c[i].compareTo("+") != 0 && c[i].compareTo("^") != 0) {
                st.push(c[i]);
            } else {
                A = st.pop();
                B = st.pop();
                st.push(concat(A, c[i], B));
            }

        }

        while (!st.isEmpty()) {
            e = e + " " + st.pop();
        }
        return e;
    }
    public static String concat(Object a, String y, Object b) { 
        return "(" +  (String) a + " " + y + " " + (String) b +  ")"; 
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
    public static String InfixToPostfix(String s){
        Stack<String> st = new <String> Stack();
        String delimiter = " ";
        String fi = "";
        String[] c = s.split(delimiter) ;  
        for (int i = 0; i < c.length; i++) { 
            if (c[i].compareTo("+") != 0 && c[i].compareTo("-") != 0 && c[i].compareTo("*") != 0 && c[i].compareTo("/") != 0 && c[i].compareTo("^") != 0 && c[i].compareTo("(") != 0 && c[i].compareTo(")") != 0 && c[i].compareTo("%") != 0) { 
                fi = fi + c[i] + " ";
            } 
            else { 
                if(c[i].compareTo("(") == 0){
                    st.push(c[i]);
                }
                else if(c[i].compareTo(")") == 0){
                    while(!st.isEmpty() && st.top().compareTo("(") != 0){
                        fi = fi + st.pop() + " "; 
                    }
                    st.pop();
                }
                else{
                    while(!st.isEmpty() && PriorityPost(c[i], "a") < PriorityPost(st.top(), "b")){
                        fi = fi + st.pop() + " ";
                    }
                    st.push(c[i]);
                }
            } 
        }
        while (!st.isEmpty()){
            fi = fi + st.pop() + " ";
        }
        return fi;
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
    public static double evalPostFix(String s){
        Stack st = new Stack();
        String delimiter = " ";
        String[] c = s.split(delimiter);
        double x = 0, y = 0, z = 0;
        for (int i = 0; i < c.length; i++) { 
            if (c[i].compareTo("*") != 0 && c[i].compareTo("/") != 0 && c[i].compareTo("-") != 0 && c[i].compareTo("+") != 0 && c[i].compareTo("^") != 0) { 
                z = Double.parseDouble(c[i]);
                st.push(Double.parseDouble(c[i])); 
            } 
            else { 
                y = (Double)st.pop(); 
                x = (Double)st.pop(); 
                st.push(evaluate(x, c[i], y)); 
            } 
        }
        return (Double)st.pop();
    }
}
