package ast;

import emitter.Emitter;
import environment.Environment;
/**
 * A Writeln is a type of Statement that is supposed
 * to print out the value of a subsequent Expression.
 * 
 * @author Ashwin Rammohan
 * @version October 12, 2017
 *
 */
public class Writeln extends Statement
{
	private Expression exp;
	
	/**
	 * Creates a new Writeln object with a certain Expression.
	 * 
	 * @param expr the Expression whose value should be
	 * 		  printed out
	 */
	public Writeln(Expression expr)
	{
		exp = expr;
	}

	/**
	 * Retrieves the Expression for the Writeln object.
	 * 
	 * @return the Expression for the Writeln object
	 */
	public Expression getExpression() 
	{
		return exp;
	}
	
	/**
	 * Sets the Expression for the Writeln object to a new Expression.
	 * 
	 * @param expr the new Expression for the Writeln object
	 */
	public void setExpression(Expression expr)
	{
		exp = expr;
	}
	
	/**
	 * Compiles a Writeln statement by first compiling
	 * the Expression in the Writeln. After the Expression
	 * has been compiled, its value will be stored in $v0.
	 * The value in $v0 is then copied to $a0, and is printed.
	 * Finally, a newline is also printed to make the output of
	 * the entire Program easier to read.
	 * 
	 * @param e the Emitter used to emit MIPS code
	 */
	public void compile(Emitter e)
	{
		exp.compile(e);
		e.emit("move $a0, $v0");
		e.emit("li $v0, 1"); //print value in $a0
		e.emit("syscall");
		e.emit("la $a0, newline");
		e.emit("li $v0, 4"); //print the newline
		e.emit("syscall");		
	}
	

}
