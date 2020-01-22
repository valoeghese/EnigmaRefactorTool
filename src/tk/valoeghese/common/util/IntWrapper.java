package tk.valoeghese.common.util;

import tk.valoeghese.common.util.function.IntToIntFunction;

public class IntWrapper {
	public IntWrapper() {
		this.value = 0;
	}

	public IntWrapper(int startValue) {
		this.value = startValue;
	}

	private int value;

	public void setValue(int value) {
		this.value = value;
	}
	
	public void modifyValue(IntToIntFunction modifier) {
		this.value = modifier.applyAsInt(this.value);
	}

	public int intValue() {
		return this.value;
	}
}
