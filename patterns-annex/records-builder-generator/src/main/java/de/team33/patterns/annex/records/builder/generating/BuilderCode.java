package de.team33.patterns.annex.records.builder.generating;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Collectors;

public class BuilderCode {

    private final String recordClass;
    private final FieldSection fieldSection;
    private final SetterSection setterSection;
    private final String buildArgs;

    public BuilderCode(final Class<?> recordClass, final List<Parameter> args) {
        this.recordClass = recordClass.getSimpleName();
        this.fieldSection = new FieldSection(args);
        this.setterSection = new SetterSection(this.recordClass, args);
        this.buildArgs = args.stream().map(Parameter::getName).collect(Collectors.joining(", "));
    }

    @Override
    public String toString() {
        return ("public class %1$sBuilder {%n%n" +
                "    %2$s%n%n" +
                "    %3$s%n%n" +
                "    public final %1$s build() {%n" +
                "        return new %1$s(%4$s);%n" +
                "    }%n" +
                "}").formatted(recordClass, fieldSection, setterSection, buildArgs);
    }
}
