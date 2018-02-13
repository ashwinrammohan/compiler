package ast;

import emitter.Emitter;

/**
 * An Assignment is a type of Statement where a
 * variable is assigned to the value of an expression.
 * 
 * @author Ashwin Rammohan
 * @version October 12, 2017
 *
 */
public class Assignment extends Statement
{
	private String var;
	private Expression exp;
	
	/**
	 * Creates a new Assignment with a given variable name
	 * and an expression. 
	 * 
	 * @param str the variable name
	 * @param expr the Expression that the variable's new value
	 *        is supposed to be
	 */
	public Assignment(String str, Expression expr)
	{
		var = str;
		exp = expr;
	}
	
	/**
	 * Returns the variable name.
	 * 
	 * @return the variable name
	 */
	public String getVar()
	{
		return var;
	}

	/**
	 * Sets the instance String variable
	 * to a new variable name.
	 *  
	 * @param var the new variable name
	 */
	public void setVar(String var)
	{
		this.var = var;
	}

	/**
	 * Returns the instance Expression.
	 * 
	 * @return the instance Expression.
	 */
	public Expression getExpression() 
	{
		return exp;
	}

	/**
	 * Sets the instance Expression to a
	 * new Expression.
	 * 
	 * @param exp the new Expression
	 */
	public void setExpression(Expression exp)
	{
		this.exp = exp;
	}
	
	/**
	 * Compiles an assignment by first compiling
	 * the expression in the assignment, which will store its value
	 * in $v0. If the variable in the assignment is not a local variable 
	 * or the procedure context has not been set yet, the value in $v0
	 * is stored directly into the variable. If the variable is a local
	 * variable, the value in $v0 is loaded into the variable's address
	 * in the stack which is obtained by adding the variable's offset
	 * to the current stack pointer address ($sp).
	 * 
	 * @param e the Emitter used to emit MIPS code
	 */
	public void compile(Emitter e)
	{
		e.emit("#" + var);
		exp.compile(e);
		if (e.getProcedureContext() == null || !e.isLocalVariable(var))
		{
			e.emit("sw $v0, " + var);
		}
		else
		{
			int offSet = e.getOffSet(var);
			e.emit("sw $v0, " + offSet + "($sp)");
		}
		e.emit("# assignment done");
	}
	
}
