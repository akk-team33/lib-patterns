package de.team33.patterns.annex.records.builder.generating;

import de.team33.patterns.annex.records.builder.generator.Main;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ClassCode {

    private final String packageName;
    private final String recordName;
    private final ImportSection imports;
    private final BuilderCode builderCode;

    public ClassCode(final Class<?> recordClass) {
        final Constructor<?> constructor = Stream.of(recordClass.getConstructors())
                                                 .filter(ClassCode::isPublic)
                                                 .reduce(ClassCode::byMoreParameters)
                                                 .orElseThrow(noAppropriateConstructor(recordClass));
        final List<Parameter> args = List.of(constructor.getParameters());
        this.packageName = recordClass.getPackage().getName();
        this.recordName = recordClass.getSimpleName();
        this.imports = new ImportSection(args.stream().map(Parameter::getType));
        this.builderCode = new BuilderCode(recordClass, args);
    }

    @Override
    public String toString() {
        return ("package %1$s;%n%n" +
                "%2$s%n%n" +
                "%3$s%n").formatted(packageName, imports, builderCode);
    }

    private static Constructor<?> byMoreParameters(final Constructor<?> left, final Constructor<?> right) {
        return (right.getParameterCount() > left.getParameterCount()) ? right : left;
    }

    private static boolean isPublic(final Constructor<?> constructor) {
        return Modifier.isPublic(constructor.getModifiers());
    }

    private static Supplier<IllegalStateException> noAppropriateConstructor(final Class<?> recordClass) {
        return () -> new IllegalStateException("No appropriate constructor found for <" + recordClass + ">");
    }
}
