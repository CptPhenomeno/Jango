package parser;

public class HtmlTemplate extends Template {

	private String openTag,closeTag;
	private Template body;
	public HtmlTemplate(String openTag, Template body, String closeTag) {
		super();
		this.openTag = openTag;
		this.body = body;
		this.closeTag = closeTag;
	}
	@Override
	public String render(Environment env) {
		return openTag.concat(body.render(env)).concat(closeTag);
	}
	
}
