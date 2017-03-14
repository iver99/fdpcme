package oracle.sysman.emaas.platform.dashboards.core.util;

import org.apache.commons.lang3.text.translate.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by guochen on 3/11/17.
 */
public class StringEscapeUtil {
    private static final Logger LOGGER = LogManager.getLogger(StringEscapeUtil.class);

    /**
     * Escape the input string with the specified replacement escape pairs
     *
     * @param original string to be escaped
     * @param escapePairs an array of string paris, and each pair contains the escape/replace 'from' string and the 'to' string.
     *                    e.g.: new String[][]{{"&", "&amp;"}, {"<", "&lt;"}, {">", "&gt;"}}
     * @return
     */
    public static String escapeWithCharPairs(String original, String[][] escapePairs) {
        if (StringUtil.isEmpty(original)) {
            return original;
        }
        if (escapePairs == null) {// for invalid escape string pairs, we raise exception to expose as early as possible
            throw new IllegalArgumentException("Invalid escape pairs (null) when escaping string: " + original);
        }
        CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(escapePairs.clone())});
        String escaped=translator.translate(original);
        LOGGER.info("Escaping input string {} to {}", original, escaped);
        return escaped;
    }
}
