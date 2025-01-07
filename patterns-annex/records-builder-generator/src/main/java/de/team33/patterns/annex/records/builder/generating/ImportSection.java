package de.team33.patterns.annex.records.builder.generating;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImportSection {

    private final List<Class<?>> types;

    public ImportSection(final Stream<Class<?>> types) {
        this.types = types.filter(type -> !type.isPrimitive())
                          .filter(type -> !"java.lang".equals(type.getPackageName()))
                          .toList();
    }

    @Override
    public String toString() {
        return types.stream()
                    .map(type -> "import %s;%n".formatted(type.getCanonicalName()))
                    .collect(Collectors.joining())
                    .trim();
    }
}
