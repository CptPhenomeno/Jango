package parser;

public class IfStmtTemplate extends Template {

	private Template body;
	public IfStmtTemplate(String context) {super(context);}
	public IfStmtTemplate(String context, Template body) {
		this(context); this.body = body;
	}
	@Override
	public String render(Environment env) {
		if(Boolean.parseBoolean(context.split(" ")[1]))
			return body.render(env);
		else 
			return "";
	}

}
