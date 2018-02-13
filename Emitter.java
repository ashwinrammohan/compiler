package emitter;
import java.util.List;
import java.io.*;

import ast.Expression;
import ast.ProcedureDeclaration;
import ast.Variable;

public class Emitter
{
	private PrintWriter out;
	private int labelcount;
	private ProcedureDeclaration pd;
	private int excessStackHeight;
	//creates an emitter for writing to a new file with given name
	public Emitter(String outputFileName)
	{
		try
		{
			out = new PrintWriter(new FileWriter(outputFileName), true);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	//prints one line of code to file (with non-labels indented)
	public void emit(String code)
	{
		if (!code.endsWith(":"))
			code = "\t" + code;
		out.println(code);
	}

	//closes the file.  should be called after all calls to emit.
	public void close()
	{
		out.close();
	}
	
	/**
	 * Pushes the value of $v0 onto the stack by subtracting 4 from
	 * the stack pointer ($sp) and storing the value of $v0 in
	 * the new address. Also increments the excessStackHeight by 1
	 * because one more value has been added onto the stack on top
	 * of the parameters of the procedure.
	 * 
	 * @param reg the register whose value should be pushed onto the stack
	 *        (usually this is $v0)
	 */
	public void emitPush(String reg)
	{
		emit("subu $sp, $sp, 4		#push " + reg);
		emit("sw " + reg + ", ($sp)");
		if (pd != null)
		{
			excessStackHeight ++;
		}
	}
	
	/**
	 * Pops the value on the stack by loading the value
	 * at the memory address of the stack pointer into the register.
	 * Then, the stack pointer address is incremented by four to simulate
	 * popping the value off of the stack. Also decrements the excessStackHeight by 1
	 * because one less value exists on the stack on top of the parameters of the procedure.
	 * 
	 * @param reg the register where the value on the stack is moved to
	 */
	public void emitPop(String reg)
	{
		emit("lw " + reg + ", ($sp)		#pop " + reg);
		emit("addu $sp, $sp, 4");
		if (pd != null)
		{
			excessStackHeight --;
		}
	}
	
	/**
	 * Returns the next label ID for if and while statements
	 * (returns 1 the first time it's called, 2 the next time,
	 * 3 the next time, and so on and so forth)
	 * 
	 * @return the next label ID
	 */
	public int nextLabelID()
	{
		labelcount ++;
		return labelcount;
	}
	
	public ProcedureDeclaration getProcedureContext()
	{
		return pd;
	}
	/**
	 * Sets the current procedure context
	 * by setting the instance ProcedureDeclaration to the
	 * ProcedureDeclaration that is taken in.
	 * 
	 * @param proc the ProcedureDeclaration that should be
	 *        remembered as the current procedure context
	 */
	public void setProcedureContext(ProcedureDeclaration proc)
	{
		pd = proc;
		excessStackHeight = 0;
	}
	
	/**
	 * Clears the current procedure context by setting the instance
	 * ProcedureDeclaration to null.
	 */
	public void clearProcedureContext()
	{
		pd = null;
	}
	
	/**
	 * Determines whether a certain variable name
	 * corresponds to a local variable (which means that it's the
	 * name of one of the current procedure declaration's parameter names)
	 * or a global variable.
	 * 
	 * @param varName the name of the variable
	 * @return true if the name belongs to a local variable; otherwise, false
	 */
	public boolean isLocalVariable(String varName)
	{
		if (varName.equals(pd.getProcedureName()))
		{
			return true;
		}
		for (Expression exp: pd.getParams())
		{
			if (((Variable)exp).getName().equals(varName))
			{
				return true;
			}
		}
		for (Expression exp: pd.getLocalVars())
		{
			if (((Variable)exp).getName().equals(varName))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Calculates the offset of a certain local variable in the stack,
	 * meaning how far it is from $sp on the stack. If this variable is
	 * one of the ProcedureDeclaration's parameters, then its offset
	 * depends on its position in the list of parameters, the number of
	 * non-parameter local variables (as these are also pushed onto the stack), and
	 * finally, the excessStackHeight(number of values pushed on top of the stack for calculations).
	 * For other local variables that are declared with a "VAR ..." statement prior to
	 * the Statement body of the ProcedureDeclaration, their offset is based only
	 * on their position in the list of local variables and the excessStackHeight.
	 * 
	 * @param localVarName the name of the local variable whose offset
	 * needs to be found
	 * @precondition localVarName is the name of a local variable for the 
	 * procedure currently being compiled
	 * @return the offset from $sp for a given local variable
	 */
	public int getOffSet(String localVarName)
	{
		int offSet = 0;
		List<Expression> localVars = pd.getLocalVars();
		for (int i = 0; i < localVars.size(); i ++)
		{
			if (((Variable)localVars.get(i)).getName().equals(localVarName))
			{
				return 4*excessStackHeight + 4*i;
			}
		}
		int size = pd.getParams().size();
		int maxOffset = 4*(size-1);
		for (int i = 0; i < size; i ++)
		{
			if (((Variable)pd.getParams().get(i)).getName().equals(localVarName))
			{
				offSet = 4*(pd.getLocalVars().size()) + 4 +(maxOffset - 4*i);
				//System.out.println("localVarName, excessStackHeight: " + excessStackHeight + ", offSet: " + offSet);
				return offSet + 4*excessStackHeight;
			}
		}
		return 0;
	}
}