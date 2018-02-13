package scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;

/**
 * The ScannerTester is used to test the functionality of the Scanner class and its ability
 * to identify individual lexemes in an input stream.
 * 
 * @author Ashwin Rammohan
 * @version September 7, 2017
 *
 */
public class ScannerTester
{
	/**
	 * Repeatedly calls the Scanner's nextToken() method in order 
	 * to list all of the lexemes in the input.
	 * @param args the user command line
	 * @throws ScanErrorException if there is a lexeme that 
	 * 		   cannot be identified by the Scanner
	 * @throws FileNotFoundException if the InputStream takes
	 * 		   in a filename that does not exist
	 */
	public static void main(String[] args) throws ScanErrorException, FileNotFoundException
	{ 
		Scanner sc = new Scanner(new FileInputStream("C:\\Users\\ashwi\\Downloads\\scannerTestAdvanced.txt"));
		while (sc.hasNext())
		{
			Token t = sc.nextToken();
			System.out.println(t.toString());
		}
		
	}
}
