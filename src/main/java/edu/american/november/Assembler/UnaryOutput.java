package edu.american.november.Assembler;

import edu.american.november.AsmParser.UnaryInstContext;

public class UnaryOutput extends AssemblerOutput {
	
	String mnemonic;
	
	public UnaryOutput(UnaryInstContext ctx) {
		mnemonic = ctx.unaryMnemonic().getText();
		if (mnemonic.equalsIgnoreCase("PUSH"))
			;
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return false;
	}

}
