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
import java.util.*;
import java.util.regex.*;
public class Reader {
    static String File = "";
    
    public void file() throws Exception{
        Scanner filename = new Scanner(new FileReader("emp.txt"));
        ArrayList<String> codeLine = new ArrayList();
        String lineOfCode;
        while(filename.hasNext()){
            Pattern p = Pattern.compile("\n");
            filename.useDelimiter(p);
            lineOfCode = filename.next();
            codeLine.add(lineOfCode);
        }
    }
}
