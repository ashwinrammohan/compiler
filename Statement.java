package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * A Statement is an element in the input stream that is
 * composed of many Expressions. Statements can be in the form
 * of WRITELN's and ASSIGNMENT's, where they take up one line
 * in the input source code. Statements can also be BLOCKS, 
 * in which case they are made up of multiple other statements.
 * 
 * @author Ashwin Rammohan
 * @version October 12, 2017
 *
 */
public abstract class Statement 
{
	/**
	 * It is expected that all classes that implement Statement
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
