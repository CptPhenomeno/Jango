package parser;

import java.util.Iterator;

public class ForStmtTemplate extends ExpressionTemplate {

	private Template body;
	public ForStmtTemplate(String context) {super(context);}
	public ForStmtTemplate(String context, Template body) {
		this(context); this.body = body;
	}
	private class GenericIterator implements Iterator {
		private Iterator iterator;
		public GenericIterator(){super();}
		public GenericIterator (Object o) {
			if(o instanceof Iterable)
				iterator = ((Iterable) o).iterator();
			else if(o.getClass().isArray())
				iterator = new ArrayIterator((Object[]) o);
			else
				throw new IllegalArgumentException(o+" isn't iterable");
		}
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public Object next() {
			return iterator.next();
		}
		private class ArrayIterator extends GenericIterator {
			private int arrayLength,index;
			private Object[] array;
			public ArrayIterator(Object[] o){
				arrayLength = o.length;
				index = 0;
				array = o;
			}
			@Override
			public boolean hasNext() {
				return index<arrayLength;
			}
			@Override
			public Object next() {
				return array[index++];
			}
		}
	}
	@Override
	public String render(Environment env) {
		String split[] = context.split(" ");
		Object list = env.find(split[3]);
		Environment forEnv = new Environment(env);
		forEnv.bind(split[1], null);
		GenericIterator iterator = new GenericIterator(list);
		StringBuilder buffer = new StringBuilder();
		while(iterator.hasNext()) {
			forEnv.set(split[1], iterator.next());
			buffer.append(body.render(forEnv));
		}
		return buffer.toString();
	}
}
