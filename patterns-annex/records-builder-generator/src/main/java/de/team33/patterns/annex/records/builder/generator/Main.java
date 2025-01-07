package de.team33.patterns.annex.records.builder.generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private final Class<?> recordClass;

    private Main(final String[] args) throws ClassNotFoundException {
        this.recordClass = Class.forName(args[0]);
    }

    public static void main(String[] args) throws ClassNotFoundException {
        new Main(args).run();
    }

    private void run() {
        // Find primary constructor ...
        final Constructor<?> constructor = Stream.of(recordClass.getConstructors())
                                                 .filter(Main::isPublic)
                                                 .reduce(Main::byMoreParameters)
                                                 .orElseThrow(noAppropriateConstructor(recordClass));
        final List<Parameter> args = List.of(constructor.getParameters());

        System.out.printf("public class %sBuilder {%n%n" +
                          "    %s%n" +
                          "%n" +
                          "    %s%n" +
                          "%n" +
                          "    public final %1$s build() {%n" +
                          "        return new %1$s(%s);%n" +
                          "    }%n" +
                          "}%n%n",
                          recordClass.getSimpleName(), fieldSection(args), setterSection(args), argsSection(args));
    }

    private String argsSection(final List<Parameter> fields) {
        return fields.stream().map(Parameter::getName).collect(Collectors.joining(", "));
    }

    private String setterSection(final List<Parameter> fields) {
        final StringBuilder sb = new StringBuilder();
        fields.forEach(field -> {
            sb.append(("    public final %1$sBuilder %2$s(final %3$s %2$s) {%n" +
                       "        this.%2$s = %2$s;%n" +
                       "        return this;%n" +
                       "    }%n%n").formatted(recordClass.getSimpleName(),
                                              field.getName(),
                                              field.getType().getSimpleName()));
        });
        return sb.toString().trim();
    }

    private String fieldSection(final List<Parameter> fields) {
        final StringBuilder sb = new StringBuilder();
        fields.forEach(field -> {
            sb.append("    private %s %s;%n".formatted(field.getType().getSimpleName(), field.getName()));
        });
        return sb.toString().trim();
    }

    private static void put(final Map<String, Class<?>> map, final Parameter parameter) {
        map.put(parameter.getName(), parameter.getType());
    }

    private static Supplier<IllegalStateException> noAppropriateConstructor(final Class<?> recordClass) {
        return () -> new IllegalStateException("No appropriate constructor found for <" + recordClass + ">");
    }

    private static Constructor<?> byMoreParameters(final Constructor<?> left, final Constructor<?> right) {
        return (right.getParameterCount() > left.getParameterCount()) ? right : left;
    }

    private static boolean isPublic(final Constructor<?> constructor) {
        return Modifier.isPublic(constructor.getModifiers());
    }
}
