package ast;

import emitter.Emitter;

/**
 * A While statement (or while loop) is a type of Statement
 * that is made up of a condition, and a statement
 * that is evaluated if the condition is true.
 * 
 * @author Ashwin Rammohan
 * @version October 12, 2017
 *
 */
public class While extends Statement
{
	private Condition cond;
	private Statement stmt;
	
	/**
	 * Creates a new While object with a certain Condition
	 * and Statement.
	 * 
	 * @param condition the Condition for the while loop
	 * @param statement the Statement that should be executed
	 * 	      if the condition evaluates to true
	 */
	public While(Condition condition, Statement statement)
	{
		cond = condition;
		stmt = statement;
	}
	
	/**
	 * Retrieves the condition for the While statement.
	 * 
	 * @return the While statement's Condition
	 */
	public Condition getCond() 
	{
		return cond;
	}
	
	/**
	 * Sets the condition of the While statement to a new Condition.
	 * 
	 * @param condition the new Condition for the While statement
	 */
	public void setCond(Condition condition)
	{
		cond = condition;
	}
	
	/**
	 * Retrieves the Statement within the While statement.
	 * 
	 * @return the Statement within the While statement
	 */
	public Statement getStmt()
	{
		return stmt;
	}
	
	/**
	 * Sets the statement of the While statement to a new Statement.
	 * 
	 * @param stmt the new Statement for the While statement
	 */
	public void setStmt(Statement stmt)
	{
		this.stmt = stmt;
	}
	
	/**
	 * Compiles a While loop by first creating a label for the
	 * While loop consisting of "while" and then the label ID generated
	 * by the Emitter's nextLabelID method. The While loop's Condition's
	 * compile method is then called, which is designed to jump to an "endWhile"
	 * label if the condition evaluates to true. The Statement in the While loop
	 * is then compiled, and a jump statement is included to loop back to the original
	 * Condition for each iteration of the loop. Finally, the "endWhile" label is defined.
	 * 
	 * @param e the Emitter used to emit MIPS code
	 */
	public void compile(Emitter e)
	{
		int i = e.nextLabelID();
		e.emit("while" + i + ":");
		cond.compile(e, "endWhile" + i + "");	
		stmt.compile(e);
		e.emit("j while" + i + "");
		e.emit("endWhile" + i + ":");		
	}
}
