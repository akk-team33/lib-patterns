package de.team33.test.patterns.random.shared;

public class Buildable {

    private final Core core;

    private Buildable(final Builder builder) {
        this.core = new Core(builder.core);
    }

    public static Builder builder() {
        return new Builder();
    }

    public final int getIntValue() {
        return core.intValue;
    }

    public final Long getLongValue() {
        return core.longValue;
    }

    public final String getStringValue() {
        return core.stringValue;
    }

    public final Builder toBuilder() {
        return new Builder(core);
    }

    private static class Core {

        private int intValue;
        private Long longValue;
        private String stringValue;

        private Core() {
        }

        private Core(final Core other) {
            intValue = other.intValue;
            longValue = other.longValue;
            stringValue = other.stringValue;
        }
    }

    public static class Builder {

        private final Core core;

        private Builder() {
            this.core = new Core();
        }

        private Builder(final Core core) {
            this.core = new Core(core);
        }

        public final Buildable build() {
            return new Buildable(this);
        }

        public final Builder setIntValue(final int intValue) {
            core.intValue = intValue;
            return this;
        }

        public final Builder setLongValue(final Long longValue) {
            core.longValue = longValue;
            return this;
        }

        public final Builder setStringValue(final String stringValue) {
            core.stringValue = stringValue;
            return this;
        }
    }
}
