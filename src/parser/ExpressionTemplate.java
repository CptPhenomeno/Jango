package parser;

import java.lang.reflect.Field;

public class ExpressionTemplate extends Template {

	public ExpressionTemplate(String context) {super(context);}
	private void nobinding(String str){
		System.err.println("Error: no binding for "+str);
		System.exit(1);
	}
	protected String getValue(String str, Environment env) {
		String [] sl = str.split("\\.");
		Object o = env.find(sl[0]);
		if(o != null) {
			Class<?> c = o.getClass();
			Field f;
			try {
				for (int i = 1; i < sl.length; i++) {
					f = c.getDeclaredField(sl[i]);
					f.setAccessible(true);
					o = f.get(o);
					if(o != null) c = o.getClass();
					else nobinding(str);
				}
				return o.toString();
			} catch (NoSuchFieldException e) {
				nobinding(str);
			} catch (SecurityException |
					IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}
		}
		else nobinding(str);
		return null;
	}
	@Override
	public String render(Environment env) {
		return getValue(context, env);
	}

}
