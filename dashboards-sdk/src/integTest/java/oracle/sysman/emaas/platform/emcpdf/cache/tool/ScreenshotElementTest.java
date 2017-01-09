package oracle.sysman.emaas.platform.emcpdf.cache.tool;

import org.testng.annotations.Test;

/**
 * Created by chehao on 2017/1/6.
 */
@Test(groups = { "s2" })
public class ScreenshotElementTest {

    @Test
    public void testScreenshotElement(){
        byte[] data={1,2,3};
        ScreenshotElement se=new ScreenshotElement("file",new Binary(data));
        se.getBuffer();
        se.setFileName(null);
        se.getFileName();
        se.setBuffer(null);
    }
}
