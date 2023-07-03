package de.team33.test.patterns.reflect.luna;

import de.team33.patterns.reflect.luna.Fields;
import org.junit.jupiter.api.Test;

import java.awt.GridBagConstraints;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PublicTest {

    private static final Supply SUPPLY = new Supply();

    private static final Fields PUBLIC_FIELDS = Fields.of(Fields.Group.PUBLIC,
                                                          GridBagConstraints.class,
                                                          "insets", "ipadx", "ipady");

    @Test
    final void toMap() {
        final GridBagConstraints source = SUPPLY.nextGridBagConstraints();
        final Map<String, Object> expected = new TreeMap<String, Object>() {{
            put("gridx", source.gridx);
            put("gridy", source.gridy);
            put("gridwidth", source.gridwidth);
            put("gridheight", source.gridheight);
            put("weightx", source.weightx);
            put("weighty", source.weighty);
            put("anchor", source.anchor);
            put("fill", source.fill);
        }};
        final Map<String, Object> result = new TreeMap<>(PUBLIC_FIELDS.toMap(field -> field.get(source)));
        assertEquals(expected, result);
    }
}
