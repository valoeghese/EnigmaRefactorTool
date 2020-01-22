package tk.valoeghese.common.util;

import java.io.IOException;

import tk.valoeghese.common.util.FileUtils.InputStreamSupplier;

public final class BytecodeUtils {
	private BytecodeUtils() {
	}

	public static boolean isInterface(InputStreamSupplier inputStreamSupplier) throws IOException {
		return (getAccessFlags(inputStreamSupplier) & 0x200) != 0;
	}

	public static int getAccessFlags(InputStreamSupplier inputStreamSupplier) throws IOException {
		IntWrapper result = new IntWrapper();

		FileUtils.openDataInputStream(inputStreamSupplier, stream -> {
			int magic = stream.readInt();
			assert magic == 0xCAFEBABE;

			stream.skipBytes(4); // version
			int constantPoolCount = stream.readUnsignedShort();

			for (int i = 1; i < constantPoolCount; ++i) {
				byte tag = stream.readByte();

				if (tag == 7 || tag == 8 || tag == 16) { // ClassInfo, String, MethodType
					stream.skipBytes(2);
				} else if (tag == 5 || tag == 6) { // Long, Double
					stream.skipBytes(8);
				} else if (tag == 1) { // UTF8
					int length = stream.readUnsignedShort();
					stream.skipBytes(length);
				} else if (tag == 15) { // MethodHandle
					stream.skipBytes(3);
				} else if (tag == 2 || tag == 13 || tag == 14 || tag == 17 || tag > 18) {
					throw new RuntimeException("Invalid tag");
				} else { // FieldRef, MethodRef, InterfaceMethodRef, NameAndType, Int, Float, InvokeDynamic
					stream.skipBytes(4);
				} // tags 2, 13, 14, 17, 19+ are invalid/unused
			}

			int accessFlags = stream.readUnsignedShort();
			result.setValue(accessFlags);
		});

		return result.intValue();
	}
}
