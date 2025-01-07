package de.team33.patterns.annex.records.builder.generating;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Collectors;

public class FieldSection {

    private final List<Parameter> args;

    public FieldSection(final List<Parameter> args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return args.stream()
                   .map(arg -> "    private %s %s;%n".formatted(arg.getType().getSimpleName(), arg.getName()))
                   .collect(Collectors.joining())
                   .trim();
    }
}
