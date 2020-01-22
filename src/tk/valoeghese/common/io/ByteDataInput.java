package tk.valoeghese.common.io;

import java.io.IOException;

public interface ByteDataInput {
	boolean readBoolean() throws IOException;
	byte readS1() throws IOException;
	int readU1() throws IOException;
	short readS2() throws IOException;
	int readU2() throws IOException;
	int readS4() throws IOException;
	long readS8() throws IOException;
	void skipBytes(int bytes) throws IOException;
	void readByteArray(byte[] arr) throws IOException;
	void readByteArray(byte[] arr, int start, int end) throws IOException;
}
