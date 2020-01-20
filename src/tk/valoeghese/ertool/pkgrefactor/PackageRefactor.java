package tk.valoeghese.ertool.pkgrefactor;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import tk.valoeghese.common.util.FileUtils;
import tk.valoeghese.ertool.Main;

public final class PackageRefactor {
	private PackageRefactor() {
	}

	public static void refactorPackage(String in, String out, boolean subPackage) {
		File mappingsDirectory = new File("mappings/");
		assert mappingsDirectory.isDirectory() : "No ./mappings/ directory found";

		// Make output directory if it does not exist
		File outDirectory = new File("mappings/" + out);
		outDirectory.mkdirs();
		String outAbsolutePath = outDirectory.getAbsolutePath();

		// Move packages and change class declarations
		File inDirectory = new File("mappings/" + in);
		Set<String> trailToPathCache = new HashSet<>();

		if (subPackage) {
			FileUtils.forEachFileOfExtension(inDirectory, "mapping", (file, trail) -> modifyFile(file, in, out, outAbsolutePath, trail, trailToPathCache));
			FileUtils.cleanupEmptyDirectories(inDirectory);
		} else {
			for (File file : inDirectory.listFiles()) {
				String absolutePath = file.getAbsolutePath();

				if (file.isFile() && absolutePath.endsWith(".mapping")) { // is mapping file
					modifyFile(file, in, out, outAbsolutePath, "", trailToPathCache);
				}
			}
		}
	}

	private static void modifyFile(File file, String in, String out, String outAbsolutePath, String trail, Set<String> trailToPathCache) {
		// refactor stuff
		String nameWithExt = file.getName();
		String name = nameWithExt.substring(0, nameWithExt.length() - 8);

		boolean trailEmpty = trail.isEmpty();

		FileUtils.modifyFile(file, data -> {
			String inExpr  = trailEmpty ? in + "/" + name  : in + trail + "/" + name;
			String outExpr = trailEmpty ? out + "/" + name : out + trail + "/" + name;

			if (Main.programArgs.beVerbose()) {
				System.out.println(inExpr + "\t->\t" + outExpr);
			}

			inExpr = inExpr.replace("$", "\\$");
			return data.replaceAll(inExpr, outExpr);
		});

		FileUtils.relocateFile(file, retrieveFilePath(trailToPathCache, outAbsolutePath, trail));
	}

	private static String retrieveFilePath(Set<String> cache, String outAbsolutePath, String trail) {
		if (trail.isEmpty()) {
			return outAbsolutePath;
		}

		String path = outAbsolutePath + trail;

		if (!cache.contains(path)) {
			File newFolder = new File(path);
			newFolder.mkdirs();
		}

		return path;
	}
}
