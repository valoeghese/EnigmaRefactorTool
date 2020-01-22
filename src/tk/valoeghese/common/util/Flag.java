package tk.valoeghese.common.util;

public class Flag {
	public Flag(boolean value) {
		this.value = value;
	}

	public Flag() {
	}

	private boolean value = false;

	public void setValue(boolean value) {
		this.value = value;
	}

	public void checkFlagIndex(int flags, int flagIndex) {
		int flag = (1 << flagIndex);
		this.value = (flags & flag) != 0;
	}

	public void checkFlagValue(int flags, int flagValue) {
		this.value = (flags & flagValue) != 0;
	}

	public void flag() {
		this.value = true;
	}

	public void unflag() {
		this.value = false;
	}

	public boolean booleanValue() {
		return this.value;
	}

	public static boolean isIndexFlagged(int flags, int flagIndex) {
		int flag = (1 << flagIndex);
		return (flags & flag) != 0;
	}
}
