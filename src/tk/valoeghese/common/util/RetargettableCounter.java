package tk.valoeghese.common.util;

import java.util.function.IntPredicate;

public class RetargettableCounter extends Counter {
	public RetargettableCounter(IntPredicate target) {
		super(target);
	}

	public RetargettableCounter(IntPredicate target, int startValue) {
		super(target, startValue);
	}

	public void setTarget(IntPredicate target) {
		this.target = target;
	}
}
