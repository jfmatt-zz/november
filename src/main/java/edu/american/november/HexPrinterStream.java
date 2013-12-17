package edu.american.november;

import java.io.OutputStream;

public class HexPrinterStream extends OutputStream {

	@Override
	public void write(int b) {
		System.out.print(Integer.toHexString(b) + " ");
	}

	@Override
	public void flush() {
		System.out.flush();
	}
}
