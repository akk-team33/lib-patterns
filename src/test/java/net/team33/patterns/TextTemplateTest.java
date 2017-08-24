package net.team33.patterns;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class TextTemplateTest {

    public static final String TEXT_TEMPLATE = "Dies ist ein TextTemplate";

    private final TextTemplate template = new TextTemplate(TEXT_TEMPLATE);

    @Test
    public final void resolve() {
        final String expected = TEXT_TEMPLATE
                .replace("i", "[I]")
                .replace("mpl", "[MPL]")
                .replace("Te", "[TE]")
                .replace("e", "[E]")
                .replace("x", "null");
        final String result = template.resolve(new HashMap<String, String>() {{
            put("i", "[I]");
            put("mpl", "[MPL]");
            put("Te", "[TE]");
            put("e", "[E]");
            put("x", null);
        }});
        Assert.assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public final void resolveNull() {
        template.resolve(new HashMap<String, String>() {{
            put(null, "[X]");
        }});
    }
}