package tk.valoeghese.ertool;

import tk.valoeghese.common.ArgsData;
import tk.valoeghese.common.ArgsParser;
import tk.valoeghese.common.IProgramArgs;
import tk.valoeghese.ertool.pkgrefactor.PackageRefactor;

public final class Main {
	public static Args programArgs;

	public static void main(String[] args) {
		programArgs = ArgsParser.of(args, new Args());

		switch (programArgs.action) {
		case CLASSNAME:
			break;
		case PACKAGE:
			PackageRefactor.refactorPackage(programArgs.in, programArgs.out);
			break;
		}
	}

	public static class Args implements IProgramArgs {
		private Args() {
		}

		private RefactorType action;
		private String in;
		private String out;
		private String pkg;

		@Override
		public void setArgs(ArgsData args) {
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
	}
}
