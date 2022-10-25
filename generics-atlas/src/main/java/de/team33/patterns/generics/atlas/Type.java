package de.team33.patterns.generics.atlas;

import de.team33.patterns.lazy.e1.Lazy;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * Represents a definite type description that can be based on a generic as well as a non-generic class. Examples:
 * </p><ul>
 * <li>an instance of <strong>{@code Type<Map<String, List<String>>>}</strong> represents the type
 * <strong>{@code Map<String, List<String>>}</strong>.</li>
 * <li>an instance of <strong>{@code Type<String>}</strong> represents the type <strong>{@code String}</strong>.</li>
 * </ul><p>
 * To get an instance of Type, you need to create a definite derivative of Type.
 * The easiest way to achieve this is to use an anonymous derivation with simultaneous instantiation. Examples:
 * </p><pre>
 * final Type&lt;Map&lt;String, List&lt;String&gt;&gt;&gt; mapStringToListOfStringType
 *         = new Type&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;() { };
 * </pre><pre>
 * final Type&lt;String&gt; stringType
 *         = new Type&lt;String&gt;() { };
 * </pre><p>
 * If, as in the last case, a simple class already fully defines the type concerned, there is a convenience method to
 * get a corresponding Type instance. Example:
 * </p><pre>
 * final Type&lt;String&gt; stringType
 *         = Type.of(String.class);
 * </pre><p>
 *
 * @see #Type()
 * @see #of(Class)
 */
@SuppressWarnings({"AbstractClassWithoutAbstractMethods", "unused"})
public abstract class Type<T> {

    private static final Stream<? extends Type<?>> EMPTY = Stream.empty();
    private static final String NOT_DECLARED_IN_THIS = "member (%s) is not declared in the context of type (%s)";

    private final Setup setup;
    private final Supplier<List<Object>> listView = new Lazy<>(this::newListView);
    private final Supplier<String> stringView = new Lazy<>(this::newStringView);
    private final Supplier<Integer> hashCode = new Lazy<>(this::newHashCode);
    private final Supplier<List<Type<?>>> actualParameters = new Lazy<>(this::newActualParameters);

    private List<Object> newListView() {
        return Arrays.asList(asClass(), getActualParameters());
    }

    private String newStringView() {
        return setup.toString();
    }

    private Integer newHashCode() {
        return listView.get().hashCode();
    }

    private List<Type<?>> newActualParameters() {
        return Collections.unmodifiableList(
                setup.getActualParameters().stream()
                     .map(Type::of)
                     .collect(Collectors.toList())
        );
    }

    /**
     * Initializes a {@link Type} based on its well-defined derivative.
     */
    protected Type() {
        final ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.setup = TypeCase.toStage(
                genericSuperclass.getActualTypeArguments()[0],
                ClassCase.toStage(getClass())
        );
    }

    private Type(final Setup setup) {
        this.setup = setup;
    }

    /**
     * Returns a {@link Type} based on a simple {@link Class}.
     */
    public static <T> Type<T> of(final Class<T> simpleClass) {
        return new Type<T>(ClassCase.toStage(simpleClass)) {
        };
    }

    private static Type<?> of(final Setup setup) {
        return new Type(setup) {
        };
    }

    /**
     * Returns the {@link Class} on which this Type is based.
     */
    public final Class<?> asClass() {
        return setup.asClass();
    }

    /**
     * Returns the formal type parameter of the generic type underlying this Type.
     *
     * @see #getActualParameters()
     */
    public final List<String> getFormalParameters() {
        return setup.getFormalParameters();
    }

    /**
     * <p>Returns the actual type parameters defining this Type.</p>
     * <p>The result may be empty even if the formal parameter list is not. Otherwise the formal
     * and actual parameter list are of the same size and order.</p>
     *
     * @see #getFormalParameters()
     */
    public final List<Type<?>> getActualParameters() {
        return actualParameters.get();
    }

    /**
     * Converts a (possibly generic) {@link java.lang.reflect.Type} that exists in the
     * {@linkplain #asClass() underlying class} of this Type into a definite Type (like this).
     *
     * @see Class#getGenericSuperclass()
     * @see Class#getGenericInterfaces()
     * @see Class#getFields()
     * @see Class#getMethods()
     * @see Field#getGenericType()
     * @see Method#getGenericReturnType()
     * @see Method#getGenericParameterTypes()
     */
    public final Type<?> getMemberType(final java.lang.reflect.Type type) {
        return new Type(TypeCase.toStage(type, setup)) {
        };
    }

