package ast;

import emitter.Emitter;

/**
 * An If Statement consists of a condition and a statement that
 * will be executed if the condition evaluates to true.
 * 
 * @author Ashwin Rammohan
 * @version October 12, 2017
 *
 */
public class If extends Statement
{
	private Condition cond;
	private Statement stmt;
	
	/**
	 * Creates a new If statement with a Condition and a Statement.
	 * 
	 * @param cond the condition contained in the If Statement
	 * @param stmt the statement that will be executed if the
	 * 		  condition evaluates to true
	 */
	public If(Condition cond, Statement stmt) 
	{
		this.cond = cond;
		this.stmt = stmt;
	}
	
	/**
	 * Retrieves the condition contained within the If statement.
	 * 
	 * @return the condition in the If statement
	 */
	public Condition getCond() 
	{
		return cond;
	}
	
	/**
	 * Sets the condition in the If statement to a new condition.
	 * 
	 * @param cond the new condition
	 */
	public void setCond(Condition cond)
	{
		this.cond = cond;
	}
	
	/**
	 * Retrieves the statement that will be executed
	 * if the condition evaluates to true.
	 * 
	 * @return the statement that will be executed
	 *         if the condition evaluates to true.
	 */
	public Statement getStmt()
	{
		return stmt;
	}
	
	/**
	 * Sets the statement in the If statement
	 * to a new statement.
	 * 
	 * @param stmt the new statement that will
	 * 		  be executed if the condition evaluates
	 * 		  to true
	 */
	public void setStmt(Statement stmt)
	{
		this.stmt = stmt;
	}

	/**
	 * Compiles an If statement by calling the Condition's
	 * compile method with the Emitter and the updated
	 * label ID for the If's statement. Then, the Emitter
	 * emits the code to define the label separately, and
	 * under this label, the MIPS code for the statement is
	 * emitted.
	 * 
	 * @param e the Emitter used to emit the MIPS code
	 */
	public void compile(Emitter e)
	{
		int i = e.nextLabelID();
		cond.compile(e, "endIf" + i + "");
		stmt.compile(e);
		e.emit("endIf" + i + ":");
	}
	
	
}
