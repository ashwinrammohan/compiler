package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * An Expression is an element in the input stream
 * that is made of terms that are added or subtracted
 * with other terms. An Expression can also just be one term.
 * Expressions can be in the form of binary operations (BinOps),
 * Numbers, and Variables. Expressions themselves can also
 * be added/subtracted/multiplied/divided together.
 * 
 * @author Ashwin Rammohan
 * @version October 12, 2017
 *
 */
public abstract class Expression
{
	/**
	 * It is expected that all classes that implement Expression
	 * override this method and have their own distinct
	 * compile methods. However, if the programmer forgets
	 * to override this method in one of the subclasses,
	 * this method will output an error message.
	 * 
	 * @param e the Emitter used to emit MIPS code
	 */
	public void compile(Emitter e)
	{
		throw new RuntimeException("Implement me!!!!!");
	}
}
