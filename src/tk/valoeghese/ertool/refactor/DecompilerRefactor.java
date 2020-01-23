package tk.valoeghese.ertool.refactor;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

import tk.valoeghese.common.util.FileUtils;
import tk.valoeghese.common.util.Flag;
import tk.valoeghese.common.util.bytecode.BytecodeUtils;
import tk.valoeghese.ertool.Main;

public final class DecompilerRefactor {
	private DecompilerRefactor() {
	}

	private static boolean verboseCache = false;
	private static String renameFileTo = "";

	public static void refactorInterfaces(String jarFile, String pkgRaw) throws IOException {
		verboseCache = Main.programArgs.beVerbose();

		File mappingsDirectory = new File("mappings/");
		assert mappingsDirectory.isDirectory() : "No ./mappings/ directory found";

		boolean subPackage = pkgRaw.endsWith("+");
		String pkg = subPackage ? pkgRaw.substring(0, pkgRaw.length() - 1) : pkgRaw;

		File directory = new File("mappings/" + pkg);
		ZipFile zipFile = new ZipFile(jarFile);

		if (subPackage) {
			FileUtils.trailFilesOfExtension(directory, "mapping", (file, trail) -> modifyFile(file, zipFile, pkg, trail));
		} else {
			FileUtils.forEachFileOfExtension(directory, "mapping", file -> modifyFile(file, zipFile, pkg, ""));
		}
	}

	private static void modifyFile(File file, ZipFile jarFile, String pkg, String trail) {
		boolean trailEmpty = trail.isEmpty();

		try {
			Flag hasNewName = new Flag();

			FileUtils.modifyFile(file, data -> {
				String[] requiredData = data.split("\n")[0].split(" ");

				if (requiredData.length == 3) {
					if (requiredData[0].equals("CLASS")) { // Should be every file that makes it here
						String unmappedName = requiredData[1].trim();
						try {
							if (BytecodeUtils.isInterface(() -> jarFile.getInputStream(jarFile.getEntry(unmappedName + ".class")))) {
								String oldMap = requiredData[2].trim();

								if (!Main.programArgs.matches(oldMap)) {
									return data;
								}

								String[] classNameEntries = oldMap.split("/");
								String oldName = classNameEntries[classNameEntries.length - 1];

								if (oldName.matches("^I[A-Z].*")) {
									return data;
								}

								String newName = "I" + oldName;
								String newMap = (pkg.isEmpty() ?
										(trailEmpty ? newName : trail.substring(1) + "/" + newName):
											(trailEmpty ? pkg + "/" + newName : pkg + trail + "/" + newName));

								if (verboseCache) {
									System.out.println(oldMap + "\t->\t" + newMap);
								}

								renameFileTo = newName + ".mapping";
								hasNewName.flag();
								return data.replace(oldMap.trim(), newMap.trim());
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				return data;
			});

			if (hasNewName.booleanValue()) {
				FileUtils.renameFile(file, renameFileTo);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
