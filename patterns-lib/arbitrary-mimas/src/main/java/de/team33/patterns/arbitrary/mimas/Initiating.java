package de.team33.patterns.arbitrary.mimas;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.lang.String.format;

final class Initiating<S extends Initiator, T> extends Supplying<S> {

    private static final String NO_SUPPLIER = Util.load(Initiating.class, "noSupplierMethodFound4Parameter.txt");
    private static final String UNFIT_CONSTRUCTOR = Util.load(Initiating.class, "constructorNotApplicable.txt");

    private final Class<T> targetType;
    private final Constructor<T> constructor;
    private final Parameter[] parameters;

    Initiating(final S source, final Class<T> targetType, final Collection<String> ignore) {
        super(source, ignore);
        this.targetType = targetType;
        this.constructor = constructor(targetType);
        this.parameters = constructor.getParameters();
    }

    private static <T> Constructor<T> constructor(final Class<T> targetType) {
        try {
            final Class<?>[] types = Stream.of(targetType.getConstructors())
                                           .reduce(Initiating::largest)
                                           .map(Constructor::getParameterTypes)
                                           .orElseThrow(() -> new NoSuchMethodException(
                                                   "No public constructor found in " + targetType));
            return targetType.getConstructor(types);
        } catch (final NoSuchMethodException e) {
            throw new LocalException(e.getMessage(), e);
        }
    }

    private static Constructor<?> largest(final Constructor<?> left, final Constructor<?> right) {
        return (left.getParameterCount() < right.getParameterCount()) ? right : left;
    }

    private Object[] arguments() {
        return Stream.of(parameters)
                     .map(this::argument)
                     .toArray(Object[]::new);
    }

    private Object argument(final Parameter parameter) {
        final Type parameterType = parameter.getParameterizedType();
        final Supplier<?> supplier = desiredSupplier(parameterType, preference(parameter));
        if (ignorable.contains(parameter.getName())) {
            return Types.defaultValue(parameterType);
        } else if (null != supplier) {
            return supplier.get();
        } else {
            throw new LocalException(this, parameter.getName(), parameterType);
        }
    }

    private static BinaryOperator<Method> preference(final Parameter parameter) {
        return (left, right) -> preference(parameter.getName(), left, right);
    }

    private static Method preference(final String parameterName, final Method left, final Method right) {
        return Methods.normalName(right).equals(parameterName) ? right : left;
    }

    final T result() {
        try {
            return constructor.newInstance(arguments());
        } catch (final InstantiationException | IllegalAccessException |
                InvocationTargetException | IllegalArgumentException e) {
            throw new LocalException(format(UNFIT_CONSTRUCTOR, targetType, constructor.toGenericString()), e);
        }
    }

    private static final class LocalException extends UnfitConditionException {

        LocalException(final String message, final Throwable cause) {
            super(message, cause);
        }

        LocalException(final Initiating<?, ?> initiating, final String parameterName, final Type parameterType) {
            super(missingMessage(initiating, parameterName, parameterType), null);
        }

        private static String missingMessage(final Initiating<?, ?> initiating, final String name, final Type type) {
            final Types.Naming naming = Types.naming(type);
            final String name1 = naming.parameterizedName(type);
            final String name2 = naming.simpleName(type);
            return format(NO_SUPPLIER, initiating.sourceType, initiating.targetType,
                          initiating.constructor.toGenericString(), name, name1, name2);
        }
    }
}
