package de.team33.patterns.typing.theta;

import de.team33.patterns.lazy.narvi.Lazy;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@code Type<T>} represents a specific <em>type</em>, just as {@link Class}{@code <T>} represents a specific
 * <em>class</em>.
 * <p>
 * For example, an instance of {@code Class<String>} uniquely represents the <em>class</em> {@link String}
 * and an instance of {@code Type<String>} uniquely represents the <em>type</em> {@link String}.
 * <p>
 * However, while there cannot be an instance of e.g. {@code Class<List<String>>}, an instance of
 * {@code Type<List<String>>} is absolutely possible. It then represents the <em>type</em> {@code List<String>}.
 * <p>
 * To get an instance of Type, you need to create a definite derivative of Type.
 * The easiest way to achieve this is to directly instantiate an anonymous derivation. Examples:
 * <pre>
 * final Type&lt;List&lt;String&gt;&gt; listOfStringType =
 *         new Type&lt;List&lt;String&gt;&gt;() { };
 * </pre><pre>
 * final Type&lt;String&gt; stringType =
 *         new Type&lt;String&gt;() { };
 * </pre><p>
 * If, as in the last case, a simple class already fully defines the type concerned, there is a convenience method to
 * get a corresponding Type instance. Example:
 * <pre>
 * final Type&lt;String&gt; stringType = Type.of(String.class);
 * </pre>
 *
 * @see #Type()
 * @see #of(Class)
 */
@SuppressWarnings({"AbstractClassWithoutAbstractMethods", "unused"})
public abstract class Type<T> {

    private static final Stream<? extends Type<?>> EMPTY = Stream.empty();
    private static final String NOT_DECLARED_IN_THIS = "member (%s) is not declared in the context of type (%s)";
    private static final String ILLEGAL_INSTANTIATION = //
            "Do not directly instantiate %1$s%n" +
            "  In fact, it just doesn't work.%n" +
            "  Instead, try one of the following:%n" +
            "  - Instantiate an anonymous derivative, something like ...%n" +
            "    new %1$s(){};%n" +
            "    (of course, using definite types instead of formal type parameters).%n" +
            "  - Create a non-generic derivative of %1$s and use that for instantiation.%n";

    private final Backing backing;
    private final transient Lazy<List<Type<?>>> lazyActualParameters = Lazy.init(this::newActualParameters);

    /**
     * Initializes a {@link Type} based on its definite derivative. Example:
     * <pre>
     * final Type&lt;List&lt;String&gt;&gt; listOfStringType =
     *         new Type&lt;List&lt;String&gt;&gt;() { };
     * </pre>
     *
     * @see Type
     */
    protected Type() {
        this.backing = extract(ClassCase.toBacking(failGeneric(getClass())));
    }

    private Type(final Backing backing) {
        this.backing = backing;
    }

    private static Backing extract(final Backing thisBacking) {
        final Class<?> thisClass = thisBacking.core();
        if (Type.class.equals(thisClass)) {
            return thisBacking.actualParameters().get(0);
        }
        final Backing superBacking = thisBacking.memberBacking(thisClass.getGenericSuperclass());
        return extract(superBacking);
    }

    private static Class<?> failGeneric(final Class<?> thisClass) {
        final var parameters = thisClass.getTypeParameters();
        if (parameters.length > 0) {
            final String signature = //
                    Stream.of(parameters)
                          .map(TypeVariable::getName)
                          .collect(Collectors.joining(", ",
                                                      thisClass.getSimpleName() + "<",
                                                      ">"));
            throw new IllegalStateException(String.format(ILLEGAL_INSTANTIATION, signature));
        }
        return thisClass;
    }

    /**
     * Returns a {@link Type} based on a simple {@link Class}. Example:
     * <pre>
     * final Type&lt;String&gt; stringType = Type.of(String.class);
     * </pre>
     *
     * @see Type
     */
    public static <T> Type<T> of(final Class<T> simpleClass) {
        return new Type<T>(ClassCase.toBacking(simpleClass)) {
        };
    }

    private static Type<?> by(final Backing backing) {
        return new Type<>(backing) {
        };
    }

