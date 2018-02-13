package ast;

import emitter.Emitter;

/**
 * A BinOp (binary operator) consists of two expressions
 * that are related by an operand.
 * 
 * @author Ashwin Rammohan
 * @version October 12, 2017
 *
 */
public class BinOp extends Expression
{
	private String op;
	private Expression exp1;
	private Expression exp2;
	
	/**
	 * Creates a new BinOp with a new operand, and two new
	 * expressions.
	 * 
	 * @param str the operand
	 * @param e1 the first Expression
	 * @param e2 the second Expression
	 */
	public BinOp(String str, Expression e1, Expression e2)
	{
		op = str;
		exp1 = e1;
		exp2 = e2;
	}

	/**
	 * Returns the operand.
	 * @return the operand relating the two Expressions.
	 */
	public String getOp() 
	{
		return op;
	}

	/**
	 * Sets the operand to a new string.
	 * @param op the new operand
	 */
	public void setOp(String op)
	{
		this.op = op;
	}

	/**
	 * Retrieves the first expression in the BinOp.
	 * @return the first expression.
	 */
	public Expression getExp1() 
	{
		return exp1;
	}

	/**
	 * Sets the first expression to a new Expression.
	 * @param exp1 the new first expression
	 */
	public void setExp1(Expression exp1)
	{
		this.exp1 = exp1;
	}

	/**
	 * Retrieves the second expression in the BinOp.
	 * @return the second expression.
	 */
	public Expression getExp2()
	{
		return exp2;
	}

	/**
	 * Sets the second expression to a new Expression.
	 * @param exp1 the new second expression
	 */
	public void setExp2(Expression exp2) 
	{
		this.exp2 = exp2;
	}
	
	/**
	 * Compiles a BinOp by first compiling the first Expression. The value
	 * of the first Expression is then pushed onto the stack. Then,
	 * the second Expression is compiled (leaving its value in $v0).
	 * The value of the first Expression is popped off of the stack
	 * and loaded into $t0. Then, based on the value of the relational
	 * operator in the Binop, a mathematical operation is performed
	 * on the two registers, and the result is stored in $v0.
	 * 
	 * @param e the Emitter used to emit MIPS code
	 */
	public void compile(Emitter e)
	{
		exp1.compile(e);
		e.emitPush("$v0");//pushes value of $v0 (exp1) onto stack
		exp2.compile(e); //$v0 now has the value of exp2
		
		e.emitPop("$t0");//pops the value off of the stack (exp1)
		//and into $t0
		switch(op)
		{
			case "+":
				e.emit("addu $v0, $v0, $t0");
				break;
			case "-":
				e.emit("subu $v0, $t0, $v0");
				break;
			case "*":
				e.emit("mult $v0, $t0");
				e.emit("mflo $v0");
				break;
			case "/":
				e.emit("div $t0, $v0");
				e.emit("mflo $vo");
				break;
		}
		
	}
}
