package tk.valoeghese.common.util.bytecode;

import java.io.DataInput;
import java.io.IOException;

import tk.valoeghese.common.util.FileUtils;
import tk.valoeghese.common.util.FileUtils.InputStreamSupplier;
import tk.valoeghese.common.util.IntWrapper;

public final class BytecodeUtils {
	private BytecodeUtils() {
	}

	private static boolean useASM = false;

	public static void setUseASM() {
		useASM = true;
	}

	public static boolean isInterface(InputStreamSupplier inputStreamSupplier) throws IOException {
		return (getAccessFlags(inputStreamSupplier) & 0x200) != 0;
	}

	public static int getAccessFlags(InputStreamSupplier inputStreamSupplier) throws IOException {
		if (useASM) {
			return ASMBytecodeUtils.getAccessFlags(inputStreamSupplier.get());
		}

		IntWrapper result = new IntWrapper();
		//DebugPrinter umn = new UniqueMessageDebugPrinter();

		FileUtils.openDataInputStream(inputStreamSupplier, stream -> {
			int magic = stream.readInt();

			if (magic != 0xCAFEBABE) {
				throw new RuntimeException("Attempted to read invalid class file");
			}

			skipBytes(stream, 4); // version
			int constantPoolCount = stream.readUnsignedShort();
			int previousTag = -1;

			for (int i = 1; i < constantPoolCount; ++i) {
				byte tag = stream.readByte();
				//umn.println(tag);

				switch (tag) {
				case CPTag.UTF8:
					int length = stream.readUnsignedShort();
					skipBytes(stream, length);
					break;
				case CPTag.LONG:
				case CPTag.DOUBLE:
					skipBytes(stream, 8);
					break;
				case CPTag.CLASS:
				case CPTag.STRING:
				case CPTag.METHODTYPE:
				case CPTag.PACKAGE:
				case CPTag.MODULE:
					skipBytes(stream, 2);
					break;
				case CPTag.METHODHANDLE:
					skipBytes(stream, 3);
					break;
				case CPTag.FIELDREF:
				case CPTag.METHODREF:
				case CPTag.IMETHODREF:
				case CPTag.NAMEANDTYPE:
				case CPTag.INT:
				case CPTag.FLOAT:
				case CPTag.INVOKEDYNAMIC:
					skipBytes(stream, 4);
					break;
				default:
					throw new RuntimeException("Invalid tag " + String.valueOf(tag) + " on constant pool entry " + String.valueOf(i) + "/" + String.valueOf(constantPoolCount - 1) + ". Previous tag: " + String.valueOf(previousTag));
				}

				previousTag = tag;
			}

			int accessFlags = stream.readUnsignedShort();
			result.setValue(accessFlags);
		});

		return result.intValue();
	}

	private static void skipBytes(DataInput stream, int bytes) throws IOException {
		if (stream.skipBytes(bytes) != bytes) {
			throw new RuntimeException("Failed to successfully skip " + String.valueOf(bytes) + " bytes!");
		}
	}
}
