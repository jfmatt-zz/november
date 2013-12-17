package edu.american.november.Assembler;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import edu.american.november.*;
import edu.american.november.AsmParser.Base16Context;
import edu.american.november.AsmParser.BinaryInstContext;
import edu.american.november.AsmParser.BinaryMnemonicContext;
import edu.american.november.AsmParser.BinaryOperandsContext;
import edu.american.november.AsmParser.Idx16Context;
import edu.american.november.AsmParser.Imm16Context;
import edu.american.november.AsmParser.Imm8Context;
import edu.american.november.AsmParser.InitContext;
import edu.american.november.AsmParser.LabelContext;
import edu.american.november.AsmParser.LeaMnemonicContext;
import edu.american.november.AsmParser.LeaOperandsContext;
import edu.american.november.AsmParser.LoadAddressContext;
import edu.american.november.AsmParser.MemoryContext;
import edu.american.november.AsmParser.NullaryInstContext;
import edu.american.november.AsmParser.NullaryMnemonicContext;
import edu.american.november.AsmParser.PtrContext;
import edu.american.november.AsmParser.Reg16Context;
import edu.american.november.AsmParser.Reg8Context;
import edu.american.november.AsmParser.RetContext;
import edu.american.november.AsmParser.Seg16Context;
import edu.american.november.AsmParser.SegInstContext;
import edu.american.november.AsmParser.SegMnemonicContext;
import edu.american.november.AsmParser.SegOperandsContext;
import edu.american.november.AsmParser.StatementContext;
import edu.american.november.AsmParser.UnaryInstContext;
import edu.american.november.AsmParser.UnaryMnemonicContext;
import edu.american.november.AsmParser.UnaryOperandContext;

public class Assembler extends CompileStep {
	
	public Assembler(InputStream i, OutputStream o) {
		super(i, o);
	}
	public Assembler(InputStream i, CompileStep next) {
		super(i, next);
		
	}
	public Assembler(InputStream i) {
		super(i);
	}
	
	@Override
	public void setInputStream(InputStream i) {
		super.setInputStream(i);
		if (this.getOutputStream() != null)
			((Listener) this.listener).setOutputStream(this.getOutputStream());
	}
	
	@Override
	public void setOutputStream(OutputStream o) {
		super.setOutputStream(o);
		if (this.listener != null)
			((Listener) this.listener).setOutputStream(o);
	}
	
	@Override
	protected Listener buildListener() {
		return new Listener();
	}
	
	@Override
	protected RuleContext doParse() {
		return ((AsmParser) this.parser).init();
	}

	@Override
	protected AsmLexer buildLexer(CharStream is) {
		return new AsmLexer(is);
	}

	@Override
	protected AsmParser buildParser(TokenStream tokens) {
		return new AsmParser(tokens);
	}
	
	private static class Listener implements AsmListener {
		private Queue<AssemblerOutput> backlog;
		private PrintWriter out;
		private OutputStream os;
		
		private AssemblerOutput current;
		
		public Listener() {
			backlog = new LinkedList<AssemblerOutput>();
		}
		
		public void setOutputStream(OutputStream o) {
			this.out = new PrintWriter(o, true);
			this.os = o;
		}
		
		@Override
		public void visitTerminal(TerminalNode node) {}

		@Override
		public void visitErrorNode(ErrorNode node) {}

		@Override
		public void enterEveryRule(ParserRuleContext ctx) {}

		@Override
		public void exitEveryRule(ParserRuleContext ctx) {}

		@Override
		public void enterRet(RetContext ctx) {}

		@Override
		public void exitRet(RetContext ctx) {}

		@Override
		public void enterIdx16(Idx16Context ctx) {}

		@Override
		public void exitIdx16(Idx16Context ctx) {}

		@Override
		public void enterUnaryInst(UnaryInstContext ctx) {}

		@Override
		public void exitUnaryInst(UnaryInstContext ctx) {
//			backlog.add(new UnaryOutput(ctx));
		}

