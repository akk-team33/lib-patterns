package de.team33.patterns.typing.atlas;

import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Like the underlying {@link DType}, represents a <em>"definite type"</em>.
 * In addition, the represented <em>type</em> is already formally reflected by the type parameter used.
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
 * @param <T> The <em>type</em> to be represented.
 *
 * @see #Type()
 * @see #of(Class)
 */
@SuppressWarnings("unused")
public abstract class Type<T> extends DType {

    private static final String ILLEGAL_INSTANTIATION = //
            "Do not directly instantiate %1$s%n" +
            "  In fact, it just doesn't work.%n" +
            "  Instead, try one of the following:%n" +
            "  - Instantiate an anonymous derivative, something like ...%n" +
            "    new %1$s(){};%n" +
            "    (of course, using definite types instead of type parameters).%n" +
            "  - Create a non-generic derivative of %1$s and use that for instantiation.%n";

    private final DType backing;

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

    @SuppressWarnings({"TailRecursion", "OptionalGetWithoutIsPresent"})
    private static DType extract(final DType type) {
        final Class<?> thisClass = type.asClass();
        if (Type.class.equals(thisClass)) {
            return type.getActualParameters().get(0);
        } else {
            return extract(type.getSuperType().get());
        }
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

    private Type(final DType backing) {
        this.backing = backing;
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
        return new Type<T>(ClassCase.toTypedef(simpleClass)) {
        };
    }

    @Override
    public final Class<?> asClass() {
        return backing.asClass();
    }

    @Override
    public final List<String> getFormalParameters() {
        return backing.getFormalParameters();
    }

    @Override
    public final List<DType> getActualParameters() {
        return backing.getActualParameters();
    }

    @Override
    public final String toString() {
        return backing.toString();
    }
}
