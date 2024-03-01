package de.team33.patterns.generics.atlas;

import de.team33.patterns.lazy.narvi.Lazy;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.Collections;
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
@SuppressWarnings({"AbstractClassWithoutAbstractMethods", "unused", "ClassWithTooManyMethods", "GrazieInspection"})
public abstract class Type<T> extends Typedef {

    private static final Stream<? extends Type<?>> EMPTY = Stream.empty();
    private static final String NOT_DECLARED_IN_THIS = "member (%s) is not declared in the context of type (%s)";
    private static final String ILLEGAL_INSTANTIATION = //
            "Do not directly instantiate %1$s%n" +
            "  In fact, it just doesn't work.%n" +
            "  Instead, try one of the following:%n" +
            "  - Instantiate an anonymous derivative, something like ...%n" +
            "    new %1$s(){};%n" +
            "    (of course, using definite types instead of type parameters).%n" +
            "  - Create a non-generic derivative of %1$s and use that for instantiation.%n";

    private final Typedef backing;
    private final transient Lazy<List<Type<?>>> actualParameters = Lazy.init(this::newActualParameters);

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
        final Class<?> thisClass = getClass();
        ensureNonGeneric(thisClass);
        this.backing = extract(ClassCase.toTypedef(thisClass));
    }

    private Type(final Typedef backing) {
        this.backing = backing;
    }

    @SuppressWarnings("TailRecursion")
    private static Typedef extract(final Typedef thisType) {
        final Class<?> thisClass = thisType.asClass();
        if (Type.class.equals(thisClass)) {
            return thisType.getActualParameters().get(0);
        }

        final Typedef superType = thisType.getMemberType(thisClass.getGenericSuperclass());
        return extract(superType);
    }

    private static void ensureNonGeneric(final Class<?> thisClass) {
        final TypeVariable<? extends Class<?>>[] parameters = thisClass.getTypeParameters();
        if (parameters.length > 0) {
            final String signature = //
                    Stream.of(parameters)
                          .map(TypeVariable::getName)
                          .collect(Collectors.joining(", ",
                                                      thisClass.getSimpleName() + "<",
                                                      ">"));
            throw new IllegalStateException(String.format(ILLEGAL_INSTANTIATION, signature));
        }
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
        return new Type<T>(ClassCase.toTypedef(simpleClass)) {};
    }

    @SuppressWarnings("rawtypes")
    private static Type<?> of(final Typedef typedef) {
        return (typedef instanceof Type) ? (Type<?>) typedef : new Type(typedef) {};
    }

    private List<Type<?>> newActualParameters() {
        return Collections.unmodifiableList(
                backing.getActualParameters()
                       .stream()
                       .map(Type::of)
                       .collect(Collectors.toList())
        );
    }

    /**
     * Returns the {@link Class} on which this Type is based.
     */
    @Override
    public final Class<?> asClass() {
        return backing.asClass();
    }

    /**
     * Returns the formal type parameter of the generic type underlying this Type.
     *
     * @see #getActualParameters()
     */
    @Override
    public final List<String> getFormalParameters() {
        return backing.getFormalParameters();
    }

    /**
     * <p>Returns the actual type parameters defining this Type.
     * <p>The result may be empty even if the formal parameter list is not. Otherwise the formal
     * and actual parameter list are of the same size and order.
     *
     * @see #getFormalParameters()
     */
    @Override
    public final List<Type<?>> getActualParameters() {
        return actualParameters.get();
    }

    /**
     * Converts a (possibly generic) {@link java.lang.reflect.Type} that somehow resides in the context of the
     * {@linkplain #asClass() underlying class} of this Type into a definite {@link Type}.
     *
     * @see Class#getGenericSuperclass()
     * @see Class#getGenericInterfaces()
     * @see Class#getFields()
     * @see Class#getMethods()
     * @see Field#getGenericType()
     * @see Method#getGenericReturnType()
     * @see Method#getGenericParameterTypes()
     */
    @Override
    public final Type<?> getMemberType(final java.lang.reflect.Type type) {
        return of(backing.getMemberType(type));
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

    private Stream<Type<?>> streamSuperType() {
        return getSuperType().map(Stream::<Type<?>>of)
                             .orElseGet(Stream::empty);
    }

    /**
     * Returns the interfaces from which this type are derived (if so).
     *
     * @see Class#getInterfaces()
     * @see Class#getGenericInterfaces()
     */
    public final List<Type<?>> getInterfaces() {
        return streamInterfaces().collect(Collectors.toList());
    }

    private Stream<Type<?>> streamInterfaces() {
        return Stream.of(asClass().getGenericInterfaces())
                     .map(this::getMemberType);
    }

    /**
     * Returns all the types (class, interfaces) from which this type is derived (if so).
     *
     * @see #getSuperType()
     * @see #getInterfaces()
     */
    public final List<Type<?>> getSuperTypes() {
        return streamSuperTypes().collect(Collectors.toList());
    }

    private Stream<Type<?>> streamSuperTypes() {
        return Stream.concat(streamSuperType(), streamInterfaces());
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
            return streamSuperTypes().map(st -> st.nullableTypeOf(member, toGenericType))
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
            return streamSuperTypes().map(st -> st.nullableTypesOf(member, toGenericTypes))
                                     .filter(Objects::nonNull)
                                     .findAny()
                                     .orElse(null);
        }
    }

    @Override
    public final String toString() {
        return backing.toString();
    }
}
