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

	public static void forEachFileOfExtension(File root, String extension, Consumer<File> callback) {
		assert root.isDirectory() : "root file is not a directory! (FileUtils#forEachFileOfExtension)";
		extension = "." + extension;
		uForEachFileOfExtension(root, extension, callback);
	}

	private static void uForEachFileOfExtension(File root, String extension, Consumer<File> callback) {
		for (File file : root.listFiles()) {
			if (file.isDirectory()) {
				uForEachFileOfExtension(file, extension, callback);
			} else if (file.getName().endsWith(extension)) {
				callback.accept(file);
			}
		}
	}
}