    private List<Type<?>> newActualParameters() {
        return backing.actualParameters()
                      .stream()
                      .map(Type::by)
                      .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Returns the {@link Class} that represents the core of <em>this</em> Type.
     */
    public final Class<?> core() {
        return backing.core();
    }

    /**
     * Returns the formal type parameters of the represented Type.
     * More precisely, the names of the formal type parameters of the {@link #core()} of <em>this</em> Type.
     *
     * @see #actualParameters()
     * @see Class#getTypeParameters()
     */
    public final List<String> formalParameters() {
        return backing.formalParameters();
    }

    /**
     * Returns the actual type parameters of the represented type.
     * <p>
     * The result may be empty even if the formal parameter list is not,
     * for instance in the case of {@code Type<Map>} - representing a raw generic type.
     * Otherwise, the formal and actual parameter list have the same size and corresponding order.
     *
     * @see #formalParameters()
     */
    public final List<Type<?>> actualParameters() {
        return lazyActualParameters.get();
    }

    private Type<?> memberType(final java.lang.reflect.Type type) {
        return by(backing.memberBacking(type));
    }

    /**
     * Returns the type from which <em>this</em> Type is derived (if so).
     *
     * @see Class#getSuperclass()
     * @see Class#getGenericSuperclass()
     */
    public final Optional<Type<?>> superType() {
        return Optional.ofNullable(core().getGenericSuperclass())
                       .map(this::memberType);
    }

    /**
     * Returns the interfaces from which <em>this</em> Type are derived (if so).
     *
     * @see Class#getInterfaces()
     * @see Class#getGenericInterfaces()
     */
    public final List<Type<?>> interfaces() {
        return streamInterfaces().collect(Collectors.toList());
    }

    private Stream<Type<?>> streamInterfaces() {
        return Stream.of(core().getGenericInterfaces())
                     .map(this::memberType);
    }

    /**
     * Returns all the types (class, interfaces) from which <em>this</em> Type is derived (if so).
     *
     * @see #superType()
     * @see #interfaces()
     */
    public final List<Type<?>> superTypes() {
        return streamSuperTypes().collect(Collectors.toList());
    }

    private Stream<Type<?>> streamSuperTypes() {
        return Stream.concat(superType().stream(), streamInterfaces());
    }

    /**
     * Returns the type of a given {@link Field} if it is defined in the type hierarchy of <em>this</em> Type.
     *
     * @throws IllegalArgumentException if the given {@link Field} is not defined in the type hierarchy of <em>this</em> Type.
     */
    public final Type<?> typeOf(final Field field) {
        return Optional
                .ofNullable(nullableTypeOf(field, Field::getGenericType))
                .orElseThrow(() -> new IllegalArgumentException(String.format(NOT_DECLARED_IN_THIS, field, this)));
    }

    /**
     * Returns the return type of a given {@link Method} if it is defined in the type hierarchy of <em>this</em> Type.
     *
     * @throws IllegalArgumentException if the given {@link Method} is not defined in the type hierarchy of <em>this</em> Type.
     */
    public final Type<?> returnTypeOf(final Method method) {
        return Optional
                .ofNullable(nullableTypeOf(method, Method::getGenericReturnType))
                .orElseThrow(() -> new IllegalArgumentException(String.format(NOT_DECLARED_IN_THIS, method, this)));
    }

    private <M extends Member> Type<?> nullableTypeOf(final M member,
                                                      final Function<M, java.lang.reflect.Type> toGenericType) {
        if (core().equals(member.getDeclaringClass())) {
            return memberType(toGenericType.apply(member));
        } else {
            return streamSuperTypes().map(st -> st.nullableTypeOf(member, toGenericType))
                                     .filter(Objects::nonNull)
                                     .findAny()
                                     .orElse(null);
        }
    }

    /**
     * Returns the parameter types of a given {@link Method} if it is defined in the type hierarchy of <em>this</em> Type.
     *
     * @throws IllegalArgumentException if the given {@link Method} is not defined in the type hierarchy of <em>this</em> Type.
     */
    public final List<Type<?>> parameterTypesOf(final Method method) {
        return Optional
                .ofNullable(nullableTypesOf(method, Method::getGenericParameterTypes))
                .orElseThrow(() -> new IllegalArgumentException(String.format(NOT_DECLARED_IN_THIS, method, this)));
    }

    /**
     * Returns the exception types of a given {@link Method} if it is defined in the type hierarchy of <em>this</em> Type.
     *
     * @throws IllegalArgumentException if the given {@link Method} is not defined in the type hierarchy of <em>this</em> Type.
     */
    public final List<Type<?>> exceptionTypesOf(final Method method) {
        return Optional
                .ofNullable(nullableTypesOf(method, Method::getGenericExceptionTypes))
                .orElseThrow(() -> new IllegalArgumentException(String.format(NOT_DECLARED_IN_THIS, method, this)));
    }

    private List<Type<?>> nullableTypesOf(final Method member,
                                          final Function<Method, java.lang.reflect.Type[]> toGenericTypes) {
        if (core().equals(member.getDeclaringClass())) {
            return Stream.of(toGenericTypes.apply(member))
                         .map(this::memberType)
                         .collect(Collectors.toList());
        } else {
            return streamSuperTypes().map(st -> st.nullableTypesOf(member, toGenericTypes))
                                     .filter(Objects::nonNull)
                                     .findAny()
                                     .orElse(null);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Two instances of Type are equal if they are {@linkplain #core() based} on the same class
     * and defined by the same {@linkplain #actualParameters() actual parameters}.
     */
    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof Type) && equals((Type<?>) obj));
    }

    private boolean equals(final Type<?> other) {
        return backing.equals(other.backing);
    }

    @Override
    public final int hashCode() {
        return backing.hashCode();
    }

    @Override
    public final String toString() {
        return backing.toString();
    }
}
