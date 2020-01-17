package tk.valoeghese.ertool.pkgrefactor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import tk.valoeghese.common.util.FileUtils;

public final class PackageRefactor {
	private PackageRefactor() {
	}

	private static final Map<String, String> refactorCache = new HashMap<>();

	public static void refactorPackage(String in, String out) {
		File mappingsDirectory = new File("mappings/");
		assert mappingsDirectory.isDirectory() : "No ./mappings/ directory found";

		// Make output directory if it does not exist
		File outDirectory = new File("mappings/" + out);
		outDirectory.mkdirs();
		String outAbsolutePath = outDirectory.getAbsolutePath();

		// Move packages and change class declarations
		File inDirectory = new File("mappings/" + in);
		for (File file : inDirectory.listFiles()) {
			String absolutePath = file.getAbsolutePath();
			if (file.isFile() && absolutePath.endsWith(".mapping")) { // is mapping file
				// refactor stuff
				String nameWithExt = file.getName();
				String name = nameWithExt.substring(0, nameWithExt.length() - 8);

				FileUtils.modifyFile(file, data -> {
					String inExpr = in.concat("/" + name).replace("$", "\\$");
					String outExpr = out + "/" + name;
					refactorCache.put("L" + inExpr, outExpr);

					return data.replaceAll(inExpr, outExpr);
				});

				FileUtils.relocateFile(file, outAbsolutePath);
			}
		}
	}
}
