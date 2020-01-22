package tk.valoeghese.ertool;

import java.io.IOException;

import tk.valoeghese.common.ArgsData;
import tk.valoeghese.common.ArgsParser;
import tk.valoeghese.common.IProgramArgs;
import tk.valoeghese.common.util.FileUtils;
import tk.valoeghese.common.util.FunctionalUtils;
import tk.valoeghese.ertool.refactor.DecompilerRefactor;
import tk.valoeghese.ertool.refactor.PackageRefactor;
import tk.valoeghese.ertool.refactor.TitleRefactor;

public final class Main {
	public static Args programArgs;

	public static void main(String[] args) throws IOException {
		programArgs = ArgsParser.of(args, new Args());
		runProgram();
	}

	public static void runProgram() throws IOException {
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
		case FILE:
			String file = programArgs.getFile();

			FileUtils.readLines(file, line -> {
				programArgs = ArgsParser.of(new String[] {"-a"}, line.split("[ \t]"), new Args());

				FileUtils.badlyHandleIOException(() -> {
					try {
						runProgram();
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				});
			});
			break;
		case IINTERFACE:
			DecompilerRefactor.refactorInterfaces(programArgs.getFile(), programArgs.getPackage());
			break;
		}
	}

	public static class Args implements IProgramArgs {
		private Args() {
		}

		private RefactorType action = RefactorType.FILE;
		private String in;
		private String out;
		private String pkg;
		private String where;
		private boolean immer = false;
		private boolean verbose;
		private String file;

		@Override
		public void setArgs(ArgsData args) {
			this.verbose = args.getBoolean("v");
			this.pkg = args.getStringOrDefault("package", "").replace('.', '/');
			this.where = args.getString("where", () -> {
				this.immer = true;
				return "";
			});

			this.file = args.getStringOrDefault("file", "");

			if (!this.file.isEmpty()) {
				String action = args.getString("action",
						() -> args.getStringOrDefault("a", null));

				if (action != null) {
					this.action = RefactorType.get(action);
				}

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
