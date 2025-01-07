package de.team33.patterns.annex.records.builder.generating;

import java.lang.reflect.Parameter;

public class SetterCode {

    private final String recordClass;
    private final Parameter arg;

    public SetterCode(final String recordClass, final Parameter arg) {
        this.recordClass = recordClass;
        this.arg = arg;
    }

    @Override
    public String toString() {
        return ("public final %1$sBuilder %2$s(final %3$s value) {%n" +
                "        this.%2$s = value;%n" +
                "        return this;%n" +
                "    }").formatted(recordClass, arg.getName(), arg.getType().getSimpleName());
    }
}
