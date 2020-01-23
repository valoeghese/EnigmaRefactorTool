package tk.valoeghese.ertool;

import java.io.IOException;
import java.util.zip.ZipFile;

import org.objectweb.asm.ClassReader;

import tk.valoeghese.common.io.Console;
import tk.valoeghese.common.util.bytecode.BytecodeUtils;

public final class TestMain {
	private static int counter = 0;

	private TestMain() {
	}

	public static void main(String[] args) throws IOException {
		//		FileUtils.readFileBytes(new File("EE.class"), TestMain::printByte);
		testZipReading();
		//byte b = (byte) 0xFF;
		//System.out.println((int) (b & 0xFF));
		//System.out.println(new byte[8].length);
	}

	static void testZipReading() throws IOException {
		ZipFile zipFile = new ZipFile("./intermediary.jar");

		String clasz =// "net/minecraft/class_3630.class";
		"net/minecraft/class_4481.class";

		ClassReader reader = new ClassReader(zipFile.getInputStream(zipFile.getEntry(clasz)));

//		System.out.println(reader.readConst(723, new char[Short.MAX_VALUE])); // this crashes asm

		Console.out.printFlags(reader.getAccess());
		Console.out.printFlags(BytecodeUtils.getAccessFlags(() -> zipFile.getInputStream(zipFile.getEntry(clasz))));

		zipFile.close();
	}

	static boolean readByte(int byt3) {
		if (counter++ == 9) {
			System.out.println(byt3 == 7);
			return false;
		}

		return true;
	}

	static boolean printByte(int byt3) {
		if (counter++ == 10) {
			System.out.println();
			counter = 0;
		}

		System.out.print(byt3);

		if (counter != 10) {
			System.out.print("\t");
		}

		return true;
	}
}
