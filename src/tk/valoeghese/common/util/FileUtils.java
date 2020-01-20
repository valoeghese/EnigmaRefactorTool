package tk.valoeghese.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import tk.valoeghese.common.exception.RuntimeIOException;

public final class FileUtils {
	private FileUtils() {
	}

	/**
	 * @throws RuntimeIOException if there was an IOException.
	 */
	public static void readLines(String path, Consumer<String> lineConsumer) {
		try (Stream<String> lineStream = Files.lines(Paths.get(path))) {
			lineStream.forEach(line -> {
				if (!line.trim().isEmpty()) {
					lineConsumer.accept(line);
				}
			});
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}

	/**
	 * @throws RuntimeIOException if there was an IOException.
	 */
	public static void modifyFile(File file, Function<String, String> modificationFunction) {
		String data = null;

		try (FileInputStream fileInput = new FileInputStream(file)) {
			data = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), StandardCharsets.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}

		try (PrintWriter fileOutput = new PrintWriter(file)) {
			fileOutput.println(modificationFunction.apply(data));
		} catch (FileNotFoundException e) {
			throw new RuntimeIOException(e);
		}
	}

	/**
	 * @throws RuntimeIOException if there was an IOException.
	 */
	public static void readFirstLine(String path, Consumer<String> lineConsumer) {
		try (Stream<String> lineStream = Files.lines(Paths.get(path))) {
			lineConsumer.accept(lineStream.findFirst().get());
		} catch (IOException e) {
			throw new RuntimeIOException(e);
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
}
