package edu.american.november;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;

import org.antlr.v4.runtime.*;

/** 
 * WARNING:
 * This class makes heavy use of java.lang.reflect and the wanton disregard for
 * exceptions that comes with it.
 * 
 * Do not read if you actually like anything about Java's type system and are offended
 * by its abuse.
 *
 * @author James Matthews
 *
 * @param <L> A subclass of org.antlr.v4.runtime.Lexer
 * @param <P> A subclass of org.antlr.v4.runtime.Parser. The root rule MUST be named 'init'.
 * 
 */

public class GenericCompileStep<L extends Lexer, P extends Parser> {
	private OutputStream os;
	private CharStream is;
	private CommonTokenStream tokens;
	private L lexer;
	private P parser;
	
	private Class<L> LexClass;
	private Class<P> ParseClass;
	
	//Factories
	public static <L2 extends Lexer, P2 extends Parser> GenericCompileStep<L2, P2>
	F(Class<L2> LC, Class<P2> PC, InputStream i, OutputStream o) {
		return new GenericCompileStep<>(LC, PC, i, o);
	}
	public static <L2 extends Lexer, P2 extends Parser> GenericCompileStep<L2, P2>
	F(Class<L2> LC, Class<P2> PC, InputStream i) {
		return new GenericCompileStep<>(LC, PC, i);
	}
	public static <L2 extends Lexer, P2 extends Parser> GenericCompileStep<L2, P2>
	F(Class<L2> LC, Class<P2> PC) {
		return new GenericCompileStep<>(LC, PC);
	}
	
	/**
	 * Invocation will generally look something like:
	 * <pre>
	 * GenericCompileStep<MyLexer, MyParser> c = new GenericCompileStep<>(MyLexer.class, MyParser.class);
	 * </pre>
	 * This is acknowledged as monstrous. Blame type erasure.
	 * 
	 * @param LC The lexer class object, should correspond to the first type parameter
	 * @param PC The parser class object; should correspond to the second type parameter
	 */
	public GenericCompileStep(Class<L> LC, Class<P> PC, InputStream i, OutputStream o) {
		this.LexClass = LC;
		this.ParseClass = PC;
		
		this.lexer = null;
		this.parser = null;
		
		this.setInputStream(i);
		this.setOutputStream(o);
	}
	public GenericCompileStep(Class<L> LC, Class<P> PC, InputStream i) {
		this.LexClass = LC;
		this.ParseClass = PC;
		
		this.lexer = null;
		this.parser = null;
		
		this.setInputStream(i);
	}
	public GenericCompileStep(Class<L> LC, Class<P> PC) {
		this.LexClass = LC;
		this.ParseClass = PC;
		
		this.lexer = null;
		this.parser = null;
	}
	
	/**
	 * Let's do it.
	 * 
	 * This method should be called in a separate thread from all other `GenericCompileStep`s
	 * on this pipe chain.
	 */
	public void execute() {

		PrintWriter out = new PrintWriter(this.os, true);		
		RuleContext result;

		try {
			result = (RuleContext) ParseClass.getMethod("init").invoke(parser);
		
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
				this.lexer = this.LexClass.getConstructor(CharStream.class).newInstance(this.is);
			else
				this.lexer.setInputStream(this.is);
			
			this.tokens = new CommonTokenStream(this.lexer);
			
			if (this.parser == null)
				this.parser = this.ParseClass.getConstructor(TokenStream.class).newInstance(this.tokens);
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
	 * Pipe to another GenericCompileStep
	 * 
	 * Usage: preprocessor.pipe(compiler);
	 * 
	 * @param other the subsequent GenericCompileStep; this Step's output becomes `other`'s input
	 */
	public <L2 extends Lexer, P2 extends Parser> void pipe(GenericCompileStep<L2, P2> other) {
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
}
