package ast;

import java.util.List;

import emitter.Emitter;

/**
 * The Program class serves as the root of the entire abstract syntax tree.
 * Each program may begin with an arbitrary number of procedure declarations, 
 * eventually ending with a statement and a period.
 * 
 * @author Ashwin Rammohan
 * @version October 26, 2017
 *
 */
public class Program
{
	private List<String> variables;
	private List<ProcedureDeclaration> procedures;
	private Statement stmt;
	
	/**
	 * Creates a new Program with a List of ProcedureDeclarations and a 
	 * Statement.
	 * 
	 * @param procedures the List of ProcedureDeclarations
	 * @param statement the statement that is located after all of
	 * 	      the ProcedureDeclarations in the Program
	 */
	public Program(List<String> variables, List<ProcedureDeclaration> procedures, Statement statement)
	{
		this.variables = variables;
		this.procedures = procedures;
		stmt = statement;
	}

	/**
	 * Retrieves the List of ProcedureDeclarations.
	 * 
	 * @return the List of ProcedureDeclarations
	 */
	public List<ProcedureDeclaration> getProcedures() 
	{
		return procedures;
	}

	/**
	 * Sets the instance List of ProcedureDeclarations to a new List.
	 * 
	 * @param procedures the new List of ProcedureDeclarations
	 */
	public void setProcedures(List<ProcedureDeclaration> procedures)
	{
		this.procedures = procedures;
	}

	/**
	 * Retrieves the statement that is located after all of
	 * the ProcedureDeclarations in the Program
	 * 
	 * @return the final statement in the Program
	 */
	public Statement getStatement() 
	{
		return stmt;
	}

	/**
	 * Sets the Statement at the end of the Program to a new 
	 * Statement.
	 * 
	 * @param statement the new Statement
	 */
	public void setStmt(Statement statement)
	{
		stmt = statement;
	}
	
	/**
	 * Retrieves the list of variables that are associated with this
	 * program.
	 * 
	 * @return the list of variables
	 */
	public List<String> getVariables() {
		return variables;
	}

	/**
	 * Sets the list of variables associated with this program
	 * to a new list.
	 * 
	 * @param variables the new list of variables
	 */
	public void setVariables(List<String> variables)
	{
		this.variables = variables;
	}

	/**
	 * Compiles a Program by calling its Statement's
	 * compile method.
	 * 
	 * @param e the Emitter used to emit MIPS code
	 */
	public void compile(Emitter e)
	{
		stmt.compile(e);
	}
}
