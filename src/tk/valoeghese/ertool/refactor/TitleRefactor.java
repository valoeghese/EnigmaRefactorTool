package tk.valoeghese.ertool.refactor;

import java.io.File;

import tk.valoeghese.common.util.FileUtils;
import tk.valoeghese.ertool.Main;

public final class TitleRefactor {
	private TitleRefactor() {
	}

	private static boolean verboseCache = false;

	public static void refactorTitle(String inRegex, String out, String pkg) {
		verboseCache = Main.programArgs.beVerbose();

		File mappingsDirectory = new File("mappings/");
		assert mappingsDirectory.isDirectory() : "No ./mappings/ directory found";

		boolean subPackage = pkg.endsWith("+");
		File directory = new File("mappings/" + (subPackage ? pkg.substring(0, pkg.length() - 1) : pkg));

		if (subPackage) {
			FileUtils.trailFilesOfExtension(directory, "mapping", (file, trail) -> modifyFile(file, inRegex, out, pkg, trail));
		} else {
			FileUtils.forEachFileOfExtension(directory, "mapping", file -> modifyFile(file, inRegex, out, pkg, ""));
		}
	}

	private static void modifyFile(File file, String inRegex, String out, String pkg, String trail) {
		// refactor stuff
		String nameWithExt = file.getName();
		String name = nameWithExt.substring(0, nameWithExt.length() - 8);
		boolean trailEmpty = trail.isEmpty();
		String original  = trailEmpty ? pkg + "/" + name  : pkg + trail + "/" + name;

		if (!Main.programArgs.matches(original)) {
			return;
		}

		String newName = name.replaceAll(inRegex, out);

		FileUtils.modifyFile(file, data -> {
			String modified = trailEmpty ? pkg + "/" + newName : pkg + trail + "/" + newName;

			if (verboseCache) {
				System.out.println(original + "\t->\t" + modified);
			}

			return data.replaceAll(original, modified);
		});

		FileUtils.renameFile(file, newName + ".mapping");
	}
}
