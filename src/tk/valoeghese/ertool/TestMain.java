package tk.valoeghese.ertool;

import java.io.IOException;
import java.util.zip.ZipFile;

import tk.valoeghese.common.util.BytecodeUtils;

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

		//Enumeration<? extends ZipEntry> entries = zipFile.entries();

		//	    while(entries.hasMoreElements()) {
		//	        ZipEntry entry = entries.nextElement();
		//	        System.out.println(entry);
		//InputStream stream = zipFile.getInputStream(entry);
		//	    }

		System.out.println(BytecodeUtils.isInterface(() -> zipFile.getInputStream(zipFile.getEntry("net/minecraft/class_3658.class"))));
		//try(InputStream e = zipFile.getInputStream(zipFile.getEntry("net/minecraft/class_3658.class"))) {
		//	FileUtils.readBytes(e, TestMain::printByte);
		//}

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
