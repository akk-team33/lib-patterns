package de.team33.patterns.random.tarvos;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class Beans {

    private static final String CANNOT_APPLY_2 = "cannot apply method <%s> with on origin <%s>";
    private static final String CANNOT_APPLY_3 = "cannot apply method <%s> with value <%s> on target <%s>";

    private Beans() {
    }

    static List<Property> getProperties(final Class<?> beanClass) {
        return Stream.of(beanClass.getMethods())
                     .filter(method -> method.getName().startsWith("set"))
                     .filter(method -> 1 == method.getParameterCount())
                     .map(setter -> new Property(beanClass, setter))
                     .collect(Collectors.toList());
    }

    @SuppressWarnings("PackageVisibleField")
    static class Property {

        final Class<?> beanClass;
        final Class<?> type;
        final BiConsumer<Object, Object> setter;
        final Method setterMethod;

        Property(final Class<?> beanClass, final Method setter) {
            this.beanClass = beanClass;
            this.setterMethod = setter;
            this.setter = toBiConsumer(setter);
            this.type = setter.getParameterTypes()[0];
        }

        @SuppressWarnings("DynamicRegexReplaceableByCompiledPattern")
        private static Optional<Method> getter(final Class<?> type, final Method setter, final String prefix) {
            try {
                return Optional.of(type.getMethod(setter.getName().replace("set", prefix)));
            } catch (final NoSuchMethodException e) {
                return Optional.empty();
            }
        }

        private static Function<Object, Object> toFunction(final Method method) {
            return origin -> {
                try {
                    return method.invoke(origin);
                } catch (final IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalArgumentException(String.format(CANNOT_APPLY_2, method, origin), e);
                }
            };
        }

        private static Function<Object, Object> toFunction(final DefaultValue defaultValue) {
            return origin -> defaultValue.value();
        }

        private static BiConsumer<Object, Object> toBiConsumer(final Method method) {
            return (target, value) -> {
                try {
                    method.invoke(target, value);
                } catch (final IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalArgumentException(String.format(CANNOT_APPLY_3, method, value, target), e);
                }
            };
        }

        final Function<Object, Object> getterOf(final Class<?> templateClass) {
            return getter(templateClass, setterMethod, "get").map(Property::toFunction)
                                                             .orElseGet(() -> boolGetterOf(templateClass));
        }

        final Function<Object, Object> boolGetterOf(final Class<?> templateClass) {
            return getter(templateClass, setterMethod, "is").map(Property::toFunction)
                                                            .orElseGet(() -> toFunction(DefaultValue.of(type)));
        }
    }
}
