package parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import ast.Statement;
import environment.Environment;
import ast.Evaluator;
import ast.Program;
import scanner.ScanErrorException;
import scanner.Scanner;
import scanner.Token;

/**
 * Tests the Parser class by parsing files containing the Pascal-like code.
 * @author Ashwin Rammohan
 * @version October 12, 2017
 *
 */
public class Tester 
{
	/**
	 * Creates a Scanner to scan an InputStream from a file and then uses a Parser to
	 * parse the statements from the stream of tokens outputted by the Scanner.
	 * 
	 * @param args the user command line
	 * @throws FileNotFoundException if the file name taken in by the 
	 * 								 FileInputStream class is invalid
	 * @throws ScanErrorException if the Scanner's methods throw this
	 * 							  exception
	 */
	public static void main(String[] args) throws FileNotFoundException, ScanErrorException
	{
		Scanner sc = new Scanner(new FileInputStream("C:\\Users\\ashwi\\Documents\\Ashwin's Harker Upper School Folder\\11th Grade Files\\Compilers and Interpreters\\CompProcTest.txt"));
		Parser p = new Parser(sc);
		Environment env = new Environment(null);
		Program prog = p.parseProgram();
		Evaluator eval = new Evaluator(env);
		eval.exec(prog, env);
		eval.compile("C:\\Users\\ashwi\\Documents\\Ashwin's Harker Upper School Folder\\11th Grade"
				+ " Files\\Compilers and Interpreters\\writeOut.s", prog);
		
	}
}
