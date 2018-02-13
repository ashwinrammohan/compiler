package ast;

import emitter.Emitter;

/**
 * A Variable is an element in the abstract syntax tree
 * that contains a variable name (a String).
 * Assignment statements are responsible
 * for assigning values to variables. Variables and their
 * corresponding values are stored in the Environment class's
 * Map.
 * 
 * @author Ashwin Rammohan
 * @version December 31, 2017
 */
public class Variable extends Expression
{
	private String name;
	
	/**
	 * Creates a new Variable with a specific variable name. 
	 * @param varName the variable name
	 */
	public Variable(String varName)
	{
		name = varName;
	}

	/**
	 * Retrieves the name of the variable.
	 * 
	 * @return the name of the variable.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the variable name to a new String. 
	 * 
	 * @param varName the new variable name
	 */
	public void setName(String varName) 
	{
		name = varName;
	}
	
	/**
	 * Compiles a variable in two different ways based on whether
	 * it is a local variable or a global variable. If the procedure context
	 * has not been set or the variable is known to not be local, the address
	 * of the variable is simply loaded into $t0. If the variable is local,
	 * the address of the variable in the stack is loaded into $t0. The exact
	 * address of the variable in the stack is referenced by modifying ($sp) by
	 * the variable's offset. Finally, in either scenario, the value stored
	 * at the variable's address is loaded into $v0.
	 * 
	 * @param e the Emitter used to emit MIPS code
	 */
	public void compile(Emitter e)
	{
		if (e.getProcedureContext() == null || !e.isLocalVariable(name))
		{
			e.emit("la $t0," + name);
		}
		else
		{
			//System.out.println(name + " offset: " + e.getOffSet(name));
			e.emit("la $t0, " + e.getOffSet(name) + "($sp)");
		}
		e.emit("lw $v0, ($t0)");
	}
}
