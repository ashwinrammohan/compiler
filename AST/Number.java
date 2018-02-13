package ast;

import emitter.Emitter;

/**
 * A Number is an element of the
 * abstract syntax tree which has
 * an integer value.
 * 
 * @author Ashwin Rammohan
 * @version October 12, 2017
 *
 */
public class Number extends Expression
{
	private int value;
	
	/**
	 * Creates a new Number with a specific integer value.
	 * 
	 * @param val the integer value
	 */
	public Number(int val)
	{
		value = val;
	}

	/**
	 * Retrieves the integer value stored in the Number.
	 * 
	 * @return the integer value stored in the Number
	 */
	public int getValue() 
	{
		return value;
	}

	/**
	 * Sets the value of the Number to a specific integer
	 * value.
	 * 
	 * @param value the new integer value for the Number
	 */
	public void setValue(int value)
	{
		this.value = value;
	}
	
	/**
	 * Compiles a Number by loading the value
	 * of the Number into the $v0 register.
	 * 
	 * @param e the Emitter used to emit MIPS code
	 */
	public void compile(Emitter e)
	{
		e.emit("li $v0, " + value);
	}
}
