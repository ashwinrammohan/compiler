package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scanner.ScanErrorException;
import scanner.Scanner;
import scanner.Token;
import ast.Assignment;
import ast.BinOp;
import ast.Block;
import ast.Condition;
import ast.Expression;
import ast.If;
import ast.Number;
import ast.ProcedureCall;
import ast.ProcedureDeclaration;
import ast.Program;
import ast.Statement;
import ast.Variable;
import ast.While;
import ast.Writeln;

/**
 * The Parser is a simple parser which executes Pascal-like phrases
 * as it parses them.
 * 
 * @author Ashwin Rammohan
 * @version October 12, 2017
 *
 */
public class Parser
{
	private Scanner sc;
	private Token curr;
	
	/**
	 * Creates a new Parser that takes in a Scanner. The parser calls the scanner's nextToken()
	 * method and assigns the returned token to an instance variable. 
	 * 
	 * @param scanner the Scanner that is taken in
	 * @throws ScanErrorException if the Scanner throws the exception
	 */
	public Parser(Scanner scanner) throws ScanErrorException
	{
		sc = scanner;
		Token t = scanner.nextToken();
		curr = t;
		while(curr == null)
			curr = sc.nextToken();
	}
	

	/**
	 * Checks whether the string to be eaten matches the current token's value. Then, 
	 * the instance variable curr is assigned to the next token returned by the Scanner's
	 * nextToken() method.
	 * 
	 * @param s the value of the Token that is to be eaten
	 * @throws ScanErrorException if the scanner throws this exception
	 * @throws IllegalArgumentException if the String that is to be eaten does not match
	 * 								    the current token's value
	 */
	private void eat(String s) throws ScanErrorException, IllegalArgumentException
	{
		if (s.equals(curr.getValue()))
		{
			curr = sc.nextToken();
			while(curr == null)
			{
				curr = sc.nextToken();
			}
		}
		else
		{
			throw new IllegalArgumentException("Expected token: " + curr.getValue() + ". Token received: " + s);
		}
	}
	
	/**
	 * Parses a NUMBER token and returns a Number object containing
	 * the integer value of the token
	 * 
	 * @precondition current token is an integer
	 * @postcondition the number token has been eaten
	 * @return a Number object with the token's integer value
	 * @throws ScanErrorException if the methods in Scanner throw
	 * 							  this exception
	 */
	private Number parseNumber() throws ScanErrorException
	{
		if (curr.getTokenType() != Scanner.TOKEN_TYPE.NUMBER)
		{
			throw new ScanErrorException("Number token not found!");
		}
		else
		{
			int num = Integer.parseInt(curr.getValue());
			eat(curr.getValue());
			return new Number(num);
		}	
	}
	
	/**
	 * Parses a Program object by creating a list of ProcedureDeclarations
	 * containing all of the declarations in the program. The method then
	 * parses the Statement at the end of the input stream and creates a
	 * new Program object with this list of ProcedureDeclarations and the Statement.
	 * 
	 * @return the newly created Program object
	 * @throws ScanErrorException if the methods in Scanner throw
	 * 							  this exception
	 */
	public Program parseProgram() throws ScanErrorException
	{
		List<ProcedureDeclaration> procedures = new ArrayList<ProcedureDeclaration>();
		List<String> variables = new ArrayList<String>();
		if (curr.getValue().equals("VAR"))
		{
			eat("VAR");
			while (curr.getTokenType().equals(Scanner.TOKEN_TYPE.IDENTIFIER) || curr.getValue().equals(","))
			{
				if (curr.getTokenType().equals(Scanner.TOKEN_TYPE.IDENTIFIER))
				{
					variables.add(curr.getValue());
				}
				eat(curr.getValue());
			}
			eat(";");
		}
		while (curr.getValue().equals("PROCEDURE"))
		{
			ProcedureDeclaration dec = parseProcedureDeclaration();
			procedures.add(dec);
		}
		Statement stmt = parseStatement();
		Program p = new Program(variables, procedures, stmt);
		return p;
	}
	
