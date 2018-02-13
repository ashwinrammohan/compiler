package ast;

import java.util.List;
import java.util.Map;
import java.util.Set;

import emitter.Emitter;
import environment.Environment;
import scanner.Token;

/**
 * An Evaluator evaluates Statements and Expressions
 * by calculating their numerical values.
 * 
 * @author Ashwin Rammohan
 * @version October 26, 2017
 *
 */
public class Evaluator 
{
	private Environment env;
	
	/**
	 * Creates a new Evaluator with a certain Environment.
	 * 
	 * @param environment the environment which contains the
	 * 	      variables that may need to be evaluated
	 */
	public Evaluator(Environment environment)
	{
		env = environment;
	}
	
	/**
	 * Compiles a Program and writes the corresponding MIPS code
	 * to a file. The Program is compiled by first emitting the 
	 * necessary variable declarations in the .data section. The variables
	 * are obtained from the Program's list of variables. Then, the
	 * main statement in the Program is compiled and this MIPS output
	 * is placed in the .text section. After the statement has been compiled,
	 * a new line is printed and the program is halted.
	 * 
	 * @param fileName the location of the output file with the MIPS code
	 * @param pr the Program to be compiled
	 */
	public void compile(String fileName, Program pr)
	{
		Emitter e = new Emitter(fileName);
		e.emit(".data");
		e.emit("newline: .asciiz \"\\n\" ");
		for (String s: pr.getVariables())
		{
			e.emit(s + ": .word 0");
		}
		e.emit(".text");
		e.emit(".globl main");
		e.emit("main:");
		pr.compile(e);
		e.emit("la $a0, newline");
		e.emit("li $v0, 4");
		e.emit("syscall");
		e.emit("li $v0, 10");
		e.emit("syscall #halt");
		
		for (ProcedureDeclaration pd: pr.getProcedures())
		{
			pd.compile(e);
		}
	}
	
	/**
	 * Executes a Program by executing each of its ProcedureDeclarations
	 * (if any) from its list of ProcedureDeclarations and then finally
	 * executes the Statement at the end of the Program.
	 * 
	 * @param program the Program to be executed
	 * @param env the Environment where the Program is supposed to be executed
	 */
	public void exec(Program program, Environment env)
	{
		List<ProcedureDeclaration> procedures = program.getProcedures();
		for (ProcedureDeclaration pd: procedures)
		{
			exec(pd, env);
		}
		Statement stmt = program.getStatement();
		exec(stmt, env);
	}
	
	
	/**
	 * This is the general method for executing statements. This method
	 * determines what kind of Statement the statement is, and based on that,
	 * it will call the appropriate exec method. 
	 * 
	 * @param stmt the Statement that should be executed
	 * @param env the Environment where the execution happens
	 */
	public void exec(Statement stmt, Environment env)
	{
		if (stmt instanceof Writeln)
		{
			exec((Writeln)stmt, env);
		}
		else if (stmt instanceof Assignment)
		{
			exec((Assignment)stmt, env);
		}
		else if (stmt instanceof Block)
		{
			exec((Block)stmt, env);
		}
		else if (stmt instanceof If)
		{
			exec((If)stmt, env);
		}
		else if (stmt instanceof While)
		{
			exec((While)stmt, env);
		}
		else if (stmt instanceof ProcedureDeclaration)
		{
			exec((ProcedureDeclaration)stmt, env);
		}
	}
	
	/**
	 * Executes a WRITELN statement by printing out the numerical value
	 * of the expression within the statement.
	 * 
	 * @param stmt the WRITELN statement
	 * @param env the Environment where the execution happens
	 */
	public void exec(Writeln stmt, Environment env)
	{
		System.out.println(eval(stmt.getExpression(), env));
	}

	/**
	 * Executes an ASSIGNMENT statement by modifying the value associated
	 * with a certain variable in the Environment's Map. If the variable does
	 * not already exist in the Environment's Map, then a new variable with
	 * the variable name is added to the map, with its value being
	 * the value of the expression in the ASSIGNMENT.
	 * 
	 * @param stmt the ASSIGNMENT statement
	 * @param env the Environment where the execution happens
	 */
	public void exec(Assignment a, Environment env)
	{
		Expression exp = a.getExpression();
		String varName = a.getVar();
		env.setVariable(varName, eval(exp,env));
	}
	
