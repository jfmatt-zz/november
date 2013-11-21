package edu.american.november;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;

public class Main {
	private static final String TEST_FILE = "lists.test";

	public static void main(String...args) {
		try {
			CharStream stream = null;
			if (args.length > 0) {
				stream = new ANTLRFileStream(args[0]);
			} else {
				stream = new ANTLRInputStream(Main.class.getClassLoader().getResourceAsStream(TEST_FILE));
			}
			NestedNameListLexer lexer = new NestedNameListLexer(stream);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			NestedNameListParser parser = new NestedNameListParser(tokens);

			String sourceName = parser.getSourceName() == null? TEST_FILE : parser.getSourceName();
			
			System.out.println("Parsing: " + sourceName);

			parser.elements();
			
			System.out.println("done");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
