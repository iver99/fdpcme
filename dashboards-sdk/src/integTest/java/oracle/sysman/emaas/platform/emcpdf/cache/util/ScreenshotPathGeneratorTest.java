package oracle.sysman.emaas.platform.emcpdf.cache.util;

import mockit.Expectations;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by chehao on 2017/1/6.
 */
@Test(groups = { "s2" })
public class ScreenshotPathGeneratorTest {

    /*@Test
    public void testSCPathenerator(@Mocked final Properties mockProperties) throws IOException {

        new NonStrictExpectations(){
            {
                mockProperties.load((InputStream)any);
                result=null;
                mockProperties.get(anyString);
                result="testProp";
            }
        };

        ScreenshotPathGenerator s=ScreenshotPathGenerator.getInstance();

        s.generateFileName(new BigInteger("1"),new Date(),new Date());

        s.generateFileName(null,new Date(),new Date());

        s.generateScreenshotUrl("baseurl",new BigInteger("1"),new Date(),new Date());

        s.generateScreenshotUrl("baseurl",null,new Date(),new Date());

        s.validFileName(new BigInteger("1"),"filename","fileName");

        s.validFileName(new BigInteger("1"),"","fileName");

        s.validFileName(new BigInteger("1"),"filename","");

        s.validFileName(new BigInteger("1"),"filename",null);

        s.validFileName(new BigInteger("1"),null,"fileName");
    }*/


    @Test
    public void testInitial(@Mocked final Properties prop) throws IOException {

       /* final InputStream in=new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };

        new Expectations(){
            {
               *//* ScreenshotPathGenerator.class.getResourceAsStream(anyString);
                result=in;*//*
                prop.load((InputStream)any);
                prop.get(anyString);
                result="TestString";

            }
        };*/
        ScreenshotPathGenerator s=ScreenshotPathGenerator.getInstance();
    }
}
