package tk.valoeghese.ertool;

import java.io.File;

import tk.valoeghese.common.ArgsData;
import tk.valoeghese.common.ArgsParser;
import tk.valoeghese.common.IProgramArgs;
import tk.valoeghese.common.util.FileUtils;
import tk.valoeghese.common.util.FunctionalUtils;
import tk.valoeghese.ertool.refactor.PackageRefactor;
import tk.valoeghese.ertool.refactor.TitleRefactor;

public final class Main {
	public static Args programArgs;

	public static void main(String[] args) {
		programArgs = ArgsParser.of(args, new Args());

		switch (programArgs.getAction()) {
		case CLASSNAME:
			TitleRefactor.refactorTitle(programArgs.getIn(), programArgs.getOut(), programArgs.getPackage());
			break;
		case PACKAGE:
			PackageRefactor.refactorPackage(programArgs.getIn(), programArgs.getOut(), false);
			break;
		case PACKAGE_PLUS:
			PackageRefactor.refactorPackage(programArgs.getIn(), programArgs.getOut(), true);
			break;
		default: // file
			String file = programArgs.getFile();
			FileUtils.readLines(file, line -> main(line.split("\n")));
			break;
		}
	}

	public static class Args implements IProgramArgs {
		private Args() {
		}

		private RefactorType action = null;
		private String in;
		private String out;
		private String pkg;
		private String where;
		private boolean immer = false;
		private boolean verbose;
		private String file;

		@Override
		public void setArgs(ArgsData args) {
			this.file = args.getStringOrDefault("file", "");

			if (!this.file.isEmpty()) {
				return;
			}

			String actionStr = args.getString("action", 
					() -> args.getString("a",
							() -> {
								throw new RuntimeException("Must Specify an action! -action [action] or -a [action]");
							}));

			this.action = RefactorType.get(actionStr);

			this.in = args.getString("in", () -> {
				throw new RuntimeException("Must Specify an input for refactor! -in [action]");
			}).replace('.', '/');

			this.out = args.getString("out", () -> {
				throw new RuntimeException("Must Specify an output for refactor! -out [action]");
			}).replace('.', '/');

			this.pkg = args.getStringOrDefault("package", "").replace('.', '/');
			this.where = args.getString("where", () -> {
				this.immer = true;
				return "";
			});

			this.verbose = args.getBoolean("v");
		}

		public RefactorType getAction() {
			return this.action;
		}

		public String getIn() {
			return this.in;
		}

		public String getOut() {
			return this.out;
		}

		public String getPackage() {
			return this.pkg;
		}

		public boolean beVerbose() {
			return this.verbose;
		}

		public boolean matches(String input) {
			return this.immer || FunctionalUtils.addFalseLogic(
					input.matches(this.where),
					() -> {
						if (this.verbose) System.out.println(input + " does not match regex \"" + this.where + "\"");
					}
					);
		}

		public String getFile() {
			return this.file;
		}
	}
}
