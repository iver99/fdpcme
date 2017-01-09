package oracle.sysman.emaas.platform.emcpdf.cache.tool;

import org.testng.annotations.Test;

import java.util.Date;

/**
 * Created by chehao on 2017/1/6.
 */
@Test(groups = { "s2" })
public class ScreenshotDataTest {

    @Test
    public void testScreenshotData(){
        ScreenshotData sd=new ScreenshotData("sc",new Date(),new Date());
        sd.getCreationDate();
        sd.getModificationDate();
        sd.getScreenshot();
        sd.setCreationDate(null);
        sd.setModificationDate(null);
        sd.setScreenshot(null);
    }
}
