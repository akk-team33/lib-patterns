package de.team33.patterns.annex.records.builder.generating;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Collectors;

public class SetterSection {

    private final List<SetterCode> setters;

    public SetterSection(final String recordClass, final List<Parameter> args) {
        this.setters = args.stream().map(arg -> new SetterCode(recordClass, arg)).toList();
    }

    @Override
    public String toString() {
        return setters.stream()
                      .map("    %s%n%n"::formatted)
                      .collect(Collectors.joining())
                      .trim();
    }
}
