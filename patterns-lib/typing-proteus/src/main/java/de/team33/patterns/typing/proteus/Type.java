package de.team33.patterns.typing.proteus;

import de.team33.patterns.lazy.janus.Features;
import de.team33.patterns.value.sinope.Equation;

import java.lang.reflect.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a Java type, including its actual type arguments.
 * <p>
 * While a {@link Class}{@code <T>} represents a Java class or interface without preserving the actual type arguments
 * with which a generic type is used, a {@code Type<T>} can represent a concrete parameterized type as well.
 * For example, {@code Class<List>} can represent the class {@code List}, whereas a {@code Type<List<String>>}
 * can represent the parameterized type {@code List<String>}.
 * In contrast, you cannot have a (valid) {@code Class<List<String>>}.
 * <p>
 * A {@code Type} is therefore particularly useful when the distinction between different parameterizations
 * of the same generic class or interface is relevant.
 * <p>
 * A {@code Type} can be created directly by instantiating an anonymous derivative whose type argument is
 * specified explicitly:
 * <pre>{@code final Type<List<String>> listOfString = new Type<List<String>>() {};}</pre>
 * <p>
 * The actual type argument is obtained from the generic type information of the derivative at runtime.
 * Consequently, a generic derivative must not be instantiated directly:
 * <pre>{@code
 * class MyType<T> extends Type<T> {}
 * // throws IllegalStateException:
 * new MyType<String>();
 * }</pre>
 * Instead, a derivative with a definite type argument can be used:
 * <pre>{@code
 * class StringType extends Type<String> {}
 * final Type<String> stringType = new StringType();
 * }</pre>
 * <p>
 * If a {@link Class}{@code <T>} already completely describes a type,
 * {@link #of(Class)} provides a simpler alternative:
 * <pre>{@code
 * final Type<String> stringType = Type.of(String.class);
 * }</pre>
 * <p>
 * In addition to representing a type, {@code Type} provides access to its generic type parameters and to
 * the types occurring in its class and interface hierarchy. It can also resolve the types of fields,
 * record components, and methods in the context of the represented type.
 *
 * @param <T> the represented Java type
 * @see #Type()
 * @see #of(Class)
 */
@SuppressWarnings({
        "AbstractClassWithoutAbstractMethods", "unused", "ClassWithTooManyMethods", "EqualsDoesntCheckParameterClass"})
public abstract class Type<T> {

    @SuppressWarnings("rawtypes")
    private static final Equation<Type> EQUATION = Equation.of(Type.class, type -> type.support);
    private static final Stream<? extends Type<?>> EMPTY = Stream.empty();
    private static final String NOT_DECLARED_IN_THIS = "member (%s) is not declared in the context of type (%s)";
    private static final String ILLEGAL_INSTANTIATION = //
            "Do not directly instantiate %1$s%n" +
            "  In fact, it just doesn't work.%n" +
            "  Instead, try one of the following:%n" +
            "  a) Instantiate an anonymous derivative, something like ...%n" +
            "     new %1$s(){};%n" +
            "     (of course, using definite type arguments instead of formal type parameters).%n" +
            "  b) Create a non-generic derivative of %1$s and use that for instantiation.%n";

    private final TypeSupport support;
    private final Features features = new Features();

    /**
     * Creates a {@code Type} from the definite generic type information of its derivative.
     * <p>
     * The usual way to invoke this constructor is by creating an anonymous derivative:
     * <pre>{@code
     * final Type<List<String>> type = new Type<List<String>>() {};
     * }</pre>
     * The constructor inspects the generic type hierarchy of the derivative to determine the represented
     * type and its actual type arguments.
     * <p>
     * A derivative that still declares formal type parameters cannot be instantiated as a {@code Type}:
     * such an instantiation does not provide sufficient information to determine a definite represented type.
     *
     * @throws IllegalStateException if the derivative does not provide sufficient information
     *                               to determine a definite represented type
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
     * Returns a {@link Type} representing the given {@link Class}.
     * <p>
     * Example:
     * <pre>{@code
     * final Type<String> stringType = Type.of(String.class);
     * }</pre>
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
     * Returns the {@link Class} associated with the represented type.
     * <p>
     * Returns {@code null} if and only if the represented type is a wildcard type.
     */
    public final Class<?> core() {
        return support.core();
    }

    /**
     * Returns the formal type parameters of the represented type.
     * More precisely, the names of the formal type parameters of the {@link #core()} of <em>this</em> type.
     * <p>
     * For an array type, the result contains exactly one element: {@code "E"}.
     * <p>
     * Returns an empty list if {@link #core()} returns {@code null}.
     *
     * @see #actualParameters()
     * @see Class#getTypeParameters()
     */
    public final List<String> formalParameters() {
        return support.formalParameters();
    }

    /**
     * Returns the actual type arguments of the represented type.
     * <p>
     * The result may be empty even if the list of formal type parameters is not,
     * for instance in the case of {@code Type<Map>} representing a raw generic type.
     * Otherwise, the lists of formal type parameters and actual type arguments have the same size
     * and corresponding order.
     * <p>
     * For an array type, the result contains exactly one element: the component {@link Type}.
     *
     * @see #formalParameters()
     * @see Class#getComponentType()
     * @see GenericArrayType#getGenericComponentType()
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
     * Returns the direct supertype of the represented type, if any.
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
     * Returns the direct superinterfaces of the represented type, if any.
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
     * Returns all direct supertypes of the represented type,
     * including its {@link #superType()} and {@link #interfaces()}.
     */
    public final List<Type<?>> superTypes() {
        return features.get(Key.SUPER_TYPES,
                            () -> Stream.concat(superType().stream(), interfaces().stream())
                                        .toList());
    }

    /**
     * Returns the type of the given {@link Field} if it is declared in the type hierarchy of <em>this</em> Type.
     *
     * @throws IllegalArgumentException if the given {@link Field} is not declared in the type hierarchy
     *                                  of <em>this</em> Type
     */
    public final Type<?> typeOf(final Field field) {
        return optTypeOf(field, Field::getGenericType)
                .orElseThrow(() -> new IllegalArgumentException(NOT_DECLARED_IN_THIS.formatted(field, this)));
    }

    /**
     * Returns the type of the given {@link RecordComponent} if it is declared in <em>this</em> Type.
     *
     * @throws IllegalArgumentException if the given {@link RecordComponent} is not declared in <em>this</em> Type
     */
    public final Type<?> typeOf(final RecordComponent component) {
        if (core().equals(component.getDeclaringRecord())) {
            return memberType(component.getGenericType());
        } else {
            throw new IllegalArgumentException(NOT_DECLARED_IN_THIS.formatted(component, this));
        }
    }

    /**
     * Returns the return type of the given {@link Method} if it is declared in the type hierarchy of
     * <em>this</em> Type.
     *
     * @throws IllegalArgumentException if the given {@link Method} is not declared in the type hierarchy
     *                                  of <em>this</em> Type
     */
    public final Type<?> returnTypeOf(final Method method) {
        return optTypeOf(method, Method::getGenericReturnType)
                .orElseThrow(() -> new IllegalArgumentException(NOT_DECLARED_IN_THIS.formatted(method, this)));
    }

    /**
     * Returns the parameter types of the given {@link Method} if it is declared in the type hierarchy of
     * <em>this</em> Type.
     *
     * @throws IllegalArgumentException if the given {@link Method} is not declared in the type hierarchy
     *                                  of <em>this</em> Type
     */
    public final List<Type<?>> parameterTypesOf(final Method method) {
        return optTypesOf(method, Method::getGenericParameterTypes)
                .orElseThrow(() -> new IllegalArgumentException(NOT_DECLARED_IN_THIS.formatted(method, this)));
    }

    /**
     * Returns the exception types of the given {@link Method} if it is declared in the type hierarchy of
     * <em>this</em> Type.
     *
     * @throws IllegalArgumentException if the given {@link Method} is not declared in the type hierarchy
     *                                  of <em>this</em> Type
     */
    public final List<Type<?>> exceptionTypesOf(final Method method) {
        return optTypesOf(method, Method::getGenericExceptionTypes)
                .orElseThrow(() -> new IllegalArgumentException(NOT_DECLARED_IN_THIS.formatted(method, this)));
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
     * Indicates whether another object is a {@link Type} and represents the same type as
     * <em>this</em> {@code Type}.
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
