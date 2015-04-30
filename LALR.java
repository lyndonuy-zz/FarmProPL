
import java.io.*;
import static java.lang.Character.isDigit;
import static java.lang.Character.*;
import static java.lang.Character.isLetter;
import java.util.*;
public class LALR {
    static Anal scan = new Anal();
    static SyntaxTree parser = new SyntaxTree();
    static SyntaxTreeNode node = new SyntaxTreeNode();
    File f;
    BufferedInputStream tokenReader;
    BufferedInputStream tR;
    FileInputStream s;
    PushbackInputStream ll;
    Token tk = null;
    static Stack<StackNode> stk = new Stack<StackNode>();
    public static void main(String[] args) {
        lexAnal lex = new lexAnal();
        scan = new Anal();
        
    }
    public void reader()throws Exception{
        int p;
        s = new FileInputStream("src/emp1.txt");
        //t = new BufferedInputStream(s);
        tokenReader = new BufferedInputStream(s);
        tR = tokenReader;
        tR.mark(0);
        ll = new PushbackInputStream(tokenReader);
        tokenReader.mark(0);
        scan.getToken();
        p = ll.read();
    }
}
