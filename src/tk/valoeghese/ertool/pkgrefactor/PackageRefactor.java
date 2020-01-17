package tk.valoeghese.ertool.pkgrefactor;

import java.io.File;

import tk.valoeghese.common.util.FileUtils;
import tk.valoeghese.ertool.Main;

public final class PackageRefactor {
	private PackageRefactor() {
	}

	//	private static final Map<String, String> refactorCache = new HashMap<>();

	public static void refactorPackage(String in, String out, boolean subPackage) {
		File mappingsDirectory = new File("mappings/");
		assert mappingsDirectory.isDirectory() : "No ./mappings/ directory found";

		// Make output directory if it does not exist
		File outDirectory = new File("mappings/" + out);
		outDirectory.mkdirs();
		String outAbsolutePath = outDirectory.getAbsolutePath();

		// Move packages and change class declarations
		File inDirectory = new File("mappings/" + in);

		if (subPackage) {
			FileUtils.forEachFileOfExtension(inDirectory, "mapping", file -> modifyFile(file, in, out, outAbsolutePath));
		} else {
			for (File file : inDirectory.listFiles()) {
				String absolutePath = file.getAbsolutePath();

				if (file.isFile() && absolutePath.endsWith(".mapping")) { // is mapping file
					modifyFile(file, in, out, outAbsolutePath);
				}
			}
		}
	}

	private static void modifyFile(File file, String in, String out, String outAbsolutePath) {
		// refactor stuff
		String nameWithExt = file.getName();
		String name = nameWithExt.substring(0, nameWithExt.length() - 8);

		FileUtils.modifyFile(file, data -> {
			String inExpr = in.concat("/" + name);
			String outExpr = out + "/" + name;

			if (Main.programArgs.beVerbose()) {
				System.out.println(inExpr + "\t->\t" + outExpr);
			}

			inExpr = inExpr.replace("$", "\\$");
			// refactorCache.put(inExpr, outExpr);
			return data.replaceAll(inExpr, outExpr);
		});

		FileUtils.relocateFile(file, outAbsolutePath);
	}
}
