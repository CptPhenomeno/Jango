package lexer;

public enum TokenType {
	String,
	HTML,
	HTMLOpenTag,
	HTMLCloseTag,
	OpenTemplExpr,
	TemplExpr,
	CloseTemplExpr,
	OpenTemplStmt,
	TemplFor,
	TemplEndFor,
	TemplIf,
	TemplEndIf,
	CloseTemplStmt,
	EOS
}
