package scanner;
import java.io.*;

import scanner.Scanner.TOKEN_TYPE;

/**
 * Scanner is a simple scanner for Compilers and Interpreters (2014-2015) lab exercise 1
 * @author Ashwin Rammohan
 * @version October 12, 2017
 *  
 * Usage: the Scanner is used to parse an input stream and identify individual lexemes.
 * It can be used by creating a Scanner object that takes in an InputStream object,
 * and then repeatedly calling the Scanner's nextToken() method in order to 
 * list all of the lexemes in the input.
 *
 */
public class Scanner
{
    private BufferedReader in;
    private char currentChar;
    private boolean eof;
 // define symbolic constants for each type of token
 	public static enum TOKEN_TYPE{IDENTIFIER, KEYWORD, NUMBER, OPERAND, 
 		DELIMETER, END_OF_FILE, RELOP, COMMENT};
    /**
     * Scanner constructor for construction of a scanner that 
     * uses an InputStream object for input.  
     * Usage: 
     * FileInputStream inStream = new FileInputStream(new File(<file name>);
     * Scanner lex = new Scanner(inStream);
     * @param inStream the input stream to use
     */
    public Scanner(InputStream inStream)
    {
        in = new BufferedReader(new InputStreamReader(inStream));
        eof = false;
        getNextChar();
    }
    /**
     * The getNextChar method attempts to get the next character from the input
     * stream.  It sets the endOfFile flag true if the end of file is reached on
     * the input stream.  Otherwise, it reads the next character from the stream
     * and converts it to a Java String object.
     * postcondition: The input stream is advanced one character if it is not at
     * end of file and the currentChar instance field is set to the String 
     * representation of the character read from the input stream.  The flag
     * endOfFile is set true if the input stream is exhausted.
     */
    private void getNextChar()
    {
        try
        {
            int inp = in.read();
            if(inp == -1) 
                eof = true;
            currentChar = (char) inp;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    /**
     * Advances the input stream by one character by calling the getNextChar() method.
     * If the expected value of the current character does not match with the actual value,
     * the method throws a ScanErrorException.
     * 
     * @param c a char value representing the expected value of the current character.
     * @throws ScanErrorException if the value of c differs from the value of the
     * 							  current character.
     */
    private void eat(char c) throws ScanErrorException
    {
    	try
    	{
    		if (c == currentChar)
        	{
        		getNextChar();
        	}
        	else
        	{
        		throw new ScanErrorException("Illegal character - expected " + currentChar + "and found " + c);
        	}
    	}
    	catch(ScanErrorException s)
    	{
    		s.printStackTrace();
    		System.exit(-1);
    	}
    }
    /**
     * Checks whether the scanner should keep reading. If the scanner is at the end
     * of the file, then it should stop reading.
     * 
     * @return true if the scanner has not reached the end of the file;
     * 		   false otherwise
     */
    public boolean hasNext()
    {
    	return !(eof);
    }
    
    /**
     * Checks whether the character is a digit by comparing its
     * integer value to 0 and 9.
     * 
     * @param c the character being examined.
     * @return whether the character is a digit or not.
     */
    public static boolean isDigit(char c)
    {
    	return c >= '0' && c <= '9';
    }
    
    /**
     * Checks whether the character is a letter by comparing its
     * value to 'A' and 'Z' as well as 'a' and 'z'.
     * 
     * @param c the character being examined.
     * @return whether the character is a letter or not.
     */
    public static boolean isLetter(char c)
    {
    	return ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'));
    }
    
    /**
     * Checks whether the character is white space seeing
     * whether it's a space, a tab, a carriage return, 
     * or a new line.
     * 
     * @param c the character being examined.
     * @return whether the character is white space or not.
     */
    public static boolean isWhiteSpace(char c)
    {
    	return (c == ' ' || c == '\t' || c == '\r' || c == '\n');
    }
    
    /**
     * Scans the current character and if it is a digit, the scanner tries to parse
     * a number. It keeps scanning until the current character is no longer a digit. 
     * Then, it returns a Token of type NUMBER containing 
     * the series of digits that were scanned. 
     * 
     * @return a Token representing the lexeme found in the input stream
     * @throws ScanErrorException if no lexeme in the form of a number can be identified
     */
    private Token scanNumber() throws ScanErrorException
    {
    	String num = "";
    	try
    	{
	    	while (isDigit(currentChar))
	    	{
	    		num += currentChar;
	    		eat(currentChar);
	    	}
    	}
    	catch(ScanErrorException s)
    	{
    		s.printStackTrace();
    		System.exit(-1);
    	}
    	return  new Token(num,TOKEN_TYPE.NUMBER);
    }
    
    /**
     * Scans the current character and if it is a letter, the scanner tries to parse
     * an identifier. It keeps scanning until the current 
     * character is neither a letter nor a digit.
     * Then, it returns a Token with type IDENTIFIER containing the series 
     * of characters that were scanned. 
     * 
     * @return a Token representing the lexeme found in the input stream
     * @throws ScanErrorException if no lexeme in the form of a identifier can be identified
     */
    private Token scanIdentifier() throws ScanErrorException
    {
    	String id = "";
    	try{
	    	while (isDigit(currentChar) || isLetter(currentChar))
	    	{
	    		id += currentChar;
	    		eat(currentChar);
	    	}
	    	if (id.equals("PROCEDURE") || id.equals("VAR") || id.equals("BEGIN")
	    		|| id.equals("WHILE") || id.equals("END") || id.equals("WRITELN") || id.equals("READLN")
	    		|| id.equals("IF"))
	    	{
	    		Token t = new Token(id, TOKEN_TYPE.KEYWORD);
	    		return t;
	    	}
    	}
    	catch(ScanErrorException s)
    	{
    		s.printStackTrace();
    		System.exit(-1);
    	}
    	return new Token(id, TOKEN_TYPE.IDENTIFIER);
    }
    
    /**
     * Scans the current character and if it is one of the operand symbols, the scanner returns a Token
     * of type OPERAND containing the operand. If the operand is ":=", the scanner checks the value
     * of the current character to be ':', and then verifies that the next one is ':' before
     * creating a new token.
     * 
     * @return a Token representing the lexeme found in the input stream
     * @throws ScanErrorException if no lexeme in the form of an operand can be identified
     */
    private Token scanOperand() throws ScanErrorException
    {
    	String operand = "";
    	Token t = new Token(operand, TOKEN_TYPE.OPERAND);
    	try
    	{
	    	if (currentChar == ':' || currentChar == '<' || currentChar == '>')
	    	{
	    		String specOp = "";
	    		specOp += currentChar;
	    		eat(currentChar);
	    		specOp += currentChar;
	    		eat('=');
	    		t = new Token(specOp, TOKEN_TYPE.OPERAND);
	    	}
	    	else
	    	{
	    		operand += currentChar;
	    		eat(currentChar);
	    		t = new Token(operand, TOKEN_TYPE.OPERAND);
	    	}
    	}
    	catch(ScanErrorException s)
    	{
    		s.printStackTrace();
    		System.exit(-1);
    	}
    	return t;
    }
    
    /**
     * Checks whether a certain character is an operand.
     * @param c the character that is being examined
     * @return a boolean representing whether the character is an operand or not.
     */
    private boolean checkIfOperand(char c)
    {
    	return (currentChar == '=' || currentChar == '+' || currentChar == '-' || currentChar == '*' || currentChar == '/'
    			|| currentChar == '%' || currentChar == '(' || currentChar == ')' || currentChar == ':');
    }
    
    /**
     * Scans the current character and if checkIfRelopChar() returns true, the scanner returns a Token
     * of type RELOP containing the relational operator. 
     * 
     * @return a Token representing the lexeme found in the input stream
     * @throws ScanErrorException if no lexeme in the form of an relop can be identified
     */
    private Token scanRelop() throws ScanErrorException
    {
    	String specOp = "";
    	try
    	{
	    	if (currentChar == '=')
	    	{
	    		specOp += currentChar;
	    		eat(currentChar);
	    	}
	    	else if (currentChar == '>' || currentChar == '<')
			{
				specOp += currentChar;
				if (currentChar == '<')
				{
					eat(currentChar);
					if (currentChar == '>')
					{
						specOp += currentChar;
						eat('>');
					}
					else if (currentChar == '=')
					{
						specOp += currentChar;
						eat('=');
					}
				}
				else
				{
					eat(currentChar);
					if (currentChar == '=')
					{
						specOp += currentChar;
						eat('=');
					}
				}
			}
    	}
    	catch(ScanErrorException s)
    	{
    		s.printStackTrace();
    		System.exit(-1);
    	}
		return new Token(specOp, TOKEN_TYPE.RELOP);
    }
    
    /**
     * Checks if the current character is the first part of a relational
     * operator.
     * 
     * @param c the character being checked
     * @return whether the character can be used to start a relational
     * 		   operator
     */
    private boolean checkIfRelopChar(char c)
    {
    	return c == '>' || c == '<' || c == '=';
    }
    /**
     * Scans the current character and if it is a semicolon, the scanner returns a token of type
     * END_OF_LINE with the value as the semicolon.
     * @precondition the current character is either a semicolon or a comma
     * @return a Token representing the lexeme found in the input stream
     * @throws ScanErrorException if no semicolon is found in the current character
     */
    private Token scanDelimeter() throws ScanErrorException
    {
    	String semi = "";
    	Token t = new Token(semi, TOKEN_TYPE.DELIMETER);
    	try
    	{
    		semi += currentChar;
    		eat(currentChar);
    		t = new Token(semi, TOKEN_TYPE.DELIMETER);
    	}
    	catch(ScanErrorException s)
    	{
    		s.printStackTrace();
    		System.exit(-1);
    	}
    	return t;
    }
    /**
     * Skips any leading white space and then proceeds to examine the value of the current
     * character. Based on the character's value, the method will call the appropriate methods
     * to return a token representing the next lexeme in the input stream.
     * If the Scanner has reached the end of the input stream when this method is called,
     * an END_OF_FILE token will be returned, with the value "END". 
     * 
     * @return and END_OF_FILE token if the scanner has reached the end of the input stream; otherwise, return
     * the next lexeme in the input stream.
     * 
     * @throws ScanErrorException if an exception is encountered in any of the scanning methods that
     * are called
     */
    public Token nextToken() throws ScanErrorException
    {
    	Token t = new Token("", TOKEN_TYPE.END_OF_FILE);
    	try
    	{
	    	String comment = "";
	    	while (isWhiteSpace(currentChar))
	    	{
	    		eat(currentChar);
	    	}
	    	if (currentChar == '/')
	    	{
	    		eat(currentChar);
	    		if (currentChar == '/')
	    		{
	    			while (hasNext() && currentChar != '\n')
	    			{
	    				eat(currentChar);
	    			}
	    			eat(currentChar);
	    			return null;
	    		}
	    		else if (currentChar == '*')
	    		{
	    			comment = "";
	    			eat(currentChar);
	    			boolean done = false;
	    			while (!done)
	    			{
	    				if (currentChar == '*')
	    				{
	    					eat(currentChar);
	    					if (currentChar == '/')
	    					{
	    						eat(currentChar);
	    						done = true;
	    					}
	    				}
	    				else
	    				{
	    					eat(currentChar);
	    				}
	    			}
	    			return null;
	    		}
	    	}
	    	if (currentChar != '/' && comment.equals("/"))
	    	{
	    		t = new Token(comment, TOKEN_TYPE.OPERAND);
	    	}
	    	while (isWhiteSpace(currentChar))
	    	{
	    		eat(currentChar);
	    	}
	    	if (isDigit(currentChar))
	    	{
	    		return scanNumber();
	    	}
	    	else if (isLetter(currentChar))
	    	{
	    		return scanIdentifier();
	    	}
	    	else if (checkIfOperand(currentChar))
	    	{
	    		return scanOperand();
	    	}
	    	else if (checkIfRelopChar(currentChar))
	    	{
	    		return scanRelop();
	    	}
	    	else if (currentChar == ';' || currentChar == ',')
	    	{
	    		return scanDelimeter();
	    	}
	    	if (currentChar == '.' || eof)
	    	{
	    		eof = true;
	    	}
	    	else
	    	{
	    		throw new ScanErrorException("Cannot parse this lexeme: " + currentChar);
	    	}
    	}
    	catch(ScanErrorException s)
    	{
    		s.printStackTrace();
    		System.exit(-1);
    	}
    	return t;   	
    }   
}
