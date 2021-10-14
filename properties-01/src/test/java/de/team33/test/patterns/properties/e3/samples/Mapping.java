package de.team33.test.patterns.properties.e3.samples;

import java.util.Map;
import java.util.TreeMap;

public class Mapping {

    static Map<String, Object> toMap(final BasicsSample sample) {
        final TreeMap<String, Object> result = new TreeMap<>();
        result.put("intProp", sample.getIntProp());
        result.put("strProp", sample.getStrProp());
        result.put("listProp", sample.getListProp());
        return result;
    }

    static Map<String, Object> toMap(final BasiXSample sample) {
        final TreeMap<String, Object> result = new TreeMap<>();
        result.put("intProp", sample.getIntProp());
        result.put("strProp", sample.getStrProp());
        result.put("listProp", sample.getListProp());
        return result;
    }
}
