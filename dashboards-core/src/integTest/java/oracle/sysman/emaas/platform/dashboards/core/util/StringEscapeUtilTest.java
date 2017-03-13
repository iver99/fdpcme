package oracle.sysman.emaas.platform.dashboards.core.util;

import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * Created by guochen on 3/11/17.
 */
public class StringEscapeUtilTest {
    @Test(groups = { "s2" })
    public void testEscapeWithCharPairs() {
        Assert.assertNull(StringEscapeUtil.escapeWithCharPairs(null, null));
        Assert.assertNull(StringEscapeUtil.escapeWithCharPairs(null, new String[][]{{}}));

        Assert.assertEquals(StringEscapeUtil.escapeWithCharPairs("test", new String[][]{{"abc", "def"}}), "test");

        String[][] escapePairs = new String[][]{{"&", "&amp;"}, {"<", "&lt;"}, {">", "&gt;"}};
        Assert.assertEquals(StringEscapeUtil.escapeWithCharPairs("test<script>&abc</script>", escapePairs), "test&lt;script&gt;&amp;abc&lt;/script&gt;");

        escapePairs = new String[][]{{"&amp;", "&"}, {"&lt;", "<"}, {"&gt;", ">"}};
        Assert.assertEquals(StringEscapeUtil.escapeWithCharPairs("test&lt;script&gt;&amp;abc&lt;/script&gt;", escapePairs), "test<script>&abc</script>");
    }

    @Test(groups = { "s2"}, expectedExceptions = IllegalArgumentException.class)
    public void testEscapeWithCharPairsIllegalArgs() {
        Assert.assertNull(StringEscapeUtil.escapeWithCharPairs("test", null));
    }
}
