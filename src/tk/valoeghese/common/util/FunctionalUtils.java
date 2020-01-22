package tk.valoeghese.common.util;

import tk.valoeghese.common.util.function.NullConsumer;

public final class FunctionalUtils {
	private FunctionalUtils() {
	}

	public static boolean addFalseLogic(boolean b, NullConsumer callback) {
		if (!b) {
			callback.run();
		}

		return b;
	}
}
