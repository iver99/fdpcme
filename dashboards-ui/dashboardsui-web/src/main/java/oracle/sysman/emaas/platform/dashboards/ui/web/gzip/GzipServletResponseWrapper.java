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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Servlet response wrapper that supports compress response with gzip
 *
 * @author guobaochen
 */
public class GzipServletResponseWrapper extends HttpServletResponseWrapper
{
	private static final Logger LOGGER = LogManager.getLogger(GzipServletResponseWrapper.class);
	private final HttpServletResponse response;
	private ServletOutputStream stream;
	private PrintWriter writer;

	public GzipServletResponseWrapper(HttpServletResponse response)
	{
		super(response);
		this.response = response;
	}

	/**
	 * Close the response wrapper, and all used resource
	 */
	public void close()
	{
		try {
			if (writer != null) {
				writer.close();
			}
			else {
				if (stream != null) {
					stream.close();
				}
			}
		}
		catch (IOException e) {
			LOGGER.info("context",e);
			
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponseWrapper#flushBuffer()
	 */
	@Override
	public void flushBuffer() throws IOException
	{
		stream.flush();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponseWrapper#getOutputStream()
	 */
	@Override
	public ServletOutputStream getOutputStream() throws IOException
	{
		if (writer != null) {
			throw new IllegalStateException("getWriter() has already been called!");
		}

		if (stream == null) {
			stream = new GzipServletOutputStream(response);
		}
		return stream;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponseWrapper#getWriter()
	 */
	@Override
	public PrintWriter getWriter() throws IOException
	{
		if (writer != null) {
			return writer;
		}

		if (stream != null) {
			throw new IllegalStateException("getOutputStream() has already been called!");
		}

		stream = new GzipServletOutputStream(response);
		writer = new PrintWriter(new OutputStreamWriter(stream, "UTF-8"));
		return writer;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponseWrapper#setContentLength(int)
	 */
	@Override
	public void setContentLength(int length)
	{
		// do nothing
	}

}
