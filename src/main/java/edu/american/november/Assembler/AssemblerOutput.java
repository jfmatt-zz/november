package edu.american.november.Assembler;

import java.util.HashMap;

import edu.american.november.CompileStepOutput;

public abstract class AssemblerOutput extends CompileStepOutput {
	
	public String mnemonic;
	public boolean is16Bit;
	
	public byte[] bytes;

	public abstract boolean isDone();
	
	public byte[] getBytes() {
		return bytes;
	}
	
	public static class CaseInsensitiveMap<V> extends HashMap<String, V> {
	    @Override
	    public V put(String key, V value) {
	       return super.put(key.toUpperCase(), value);
	    }

	    public V get(String key) {
	       return super.get(key.toUpperCase());
	    }
	}
}
