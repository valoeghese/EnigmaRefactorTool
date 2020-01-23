package tk.valoeghese.common.util;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import tk.valoeghese.common.io.ByteDataInput;
import tk.valoeghese.common.io.LittleEndianInputStream;
import tk.valoeghese.common.util.function.IOThrowingConsumer;
import tk.valoeghese.common.util.function.NullConsumer;

public final class FileUtils {
	private FileUtils() {
	}

	/**
	 * @throws IOException.
	 */
	public static void readLines(String path, Consumer<String> lineConsumer) throws IOException {
		try (Stream<String> lineStream = Files.lines(Paths.get(path))) {
			lineStream.forEach(line -> {
				if (!line.trim().isEmpty()) {
					lineConsumer.accept(line);
				}
			});
		}
	}

	/**
	 * @throws IOException.
	 */
	public static void modifyFile(File file, Function<String, String> modificationFunction) throws IOException {
		String data = null;

		try (FileInputStream fileInput = new FileInputStream(file)) {
			data = modificationFunction.apply(new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), StandardCharsets.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		try (PrintWriter fileOutput = new PrintWriter(file)) {
			fileOutput.print(data);
		}
	}

	/**
	 * @throws IOException.
	 */
	public static void readFirstLine(String path, Consumer<String> lineConsumer) throws IOException {
		try (Stream<String> lineStream = Files.lines(Paths.get(path))) {
			lineConsumer.accept(lineStream.findFirst().get());
		}
	}

	/**
	 * @throws SecurityException if a security manager exists and denies access, as specified in {@link File#renameTo(File)}, used in this method
	 */
	public static void relocateFile(File file, String newFolderLocation) throws SecurityException {
		file.renameTo(new File(newFolderLocation + "/" + file.getName()));
	}

	/**
	 * @throws SecurityException if a security manager exists and denies access, as specified in {@link File#renameTo(File)} or {@link File#getAbsoluteFile()}, used in this method
	 */
	public static void renameFile(File file, String newFileName) throws SecurityException {
		file.renameTo(new File(file.getAbsoluteFile().getParent() + "/" + newFileName));
	}

	public static void trailFilesOfExtension(File root, String extension, BiConsumer<File, String> callback) {
		assert root.isDirectory() : "root file is not a directory! (FileUtils#trailFilesOfExtension)";
		extension = "." + extension;
		uTrailFilesOfExtension(root, extension, callback, "");
	}

	private static void uTrailFilesOfExtension(File root, String extension, BiConsumer<File, String> callback, String directoryTrail) {
		for (File file : root.listFiles()) {
			if (file.isDirectory()) {
				uTrailFilesOfExtension(file, extension, callback, directoryTrail + "/" + file.getName());
			} else if (file.getName().endsWith(extension)) {
				callback.accept(file, directoryTrail);
			}
		}
	}

	public static void trailZipFileItems(String zipFile, BiConsumer<ZipFile, ZipEntry> callback) throws IOException {
		ZipFile zip = new ZipFile(zipFile);
		Enumeration<? extends ZipEntry> entries = zip.entries();

		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			callback.accept(zip, entry);
		}

		zip.close();
	}

	public static void forEachFileOfExtension(File directory, String extension, Consumer<File> callback) {
		assert directory.isDirectory() : "directory file is not a directory! (FileUtils#forEachFileOfExtension)";
		extension = "." + extension;

		for (File file : directory.listFiles()) {
			if (!file.isDirectory() && file.getName().endsWith(extension)) {
				callback.accept(file);
			}
		}
	}

	public static void cleanupEmptyDirectories(File root) {
		assert root.isDirectory() : "root file is not a directory! (FileUtils#cleanupEmptyDirectories)";
		File[] files = root.listFiles();

		if (files.length == 0) {
			root.delete();
		} else {
			uCleanupEmptyDirectories(files);
			files = root.listFiles();

			if (files.length == 0) {
				root.delete();
			}
		}
	}

	private static void uCleanupEmptyDirectories(File[] files) {
		for (File file : files) {
			if (file.isDirectory()) {
				File[] newFiles = file.listFiles();

				if (newFiles.length == 0) {
					file.delete();
				} else {
					uCleanupEmptyDirectories(newFiles);
					newFiles = file.listFiles();

					if (newFiles.length == 0) {
						file.delete();
					}
				}
			}
		}
	}

	public static void writeFile(File file, Consumer<PrintWriter> writeFunction) throws IOException {
		if (!file.exists()) {
			file.createNewFile();
		}

		try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
			writeFunction.accept(writer);
		}
	}

	public static void writeStringToFile(File file, String data) throws IOException {
		writeFile(file, writer -> writer.print(data));
	}

	public static void readFileBytes(File file, IntPredicate callback) throws IOException {
		try (FileInputStream f = new FileInputStream(file)) {
			readBytes(f, callback);
		}
	}

	public static void readBytes(InputStream inputStream, IntPredicate callback) throws IOException {
		int data;

		while ((data = inputStream.read()) != -1) { 
			if (!callback.test(data)) {
				return;
			}
		}
	}

	public static void badlyHandleIOException(NullConsumer.IOThrowing callback) throws UncheckedIOException {
		try {
			callback.run();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public static void openDataInputStream(InputStreamSupplier inputStreamSupplier, IOThrowingConsumer<DataInput> callback) throws IOException {
		try (DataInputStream dataInputStream = new DataInputStream(inputStreamSupplier.get())) {
			callback.accept(dataInputStream);
		}
	}

	public static void openLittleEndianInputStream(InputStreamSupplier inputStreamSupplier, IOThrowingConsumer<ByteDataInput> callback) throws IOException {
		try (LittleEndianInputStream leInputStream = new LittleEndianInputStream(inputStreamSupplier.get())) {
			callback.accept(leInputStream);
		}
	}

	public static interface InputStreamSupplier {
		InputStream get() throws IOException;
	}
}
