package ast;

import java.util.List;

import emitter.Emitter;
import environment.Environment;

/**
 * A ProcedureCall consists of a method (a procedre) being claled
 * within a Block. The ProcedureCall may or may not take in parameters
 * based on the nature of the ProcedureDeclaration that it is based upon.
 * 
 * @author Ashwin Rammohan
 * @version October 25, 2017
 *
 */
public class ProcedureCall extends Expression
{
	private String procedure;
	private List<Expression> params;
	
	/**
	 * Creates a new ProcedureCall with a ProcedureDeclaration
	 * name and a list of possible parameters (the list can be length
	 * 0 if there are no parameters).
	 * 
	 * @param dec the name of the corresponding ProcedureDeclaration
	 * @param parameters the list of parameters
	 */
	public ProcedureCall(String dec, List<Expression> parameters)
	{
		procedure = dec;
		params = parameters;
	}

	/**
	 * Retrieves the list of parameters.
	 * @return the list of parameters
	 */
	public List<Expression> getParams() 
	{
		return params;
	}

	/**
	 * Sets the list of parameters to a new list of Expressions.
	 * @param parameters the new list of Expressions
	 */
	public void setParams(List<Expression> parameters)
	{
		params = parameters;
	}
	
	/**
	 * Retrieves the name of the corresponding ProcedureDeclaration.
	 * @return the name of the corresponding ProcedureDeclaration
	 */
	public String getProcedure() 
	{
		return procedure;
	}

	/**
	 * Sets the corresponding ProcedureDeclaration to a new
	 * ProcedureDeclaration (by changing the name).
	 * @param procedure the name of the new ProcedureDeclaration
	 */
	public void setProcedure(String procedure) 
	{
		this.procedure = procedure;
	}
	
	public void compile(Emitter e)
	{
		e.emitPush("$ra");
		for (Expression exp: params) //pushes values of args onto stack
		{
			exp.compile(e);
			e.emitPush("$v0");
		}
		e.emit("# pushing return value");
		e.emit("li $v0, 0");
		e.emitPush("$v0");
		e.emit("jal proc" + procedure);
		for (Expression exp: params) //pops values of args off of the stack
		{
			e.emitPop("$t0");
		}
		e.emitPop("$ra");
	}
}
