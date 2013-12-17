package edu.american.november.Assembler;

import edu.american.november.AsmParser.NullaryInstContext;

public class NullaryOutput extends AssemblerOutput {

	private static CaseInsensitiveMap<Byte> opcodes;
	static {
		opcodes = new CaseInsensitiveMap<Byte>();
		opcodes.put("AAA",   (byte) 0x37);
		opcodes.put("AAD",   (byte) 0xD5);
		opcodes.put("AAM",   (byte) 0xD4);
		opcodes.put("AAS",   (byte) 0x3F);
		opcodes.put("CBW",   (byte) 0x98);
		opcodes.put("CLC",   (byte) 0xF8);
		opcodes.put("CLD",   (byte) 0xFC);
		opcodes.put("CLI",   (byte) 0xFA);
		opcodes.put("CMC",   (byte) 0xF5);
		opcodes.put("CWD",   (byte) 0x99);
		opcodes.put("DAA",   (byte) 0x27);
		opcodes.put("DAS",   (byte) 0x2F);
		opcodes.put("IRET",  (byte) 0xCF);
		opcodes.put("LAHF",  (byte) 0x9F);
		opcodes.put("LOCK",  (byte) 0xF0);
		opcodes.put("LODSB", (byte) 0xAC);
		opcodes.put("LODSW", (byte) 0xAD);
		opcodes.put("MOVSB", (byte) 0xA4);
		opcodes.put("MOVSW", (byte) 0xA5);
		opcodes.put("NOP",   (byte) 0x90);
		opcodes.put("POPF",  (byte) 0x9D);
		opcodes.put("PUSHF", (byte) 0x9C);
		opcodes.put("REPZ",  (byte) 0xF3);
		opcodes.put("REPNZ", (byte) 0xF4);
		opcodes.put("SAHF",  (byte) 0x9E);
		opcodes.put("SCASB", (byte) 0xAE);
		opcodes.put("SCASW", (byte) 0xAF);
		opcodes.put("STC",   (byte) 0xF9);
		opcodes.put("STD",   (byte) 0xFD);
		opcodes.put("STI",   (byte) 0xFB);
		opcodes.put("STOSB", (byte) 0xAA);
		opcodes.put("STOSW", (byte) 0xAB);
		opcodes.put("WAIT",  (byte) 0x9B);
		opcodes.put("XLAT",  (byte) 0xD7);
	}
	
	public NullaryOutput(NullaryInstContext ctx) {
		String mnemonic = ctx.nullaryMnemonic().getText();

		//These two have an extra argument in machine code for some reason
		if (mnemonic.equalsIgnoreCase("AAM") || mnemonic.equalsIgnoreCase("AAD")) {
			this.bytes = new byte[2];
			this.bytes[1] = (byte) 0x0A;
		}
		else {
			this.bytes = new byte[1];
		}
		
		//Load opcode
		System.out.println(mnemonic);
		this.bytes[0] = opcodes.get(mnemonic);
	}

	@Override
	public boolean isDone() {
		return true;
	}
	
}
