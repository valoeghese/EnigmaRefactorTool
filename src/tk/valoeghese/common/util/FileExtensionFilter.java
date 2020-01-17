package tk.valoeghese.common.util;

import java.io.File;
import java.io.FilenameFilter;

public class FileExtensionFilter implements FilenameFilter {
	private FileExtensionFilter(String extension) {
		this.match = "." + extension;
	}

	private final String match;

	@Override
	public boolean accept(File dir, String name) {
		return name.endsWith(match);
	}

	public static FileExtensionFilter of(String extension) {
		return new FileExtensionFilter(extension);
	}
}
