package environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ast.Variable;
import ast.Number;
import ast.ProcedureDeclaration;
/**
 * An Environment contains all the Variables and their
 * integer values for an abstract syntax tree. When the
 * Evaluator evaluates Assignment statements, it references
 * the Environment to modify the integer values associated
 * with each Variable. 
 * 
 * @author Ashwin Rammohan
 * @version October 12, 2017
 *
 */
public class Environment
{
	private Environment parent;
	private Map<String, Integer> values;
	private Map<String, ProcedureDeclaration> procedures;

	
	/**
	 * Creates a new Environment and makes the instance Map
	 * a new HashMap that stores Strings as keys (variable names)
	 * and Integers as the values.
	 */
	public Environment(Environment par)
	{
		parent = par;
		values = new HashMap<String, Integer>();
		procedures = new HashMap<String, ProcedureDeclaration>();
	}

	/**
	 * Retrieves the parent Environment of the current Environment.
	 * 
	 * @return the parent Environment
	 */
	public Environment getParent()
	{
		return parent;
	}

	/**
	 * Sets the parent of the current Environment
	 * to a new Environment.
	 * 
	 * @param parent the new parent Environment
	 */
	public void setParent(Environment parent)
	{
		this.parent = parent;
	}
	
	/**
	 * Retrieves the integer value associated with a certain variable, based
	 * on a given variable name
	 * 
	 * @param variable the variable whose value is required
	 * @return the value of the variable with the correct name
	 */
	public int getVariable(String variable)
	{
		if (values.containsKey(variable))
		{
			return values.get(variable);
		}
		else
		{
			return parent.getVariable(variable);
		}
	}
	
	/**
	 * Declares the value of a Variable in the current
	 * Environment.
	 * 
	 * @param variable the name of the variable
	 * @param value the new value of the variable
	 */
	public void declareVariable(String variable, int value)
	{
		values.put(variable, value);
	}
	
	/**
	 * Sets a variable's value to a new integer value. If the variable
	 * already exists in the current Environment, it's value is updated.
	 * If the variable does not already exist in the current Environment, then
	 * the variable will be placed in the global environment. 
	 * 
	 * @param variable the Variable whose value is to be modified
	 * @param value the new value of the Variable
	 */
	public void setVariable(String variable, int value)
	{
		if (values.containsKey(variable))
		{
			declareVariable(variable,value);
		}
		else
		{
			Environment p = this;
			boolean flag = false;
			while (!flag && p.getParent()!= null)
			{
				p = p.getParent();
				if (p.getValues().containsKey(variable))
				{
					p.declareVariable(variable, value);
					flag = true;
				}
			}
			if (flag == false && p.getParent() == null)
			{
				if (p.getValues().containsKey(variable))
				{
					p.declareVariable(variable, value);
				}
				else
				{
					this.declareVariable(variable, value);
				}
			}			
		}
	}

	/**
	 * Retrieves the Map storing all the Variables and their values.
	 * 
	 * @return the Map storing all the Variables and their values
	 */
	public Map<String, Integer> getValues() 
	{
		return values;
	}

	/**
	 * Sets the instance Map to a new Map of Strings and Integers.
	 * 
	 * @param vals the new Map
	 */
	public void setValues(Map<String, Integer> vals) 
	{
		values = vals;
	}
	
	/**
	 * Retrieves the Map containing names of ProcedureDeclarations and
	 * their corresponding ProcedureDeclarations from the Environment.
	 * 
	 * @return the Map containing names of ProcedureDeclarations and
	 * their corresponding ProcedureDeclarations
	 */
	public Map<String, ProcedureDeclaration> getProcedures()
	{
		return procedures;
	}

	/**
	 * Sets the Map of names of ProcedureDeclarations and their corresponding
	 * ProcedureDeclarations to a new Map.
	 * 
	 * @param procedures the new Map of ProcedureDeclaration names
	 *        and their corresponding ProcedureDeclarations
	 */
	public void setProcedures(Map<String, ProcedureDeclaration> procedures)
	{
		this.procedures = procedures;
	}

	/**
	 * Retrieves the ProcedureDeclaration associated with a specific
	 * procedure name in the instance map of the global environment.
	 * 
	 * @param name the name of the procedure
	 * @return the ProcedureDeclaration associated with the name
	 */
	public ProcedureDeclaration getProcedure(String name)
	{
		Environment p = this;
		while (p.getParent() != null)
		{
			p = p.getParent();
		}
		return p.getProcedures().get(name);
	}
	
	/**
	 * Adds a new procedure name, ProcedureDeclaration pair to the global environment's
	 * Map. Or, if the procedure name already exists in the global environment's Map,
	 * this method replaces the ProcedureDeclaration associated with that name
	 * with a new ProcedureDeclaration.
	 * 
	 * @param name the name of the procedure
	 * @param dec the ProcedureDeclaration
	 */
	public void setProcedure(String name, ProcedureDeclaration dec)
	{
		Environment p = this;
		while (p.getParent() != null)
		{
			p = p.getParent();
		}
		p.getProcedures().put(name, dec);
	}



	
}
