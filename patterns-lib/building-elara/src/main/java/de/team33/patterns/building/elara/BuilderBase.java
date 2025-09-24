package de.team33.patterns.building.elara;

/**
 * Base class of a generic class hierarchy that has a type parameter intended to represent the effective type of
 * concrete implementation.
 * <p>
 * The main purpose is the realization of a generic builder pattern, whereby there should be methods that should
 * result in <em>this</em> - i.e. the builder instance itself - in order to chain further method calls.
 * <p>
 * For this purpose, it should be ensured in the constructor that <em>this</em> actually corresponds to the
 * designated type.
 * <p>
 * TODO: should be more independent, suggestion: "class SelfReferring"
 *
 * @param <B> The builder type: the intended effective type of the concrete builder implementation.
 */
@SuppressWarnings("WeakerAccess")
public class BuilderBase<B extends BuilderBase<B>> {

    private static final String ILLEGAL_THIS_CLASS =
            "<thisClass> is expected to represent the class of <this> (%s) - but was %s";

    /**
     * Initializes a new instance and checks the intended builder type for consistency.
     *
     * @param thisClass The {@link Class} representation of the intended effective builder type.
     * @throws IllegalArgumentException if the given builder class does not represent <em>this</em> instance.
     */
    protected BuilderBase(final Class<B> thisClass) {
        if (!thisClass.isAssignableFrom(getClass())) {
            throw new IllegalArgumentException(String.format(ILLEGAL_THIS_CLASS, getClass(), thisClass));
        }
    }

    /**
     * Returns <em>this</em> as an instance of the effective builder type {@code <B>}.
     */
    @SuppressWarnings("unchecked")
    protected final B THIS() {
        return (B) this;
    }
}
