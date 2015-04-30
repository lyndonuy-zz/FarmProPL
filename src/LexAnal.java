//package farmpro;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class LexAnal {
    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static final String NUMBERS = "0123456789";
    public static final String DECIMALNUMBERS = "." + NUMBERS;
    public static final String ALPHANUMERIC = ALPHABET + NUMBERS;
    
    public static final String INPUTFILE = "SSCLittle.txt";
    
    FileInputStream fis;
    BufferedInputStream bis;
    static PushbackInputStream pis;
    public static long lineNumber = 1;
    public static String formingLexeme = "";
    public static boolean errorFound = false;
    public static String errorMsg = "";
    public static int currentBufferIndex = 0;
    public static LinkedList<Long> WorkingLineNumberBuffer = new LinkedList<>();
    public static LinkedList<Character> inputBuffer = new LinkedList<>();
    public static LinkedList<String> WorkingErrorMessages = new LinkedList<>();
    public static LinkedList<Token> WorkingTokenBuffer = new LinkedList<>();
    public static HashMap<String, Attributes> WorkingSymbolTable = new HashMap();
    public static int globalTokenBufferIndex = 0;
    
    public int numberOfTokens = 0;
    public LinkedList<Long> returnerLineNumber = new LinkedList<>();
    public LinkedList<String> returnerErrorMessages = new LinkedList<>();
    public LinkedList<Token> returnerTokenBuffer = new LinkedList<>();
    public HashMap<String, Attributes> symbolTable = new HashMap();
    
    public LexAnal(String SourceCode) throws FileNotFoundException, IOException{
        fis = new FileInputStream(SourceCode);
        bis = new BufferedInputStream (fis);
        pis = new PushbackInputStream(bis);
        int currentCharASCII;
        while( (currentCharASCII = pis.read() ) != -1) {    // First pass: transfer all symbols to inputBuffer LinkedList for ease
            inputBuffer.add( (char) currentCharASCII );
        }
        pis.close();
        bis.close();
        fis.close();
        
        char currentChar;
        Token foundToken;
        for(currentBufferIndex = 0; isStillNotEOF(); currentBufferIndex++) {
            currentChar = inputBuffer.get(currentBufferIndex);
            updateLineNumber(currentChar);
            foundToken = builder(currentChar);
            if(foundToken != null) { // A lexeme was recognized and a token was formed
                putTokenInBuffer(foundToken);
                formingLexeme = "";
                this.numberOfTokens++;
            }
        }
        this.returnerLineNumber = WorkingLineNumberBuffer;
        this.returnerErrorMessages = WorkingErrorMessages;
        this.symbolTable = WorkingSymbolTable;
        this.returnerTokenBuffer = WorkingTokenBuffer;
    }
    
    public Token getNextToken() {
        globalTokenBufferIndex++;
        return returnerTokenBuffer.get(globalTokenBufferIndex-1);
    }
    public LinkedList buildList() throws Exception{
        return returnerTokenBuffer;
    }
    public LinkedList buildError() throws Exception{
        return returnerLineNumber;
    }
    public long getTokenLineNumber() {  return returnerLineNumber.get(globalTokenBufferIndex-1);    }
    public Map getSymbolTable() {
        return symbolTable;
    }
    public boolean hasError() { return (!this.returnerErrorMessages.isEmpty()); }
    
    @Override
    public String toString() {
        String str = "";
        str += "Symbol Table: \n";
        for(Entry<String,Attributes> entry : this.symbolTable.entrySet()) {
            String key = entry.getKey();
            Attributes attribute = entry.getValue();
            str += "Identifier: " + key + "\t";
            str += "ID Type: "    + attribute.getIDType()     + "\t";
            str += "Data Type: "  + attribute.getDataType()   + "\t";
            str += "Value: "      + attribute.getValue()      + "\n";
        }
        str += "\n\nLexical Analysis: \n";
        int index=0;	// for initialization statement
        for(Token tok : this.returnerTokenBuffer) {
        	// No need for for condition statement since it's a parallel array
        	str += "Line " + this.returnerLineNumber.get(index) + "\t: ";
            str += "Token:\t" + tok.getTokenType()+ "\t";
            str += "Lexeme:\t" + tok.getLexeme();
            str += "\n";
            index++;	// for update statement
        }
        if(!this.returnerErrorMessages.isEmpty()) {
            str += "\n\nError logs: \n";
            for(String errmsg : this.returnerErrorMessages) {
                str += errmsg + "\n";
            }
        }
        return str;
    }
    public static String showCurrentAndNextCharacter() {    return ( "Current character: " + inputBuffer.get(currentBufferIndex) + "\tNext character: " + inputBuffer.get(currentBufferIndex+1) );  }
    public static boolean isStillNotEOF() {    return ( currentBufferIndex < inputBuffer.size() ); }
    public static boolean isStillNotOutOfBounds() { return ( currentBufferIndex+1 != inputBuffer.size() );  }
    public static char peekInputBuffer() {
    	char chr = '\0';
    	if(currentBufferIndex+1 != inputBuffer.size())
    		chr = inputBuffer.get(currentBufferIndex+1);
    	return chr;   
    }
    public static String peekInputBufferK(int lookAheadValue){
        String lookAhead = "";
        for(int tmp_index = currentBufferIndex; lookAheadValue > 0; tmp_index++, lookAheadValue--) {
            lookAhead += inputBuffer.get(tmp_index);
        }
        return lookAhead;
    }
    public static char readNextSymbol(){
        if (isStillNotOutOfBounds())
            currentBufferIndex++;
        return ( inputBuffer.get( currentBufferIndex ) );
    }
    
    public static void unreadConsumedSymbolK(int noOfChars) {
        for(; noOfChars > 0; currentBufferIndex--, noOfChars--) { }
    }
    
    public static long updateLineNumber(char chr){
    	if(chr == '\n'){
    		lineNumber++;
    	}
    	return lineNumber;
    }
    
    public static String bypassSymbolK(int skipValue) {
        String str = "";
        char chr;
        for(; skipValue > 0; skipValue--) {
            chr = readNextSymbol();
        	str += chr;
            updateLineNumber(chr);
        }
        return str;
    }
    
    public static String bypassCurrentLine() {
        boolean newLineFound = false;
        char chr;
        String str = "";
        while(!newLineFound && isStillNotOutOfBounds()) {   // while new line symbol has not yet been found
            if( ( chr = readNextSymbol() ) == '\n'){
            	 newLineFound = true;
            	 updateLineNumber(chr);
            }
            str += chr;
        }
        return str.substring(0, str.length() - 2);
    }
    
    public static String bypassBlockComment() {
        String str = "";
        char chr;
        boolean closerFound = false;
        while( isStillNotOutOfBounds() ) {
            if( ":#".equals(peekInputBufferK(2)) ) {
                closerFound = true;
                bypassSymbolK(2);
                break;  // closer :# already found exit from this loop
            }
            else{
            	chr = readNextSymbol();
            	updateLineNumber(chr);
                str += chr;
            }
        }
        if(!closerFound || !isStillNotOutOfBounds()) {
            errorFound = true;
            errorMsg = "[ERR @line#"+lineNumber+"] Block comment missing ':#' symbol.";
            WorkingErrorMessages.add(errorMsg);
        }
            
        return str.substring(0, str.length()-1);    // removes the symbol colon (:)
    }
    
    public static String readNumericConst(String start_num) {
        String str = start_num;
        String type = "";
        
        while( ( DECIMALNUMBERS.contains( Character.toString( peekInputBuffer() ) ) ) && isStillNotEOF() ){   // Checks if following symbol is decimal number alphabet and if EOF is not yet reached
            str += readNextSymbol();
        }
        if ((Pattern.compile("([0-9])*(\\.)[0-9]*")).matcher(str).find())
            type = "decimal";
        else if ((Pattern.compile("([0-9])*")).matcher(str).find())
            type = "integer";
        if (str.indexOf(".") != str.lastIndexOf(".")) {   // Checks if there are more than one decimal point / dot (.) symbol
            type = "error";
            errorMsg = "[ERR @line#"+lineNumber+"] Invalid decimal constant.";
            WorkingErrorMessages.add(errorMsg);
        }
        return (type + " " + str);
    }
    
    public static String readStringConst() {
        boolean closerFound = false;
        char chr;
        String str = "";
        while( !closerFound && isStillNotEOF() && isStillNotOutOfBounds()){   // while '"' symbol has not yet been found and not EOF
            chr = readNextSymbol();
            if(chr == '@') {
                chr = readNextSymbol();
                if(chr == '"')
                    str += chr;
                else if(chr == 'n')
                    str += '\n';
                else if(chr == 't')
                    str += '\t';
            }
            else if(chr == '"') {
                closerFound = true;
                str += chr;
            }
            else
                str += chr;
        }
        
        int substringIndex = str.length() - 1;
        if(!closerFound) {
            errorFound = true;
            errorMsg = "[ERR @line#"+lineNumber+"] String constant missing '\"' symbol.";
            WorkingErrorMessages.add(errorMsg);
            substringIndex++;
        }
        return str.substring(0, substringIndex);    // removes closing '"' symbol
    }
    
    public static String readVariable() {
        String str = "";
        while( ( ALPHANUMERIC.contains( Character.toString( peekInputBuffer() ) ) ) && isStillNotEOF() ) {
            str += readNextSymbol();
        }
        return str;
    }
    
    public static void putTokenInBuffer(Token tok) {
    	long temp = lineNumber;
    	if(tok.getTokenType() == "line_comment" || tok.getTokenType() == "block_comment")		//Nyker's magic kaya -1 
    		temp--;
    	WorkingLineNumberBuffer.add(temp);
        WorkingTokenBuffer.add(tok);
    }
    
    public static Token builder(char currentChar) throws IOException {
        Token tokFormed = null;
        if(Character.isWhitespace(currentChar)) // disregarding whitespace
            formingLexeme = "";
        else {
            formingLexeme += currentChar ;
            tokFormed = whatKeyword(formingLexeme);
        }
        return tokFormed;
    }
    
    public static Token whatKeyword(String lexeme) {
        Token tokKeyword = null;
        String comment;
        String str_const;
        String num_const;
        switch(lexeme){
            // Constants
            case "YES": case "NO": {tokKeyword = new Token(lexeme, TokenType.bool_const);    break;}
            case "-": {
                if( Character.isWhitespace(peekInputBuffer()) || "dec_const".equals(WorkingTokenBuffer.getLast().getTokenType()) || "int_const".equals(WorkingTokenBuffer.getLast().getTokenType()) ) { 
                    tokKeyword = new Token(lexeme, TokenType.arithas);
                    break;
                }
                else
                    lexeme += bypassSymbolK(1);
            }
            case "0": case "1": case "2": case "3": case "4": case "5": case "6": case "7": case "8": case "9": {
                num_const = readNumericConst(lexeme);
                if(num_const.split(" ")[0] != null){
                    switch (num_const.split(" ")[0]) {
                        case "decimal": {tokKeyword = new Token(num_const.split(" ")[1], TokenType.dec_const);  break;}
                        case "integer": {tokKeyword = new Token(num_const.split(" ")[1], TokenType.int_const);  break;}
                        case "error":   {tokKeyword = new Token(num_const.split(" ")[1], TokenType.error);  break;}
                    }
                }
                break;
            }
            case "\""       : {
                str_const = readStringConst();
                if(errorFound) {
                    tokKeyword = new Token(errorMsg, TokenType.error);    
                    errorFound = false;
                }
                else
                    tokKeyword = new Token(str_const, TokenType.string_const);    
                break;
            }
            // Variables
            case "$"        : {
                String formedVariable = readVariable();
                tokKeyword = new Token(formedVariable, TokenType.identifier);
                // 1st Priority: Check if variable is existing/ was declared (in symbol table)
                if(!WorkingSymbolTable.containsKey(formedVariable)) {   // If non-existing entry
                    String data_type = null;
                    String default_value = null;
                    switch( WorkingTokenBuffer.getLast().getTokenType() ) {
                        case "data_type_bool"   : { data_type = "boolean";  default_value = "NO";   break; }
                        case "data_type_dec"    : { data_type = "decimal";  default_value = "0.0";  break; }
                        case "data_type_int"    : { data_type = "integer";  default_value = "0";    break; }
                        case "data_type_string" : { data_type = "string";   default_value = "null"; break; }
                    }
//                    if(data_type == null) {
//                        tokKeyword = new Token(formedVariable, TokenType.error);
//                        errorMsg = "[ERR @line#"+lineNumber+"] undeclared variable \"" + formedVariable + "\"";
//                        WorkingErrorMessages.add(errorMsg);
//                    }
//                    else
                        WorkingSymbolTable.put(formedVariable, new Attributes("variable", data_type, default_value) );   // then put in symbol table
                    // System.out.println("New variable: " + formedVariable);
                }
                break;
            }
            // Comments
            case "##"       : {tokKeyword = new Token(bypassCurrentLine(), TokenType.line_comment);     break;}
            case "#:"       : {
                comment = bypassBlockComment();
                if(errorFound) {
                    tokKeyword = new Token(errorMsg, TokenType.error);    
                    errorFound = false;
                }
                else
                    tokKeyword = new Token(comment, TokenType.block_comment);    
                break;
            }
            // Symbols and operators
            case "{"        :   {tokKeyword = new Token(lexeme, TokenType.lcurl);    break;}
            case "}"        :   {tokKeyword = new Token(lexeme, TokenType.rcurl);    break;}
            case "("        :   {tokKeyword = new Token(lexeme, TokenType.lpar);    break;}
            case ")"        :   {tokKeyword = new Token(lexeme, TokenType.rpar);    break;}
            case "+"        :   {tokKeyword = new Token(lexeme, TokenType.arithas);    break;}
            case "*"        :   {tokKeyword = new Token(lexeme, TokenType.arithmd);    break;}
            case "/"        :   {tokKeyword = new Token(lexeme, TokenType.arithmd);    break;}
            case "GROW"     :   {tokKeyword = new Token(lexeme, TokenType.increment);    break;}
            case "TRIM"     :   {tokKeyword = new Token(lexeme, TokenType.decrement);    break;}
            case "NOT"      :   {tokKeyword = new Token(lexeme, TokenType.logn_optr);    break;}
            case "AND"      :   {tokKeyword = new Token(lexeme, TokenType.logao_optr);    break;}
            case "OR"       :   {tokKeyword = new Token(lexeme, TokenType.logao_optr);    break;}
            case ">"        :   {
                if(peekInputBuffer() == '=') {
                    tokKeyword = new Token(">=", TokenType.relop);    
                    bypassSymbolK(1);
                }
                else
                    tokKeyword = new Token(lexeme, TokenType.relop);    
                break;
            }
            case "<"        :   {
                if(peekInputBuffer() == '=') {
                    tokKeyword = new Token("<=", TokenType.relop);    
                    bypassSymbolK(1);
                }
                else
                    tokKeyword = new Token(lexeme, TokenType.relop);    
                break;
            }
            case "IS"       :   {
                if( "SEQUAL".equals( peekInputBufferK(6) ) ) { // formedLexeme = "ISEQUAL"
                    tokKeyword = new Token("ISEQUAL", TokenType.relop);    
                    bypassSymbolK(5);
                }
                else if( "SNOTEQUAL".equals( peekInputBufferK(9) ) ) {  // formedLexeme = "ISNOTEQUAL"
                    tokKeyword = new Token("ISNOTEQUAL", TokenType.relop);  
                    bypassSymbolK(8);
                }
                else    // formedLexeme = "IS"
                    tokKeyword = new Token(lexeme, TokenType.assign_op);
                break;
            }
            // Keywords
            case "SUNRISE"  :   {tokKeyword = new Token(lexeme, TokenType.start);    break;}
            case "SUNSET"   :   {tokKeyword = new Token(lexeme, TokenType.end);    break;}
            case "FARMIN"   :   {tokKeyword = new Token(lexeme, TokenType.start_main);    break;}
            case "FARMOUT"  :   {tokKeyword = new Token(lexeme, TokenType.end_main);    break;}
            case "PLANTBOOL":   {tokKeyword = new Token(lexeme, TokenType.data_type_bool);    break;}
            case "PLANTDEC" :   {tokKeyword = new Token(lexeme, TokenType.data_type_dec);    break;}
            case "PLANTINT" :   {tokKeyword = new Token(lexeme, TokenType.data_type_int);    break;}
            case "PLANTSTRING": {tokKeyword = new Token(lexeme, TokenType.data_type_string);    break;}
            case "HARVEST"  :   {tokKeyword = new Token(lexeme, TokenType.output_type);    break;}
            case "FERTILIZEBOOL": case "FERTILIZEDEC": case "FERTILIZEINT": case "FERTILIZESTRING": {
                tokKeyword = new Token(lexeme, TokenType.input_type);    break;
            }
            case ";"        :   {tokKeyword = new Token(lexeme, TokenType.terminator);    break;}
            case "PLOW"     :   {tokKeyword = new Token(lexeme, TokenType.loop_do);    break;}
            case "WHILE"    :   {tokKeyword = new Token(lexeme, TokenType.loop_while);    break;}
            case "CHANGE"   :   {tokKeyword = new Token(lexeme, TokenType.switcher);    break;}
            case "SEASON"   :   {tokKeyword = new Token(lexeme, TokenType.caser);    break;}
            case ":"        :   {tokKeyword = new Token(lexeme, TokenType.colon);    break;}
            case "DEFAULT"  :   {tokKeyword = new Token(lexeme, TokenType.defaulter);    break;}
            case "ENDSEASON":   {tokKeyword = new Token(lexeme, TokenType.breaker);    break;}
            case "IF"       :   {tokKeyword = new Token(lexeme, TokenType.cond_if);    break;}
            case "THEN"     :   {tokKeyword = new Token(lexeme, TokenType.cond_then);    break;}
            case "ENDIF"    :   {tokKeyword = new Token(lexeme, TokenType.cond_endif);    break;}
            case "ELSE"     :   {
                if( "EIF".equals( peekInputBufferK(3) ) ) { // formedLexeme = "ELSEIF"
                    tokKeyword = new Token("ELSEIF", TokenType.cond_elseif);    
                    bypassSymbolK(2);
                }
                else 
                    tokKeyword = new Token(lexeme, TokenType.cond_else);    
                break;
            }
            // For others
            default : {
                if(!WorkingTokenBuffer.isEmpty()) { // if there are already recognized tokens
                    // Program name
                    switch(WorkingTokenBuffer.getLast().getTokenType()) {   // Identify if current lexeme is an identifier token by checking the previous token (in the pis)
                        case "start_main" : {
                            if(Character.isWhitespace(peekInputBuffer())) {
                                tokKeyword = new Token(lexeme, TokenType.identifier);
                                WorkingSymbolTable.put(lexeme, new Attributes("program_name") );
                            }
                            break;
                        }
                    }
                }
                // Error: 
                if( ( peekInputBuffer()=='\0' || Character.isWhitespace(peekInputBuffer()) ) && tokKeyword == null ) {
                    tokKeyword = new Token(lexeme, TokenType.error);
                    errorMsg = "[ERR @line#"+lineNumber+"] Invalid keyword";
                    WorkingErrorMessages.add(errorMsg);
                }
            }
        }
        return tokKeyword;
    }
    public static void main(String[] args) throws IOException {
        LexAnal lex = new LexAnal(INPUTFILE);
        // Checker only
//		System.out.println(lex.toString());
        System.out.println("Symbol Table: ");
        for(Entry<String,Attributes> entry : lex.symbolTable.entrySet()) {
            String key = entry.getKey();
            Attributes attribute = entry.getValue();
            System.out.print("Identifier: " + key + "\t");
            System.out.print("ID Type: "    + attribute.getIDType()     + "\t");
            System.out.print("Data Type: "  + attribute.getDataType()   + "\t");
            System.out.print("Value: "      + attribute.getValue()      + "\n");
        }
        System.out.println("\nLexical Analysis:");
        // Dummy parser asks for a token and prints it.
        Token tok;
        for(int ctr = lex.numberOfTokens; ctr > 0; ctr--) {
            tok = lex.getNextToken();
            System.out.print("Line " + lex.getTokenLineNumber() + "\t: ");
            System.out.print("Token:\t" + tok.getTokenType() + "\t");
            System.out.println("Lexeme:\t" + tok.getLexeme()); 
        }
        if(!lex.returnerErrorMessages.isEmpty()) {
            System.out.println("\n\nError logs: ");
            for(String str : lex.returnerErrorMessages) {
                System.out.println(str);
            }
        }
        
        System.exit(0);
    }
}
