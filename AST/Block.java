package ast;

import java.util.List;

import emitter.Emitter;

/**
 * A Block is a type of Statement that is made up
 * of several Statements. When a Block is executed,
 * all of the statements within the Block are executed.
 * 
 * @author Ashwin Rammohan
 * @version October 12, 2017
 *
 */
public class Block extends Statement
{
	private List<Statement> stmts;

	/**
	 * Takes in a list of Statements and creates
	 * a Block that contains them.
	 * 
	 * @param statements the list of statements
	 */
	public Block(List<Statement> statements)
	{
		stmts = statements;
	}

	/**
	 * Retrieves the list of Statements that are contained
	 * within the Block.
	 * 
	 * @return the list of Statements in the Block
	 */
	public List<Statement> getStatements()
	{
		return stmts;
	}

	/**
	 * Resets the list of Statements within the Block.
	 * @param statements the new list of Statements that
	 * 		  are supposed to be within the Block
	 */
	public void setStatements(List<Statement> statements)
	{
		stmts = statements;
	}
	
	/**
	 * Compiles a Block by compiling each Statement inside
	 * of the Block.
	 * 
	 * @param e the Emitter used to output the MIPS code	 
	 */
	public void compile(Emitter e)
	{
		for (Statement s: stmts)
		{
			s.compile(e);
		}
	}
	
}
