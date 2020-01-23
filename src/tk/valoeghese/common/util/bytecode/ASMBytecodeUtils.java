package tk.valoeghese.common.util.bytecode;

import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;

final class ASMBytecodeUtils {
	private ASMBytecodeUtils() {
	}

	public static int getAccessFlags(InputStream inputStream) throws IOException {
		return new ClassReader(inputStream).getAccess();
	}
}
