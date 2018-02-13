package ast;

import java.util.List;

import emitter.Emitter;
import environment.Environment;

/**
 * A ProcedureDeclaration declares a new Procedure, consisting of
 * a procedure name, a list of possible parameters (the list could
 * be empty), and a statement that is executed when the procedure
 * is called in a ProcedureCall.
 * 
 * @author Ashwin Rammohan
 * @version October 25, 2017
 *
 */
public class ProcedureDeclaration extends Statement
{
	private String procedureName;
	private Statement stmt;
	private List<Expression> params;
	private List<Expression> localVars;
	
	/**
	 * Creates a new ProcedureDeclaration with a procedure name,
	 * a Statement to be executed, and a list of parameters (which can be of length
	 * 0).
	 * @param name the name of the procedure
	 * @param statement the statement to be executed when the procedure is called
	 * @param parameters the list of parameters for the ProcedureDeclaration
	 */
	public ProcedureDeclaration(String name, Statement statement, List<Expression> parameters, 
			List<Expression> locals)
	{
		procedureName = name;
		stmt = statement;
		params = parameters;
		localVars = locals;
	}

	public List<Expression> getLocalVars() 
	{
		return localVars;
	}

	public void setLocalVars(List<Expression> locals) 
	{
		localVars = locals;
	}

	/**
	 * Retrieves the list of parameters for the ProcedureDeclaration.
	 * @return the list of parameters
	 */
	public List<Expression> getParams() 
	{
		return params;
	}

	/**
	 * Sets the list of parameters to a new list of Expressions.
	 * @param parameters the new list of parameters
	 */
	public void setParams(List<Expression> parameters)
	{
		params = parameters;
	}

	/**
	 * Retrieves the name of the ProcedureDeclaration.
	 * @return the name of the ProcedureDeclaration
	 */
	public String getProcedureName() 
	{
		return procedureName;
	}

	/**
	 * Sets the name of the ProcedureDeclaration to a new name.
	 * @param procedureName the new name of the ProcedureDeclaration
	 */
	public void setProcedureName(String procedureName) 
	{
		this.procedureName = procedureName;
	}

	/**
	 * Retrieves the Statement in the ProcedureDeclaration.
	 * @return the Statement in the ProcedureDeclaration
	 */
	public Statement getStatement() 
	{
		return stmt;
	}

	/**
	 * Sets the Statement in the ProcedureDeclaration to a new
	 * Statement.
	 * @param stmt the new Statement for the ProcedureDeclaration
	 */
	public void setStatement(Statement stmt)
	{
		this.stmt = stmt;
	}
	
	/**
	 * Compiles a ProcedureDeclaration by creating a subroutine for the
	 * procedure. Then, each of the ProcedureDeclaration's local variables
	 * is pushed onto the stack (on top of the return value and parameters
	 * for the ProcedureCall). The Emitter then sets its procedure context
	 * to this procedure, which is useful for later identifying local
	 * variables based on whether they are part of the procedure's parameter
	 * list. Then, after the statement in the ProcedureDeclaration is compiled,
	 * the local variables are popped off of the stack, the return value
	 * is popped off into $v0, and the program jumps back to the last known
	 * return address. Finally, the emitter's instance variable for
	 * procedure context is reset to null.
	 * 
	 * @param e the Emitter used to emit MIPS code
	 */
	public void compile(Emitter e)
	{
		e.emit("proc" + procedureName + ":");
		e.emit("# pushing local vars");
		for (Expression exp: localVars)
		{
			e.emit("li $v0, 0");
			e.emitPush("$v0");
		}
		e.setProcedureContext(this);
		stmt.compile(e);
		for (Expression exp: localVars)
		{
			e.emitPop("$t0");
		}
		e.emit("#popping return value");
		e.emitPop("$v0"); //pops off the return value to $v0
		e.emit("jr $ra");
		e.clearProcedureContext();
	}
}