		@Override
		public void enterSeg16(Seg16Context ctx) {}

		@Override
		public void exitSeg16(Seg16Context ctx) {}

		@Override
		public void enterNullaryMnemonic(NullaryMnemonicContext ctx) {}

		@Override
		public void exitNullaryMnemonic(NullaryMnemonicContext ctx) {}

		@Override
		public void enterSegMnemonic(SegMnemonicContext ctx) {}

		@Override
		public void exitSegMnemonic(SegMnemonicContext ctx) {}

		@Override
		public void enterBinaryInst(BinaryInstContext ctx) {}

		@Override
		public void exitBinaryInst(BinaryInstContext ctx) {}

		@Override
		public void enterUnaryMnemonic(UnaryMnemonicContext ctx) {}

		@Override
		public void exitUnaryMnemonic(UnaryMnemonicContext ctx) {}

		@Override
		public void enterReg8(Reg8Context ctx) {}

		@Override
		public void exitReg8(Reg8Context ctx) {}

		@Override
		public void enterBase16(Base16Context ctx) {}

		@Override
		public void exitBase16(Base16Context ctx) {}

		@Override
		public void enterUnaryOperand(UnaryOperandContext ctx) {}

		@Override
		public void exitUnaryOperand(UnaryOperandContext ctx) {}

		@Override
		public void enterLeaMnemonic(LeaMnemonicContext ctx) {}

		@Override
		public void exitLeaMnemonic(LeaMnemonicContext ctx) {}

		@Override
		public void enterSegOperands(SegOperandsContext ctx) {}

		@Override
		public void exitSegOperands(SegOperandsContext ctx) {}

		@Override
		public void enterLoadAddress(LoadAddressContext ctx) {}

		@Override
		public void exitLoadAddress(LoadAddressContext ctx) {}

		@Override
		public void enterLeaOperands(LeaOperandsContext ctx) {}

		@Override
		public void exitLeaOperands(LeaOperandsContext ctx) {}

		@Override
		public void enterInit(InitContext ctx) {}

		@Override
		public void exitInit(InitContext ctx) {}

		@Override
		public void enterLabel(LabelContext ctx) {}

		@Override
		public void exitLabel(LabelContext ctx) {}

		@Override
		public void enterMemory(MemoryContext ctx) {}

		@Override
		public void exitMemory(MemoryContext ctx) {}

		@Override
		public void enterStatement(StatementContext ctx) {
		}

		@Override
		public void exitStatement(StatementContext ctx) {
			//At the end of each statement, output all that are now fully resolved
			for (AssemblerOutput ao : backlog) {
				if (ao.isDone()) {
					try {
						backlog.remove();
						this.os.write(ao.getBytes());
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				else {
					break;
				}
			}
		}

		@Override
		public void enterSegInst(SegInstContext ctx) {}

		@Override
		public void exitSegInst(SegInstContext ctx) {}

		@Override
		public void enterBinaryOperands(BinaryOperandsContext ctx) {}

		@Override
		public void exitBinaryOperands(BinaryOperandsContext ctx) {}

		@Override
		public void enterReg16(Reg16Context ctx) {}

		@Override
		public void exitReg16(Reg16Context ctx) {}

		@Override
		public void enterImm8(Imm8Context ctx) {}

		@Override
		public void exitImm8(Imm8Context ctx) {}

		@Override
		public void enterImm16(Imm16Context ctx) {}

		@Override
		public void exitImm16(Imm16Context ctx) {}

		@Override
		public void enterBinaryMnemonic(BinaryMnemonicContext ctx) {}

		@Override
		public void exitBinaryMnemonic(BinaryMnemonicContext ctx) {}

		@Override
		public void enterNullaryInst(NullaryInstContext ctx) {
		}

		@Override
		public void exitNullaryInst(NullaryInstContext ctx) {
			backlog.add(new NullaryOutput(ctx));
		}

		@Override
		public void enterPtr(PtrContext ctx) {}

		@Override
		public void exitPtr(PtrContext ctx) {}
		
	}

}