	/**
	 * Parses a ProcedureDeclaration. First, this method stores the procedure's name.
	 * Then, it iterates through all of the parameters in the ProcedureDeclaration,
	 * adding each one to an ArrayList. The method then does the same for all of
	 * the ProcedureDeclaration's local variables. Finally, the method parses the 
	 * statement that is to be executed when the procedure is called.
	 * 
	 * @return a new ProcedureDeclaration with the appropriate name, Statement,
	 * 		   list of parameters, and list of local variables
	 * @throws ScanErrorException if the methods in Scanner throw
	 * 							  this exception
	 */
	public ProcedureDeclaration parseProcedureDeclaration() throws ScanErrorException
	{
		List<Expression> params = new ArrayList<Expression>();
		eat("PROCEDURE");
		String procedureName = curr.getValue();
		eat(procedureName);
		eat("(");
		if (!curr.getValue().equals(")"))
		{
			while (!curr.getValue().equals(")"))
			{
				if (curr.getValue().equals(","))
				{
					eat(",");
				}
				params.add(parseExpression());
			}
		}
		eat(")");
		eat(";");
		List<Expression> locals = new ArrayList<Expression>();
		if (curr.getValue().equals("VAR"))
		{
			eat("VAR");
			while (curr.getTokenType().equals(Scanner.TOKEN_TYPE.IDENTIFIER) || curr.getValue().equals(","))
			{
				if (curr.getTokenType().equals(Scanner.TOKEN_TYPE.IDENTIFIER))
				{
					locals.add(parseExpression());
				}
				if (curr.getValue().equals(","))
				{
					eat(",");
				}
			}
			eat(";");
		}
		Statement stmt = parseStatement();
		return new ProcedureDeclaration(procedureName, stmt, params, locals);
		
	}
	/**
	 * Parses one statement in the Pascal-like program and returns a Statement
	 * object that represents it.
	 * 
	 * @return a Statement object representing the Statement that was parsed
	 * @throws ScanErrorException if the methods in Scanner throw
	 * 							  this exception
	 */
	public Statement parseStatement() throws ScanErrorException
	{
		if (curr.getValue().equals("BEGIN"))
		{
			eat("BEGIN");
			List<Statement> stmts = new ArrayList<Statement>();
			while (!curr.getValue().equals("END"))
			{
				stmts.add(parseStatement());
			}
			Block b = new Block(stmts);
			eat("END");
			eat(";");
			return b;
		}
		else if (curr.getTokenType().equals(Scanner.TOKEN_TYPE.IDENTIFIER))
		{
			String varName = curr.getValue();
			eat(varName);
			eat(":=");
			Assignment a = new Assignment(varName, parseExpression());
			eat(";");
			return a;
		}
		else if (curr.getValue().equals("WRITELN"))
		{
			eat("WRITELN");
			eat("(");
			Expression exp = parseExpression();
			eat(")");
			eat(";");
			return new Writeln(exp);
		}
		else if (curr.getValue().equals("READLN"))
		{
			eat("READLN");
			eat("(");
			String var = curr.getValue();
			Scanner input = new Scanner(System.in);
			Number num = new Number(Integer.parseInt(input.nextToken().getValue()));
			eat(curr.getValue());
			eat(")");
			eat(";");
			return new Assignment(var, num);
		}
		else if (curr.getValue().equals("IF"))
		{
			return parseIf();
		}
		else if (curr.getValue().equals("WHILE"))
		{
			return parseWhile();
		}
		return null;
	}

	/**
	 * Parses the individual factors in an expression. Factors are terms that can
	 * be multiplied or divided by other terms
	 * 
	 * @precondition the currentChar is inside of the WRITELN() expression
	 * @return an Expression representing the parsed factor
	 * @throws ScanErrorException if the methods from the Scanner class throw this exception 
	 */
	public Expression parseFactor() throws ScanErrorException
	{
		if (curr.getTokenType().equals(Scanner.TOKEN_TYPE.IDENTIFIER))
		{
			String name = curr.getValue();
			eat(name);
			if (curr.getValue().equals("("))
			{
				List<Expression> params = new ArrayList<Expression>();
				eat("(");
				if (!curr.getValue().equals(")"))
				{
					while (!curr.getValue().equals(")"))
					{
						if (curr.getValue().equals(","))
						{
							eat(",");
						}
						params.add(parseExpression());
					}
				}
				eat(")");
				return new ProcedureCall(name, params);
			}
			return new Variable(name);
		}
		if (curr.getTokenType().equals(Scanner.TOKEN_TYPE.NUMBER))
		{
			return parseNumber();
		}
		else if (curr.getValue().equals("("))
		{
			eat("(");
			Expression exp = parseExpression();
			eat(")");
			return exp;
		}
		else if (curr.getValue().equals("-"))
		{
			eat(curr.getValue());
			BinOp negate = new BinOp("-", new Number(0), new Number(1));
			return new BinOp("*", negate, parseFactor());
		}
		return new Number(0);
	}
	
