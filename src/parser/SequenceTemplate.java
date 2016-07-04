package parser;

public class SequenceTemplate extends Template {

	private Template t1,t2;
	public SequenceTemplate(Template t1, Template t2) {
		super();
		this.t1 = t1;this.t2=t2;
	}
	@Override
	public String render(Environment env) {
		return t1.render(env).concat(t2.render(env));
	}
}
