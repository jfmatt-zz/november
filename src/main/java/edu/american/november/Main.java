package edu.american.november;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {
	private static final String TEST_FILE = "lists.test";

	public static void main(String...args) {
		try {
			CharStream stream = null;
			if (args.length > 0) {
				stream = new ANTLRNoCaseFileStream(args[0]);
			} else {
				stream = new ANTLRNoCaseInputStream(Main.class.getClassLoader().getResourceAsStream(TEST_FILE));
			}
			AsmLexer lexer = new AsmLexer(stream);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			AsmParser parser = new AsmParser(tokens);

			String sourceName = parser.getSourceName() == null? TEST_FILE : parser.getSourceName();
			
			System.out.println("Parsing: " + sourceName);

			System.out.println(parser.program().toStringTree(parser));
			
			System.out.println("done");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
