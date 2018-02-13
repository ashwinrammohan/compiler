package scanner;
/**
 * A Token is an object with a value and a type that represents each lexeme.
 * 
 * @author Ashwin Rammohan
 * @version September 7, 2017
 *
 */
public class Token {
	private final Scanner.TOKEN_TYPE T;
	private String value;
	
	/**
	 * Creates a new token based on a value and a token type.
	 * 
	 * @param str the value of the token
	 * @param token_type the type that the token should be
	 */
	public Token(String str, Scanner.TOKEN_TYPE token_type)
	{
		T = token_type;
		value = str;
	}
	
	/**
	 * Retrieves the value of the token.
	 * 
	 * @return the value of the token.
	 */
	public String getValue()
	{
		return value;
	}
	
	/**
	 * Retrieves the token type.
	 * 
	 * @return the token type
	 */
	public Scanner.TOKEN_TYPE getTokenType()
	{
		return T;
	}
	
	/**
	 * Creates a string representation of the string.
	 * 
	 * @return a string representing the token
	 */
	public String toString()
	{
		String s = "Type: " + T + ", value: " + value;
		return s;
	}
	
	/**
	 * Compares this token to another token to test their equality.
	 * 
	 * @return whether the two tokens are the same based on their value
	 */
	public boolean equals(Object token)
	{
		return ((Token)(token)).getValue().equals(getValue());
	}
	
	/**
	 * Generates a hashcode for the token.
	 * 
	 * @return the token's value's hashcode
	 */
	public int hashCode()
	{
		return toString().hashCode();
	}
}
