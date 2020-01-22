package tk.valoeghese.common.util.function;

import java.io.IOException;

@FunctionalInterface
public interface NullConsumer {
	void run();

	@FunctionalInterface
	public static interface IOThrowing {
		void run() throws IOException;
	}
}
