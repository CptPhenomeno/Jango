package parser;

import java.util.Arrays;


public class ParserMain {

	private static class Man {
		private String first;
		private String last;
		public Man(String first, String last) {
			super();
			this.first = first;
			this.last = last;
		}		
	}
	
	public static void main(String[] args) {
		String parseProf = "<table>\n"
						 + "{%for man in men%}"
						 + "	<tr><td>{{man.first}}</td><td>{{man.last}}</td></tr>\n"
						 + "{%endfor%}"
						 + "</table>\n";
		String toPrint = "<table>\n"
				 + "{%for man in men%}\n"
				 + "	<tr><td>{{man.first}}</td><td>{{man.last}}</td></tr>\n"
				 + "{%endfor%}\n"
				 + "</table>\n";
		Template templProf = Template.parse(parseProf);
		Environment envProf = new Environment();
		Man list[] = new Man[] {
				new Man("Matteo", "Puccinelli"), 
				new Man("Alessandro Andrea", "Bendinelli"),
				new Man("Alessandro", "Donatini"),
				new Man("Federico", "Della Bona"),
				new Man("Luca", "Briganti"),
				new Man("Alessandro", "Lensi"),
		};
		envProf.bind("men", list);
		System.out.println(toPrint+"\n\n"+templProf.render(envProf)+"\n\n");
	
		
		System.out.println("\n\n\n\n");
		Template tableGen = new HtmlTemplate("<table>\n", new ForStmtTemplate("for it in list", new CallTemplate("row(it)")), "</table>");
		Template rowGen = new LambdaTemplate("(user)", new HtmlTemplate("<tr>\n", new SequenceTemplate(
				new HtmlTemplate("<td>",new ExpressionTemplate("user.last"),"</td>"), new HtmlTemplate("<td>",new ExpressionTemplate("user.first"),"</td>\n")), "</tr>"));
		Environment e = new Environment();
		e.bind("list", list);
		e.bind("row", rowGen);
		System.out.println(tableGen.render(e));
	}
}
