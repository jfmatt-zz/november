package edu.american.november;

import java.io.InputStream;
import java.io.OutputStream;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.TokenStream;

public class AssembleStep extends CompileStep {

	public AssembleStep(InputStream i, OutputStream o) {
		super(i, o);
	}
	public AssembleStep(InputStream i, CompileStep next) {
		super(i, next);
	}
	public AssembleStep(InputStream i) {
		super(i);
	}

	@Override
	RuleContext doParse() {
		return ((AsmParser) this.parser).init();
	}

	@Override
	Lexer buildLexer(CharStream is) {
		return new AsmLexer(is);
	}

	@Override
	Parser buildParser(TokenStream tokens) {
		return new AsmParser(tokens);
	}

}
