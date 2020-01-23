package tk.valoeghese.common.util.bytecode;

public final class CPTag {
	private CPTag() {
	}

	public static final int UTF8 = 1;
	public static final int INT = 3;
	public static final int FLOAT = 4;
	public static final int LONG = 5;
	public static final int DOUBLE = 6;
	public static final int CLASS = 7;
	public static final int STRING = 8;
	public static final int FIELDREF = 9;
	public static final int METHODREF = 10;
	public static final int IMETHODREF = 11;
	public static final int NAMEANDTYPE = 12;
	public static final int METHODHANDLE = 15;
	public static final int METHODTYPE = 16;
	public static final int INVOKEDYNAMIC = 18;
	public static final int PACKAGE = 19;
	public static final int MODULE = 20;
}
