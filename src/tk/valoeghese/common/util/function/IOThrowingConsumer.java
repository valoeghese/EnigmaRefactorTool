package tk.valoeghese.common.util.function;

import java.io.IOException;

public interface IOThrowingConsumer<T> {
	void accept(T arg0) throws IOException;
}
