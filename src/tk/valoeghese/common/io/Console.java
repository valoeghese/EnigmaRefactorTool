package tk.valoeghese.common.io;

import java.io.PrintStream;

public final class Console extends PrintStream {
	public Console(PrintStream parent) {
		super(parent);
	}

	public void printFlags(int flags) {
		StringBuilder sb = new StringBuilder();
		int flag = 1;
		boolean initialFlag = true;

		for (int i = 0; i < 32; ++i) {
			if (flags < flag) {
				break;
			}

			if ((flag & flags) != 0) {
				if (initialFlag) {
					initialFlag = false;
				} else {
					sb.append(" ");
				}

				sb.append("0x").append(Integer.toHexString(flag));
			}

			flag <<= 1;
		}

		this.println(sb.toString());
	}

	public static final Console out = new Console(System.out);
}
