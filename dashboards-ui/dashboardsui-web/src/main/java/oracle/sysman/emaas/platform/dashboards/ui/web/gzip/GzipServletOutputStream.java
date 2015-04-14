/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.web.gzip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * ServletOutputStream supports to compress specified response with gzip
 *
 * @author guobaochen
 */
public class GzipServletOutputStream extends ServletOutputStream
{
	private final ByteArrayOutputStream baos;
	private final GZIPOutputStream gzipstream;
	private boolean closed;
	private final HttpServletResponse response;

	public GzipServletOutputStream(HttpServletResponse response) throws IOException
	{
		super();
		closed = false;
		this.response = response;
		baos = new ByteArrayOutputStream();
		gzipstream = new GZIPOutputStream(baos);
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#close()
	 */
	@Override
	public void close() throws IOException
	{
		if (closed) {
			throw new IOException("This output stream has already been closed");
		}
		gzipstream.finish();

		byte[] bytes = baos.toByteArray();

		response.addHeader("Content-Length", Integer.toString(bytes.length));
		response.addHeader("Content-Encoding", "gzip");
		ServletOutputStream output = response.getOutputStream();
		output.write(bytes);
		output.flush();
		output.close();
		closed = true;
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#flush()
	 */
	@Override
	public void flush() throws IOException
	{
		if (closed) {
			throw new IOException("Cannot flush a closed output stream");
		}
		gzipstream.flush();
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(byte[])
	 */
	@Override
	public void write(byte b[]) throws IOException
	{
		write(b, 0, b.length);
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(byte b[], int off, int len) throws IOException
	{
		if (closed) {
			throw new IOException("Cannot write to a closed output stream");
		}
		gzipstream.write(b, off, len);
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException
	{
		if (closed) {
			throw new IOException("Cannot write to a closed output stream");
		}
		gzipstream.write((byte) b);
	}
}
