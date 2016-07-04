package lexer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LexerMain {
	
	public static void main(String[] args) {
		Lexer lexer = new Lexer();
		lexer.tokenizeString("{%for i in list%}do_something{%endfor%}");
		Token actual;
		actual = lexer.scan();
		while(actual.getType() != TokenType.EOS) {
			System.out.println(actual.toString());
			actual = lexer.scan();
		}
		
		System.out.println();
	
		//Riconoscimento
		String test = "{{{x}}}";
//		System.out.println(test.subSequence(0, 1)); //index = 0; start = 1; end = 1
//		System.out.println(test.subSequence(1, 3)); //index = 1; start = 3; end = 3
//		System.out.println(test.subSequence(3, 4)); //index = 3; start = 4; end = 4
//		System.out.println(test.subSequence(4, 6)); //index = 4; start = 6; end = 6
//		System.out.println(test.subSequence(6, 7)); //index = 6; start = 7; end = 7
//		String regex = "(?=(?<=\\{)\\{\\{)|(?=(?<=}})})";
//		Pattern p =	 Pattern.compile(regex);
//		String rec[] = test.split(regex);
//		System.out.println(Arrays.toString(rec));;
		
		
	}
	
	private static String[] oimmena(Pattern p, CharSequence input, int limit) {
        int index = 0;
        boolean matchLimited = limit > 0;
        ArrayList<String> matchList = new ArrayList<>();
        Matcher m = p.matcher(input);

        // Add segments before each match found
        while(m.find()) {
            if (!matchLimited || matchList.size() < limit - 1) {
                if (index == 0 && index == m.start() && m.start() == m.end()) {
                    // no empty leading substring included for zero-width match
                    // at the beginning of the input char sequence.
                    continue;
                }
                String match = input.subSequence(index, m.start()).toString();
                matchList.add(match);
                index = m.end();
            } else if (matchList.size() == limit - 1) { // last one
                String match = input.subSequence(index,
                                                 input.length()).toString();
                matchList.add(match);
                index = m.end();
            }
        }

        // If no match was found, return this
        if (index == 0)
            return new String[] {input.toString()};

        // Add remaining segment
        if (!matchLimited || matchList.size() < limit)
            matchList.add(input.subSequence(index, input.length()).toString());

        // Construct result
        int resultSize = matchList.size();
        if (limit == 0)
            while (resultSize > 0 && matchList.get(resultSize-1).equals(""))
                resultSize--;
        String[] result = new String[resultSize];
        return matchList.subList(0, resultSize).toArray(result);
    }
	
}
