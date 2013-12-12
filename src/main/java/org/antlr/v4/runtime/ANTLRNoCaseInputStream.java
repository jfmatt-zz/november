package org.antlr.v4.runtime;

public class ANTLRNoCaseInputStream extends ANTLRInputStream {
	public ANTLRNoCaseInputStream(java.io.InputStream is)
	throws java.io.IOException {
		super(is);
	}
	public ANTLRNoCaseInputStream(char[] str, int count) {
		super(str, count);
	}
	
	@Override
    public int LA(int i) {
        if ( i==0 ) {
            return 0; // undefined
        }
        if ( i<0 ) {
            i++; // e.g., translate LA(-1) to use offset 0
        }
 
        if ( (p+i-1) >= n ) {
 
            return CharStream.EOF;
        }

//       	System.out.println("LA: " + Character.toUpperCase(data[p + i - 1]));
        return Character.toUpperCase(data[p+i-1]);
	}
}
