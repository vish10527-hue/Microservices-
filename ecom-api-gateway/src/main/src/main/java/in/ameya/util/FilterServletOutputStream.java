package in.ameya.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;

public class FilterServletOutputStream extends ServletOutputStream {

	DataOutputStream output;

	public FilterServletOutputStream(OutputStream output) {
		this.output = new DataOutputStream(output);
	}

	@Override
	public void write(int arg0) throws IOException {
		output.write(arg0);
	}

	@Override
	public void write(byte[] arg0, int arg1, int arg2) throws IOException {
		output.write(arg0, arg1, arg2);
	}

	@Override
	public void write(byte[] arg0) throws IOException {
		output.write(arg0);
	}

	@Override
	public boolean isReady() {
		return false;
	}

	@Override
	public void setWriteListener(WriteListener listener) {
	}
}