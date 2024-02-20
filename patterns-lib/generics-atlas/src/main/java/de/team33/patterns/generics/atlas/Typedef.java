package de.team33.patterns.generics.atlas;

import de.team33.patterns.lazy.narvi.Lazy;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * {@code Typedef} represents a specific <em>type</em>, just as {@link Class}{@code <?>} represents a specific
 * <em>class</em>.
 * <p>
 * For example, an instance of {@code Class<?>} may uniquely represent the <em>class</em> {@link String}
 * and an instance of {@code Type} may uniquely represent the <em>type</em> {@link String}.
 * <p>
 * However, while there cannot be an instance of {@code Class<?>} e.g. representing {@code List<String>}, an instance
 * of {@code Type} representing the <em>type</em> {@code List<String>} is absolutely possible.
 * <p>
 * To get an instance of Typedef, ... (to be continued)
 */
@SuppressWarnings("ClassWithTooManyMethods")
public abstract class Typedef {

    private static final String NOT_DECLARED_IN_THIS = "member (%s) is not declared in the context of type (%s)";

    private final transient Lazy<List<Object>> listView;
    private final transient Lazy<Integer> hashValue;

    Typedef() {
        this.listView = Lazy.init(() -> Arrays.asList(asClass(), getActualParameters()));
        this.hashValue = Lazy.init(() -> listView.get().hashCode());
    }

    public static Typedef by(final Class<?> tClass) {
        return ClassCase.toTypedef(tClass);
    }

    /**
     * Returns the {@link Class} on which this <em>type</em>> is based.
     */
    public abstract Class<?> asClass();

    /**
     * Returns the formal type parameters of the generic type underlying this <em>type</em>.
     *
     * @see #getActualParameters()
     */
    public abstract List<String> getFormalParameters();

    /**
     * <p>Returns the actual type parameters defining this <em>type</em>.
     * <p>The result may be empty even if the formal parameter list is not. Otherwise, the formal
     * and actual parameter list are of the same size and order.
     *
     * @see #getFormalParameters()
     */
    public abstract List<Typedef> getActualParameters();

    final Typedef getActualParameter(final String name) {
        final List<String> formalParameters = getFormalParameters();
        return Optional.of(formalParameters.indexOf(name))
                       .filter(index -> 0 <= index)
                       .map(index -> getActualParameterByIndex(name, index))
                       .orElseThrow(() -> new IllegalArgumentException(
                               format("formal parameter <%s> not found in %s", name, formalParameters)));
    }

    private Typedef getActualParameterByIndex(final String name, final int index) {
        final List<Typedef> actualParameters = getActualParameters();
        if (index < actualParameters.size()) {
            return actualParameters.get(index);
        } else {
            throw new IllegalStateException(
                    format("actual parameter for <%s> not found in %s", name, actualParameters));
        }
    }

    final Typedef getMemberType(final Type type) {
        return TypeCase.toAssembly(type, this);
    }

    /**
     * Returns the type from which this type is derived (if so).
     *
     * @see Class#getSuperclass()
     * @see Class#getGenericSuperclass()
     */
    public final Optional<Typedef> getSuperType() {
        return Optional.ofNullable(asClass().getGenericSuperclass())
                       .map(this::getMemberType);
    }

    private Stream<Typedef> streamSuperType() {
        return getSuperType().map(Stream::of)
                             .orElseGet(Stream::empty);
    }

    /**
     * Returns the interfaces from which this type are derived (if so).
     *
     * @see Class#getInterfaces()
     * @see Class#getGenericInterfaces()
     */
    public final List<Typedef> getInterfaces() {
        return streamInterfaces().collect(Collectors.toList());
    }

    private Stream<Typedef> streamInterfaces() {
        return Stream.of(asClass().getGenericInterfaces())
                     .map(this::getMemberType);
    }

    /**
     * Returns all the types (class, interfaces) from which this type is derived (if so).
     *
     * @see #getSuperType()
     * @see #getInterfaces()
     */
    public final List<Typedef> getSuperTypes() {
        return streamSuperTypes().collect(Collectors.toList());
    }

    private Stream<Typedef> streamSuperTypes() {
        return Stream.concat(streamSuperType(), streamInterfaces());
    }

    /**
     * Returns the type of given {@link Field} if it is defined in the type hierarchy of this type.
     *
     * @throws IllegalArgumentException if the given {@link Field} is not defined in the type hierarchy of this type.
     */
    public final Typedef typeOf(final Field field) {
        return Optional.ofNullable(nullableTypeOf(field, Field::getGenericType))
                       .orElseThrow(() -> new IllegalArgumentException(format(NOT_DECLARED_IN_THIS, field, this)));
    }

    private <M extends Member> Typedef nullableTypeOf(final M member, final Function<M, Type> toGenericType) {
        if (asClass().equals(member.getDeclaringClass())) {
            return getMemberType(toGenericType.apply(member));
        } else {
            return streamSuperTypes().map(st -> st.nullableTypeOf(member, toGenericType))
                                     .filter(Objects::nonNull)
                                     .findAny()
                                     .orElse(null);
        }
    }

    /**
     * Returns the return type of a given {@link Method} if it is defined in the type hierarchy of this type.
     *
     * @throws IllegalArgumentException if the given {@link Method} is not defined in the type hierarchy of this type.
     */
    public final Typedef returnTypeOf(final Method method) {
        return Optional.ofNullable(nullableTypeOf(method, Method::getGenericReturnType))
                       .orElseThrow(() -> new IllegalArgumentException(format(NOT_DECLARED_IN_THIS, method, this)));
    }

    /**
     * Returns the parameter types of a given {@link Method} if it is defined in the type hierarchy of this type.
     *
     * @throws IllegalArgumentException if the given {@link Method} is not defined in the type hierarchy of this type.
     */
    public final List<Typedef> parameterTypesOf(final Method method) {
        return Optional.ofNullable(nullableTypesOf(method, Method::getGenericParameterTypes))
                       .orElseThrow(() -> new IllegalArgumentException(format(NOT_DECLARED_IN_THIS, method, this)));
    }

    /**
     * Returns the exception types of a given {@link Method} if it is defined in the type hierarchy of this type.
     *
     * @throws IllegalArgumentException if the given {@link Method} is not defined in the type hierarchy of this type.
     */
    public final List<Typedef> exceptionTypesOf(final Method method) {
        return Optional.ofNullable(nullableTypesOf(method, Method::getGenericExceptionTypes))
                       .orElseThrow(() -> new IllegalArgumentException(format(NOT_DECLARED_IN_THIS, method, this)));
    }

    private List<Typedef> nullableTypesOf(final Method member, final Function<Method, Type[]> toGenericTypes) {
        if (asClass().equals(member.getDeclaringClass())) {
            return Stream.of(toGenericTypes.apply(member))
                         .map(this::getMemberType)
                         .collect(Collectors.toList());
        } else {
            return streamSuperTypes().map(st -> st.nullableTypesOf(member, toGenericTypes))
                                     .filter(Objects::nonNull)
                                     .findAny()
                                     .orElse(null);
        }
    }

    private boolean equals(final Typedef other) {
        return listView.get().equals(other.listView.get());
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof Typedef) && equals((Typedef) obj));
    }

    @Override
    public final int hashCode() {
        return hashValue.get();
    }

    @Override
    public abstract String toString();
}
