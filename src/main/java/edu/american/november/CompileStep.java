package edu.american.november;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;

import org.antlr.v4.runtime.*;

public abstract class CompileStep {
	OutputStream os;
	CharStream is;
	TokenStream tokens;

	Parser parser;
	Lexer lexer;
	
	public CompileStep(InputStream i, OutputStream o) {
		this.setInputStream(i);
		this.setOutputStream(o);
	}
	public CompileStep(InputStream i, CompileStep next) {
		this.setInputStream(i);
		this.pipe(next);
	}
	public CompileStep(InputStream i) {
		this.setInputStream(i);
	}
		
	public void execute() {

		PrintWriter out = new PrintWriter(this.os, true);		
		RuleContext result;

		try {
			result = this.doParse();
		
			out.println(result.toStringTree(parser));
			out.println("done");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void setInputStream(InputStream i) {
		try {
			this.setInputStream(new ANTLRNoCaseInputStream(i));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setInputStream(CharStream i) {
		try {
			this.is = i;
			
			if (this.lexer == null)
				this.lexer = this.buildLexer(this.is);
			else
				this.lexer.setInputStream(this.is);
			
			this.tokens = new CommonTokenStream(this.lexer);
			
			if (this.parser == null)
				this.parser = this.buildParser(this.tokens);
			else
				this.parser.setTokenStream(this.tokens);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public OutputStream getOutputStream() {
		return this.os;
	}
	
	public void setOutputStream(OutputStream o) {
		this.os = o;
	}
	
	/**
	 * Pipe to another CompileStep
	 * 
	 * Usage: preprocessor.pipe(compiler);
	 * 
	 * @param other the subsequent CompileStep; this Step's output becomes `other`'s input
	 */
	public void pipe(CompileStep other) {
		PipedInputStream pis = new PipedInputStream();
		PipedOutputStream pos = new PipedOutputStream();
		try {
			pis.connect(pos);
			other.setInputStream(pis);
			this.setOutputStream(pos);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	abstract RuleContext doParse();
	abstract Lexer buildLexer(CharStream is);
	abstract Parser buildParser(TokenStream tokens);
	
}