    /**
     * Returns the type from which this type is derived (if so).
     *
     * @see Class#getSuperclass()
     * @see Class#getGenericSuperclass()
     */
    public final Optional<Type<?>> getSuperType() {
        return Optional.ofNullable(asClass().getGenericSuperclass())
                       .map(this::getMemberType);
    }

    /**
     * Returns the interfaces from which this type are derived (if so).
     *
     * @see Class#getInterfaces()
     * @see Class#getGenericInterfaces()
     */
    public final Stream<Type<?>> getInterfaces() {
        return Stream.of(asClass().getGenericInterfaces())
                     .map(this::getMemberType);
    }

    /**
     * Returns all the types (class, interfaces) from which this type is derived (if so).
     *
     * @see #getSuperType()
     * @see #getInterfaces()
     */
    public final Stream<Type<?>> getSuperTypes() {
        return Stream.concat(
                getSuperType().map(Stream::of).orElseGet(Stream::empty),
                getInterfaces()
        );
    }

    /**
     * Returns the type of a given {@link Field} if it is defined in the type hierarchy of this type.
     *
     * @throws IllegalArgumentException if the given {@link Field} is not defined in the type hierarchy of this type.
     */
    public final Type<?> typeOf(final Field field) {
        return Optional
                .ofNullable(nullableTypeOf(field, Field::getGenericType))
                .orElseThrow(() -> new IllegalArgumentException(String.format(NOT_DECLARED_IN_THIS, field, this)));
    }

    /**
     * Returns the return type of a given {@link Method} if it is defined in the type hierarchy of this type.
     *
     * @throws IllegalArgumentException if the given {@link Method} is not defined in the type hierarchy of this type.
     */
    public final Type<?> returnTypeOf(final Method method) {
        return Optional
                .ofNullable(nullableTypeOf(method, Method::getGenericReturnType))
                .orElseThrow(() -> new IllegalArgumentException(String.format(NOT_DECLARED_IN_THIS, method, this)));
    }

    private <M extends Member> Type<?> nullableTypeOf(final M member,
                                                      final Function<M, java.lang.reflect.Type> toGenericType) {
        if (asClass().equals(member.getDeclaringClass())) {
            return getMemberType(toGenericType.apply(member));
        } else {
            return getSuperTypes()
                    .map(st -> st.nullableTypeOf(member, toGenericType))
                    .filter(Objects::nonNull)
                    .findAny()
                    .orElse(null);
        }
    }

    /**
     * Returns the parameter types of a given {@link Method} if it is defined in the type hierarchy of this type.
     *
     * @throws IllegalArgumentException if the given {@link Method} is not defined in the type hierarchy of this type.
     */
    public final List<Type<?>> parameterTypesOf(final Method method) {
        return Optional
                .ofNullable(nullableTypesOf(method, Method::getGenericParameterTypes))
                .orElseThrow(() -> new IllegalArgumentException(String.format(NOT_DECLARED_IN_THIS, method, this)));
    }

    /**
     * Returns the exception types of a given {@link Method} if it is defined in the type hierarchy of this type.
     *
     * @throws IllegalArgumentException if the given {@link Method} is not defined in the type hierarchy of this type.
     */
    public final List<Type<?>> exceptionTypesOf(final Method method) {
        return Optional
                .ofNullable(nullableTypesOf(method, Method::getGenericExceptionTypes))
                .orElseThrow(() -> new IllegalArgumentException(String.format(NOT_DECLARED_IN_THIS, method, this)));
    }

    private List<Type<?>> nullableTypesOf(final Method member,
                                          final Function<Method, java.lang.reflect.Type[]> toGenericTypes) {
        if (asClass().equals(member.getDeclaringClass())) {
            return Stream.of(toGenericTypes.apply(member))
                         .map(this::getMemberType)
                         .collect(Collectors.toList());
        } else {
            return getSuperTypes()
                    .map(st -> st.nullableTypesOf(member, toGenericTypes))
                    .filter(Objects::nonNull)
                    .findAny()
                    .orElse(null);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Two instances of Type are equal if they are {@linkplain #asClass() based} on the same class
     * and defined by the same {@linkplain #getActualParameters() actual parameters}.
     * </p>
     */
    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof Type) && isEqual((Type<?>) obj));
    }

    private boolean isEqual(final Type<?> other) {
        return listView.get().equals(other.listView.get());
    }

    @Override
    public final int hashCode() {
        return hashCode.get();
    }

    @Override
    public final String toString() {
        return stringView.get();
    }
}
