package lexer;

public class Token {
	
	private TokenType type;
	private String lexeme;
	public Token(TokenType type, String value) {
		this.type = type; 
		this.lexeme= value;}
	public String getValue() {return lexeme;}
	public TokenType getType() {return type;}
	@Override
	public String toString() {
		return "<"+getType().toString()+"; "+getValue()+">";}
}
