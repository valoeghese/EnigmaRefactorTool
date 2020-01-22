package tk.valoeghese.common.util;

import java.io.IOException;

import tk.valoeghese.common.util.FileUtils.InputStreamSupplier;

public final class BytecodeUtils {
	private BytecodeUtils() {
	}

	public static boolean isInterface(InputStreamSupplier inputStreamSupplier) throws IOException {
		Flag result = new Flag();

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
				} else { // FieldRef, MethodRef, InterfaceMethodRef, NameAndType, Int, Float, InvokeDynamic
					stream.skipBytes(4);
				} // tags 2, 13, 14, 17, 19+ are invalid/unused
			}
			
			int accessFlags = stream.readUnsignedShort();

			result.checkFlagValue(accessFlags, 0x200);
		});

		return result.booleanValue();
	}
}
