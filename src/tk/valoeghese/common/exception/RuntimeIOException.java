package tk.valoeghese.common.exception;

import java.io.IOException;

public final class RuntimeIOException extends RuntimeException {
	private static final long serialVersionUID = -4575562495461021317L;

	public RuntimeIOException(IOException e) {
		super(e);
	}
}
