package de.team33.patterns.typing.proteus;

import de.team33.patterns.lazy.janus.Features;

import java.lang.reflect.*;
import java.util.List;
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
@SuppressWarnings({"AbstractClassWithoutAbstractMethods", "unused", "ClassWithTooManyMethods", "EqualsDoesntCheckParameterClass"})
public abstract class Type<T> {

    @SuppressWarnings("rawtypes")
    private static final Equation<Type> EQUATION = Equation.of(Type.class, type -> type.support);
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

    private final TypeSupport support;
    private final Features features = new Features();

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
        this.support = mainSupport(ClassCase.support(failGeneric(getClass())));
    }

    private Type(final TypeSupport support) {
        this.support = support;
    }

    private static TypeSupport mainSupport(final TypeSupport support) {
        final Class<?> core = support.core();
        if (Type.class.equals(support.core())) {
            return support.actualParameters().get(0);
        }
        final TypeSupport superSupport = support.memberSupport(core.getGenericSuperclass());
        return mainSupport(superSupport);
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
        return new Type<>(ClassCase.support(simpleClass)) {};
    }

    private static Type<?> by(final TypeSupport support) {
        return new Type<>(support) {};
    }

    /**
     * Returns the {@link Class} that represents the core of <em>this</em> Type.
     */
    public final Class<?> core() {
        return support.core();
    }

    /**
     * Returns the formal type parameters of the represented Type.
     * More precisely, the names of the formal type parameters of the {@link #core()} of <em>this</em> Type.
     *
     * @see #actualParameters()
     * @see Class#getTypeParameters()
     */
    public final List<String> formalParameters() {
        return support.formalParameters();
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
        return features.get(Key.ACTUAL_PARAMETERS,
                            () -> support.actualParameters()
                                         .stream()
                                         .map(Type::by)
                                         .collect(Collectors.toUnmodifiableList()));
    }

    private Type<?> memberType(final java.lang.reflect.Type type) {
        return by(support.memberSupport(type));
    }

    /**
     * Returns the type from which <em>this</em> Type is derived (if so).
     *
     * @see Class#getSuperclass()
     * @see Class#getGenericSuperclass()
     */
    public final Optional<Type<?>> superType() {
        return features.get(Key.SUPER_TYPE,
                            () -> Optional.ofNullable(core().getGenericSuperclass())
                                          .map(this::memberType));
    }

    /**
     * Returns the interfaces from which <em>this</em> Type are derived (if so).
     *
     * @see Class#getInterfaces()
     * @see Class#getGenericInterfaces()
     */
    public final List<Type<?>> interfaces() {
        return features.get(Key.INTERFACES,
                            () -> Stream.of(core().getGenericInterfaces())
                                        .map(this::memberType)
                                        .collect(Collectors.toUnmodifiableList()));
    }

    /**
     * Returns all the types (class, interfaces) from which <em>this</em> Type is derived (if so).
     *
     * @see #superType()
     * @see #interfaces()
     */
    public final List<Type<?>> superTypes() {
        return features.get(Key.SUPER_TYPES,
                            () -> Stream.concat(superType().stream(), interfaces().stream())
                                        .toList());
    }

    /**
     * Returns the type of the given {@link Field} if it is defined in the type hierarchy of <em>this</em> Type.
     *
     * @throws IllegalArgumentException if the given {@link Field} is not defined in the type hierarchy
     *                                  of <em>this</em> Type.
     */
    public final Type<?> typeOf(final Field field) {
        return optTypeOf(field, Field::getGenericType)
                .orElseThrow(() -> new IllegalArgumentException(NOT_DECLARED_IN_THIS.formatted(field, this)));
    }

    /**
     * Returns the type of the given {@link RecordComponent} if it is defined in <em>this</em> Type.
     *
     * @throws IllegalArgumentException if the given {@link RecordComponent} is not defined in <em>this</em> Type.
     */
    public final Type<?> typeOf(final RecordComponent component) {
        if (core().equals(component.getDeclaringRecord())) {
            return memberType(component.getGenericType());
        } else {
            throw new IllegalArgumentException(NOT_DECLARED_IN_THIS.formatted(component, this));
        }
    }

    /**
     * Returns the return type of the given {@link Method} if it is defined in the type hierarchy of <em>this</em> Type.
     *
     * @throws IllegalArgumentException if the given {@link Method} is not defined in the type hierarchy
     *                                  of <em>this</em> Type.
     */
    public final Type<?> returnTypeOf(final Method method) {
        return optTypeOf(method, Method::getGenericReturnType)
                .orElseThrow(() -> new IllegalArgumentException((NOT_DECLARED_IN_THIS.formatted(method, this))));
    }

    private <M extends Member> Optional<Type<?>> optTypeOf(final M member,
                                                           final Function<M, java.lang.reflect.Type> toGenericType) {
        if (core().equals(member.getDeclaringClass())) {
            return Optional.of(memberType(toGenericType.apply(member)));
        } else {
            return superTypes().stream()
                               .map(type -> type.optTypeOf(member, toGenericType))
                               .flatMap(Optional::stream)
                               .findAny();
        }
    }

    /**
     * Returns the parameter types of a given {@link Method} if it is defined in the type hierarchy of <em>this</em> Type.
     *
     * @throws IllegalArgumentException if the given {@link Method} is not defined in the type hierarchy
     *                                  of <em>this</em> Type.
     */
    public final List<Type<?>> parameterTypesOf(final Method method) {
        return optTypesOf(method, Method::getGenericParameterTypes)
                .orElseThrow(() -> new IllegalArgumentException(NOT_DECLARED_IN_THIS.formatted(method, this)));
    }

    /**
     * Returns the exception types of a given {@link Method} if it is defined in the type hierarchy of <em>this</em> Type.
     *
     * @throws IllegalArgumentException if the given {@link Method} is not defined in the type hierarchy
     *                                  of <em>this</em> Type.
     */
    public final List<Type<?>> exceptionTypesOf(final Method method) {
        return optTypesOf(method, Method::getGenericExceptionTypes)
                .orElseThrow(() -> new IllegalArgumentException(NOT_DECLARED_IN_THIS.formatted(method, this)));
    }

    @SuppressWarnings("OptionalContainsCollection")
    private Optional<List<Type<?>>> optTypesOf(final Method member,
                                               final Function<Method, java.lang.reflect.Type[]> toGenericTypes) {
        if (core().equals(member.getDeclaringClass())) {
            final List<Type<?>> list = Stream.of(toGenericTypes.apply(member))
                                             .map(this::memberType)
                                             .collect(Collectors.toUnmodifiableList());
            return Optional.of(list);
        } else {
            return superTypes().stream()
                               .map(type -> type.optTypesOf(member, toGenericTypes))
                               .flatMap(Optional::stream)
                               .findAny();
        }
    }

    /**
     * Two instances of Type are equal if they are based on the same {@linkplain #core() class}
     * and defined by the same {@linkplain #actualParameters() actual type parameters}.
     */
    @Override
    public final boolean equals(final Object obj) {
        return EQUATION.equals(this, obj);
    }

    @Override
    public final int hashCode() {
        return EQUATION.hashCode(this);
    }

    @Override
    public final String toString() {
        return EQUATION.toString(this);
    }

    private interface Key<T> extends Features.Key<T> {

        Key<Optional<Type<?>>> SUPER_TYPE = named("SUPER_TYPE");
        Key<List<Type<?>>> ACTUAL_PARAMETERS = named("ACTUAL_PARAMETERS");
        Key<List<Type<?>>> INTERFACES = named("INTERFACES");
        Key<List<Type<?>>> SUPER_TYPES = named("SUPER_TYPES");

        static <T> Key<T> named(final String name) {
            return new Key<>() {
                @Override
                public String toString() {
                    return name;
                }
            };
        }
    }
}
