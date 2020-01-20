package tk.valoeghese.common.util;

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
