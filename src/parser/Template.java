package parser;

import lexer.Lexer;
import lexer.Token;
import lexer.TokenType;

public abstract class Template {

	protected String context;
	private static Token look;
	private static Lexer lexer;
	public Template(){super();}
	public Template(String string){context = string;}
	private static void move(){look = lexer.scan();}
	private static void match(TokenType t){
		if(t.equals(look.getType()))
			move();
		else
			error("Syntax error: I want "+t+" but I've found "+look.getType());
	}
	private static void error (String message) {
		System.out.flush();System.err.flush();
		System.err.println(message);
		System.exit(1);
	}
	private static void setInputToParse(String input) {
		lexer = new Lexer();
		lexer.tokenizeString(input);
		move();
	}
	public static Template parse(String input) {
		setInputToParse(input);
		return template();
	}
	private static Template template() {
		Template template = new EmptyTemplate();
		switch (look.getType()) {
			case OpenTemplExpr:
				template = templExpr();
				break;
			case OpenTemplStmt:
				template = templStmt();
				break;
			case HTMLOpenTag:
				template = htmlCode();
				break;
			case String:
				template = string();
				break;
			case HTMLCloseTag:
			case EOS:
			case TemplEndFor:
			case TemplEndIf:
				break;
			default:
				error("Syntax error: Unexpected token "+look);
				break;
		}
		return template;
	}
	private static Template templExpr(){
		match(TokenType.OpenTemplExpr);
		Token t = look;
		match(TokenType.TemplExpr);
		match(TokenType.CloseTemplExpr);
		ExpressionTemplate expr = new ExpressionTemplate(t.getValue()); 
		return new SequenceTemplate(expr, template());
	}
	private static Template templStmt() {
		Template t = null;
		match(TokenType.OpenTemplStmt);
		switch (look.getType()) {
			case TemplFor:
				t = forStmt();
				break;
			case TemplIf:
				t = ifStmt();
				break;
			default:
				error("Syntax error: Unexpected token "+look);
				break;
		}
		return new SequenceTemplate(t, template());
	}
	private static Template forStmt() {
		Token forStmt = look;
		match(TokenType.TemplFor);
		match(TokenType.CloseTemplStmt);
		Template body = template();
		match(TokenType.TemplEndFor);
		Template forTempl = new ForStmtTemplate(forStmt.getValue(), body);
		return new SequenceTemplate(forTempl, template());
	}
	private static Template ifStmt() {
		Token ifStmt = look;
		match(TokenType.TemplIf);
		match(TokenType.CloseTemplStmt);
		Template body = template();
		match(TokenType.TemplEndIf);
		Template forTempl = new IfStmtTemplate(ifStmt.getValue(), body);
		return new SequenceTemplate(forTempl, template());
	}
	private static Template htmlCode(){
		Token t = look;
		match(TokenType.HTMLOpenTag);
		String s1 = t.getValue();int indexOf = s1.indexOf(' '); 
		String openTag = s1.substring(1, (indexOf>0)?indexOf:s1.length()-1);		
		Template body = template();
		t = look;
		match(TokenType.HTMLCloseTag);
		String s2 = t.getValue();indexOf = s2.indexOf(' ');
		String closeTag = s2.substring(2, (indexOf>0)?indexOf:s2.length()-1);
		if(!openTag.equals(closeTag))
			error("Wrong tag: "+closeTag+" tag cannot close "+openTag+" tag");
		Template html = new HtmlTemplate(s1, body, s2);
		return new SequenceTemplate(html, template());
	}
	private static Template string(){
		Token t = look;
		match(TokenType.String);
		Template text = new TextTemplate(t.getValue());
		return new SequenceTemplate(text, template());
	}

	public abstract String render(Environment env);
}
