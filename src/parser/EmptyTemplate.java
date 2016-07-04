package parser;

public class EmptyTemplate extends Template {

	public EmptyTemplate(){super();}

	@Override
	public String render(Environment env) {return "";}
	
}
