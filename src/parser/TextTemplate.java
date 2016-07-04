package parser;

public class TextTemplate extends Template {

	public TextTemplate(String text) {super(text);}
	@Override
	public String render(Environment env) {
		return context;
	}
	
}