	/**
	 * Executes a BLOCK by going through each of the statements in the BLOCK
	 * and executing each of them. 
	 * 
	 * @param b the BLOCK containing all the statements
	 * @param env the Environment where the execution of the BLOCK happens
	 */
	public void exec(Block b, Environment env)
	{
		List<Statement> stmts = b.getStatements();
		
		for (Statement s: stmts)
		{
			exec(s,env);
		}
	}
	
	/**
	 * Checks a condition involving two integer values and a relational operator. This method
	 * is used in evaluating if statements and while loops, where the condition must be satisfied
	 * in order for the if/while to be executed.
	 * 
	 * @param val the first integer value
	 * @param val2 the second integer value
	 * @param relop the relational operator
	 * 
	 * @return true if the condition is true; otherwise, false
	 */
	public boolean checkCondition(int val, int val2, String relop)
	{
		boolean bool = false;
		switch(relop)
		{
		case "<":
			if (val < val2)
				bool = true;
			break;
		case ">":
			if (val > val2)
				bool = true;
			break;
		case "<>":
			if (val != val2)
				bool = true;
			break;
		case ">=":
			if (val >= val2)
				bool = true;
			break;
		case "<=":
			if (val <= val2)
				bool = true;
			break;
		case "=":
			if (val == val2)
				bool = true;
			break;
		}
		return bool;
	}
	/**
	 * Executes an IF statement by checking whether the condition
	 * in the IF statement is true. If it is true, the statement
	 * in the IF statement is executed using one of the exec methods
	 * in this class.
	 * 
	 * @param iff the IF statement
	 * @param env the Environment where the execution of the IF
	 * 	      statement happens
	 */
	public void exec(If iff, Environment env)
	{
		Condition cond = iff.getCond();
		Statement stmt = iff.getStmt();
		Expression exp1 = cond.getExp1();
		Token relop = cond.getRelop();
		Expression exp2 = cond.getExp2();
		String relopStr = relop.getValue();
		int exp1_value = eval(exp1, env);
		int exp2_value = eval(exp2, env);
		
		if (checkCondition(exp1_value, exp2_value,relopStr))
		{
			exec(stmt, env);
		}
	}
	
	/**
	 * Executes a WHILE loop by checking whether the condition
	 * in the WHILE loop is true. If it is true, the statement
	 * in the WHILE loop is executed using one of the exec methods
	 * in this class.
	 * 
	 * @param wh the WHILE loop
	 * @param env the Environment where the execution of the WHILE loop
	 * 		  happens
	 */
	public void exec(While wh, Environment env)
	{
		Condition cond = wh.getCond();
		Statement stmt = wh.getStmt();
		Expression exp1 = cond.getExp1();
		Token relop = cond.getRelop();
		Expression exp2 = cond.getExp2();
		String relopStr = relop.getValue();
		int exp1_value = eval(exp1, env);
		int exp2_value = eval(exp2, env);
		
		while (checkCondition(exp1_value, exp2_value, relopStr))
		{
			exec(stmt, env);
			exp1_value = eval(exp1, env);
			exp2_value = eval(exp2, env);
		}
	}
	
	/**
	 * Executes a ProcedureDeclaration by adding
	 * it's name and the ProcedureDeclaration itself
	 * to the Map in the global environment.
	 * 
	 * @param pd the ProcedureDeclaration
	 * @param env the current Environment
	 */
	public void exec(ProcedureDeclaration pd, Environment env)
	{
		env.setProcedure(pd.getProcedureName(), pd);
	}
	
