package oracle.sysman.emaas.platform.emcpdf.cache.util;

import oracle.sysman.emaas.platform.emcpdf.cache.tool.CacheConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

/**
 * Created by chehao on 2017/2/21 10:42.
 */
public class CacheSAXParser extends DefaultHandler {
    private final Logger LOGGER= LogManager.getLogger(CacheSAXParser.class);

    private final int NAME_STATE = 1;
    private final int CAPACITY_STATE = 2;
    private final int EXPIRY_STATE = 3;
    private final String ROOT_TAG = "root";
    private final String CACHE_GROUP_TAG = "cache-group";
    private final String NAME_TAG = "name";
    private final String CAPACITY_TAG = "capacity";
    private final String EXPIRY_TAG = "expiry";

    CacheConfig cacheConfig =null;

    private int currentState;


    @Override
    public void startDocument() throws SAXException {
        LOGGER.info("…………Begin to parse cache configuration file…………\n");
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        LOGGER.info("Begin to parsing tag " + qName);
        if(qName.equals(ROOT_TAG))
            return;
        if(qName.equals(CACHE_GROUP_TAG)){
            cacheConfig = new CacheConfig();
        }
        if(qName.equals(NAME_TAG)){
            currentState = NAME_STATE;
        }
        if(qName.equals(CAPACITY_TAG)){
            currentState = CAPACITY_STATE;
        }
        if(qName.equals(EXPIRY_TAG)){
            currentState = EXPIRY_STATE;
        }
    }

    /**
     * Receive notification of the end of the document.
     * <p>
     * <p>By default, do nothing.  Application writers may override this
     * method in a subclass to take specific actions at the end
     * of a document (such as finalising a tree or closing an output
     * file).</p>
     *
     * @throws SAXException Any SAX exception, possibly
     *                      wrapping another exception.
     * @see ContentHandler#endDocument
     */
    @Override
    public void endDocument() throws SAXException {
        System.out.println("\n…………结束解析文档…………");
        super.endDocument();
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if(qName.equals(CACHE_GROUP_TAG)){
            CacheConfig.cacheConfigList.add(cacheConfig);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String elementValue = new String(ch, start, length);
        switch(currentState){
            case NAME_STATE:
                LOGGER.info("Name: "+elementValue);
                cacheConfig.setName(elementValue);
                break;
            case CAPACITY_STATE:
                LOGGER.info("Capacity: "+elementValue);
                cacheConfig.setCapacity(Integer.valueOf(elementValue));
                break;
            case EXPIRY_STATE:
                LOGGER.info("Expiry: "+elementValue);
                cacheConfig.setExpiry(Long.valueOf(elementValue));
                break;

        }
    }
    public void parser(File xmlFile) throws SAXException, IOException,
            ParserConfigurationException {
        if (xmlFile == null) {
            throw new IllegalArgumentException(
                    "parameter 'xmlFile' must not null !");
        }
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        try {
            parser.parse(xmlFile, this);
        } catch (SAXException e) {
            LOGGER.error("Cann't parse " + xmlFile.getAbsolutePath());
            throw e;
        }
    }
}
