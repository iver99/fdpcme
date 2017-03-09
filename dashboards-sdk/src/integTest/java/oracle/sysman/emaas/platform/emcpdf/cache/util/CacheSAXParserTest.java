package oracle.sysman.emaas.platform.emcpdf.cache.util;

import mockit.Deencapsulation;
import mockit.Mock;
import mockit.Mocked;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.CacheConfig;
import org.testng.annotations.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Created by chehao on 2017/2/23 16:00.
 */
@Test(groups = {"s2"})
public class CacheSAXParserTest {
    CacheSAXParser cacheSAXParser = new CacheSAXParser();

    @Test
    public void testStartDocument() throws SAXException {
        cacheSAXParser.startDocument();
    }

    @Test
    public void testEndDocument() throws SAXException {
        cacheSAXParser.endDocument();
    }

    @Test
    public void testStartElement(@Mocked final Attributes attributes) throws SAXException {

        cacheSAXParser.startElement("uri","localName","root",attributes);

        cacheSAXParser.startElement("uri","localName","cache-group",attributes);

        cacheSAXParser.startElement("uri","localName","name",attributes);

        cacheSAXParser.startElement("uri","localName","capacity",attributes);

        cacheSAXParser.startElement("uri","localName","expiry",attributes);
    }

    @Test
    public void testEndElement() throws SAXException {
        cacheSAXParser.endElement("url", "localName", "cache-group");
    }
    @Test
    public void testCharacters(@Mocked final CacheConfig cacheConfig) throws SAXException {
        cacheSAXParser.characters(new char[]{'t','e','s','t'},1,1);
    }
}