	/**
	 * This is the general method for evaluating expressions. This method
	 * determines what kind of Expression the expression is, and based on that,
	 * it will call the appropriate eval method. 
	 * 
	 * @param exp the Expression that is supposed to be evaluated
	 * @param env the Environment where the execution of the Expression
	 * 	      happens
	 * @return the numerical value of the evaluated expression
	 */
	public int eval(Expression exp, Environment env)
	{
		if (exp instanceof Number)
		{
			return eval((Number)exp, env);
		}
		else if (exp instanceof BinOp)
		{
			return eval((BinOp)exp, env);
		}
		else if (exp instanceof Variable)
		{
			return eval((Variable)exp, env);
		}
		else if (exp instanceof ProcedureCall)
		{
			return eval((ProcedureCall)exp, env);
		}
		return 0;
	}
	
	/**
	 * Evaluates BinOps (binary operations) by
	 * using the operand to perform an operation involving
	 * the two expressions.
	 * 
	 * @param binop the BinOp that is supposed to be executed
	 * @param env the Environment where the execution of the BinOp
	 * 		  happens
	 * @return the numerical value of the evaluated BinOp
	 */
	public int eval(BinOp binop, Environment env) 
	{
		String operand = binop.getOp();
		Expression exp1 = binop.getExp1();
		Expression exp2 = binop.getExp2();
		
		switch(operand)
		{
		case ("+"):
			return eval(exp1, env) + eval(exp2, env);
		case ("-"):
			return eval(exp1, env) - eval(exp2, env);
		case ("*"):
			return eval(exp1, env) * eval(exp2, env);
		case ("/"):
			return eval(exp1, env) / eval(exp2, env);
		case ("mod"):
			return eval(exp1, env) % eval(exp2, env);
		default:
			return 0;
		}
	}
	
	/**
	 * Evaluates Numbers by simply returning the integer value
	 * associated with the Number.
	 * 
	 * @param num the Number
	 * @param env the Environment where the execution of the Number
	 *        happens
	 * @return the integer value of the Number
	 */
	public int eval(Number num, Environment env)
	{
		return num.getValue();
	}
	
	/**
	 * Evaluates Variables by calling an Environment's 
	 * getVariable() method, which will retrieve the variable's
	 * value if it is stored in the Environment's Map, or otherwise,
	 * it will look at it's parent Environment's Map to retrieve the value.
	 * 
	 * @param var the Variable that is being evaluated
	 * @param env the Environment where the execution of the Variable
	 *        happens
	 * @return the numerical value associated with the Variable
	 */
	public int eval(Variable var, Environment env)
	{
		return env.getVariable(var.getName());
	}
	
	/**
	 * Evaluates a ProcedureCall. Creates a separate
	 * child environment (whose parent is the current
	 * Environment) for the ProcedureCall. First, a variable with
	 * the same name as the ProcedureCall is initialized in the child
	 * Environment, and its value is set to 0 (this will be used
	 * for return values). If the ProcedureDeclaration is meant to 
	 * return a value, the value of this variable will be changed; otherwise,
	 * the evaluation of the ProcedureCall will always return 0. Then,
	 * the child Environment's setVariable method is used to map the values of
	 * params in the ProcedureCall to the values of the Variable parameters in the
	 * initial ProcedureDeclaration. Finally, the Statement at the end of the ProcedureDeclaration
	 * that corresponds to this ProcedureCall is executed. Finally, the value of the
	 * variable with the same name as the ProcedureCall is returned.
	 * 
	 * @param pc the ProcedureCall that is to be evaluated
	 * @param env the current Environment
	 * @return the value of the variable with the same name as the ProcedureCall
	 */
	public int eval(ProcedureCall pc, Environment env)
	{
		String pd = pc.getProcedure();
		Environment child = new Environment(env);
		child.declareVariable(pd, 0);
		ProcedureDeclaration dec = child.getProcedure(pd);
		List<Expression> decParams = dec.getParams();
		List<Expression> callParams = pc.getParams();
		for (int i = 0; i < callParams.size(); i ++)
		{
			String varName = ((Variable)decParams.get(i)).getName();
			Expression exp = callParams.get(i);
			child.setVariable(varName, eval(exp, child));

		}
		exec(dec.getStatement(), child);
		return child.getVariable(pd);
	}
}
