package oracle.sysman.emaas.platform.dashboards.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 * @author danfjian
 * @since 2016/1/22.
 */
public class MockHttpServletRequest implements HttpServletRequest
{
	private final HashMap<String, String> headers = new HashMap<>();

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#authenticate(javax.servlet.http.HttpServletResponse)
	 */
	public boolean authenticate(HttpServletResponse arg0) throws IOException, ServletException
	{
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getAsyncContext()
	 */
	public AsyncContext getAsyncContext()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAttribute(String s)
	{
		return null;
	}

	@Override
	public Enumeration getAttributeNames()
	{
		return null;
	}

	@Override
	public String getAuthType()
	{
		return null;
	}

	@Override
	public String getCharacterEncoding()
	{
		return null;
	}

	@Override
	public int getContentLength()
	{
		return 0;
	}

	@Override
	public String getContentType()
	{
		return null;
	}

	@Override
	public String getContextPath()
	{
		return null;
	}

	@Override
	public Cookie[] getCookies()
	{
		return new Cookie[0];
	}

	@Override
	public long getDateHeader(String s)
	{
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getDispatcherType()
	 */
	 public DispatcherType getDispatcherType()
	 {
		 // TODO Auto-generated method stub
		 return null;
	 }

	@Override
	public String getHeader(String s)
	{
		return headers.get(s);
	}

	@Override
	public Enumeration getHeaderNames()
	{
		return new Vector<>(headers.keySet()).elements();
	}

	@Override
	public Enumeration getHeaders(String s)
	{
		return null;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException
	{
		return null;
	}

	@Override
	public int getIntHeader(String s)
	{
		return 0;
	}

	@Override
	public String getLocalAddr()
	{
		return null;
	}

	@Override
	public Locale getLocale()
	{
		return null;
	}

	@Override
	public Enumeration getLocales()
	{
		return null;
	}

	@Override
	public String getLocalName()
	{
		return null;
	}

	@Override
	public int getLocalPort()
	{
		return 0;
	}

	@Override
	public String getMethod()
	{
		return null;
	}

	@Override
	public String getParameter(String s)
	{
		return null;
	}

	@Override
	public Map getParameterMap()
	{
		return null;
	}

	@Override
	public Enumeration getParameterNames()
	{
		return null;
	}

	@Override
	public String[] getParameterValues(String s)
	{
		return new String[0];
	}

	/* (non-Javadoc)
	  * @see javax.servlet.http.HttpServletRequest#getPart(java.lang.String)
	  */
	  public Part getPart(String arg0) throws IOException, ServletException
	  {
		  // TODO Auto-generated method stub
		  return null;
	  }

	/* (non-Javadoc)
	   * @see javax.servlet.http.HttpServletRequest#getParts()
	   */
	  public Collection<Part> getParts() throws IOException, ServletException
	  {
		  // TODO Auto-generated method stub
		  return Collections.emptySet();
	  }

	@Override
	public String getPathInfo()
	{
		return null;
	}

	@Override
	public String getPathTranslated()
	{
		return null;
	}

	@Override
	public String getProtocol()
	{
		return null;
	}

	@Override
	public String getQueryString()
	{
		return null;
	}

	@Override
	public BufferedReader getReader() throws IOException
	{
		return null;
	}

	@Override
	public String getRealPath(String s)
	{
		return null;
	}

	@Override
	public String getRemoteAddr()
	{
		return null;
	}

	@Override
	public String getRemoteHost()
	{
		return null;
	}

	@Override
	public int getRemotePort()
	{
		return 0;
	}

	@Override
	public String getRemoteUser()
	{
		return null;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String s)
	{
		return null;
	}

	@Override
	public String getRequestedSessionId()
	{
		return null;
	}

	@Override
	public String getRequestURI()
	{
		return null;
	}

	@Override
	public StringBuffer getRequestURL()
	{
		return null;
	}

	@Override
	public String getScheme()
	{
		return null;
	}

	@Override
	public String getServerName()
	{
		return null;
	}

	@Override
	public int getServerPort()
	{
		return 0;
	}

	/* (non-Javadoc)
	   * @see javax.servlet.ServletRequest#getServletContext()
	   */
	   public ServletContext getServletContext()
	   {
		   // TODO Auto-generated method stub
		   return null;
	   }

	@Override
	public String getServletPath()
	{
		return null;
	}

	@Override
	public HttpSession getSession()
	{
		return null;
	}

	@Override
	public HttpSession getSession(boolean b)
	{
		return null;
	}

	@Override
	public Principal getUserPrincipal()
	{
		return null;
	}

	/* (non-Javadoc)
	    * @see javax.servlet.ServletRequest#isAsyncStarted()
	    */
	   public boolean isAsyncStarted()
	   {
		   // TODO Auto-generated method stub
		   return false;
	   }

	/* (non-Javadoc)
	    * @see javax.servlet.ServletRequest#isAsyncSupported()
	    */
	   public boolean isAsyncSupported()
	   {
		   // TODO Auto-generated method stub
		   return false;
	   }

	@Override
	public boolean isRequestedSessionIdFromCookie()
	{
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl()
	{
		return false;
	}

	   @Override
	public boolean isRequestedSessionIdFromURL()
	{
		return false;
	}

	   @Override
	public boolean isRequestedSessionIdValid()
	{
		return false;
	}

	   @Override
	public boolean isSecure()
	{
		return false;
	}

	   @Override
	public boolean isUserInRole(String s)
	{
		return false;
	}

	   /* (non-Javadoc)
	    * @see javax.servlet.http.HttpServletRequest#login(java.lang.String, java.lang.String)
	    */
	   public void login(String arg0, String arg1) throws ServletException
	   {
		   // TODO Auto-generated method stub

	}

	   /* (non-Javadoc)
	    * @see javax.servlet.http.HttpServletRequest#logout()
	    */
	   public void logout() throws ServletException
	   {
		   // TODO Auto-generated method stub

	}

	   @Override
	public void removeAttribute(String s)
	{

	}

	   @Override
	public void setAttribute(String s, Object o)
	{

	}

	   @Override
	public void setCharacterEncoding(String s) throws UnsupportedEncodingException
	{

	}

	   public void setHeader(String key, String value)
	{
		headers.put(key, value);
	}

	   /* (non-Javadoc)
	    * @see javax.servlet.ServletRequest#startAsync()
	    */
	   public AsyncContext startAsync() throws IllegalStateException
	   {
		   // TODO Auto-generated method stub
		   return null;
	   }

	   /* (non-Javadoc)
	    * @see javax.servlet.ServletRequest#startAsync(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	    */
	   public AsyncContext startAsync(ServletRequest arg0, ServletResponse arg1) throws IllegalStateException
	   {
		   // TODO Auto-generated method stub
		   return null;
	   }
}
