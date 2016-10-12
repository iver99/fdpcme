package oracle.sysman.emaas.platform.dashboards.ui.web.gzip;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import mockit.Deencapsulation;
import mockit.Mocked;

import org.testng.Assert;
import org.testng.annotations.Test;

public class GzipServletOutputStreamTest {
  @Test(groups = { "s1" })
  public void testClose(@Mocked final HttpServletResponse response) throws IOException {
	  GzipServletOutputStream os = new GzipServletOutputStream(response);
	  
	  os.write(10);
	  os.write(new byte[10]);
	  os.write(new byte[10], 0, 10);
	  os.flush();
	  
	  Assert.assertEquals(Deencapsulation.getField(os, "closed"), false);
	  os.close();
	  Assert.assertEquals(Deencapsulation.getField(os, "closed"), true);
  }
}
