package lexer;

import java.util.Iterator;
import java.util.regex.Pattern;


public class Lexer {
	
	private String regex;
	private LexerStringIterator tokens;
	private boolean insideTemplate = false, insideStatement = false;
	private Pattern htmlOpenMatcher;
	private Pattern htmlCloseMatcher;
	private Pattern templExprMatcher;
	private Pattern templForMatcher;
	private Pattern templIfMatcher;
	
	private void buildRegex() {
		String tmplExprDelimiter = "(?=\\{\\{)|(?<=}})|(?<=\\{\\{)|(?=}})";
		String stmtDelimiter = "(?=\\{%)|(?<=%})|(?<=\\{%)|(?=%})";
		String htmlDelimiter = "(?=<\\p{Alnum}+>)|(?<=</\\p{Alnum}{1,5}>)|"
							 + "(?<=<\\p{Alnum}{1,5}>)|(?=</\\p{Alnum}+>)";
		regex = tmplExprDelimiter+"|"+stmtDelimiter+"|"+htmlDelimiter;
		
		htmlOpenMatcher = Pattern.compile("<\\p{Alnum}+(\\s.*)*>");
		htmlCloseMatcher = Pattern.compile("</\\p{Alnum}+>");
		templExprMatcher = Pattern.compile("^[\\p{Alnum}_.]+$");
		templForMatcher = Pattern.compile("^for [\\p{Alnum}_.]+ in [\\p{Alnum}_.]+$");
		templIfMatcher = Pattern.compile("^if [\\p{Alnum}_.]+$");
	}
	
	public Lexer(){super();buildRegex();}
	public Lexer(String input){tokenizeString(input);}
	private void error(String string) {
		System.out.flush();System.err.flush();
		System.err.println("Invalid sequence of char in template expression: "+string);
		System.exit(1);
	}
	public void tokenizeString(String string){
		tokens = new LexerStringIterator(string.split(regex));
	}
	public Token scan() {
		if(tokens.hasNext()) {
			return getToken(tokens.next());
		}
		return new Token(TokenType.EOS, null);
	}
	private Token getToken(String string) {
		if(string.equals("{{")) {
			insideTemplate = true;
			return new Token(TokenType.OpenTemplExpr, string);
		}
		if(string.equals("}}")) {
			insideTemplate = false;
			return new Token(TokenType.CloseTemplExpr, string);
		}
		if(string.equals("{%")) {
			String next;
			boolean goBack = false;
			if(tokens.hasNext()){
				goBack = true;
				next = tokens.next();
				if(next.equals("endfor")) {
					if(tokens.hasNext()){
						next = tokens.next();
						if(next.equals("%}"))
							return new Token(TokenType.TemplEndFor, string);
					}
					return new Token(TokenType.String, "{%endfor"+next);
				}
				if(next.equals("endif")) {
					if(tokens.hasNext()) {
						next = tokens.next();
						if(next.equals("%}"))
						return new Token(TokenType.TemplEndIf, string);
					}
					return new Token(TokenType.String, "{%endif"+next);
				}
			}
			if(goBack)
				tokens.back();
			insideStatement = true;
			return new Token(TokenType.OpenTemplStmt, string);
		}
		if(string.equals("%}")) {
			insideStatement = false;
			return new Token(TokenType.CloseTemplStmt, string);
		}
		if(htmlOpenMatcher.matcher(string).matches())
			return new Token(TokenType.HTMLOpenTag, string);
		if(htmlCloseMatcher.matcher(string).matches())
			return new Token(TokenType.HTMLCloseTag, string);
		if(insideTemplate) {
			if(templExprMatcher.matcher(string).matches())
				return new Token(TokenType.TemplExpr, string);
			else 
				error(string);
		}
		if(insideStatement) {
			if(templForMatcher.matcher(string).matches()) 
				return new Token(TokenType.TemplFor, string);
			else if(templIfMatcher.matcher(string).matches())
				return new Token(TokenType.TemplIf, string);
			else 
				error(string);
		}
		return new Token(TokenType.String, string);
	}
	private class LexerStringIterator implements Iterator<String> {
		private int index = 0;
		private String[] list;
		public LexerStringIterator(String[] list) {
			this.list = list;
		}

		@Override
		public boolean hasNext() {
			return index < list.length;
		}

		@Override
		public String next() {
			return list[index++];
		}
		
		public void back() {
			if(index > 0)
				index--;
		}
		
	}
}
