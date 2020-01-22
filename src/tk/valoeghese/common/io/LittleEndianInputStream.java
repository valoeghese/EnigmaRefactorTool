package tk.valoeghese.common.io;

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LittleEndianInputStream extends FilterInputStream implements ByteDataInput {
	public LittleEndianInputStream(InputStream parent) {
		super(parent);
	}

	private byte[] longReadBuffer = new byte[8];

	@Override
	public final boolean readBoolean() throws IOException {
		int value = this.in.read();

		if (value < 0) {
			throw new EOFException();
		}

		return (value != 0);
	}

	@Override
	public final byte readS1() throws IOException {
		int value = this.in.read();

		if (value < 0) {
			throw new EOFException();
		}

		return (byte) value;
	}

	@Override
	public final int readU1() throws IOException {
		int value = this.in.read();

		if (value < 0) {
			throw new EOFException();
		}

		return value;
	}

	@Override
	public short readS2() throws IOException {
		int byt1 = this.in.read();
		int byt2 = this.in.read();

		if ((byt1 | byt2) < 0) {
			throw new EOFException();
		}

		return (short) ((byt2 << 8) + byt1);
	}

	@Override
	public int readU2() throws IOException {
		int byt1 = this.in.read();
		int byt2 = this.in.read();

		if ((byt1 | byt2) < 0) {
			throw new EOFException();
		}

		return (byt2 << 8) + byt1;
	}

	@Override
	public int readS4() throws IOException {
		int byt1 = this.in.read();
		int byt2 = this.in.read();
		int byt3 = this.in.read();
		int byt4 = this.in.read();

		if ((byt1 | byt2 | byt3 | byt4) < 0) {
			throw new EOFException();
		}

		return (byt4 << 24) + (byt3 << 16) + (byt2 << 8) + byt1;
	}

	@Override
	public long readS8() throws IOException {
		this.readByteArray(this.longReadBuffer);

		return  (((long) this.longReadBuffer[7] << 56)         +
				((long) (this.longReadBuffer[6] & 0xFF) << 48) +
				((long) (this.longReadBuffer[5] & 0xFF) << 40) +
				((long) (this.longReadBuffer[4] & 0xFF) << 32) +
				((long) (this.longReadBuffer[3] & 0xFF) << 24) +
				((this.longReadBuffer[2] & 255) << 16)         +
				((this.longReadBuffer[1] & 255) <<  8)         +
				((this.longReadBuffer[0] & 255) <<  0));
	}

	@Override
	public void skipBytes(int bytes) throws IOException {
		this.in.skip(bytes);
	}

	@Override
	public void readByteArray(byte[] arr) throws IOException {
		this.readByteArray(arr, 0, arr.length);
	}

	@Override
	public void readByteArray(byte[] arr, int start, int end) throws IOException {
		for (int i = start; i < end; ++i) {
			int byt = this.in.read();

			if (byt < 0) {
				throw new EOFException();
			}

			arr[i] = (byte) byt;
		}
	}
}
