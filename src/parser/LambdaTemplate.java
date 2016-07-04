package parser;

public class LambdaTemplate extends ExpressionTemplate {
	
	private Template body;
	public LambdaTemplate(String context) {super(context);}
	public LambdaTemplate(String context, Template body) {
		this(context); this.body = body;
	}
	
	@Override
	public String render(Environment env) {
		Environment innerEnv = new Environment();
		String[] params = 
				context.substring(context.indexOf('(')+1, context.lastIndexOf(')')).split(",\\s*");
		for(int i = 0; i < params.length; i++)
			innerEnv.bind(params[i], env.find("__param"+i+"__"));
		return body.render(innerEnv);
	}
}
