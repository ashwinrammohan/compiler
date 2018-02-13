package ast;
import emitter.Emitter;
import scanner.Token;

/**
 * A Condition consists of two Expressions related by a relational
 * operator. A Condition represents a boolean value, meaning 
 * that it is either true or false.
 * 
 * @author Ashwin Rammohan
 * @version October 12, 2017
 *
 */
public class Condition 
{
	private Expression exp1;
	private Token relop;
	private Expression exp2;
	
	/**
	 * Creates a new Condition with two Expressions and a relational operator (contained
	 * in a token).
	 * 
	 * @param exp1 the first Expression
	 * @param relop the token containing the relational operator
	 * @param exp2 the second Expression
	 */
	public Condition(Expression exp1, Token relop, Expression exp2)
	{
		this.exp1 = exp1;
		this.relop = relop;
		this.exp2 = exp2;
	}
	
	/**
	 * Retrieves the first Expression.
	 * 
	 * @return the first Expression
	 */
	public Expression getExp1() 
	{
		return exp1;
	}
	
	/**
	 * Sets the first Expression to a new Expression.
	 * 
	 * @param exp the new Expression
	 */
	public void setExp1(Expression exp) 
	{
		exp1 = exp;
	}
	
	/**
	 * Retrieves the token that contains the relational operator.
	 * 
	 * @return the token with the relational operator.
	 */
	public Token getRelop() 
	{
		return relop;
	}
	
	/**
	 * Sets the token containing the relational operator to a new Token.
	 * @param reloperator the new Token (which should be of type RELOP)
	 */
	public void setRelop(Token reloperator) 
	{
		relop = reloperator;
	}
	
	/**
	 * Retrieves the first Expression.
	 * 
	 * @return the first Expression
	 */
	public Expression getExp2()
	{
		return exp2;
	}
	
	/**
	 * Sets the first Expression to a new Expression.
	 * 
	 * @param exp the new Expression
	 */
	public void setExp2(Expression exp)
	{
		exp2 = exp;
	}
	
	/**
	 * Compiles Conditions by first compiling the first
	 * Expression. The value of this Expression is then
	 * pushed onto the stack. Then, the second Expression
	 * is compiled, leaving its value in $v0. The value of
	 * the first Expression is popped off of the stack and
	 * loaded into $t0. Then, based on the value of the
	 * relational operator, the values stored in the
	 * two registers are compared with the opposite
	 * relational operator (if the operator in SIMPLE
	 * was ">", then "ble" would be used in the condition
	 * in MIPS code so that if the MIPS condition
	 * evaluates to true, this method jumps to the label
	 * endIf.
	 *  
	 * @param e the Emitter used to emit MIPS code
	 * @param endIf the label that will be jumped to
	 * 		  if the initial condition (in the SIMPLE code)
	 * 		  is not fulfilled
	 */
	public void compile(Emitter e, String endIf)
	{
		exp1.compile(e);
		e.emitPush("$v0");
		exp2.compile(e);
		e.emitPop("$t0");
		switch (relop.getValue())
		{
			case ">":
				e.emit("ble $t0, $v0," + endIf);
				break;
			case "<":
				e.emit("bge $t0, $v0," + endIf);
				break;
			case ">=":
				e.emit("blt $t0, $v0," + endIf);
				break;
			case "<=":
				e.emit("bgt $t0, $v0," + endIf);
				break;
			case "<>":
				e.emit("beq $t0, $v0," +  endIf);
				break;
			case "=":
				e.emit("bne $t0, $v0," + endIf);
				break;
		}
	}
}
