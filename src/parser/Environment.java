package parser;

import java.util.HashMap;
import java.util.Map;

public class Environment {

	private Map<String, Object> environment;
	private Environment prec = null;
	public Environment() {environment = new HashMap<String, Object>();}
	public Environment(Environment env) {
		environment = new HashMap<String, Object>();
		this.prec = env;
	}
	public void bind(String key, Object value) {
		if(!environment.containsKey(key))
			environment.put(key, value);
		else
			throw new IllegalArgumentException("A binding for "+key+"already exist.");
	}
	public Object find(String key) {
		for(Environment e = this; e != null; e = prec) {
			Object o = e.environment.get(key);
			if(o != null)return o;
		}
		throw new IllegalArgumentException(key+" undeclared.");
	}
	public void set(String key, Object newValue) {
		if(!environment.containsKey(key))
			throw new IllegalArgumentException(key+" undeclared.");
		else
			environment.replace(key, newValue);
	}
}