	/**
	 * Parses terms in the input using the following grammar that has 
	 * been left-factored to prevent the program from entering an 
	 * infinite while loop:
	 * 
	 * statement -> WRITELN(term);
	 * term -> factor whileterm
	 * whileterm -> *factor whileterm | /factor whileterm | epsilon (nothing)
	 * factor -> (term) | -factor | num
	 * 
	 * 
	 * @return an Expression representing the term that was parsed
	 * @throws ScanErrorException if an exception is thrown inside parseNumber
	 * 							  (when no number token is found)
	 */
	public Expression parseTerm() throws ScanErrorException
	{
		Expression factor = parseFactor();
		String val = curr.getValue();
		Number num = new Number(0);
		Expression binop = factor;
		while (val.equals("*") || val.equals("/") || val.equals("mod"))
		{
			if (val.equals("*"))
			{
				eat(curr.getValue());
				binop = new BinOp("*", binop, parseFactor());
			}
			else if (val.equals("/"))
			{
				eat(curr.getValue());
				binop = new BinOp("/", binop, parseFactor());
			}
			else if (val.equals("mod"))
			{
				eat(curr.getValue());
				binop = new BinOp("mod", binop, parseFactor());
			}
			val = curr.getValue();
		}
		return binop;
	}
	
	/**
	 * Parses a single expression within a statement, and returns
	 * an Expression object representing this expression.
	 * 
	 * @return an Expression object representing this expression
	 * @throws ScanErrorException if the methods from the Scanner class
	 * 							  throw this exception
	 */
	public Expression parseExpression() throws ScanErrorException
	{
		Expression term = parseTerm();
		String val = curr.getValue();
		Expression binop = term;
		while (val.equals("+") || val.equals("-"))
		{
			if (val.equals("+"))
			{
				eat(curr.getValue());
				binop = new BinOp("+", binop, parseTerm());
			}
			else if (val.equals("-"))
			{
				eat(curr.getValue());
				binop = new BinOp("-", binop, parseTerm());
			}
			val = curr.getValue();
		}
		return binop;
	}
	
	/**
	 * Parses an If statement, identifying the condition, and the statements
	 * that are supposed to be executed if the condition is true.
	 * 
	 * @return an If object (a type of Statement) representing
	 * 		   the parsed If statement
	 * @throws ScanErrorException if the methods from the Scanner class
	 * 						      throw this exception
	 */
	public If parseIf() throws ScanErrorException
	{
		eat("IF");
		Condition cond = parseCondition();
		eat("THEN");
		Statement stmt = parseStatement();
		return new If(cond, stmt);
	}
	
	/**
	 * Parses a While loop, identifying the condition, and the statements
	 * that are supposed to be executed if the condition is true.
	 * 
	 * @return an While object (a type of Statement) representing
	 * 		   the parsed While loop
	 * @throws ScanErrorException if the methods from the Scanner class
	 * 						      throw this exception
	 */
	public While parseWhile() throws ScanErrorException
	{
		eat("WHILE");
		Condition cond = parseCondition();
		eat("DO");
		Statement stmt = parseStatement();
		return new While(cond, stmt);
	}
	
	/**
	 * Parses a condition, identifying the first expression,
	 * the relational operator, and the second expression.
	 * 
	 * @return a Condition object with the parsed Expressions
	 * and the relational operator
	 * @throws ScanErrorException if the methods from the Scanner class
	 * 						      throw this exception
	 */
	public Condition parseCondition() throws ScanErrorException
	{
		Expression exp1 = parseExpression();
		Token relop = curr;
		eat(curr.getValue());
		Expression exp2 = parseExpression();
		return new Condition(exp1, relop, exp2);	
	}
}
