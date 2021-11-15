package de.team33.test.patterns.producing.shared;

public class SuperComplex {

    private final int intValue;
    private final String stringValue;

    SuperComplex(final Collector collector) {
        intValue = collector.intValue;
        stringValue = collector.stringValue;
    }

    public final int getIntValue() {
        return intValue;
    }

    public final String getStringValue() {
        return stringValue;
    }

    @SuppressWarnings("FieldHasSetterButNoGetter")
    public static class Collector {

        private int intValue;
        private String stringValue;

        public final Collector setIntValue(final int intValue) {
            this.intValue = intValue;
            return this;
        }

        public final Collector setStringValue(final String stringValue) {
            this.stringValue = stringValue;
            return this;
        }
    }
}
