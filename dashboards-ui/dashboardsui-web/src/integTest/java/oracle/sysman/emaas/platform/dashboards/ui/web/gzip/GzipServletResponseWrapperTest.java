package oracle.sysman.emaas.platform.dashboards.ui.web.gzip;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import mockit.Mocked;

import org.testng.Assert;
import org.testng.annotations.Test;

public class GzipServletResponseWrapperTest {
  @Test
  public void testGetOutputStream(@Mocked final HttpServletResponse response) throws IOException {
	  GzipServletResponseWrapper rw = new GzipServletResponseWrapper(response);
	  Assert.assertNotNull(rw.getOutputStream());
  }
  
  @Test
  public void testGetWriter(@Mocked final HttpServletResponse response) throws IOException {
	  GzipServletResponseWrapper rw = new GzipServletResponseWrapper(response);
	  Assert.assertNotNull(rw.getWriter());
  }
  
  @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp="Cannot write to a closed output stream")
  public void testClose1(@Mocked final HttpServletResponse response) throws Exception {
	  GzipServletResponseWrapper rw = new GzipServletResponseWrapper(response);
	  rw.getOutputStream();
	  rw.close();
	  
	  rw.getOutputStream().print("anything");
  }
}
