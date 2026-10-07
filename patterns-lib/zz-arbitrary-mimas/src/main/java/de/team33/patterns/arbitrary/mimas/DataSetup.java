package de.team33.patterns.arbitrary.mimas;

import de.team33.patterns.typing.proteus.Type;

import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BinaryOperator;

import static de.team33.patterns.exceptional.dione.Conversion.function;

class DataSetup {

    private final BitGenerator generator;
    private final Type<?> genType;

    DataSetup(final BitGenerator generator) {
        this.genType = Type.of(generator.getClass());
        this.generator = generator;
    }

    final Map<String, Object> generate(final Map<String, Type<?>> description) {
        return description.entrySet().stream()
                          .map(this::generate)
                          .collect(LinkedHashMap::new,
                                   (map, entry) -> map.put(entry.getKey(), entry.getValue()),
                                   Map::putAll);
    }

    private Map.Entry<String, Object> generate(final Map.Entry<String, Type<?>> entry) {
        return new AbstractMap.SimpleEntry<>(entry.getKey(), generate(entry.getValue(), new Priority(entry)));
    }

    private Object generate(final Type<?> type, final BinaryOperator<Method> priority) {
        return Methods.publicGetters(genType.core())
                      .filter(method -> type.boxed().isAssignableFrom(genType.returnTypeOf(method).boxed()))
                      .reduce(priority)
                      .map(function(method -> method.invoke(generator)))
                      .orElse(null);
    }

    private final class Priority implements BinaryOperator<Method> {

        private static final String ANY = "any";

        private final String name;
        private final Type<?> type;

        private Priority(final Map.Entry<String, Type<?>> entry) {
            this.name = entry.getKey();
            this.type = entry.getValue();
        }

        private static String firstToUpper(final String name) {
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }

        @SuppressWarnings("MethodWithMultipleReturnPoints")
        @Override
        public final Method apply(final Method left, final Method right) {
            final String nameByName = ANY + firstToUpper(name);
            final String leftName = left.getName();
            if (leftName.equals(nameByName)) {
                return left;
            }
            final String rightName = right.getName();
            if (rightName.equals(nameByName)) {
                return right;
            }

            final String nameByType = ANY + firstToUpper(type.core().getSimpleName());
            if (leftName.equals(nameByType)) {
                return left;
            }
            if (rightName.equals(nameByType)) {
                return right;
            }

            if (leftName.startsWith(ANY)) {
                return left;
            }
            if (rightName.startsWith(ANY)) {
                return right;
            }

            if (genType.returnTypeOf(left).equals(type)) {
                return left;
            }
            if (genType.returnTypeOf(right).equals(type)) {
                return right;
            }
            return left;
        }
    }
}
