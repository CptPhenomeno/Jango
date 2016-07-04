package parser;

public class CallTemplate extends Template {
		
	public CallTemplate(String context) {super(context);}
	@Override
	public String render(Environment env) {
		Environment innerEnv = new Environment();
		String templateName = context.substring(0, context.indexOf('('));
		Template body = (Template)env.find(templateName);
		String[] params = 
				context.substring(context.indexOf('(')+1, context.lastIndexOf(')')).split(",\\s*");
		for(int i = 0; i < params.length; i++)
			innerEnv.bind("__param"+i+"__", env.find(params[i]));
		return body.render(innerEnv);
	}
}
