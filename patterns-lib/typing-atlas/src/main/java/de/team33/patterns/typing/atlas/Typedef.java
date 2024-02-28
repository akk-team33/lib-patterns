package de.team33.patterns.typing.atlas;

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
 * Represents a specific <em>type</em>, just as {@link Class}{@code <?>} represents a specific <em>class</em>.
 * <p>
 * For example, an instance of {@link Class}{@code <?>} may uniquely represent the <em>class</em> {@link String}
 * and an instance of {@link Typedef} may uniquely represent the <em>type</em> {@link String}.
 * <p>
 * However, while there cannot be an instance of {@link Class}{@code <?>} e.g. representing a <em>class</em>
 * {@code List<String>}, an instance of {@link Typedef} representing the <em>type</em>
 * {@code List<String>} is absolutely possible.
 * <p>
 * To get an instance of {@link Typedef} see {@link de.team33.patterns.typing.atlas.generic.Type}.
 * If a simple class already fully defines the <em>type</em> concerned, there is a convenience method to
 * get a corresponding {@link Typedef} instance. Example:
 * <pre>
 * final Typedef stringType = Typedef.by(String.class);
 * </pre>
 *
 * @see de.team33.patterns.typing.atlas.generic.Type
 * @see #by(Class)
 */
@SuppressWarnings("ClassWithTooManyMethods")
public abstract class Typedef {

    private static final String NOT_DECLARED_IN_THIS = "member (%s) is not declared in the context of type (%s)";

    private final transient Lazy<List<Object>> listView;
    private final transient Lazy<Integer> hashValue;

    protected Typedef() {
        this.listView = Lazy.init(() -> Arrays.asList(asClass(), getActualParameters()));
        this.hashValue = Lazy.init(() -> listView.get().hashCode());
    }

    /**
     * Returns a {@link Typedef} based on a simple {@link Class}. Example:
     * <pre>
     * final Typedef stringType = Typedef.by(String.class);
     * </pre>
     *
     * @see Typedef
     */
    public static Typedef by(final Class<?> tClass) {
        return ClassCase.toTypedef(tClass);
    }

    /**
     * Returns the {@link Class} on which this {@link Typedef} is based.
     */
    public abstract Class<?> asClass();

    /**
     * Returns the names of the formal <em>type parameters</em> of the generic {@linkplain #asClass() class underlying}
     * <em>this</em> {@link Typedef}.
     * <p>
     * Returns an empty {@link List} if the {@linkplain #asClass() underlying class} is not generic.
     *
     * @see #getActualParameters()
     */
    public abstract List<String> getFormalParameters();

    /**
     * <p>Returns the actual <em>type parameters</em> defining this {@link Typedef}.
     * <p>The result may be empty even if the formal parameter list is not. Otherwise, the formal
     * and actual parameter list are of the same size and corresponding order.
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
        return TypeCase.toTypedef(type, this);
    }

    /**
     * Returns the <em>type</em> from which this <em>type</em> is derived (if so).
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
     * Returns the interfaces from which this <em>type</em> are derived (if so).
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
     * Returns all the types (class, interfaces) from which this <em>type</em> is derived (if so).
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
     * Returns the <em>type</em> of given {@link Field} if it is defined in the <em>type hierarchy</em> of this
     * <em>type</em>.
     *
     * @throws IllegalArgumentException if the given {@link Field} is not defined in the <em>type hierarchy</em> of
     * this <em>type</em>.
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
     * Returns the <em>return type</em> of a given {@link Method} if it is defined in the <em>type hierarchy</em> of
     * this <em>type</em>.
     *
     * @throws IllegalArgumentException if the given {@link Method} is not defined in the <em>type hierarchy</em> of
     * this <em>type</em>.
     */
    public final Typedef returnTypeOf(final Method method) {
        return Optional.ofNullable(nullableTypeOf(method, Method::getGenericReturnType))
                       .orElseThrow(() -> new IllegalArgumentException(format(NOT_DECLARED_IN_THIS, method, this)));
    }

    /**
     * Returns the parameter types of a given {@link Method} if it is defined in the <em>type hierarchy</em> of this
     * <em>type</em>.
     *
     * @throws IllegalArgumentException if the given {@link Method} is not defined in the <em>type hierarchy</em> of
     * this <em>type</em>.
     */
    public final List<Typedef> parameterTypesOf(final Method method) {
        return Optional.ofNullable(nullableTypesOf(method, Method::getGenericParameterTypes))
                       .orElseThrow(() -> new IllegalArgumentException(format(NOT_DECLARED_IN_THIS, method, this)));
    }

    /**
     * Returns the exception types of a given {@link Method} if it is defined in the <em>type hierarchy</em> of this
     * <em>type</em>.
     *
     * @throws IllegalArgumentException if the given {@link Method} is not defined in the <em>type hierarchy</em> of
     * this <em>type</em>.
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

    /**
     * The <em>obj</em> is equal to <em>this</em> if and only if the <em>obj</em> is an instance of {@link Typedef}
     * and their {@linkplain #asClass() underlying classes} are the same and their
     * {@linkplain #getActualParameters() actual type parameters} are equal.
     */
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
