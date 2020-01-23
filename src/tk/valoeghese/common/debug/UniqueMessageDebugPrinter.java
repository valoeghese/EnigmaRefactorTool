package tk.valoeghese.common.debug;

import java.util.HashSet;
import java.util.Set;

public final class UniqueMessageDebugPrinter implements DebugPrinter {
	private final Set<String> sentMessages = new HashSet<>();

	public void println(String message) {
		if (!sentMessages.contains(message)) {
			sentMessages.add(message);
			System.out.println(message);
		}
	}

	@Override
	public void println(int message) {
		this.println(String.valueOf(message));
	}
}
