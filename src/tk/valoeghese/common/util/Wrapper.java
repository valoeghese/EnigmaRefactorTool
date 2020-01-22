package tk.valoeghese.common.util;

public class Wrapper<T> {
	public Wrapper(T startValue) {
		this.value = startValue;
	}

	protected T value;

	public T getValue() {
		return this.value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Wrapper) {
			return ((Wrapper<?>) other).value.equals(this.value);
		}

		return false;
	}
}
