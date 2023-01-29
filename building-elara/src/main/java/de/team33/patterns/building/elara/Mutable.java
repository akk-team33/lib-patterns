package de.team33.patterns.building.elara;

import java.util.function.Consumer;

/**
 * Utility class that supports a builder pattern for mutable types.
 */
public final class Mutable {

    private static final String ILLEGAL_BUILDER_CLASS =
            "<builderClass> is expected to represent <this> (%s) - but was %s";

    private Mutable() {
    }

    /**
     * Returns a new {@link Builder} that is tightly bound to a predetermined <em>subject</em>.
     * This means that the {@link Builder#subject() subject()} method always returns the same predetermined instance
     * when used multiple times and that {@link Builder#setup(Consumer) modifications} via the builder
     * always affect the bound subject and vice versa!
     *
     * @param <S> The subject type: the type of instance to be initialized.
     */
    public static <S> Builder<S, ?> builder(final S subject) {
        return new Builder<>(subject, Builder.class);
    }

    /**
     * Serves for the declarative initialization of instances (<em>subjects</em>) of any but specific type,
     * which are mutable and can therefore also be initialized step by step, but whose setter methods do not adhere
     * to the <em>builder pattern</em> and are therefore not suitable for declarative initialization.
     * <p>
     * An instance can be created and used directly using {@link Mutable#builder(Object)}, but this class is intended
     * primarily as a base class for builder implementations specialized for initializing <em>subjects</em> of more
     * specific types.
     *
     * @param <S> The subject type: the type of instance to be initialized.
     * @param <B> The builder type: the effective type of the derived builder implementation, at least this type itself.
     */
    public static class Builder<S, B extends Builder<S, B>> {

        private final S subject;
        private final Class<B> builderClass;

        protected Builder(final S subject, final Class<B> builderClass) {
            if (builderClass.isAssignableFrom(getClass())) {
                this.subject = subject;
                this.builderClass = builderClass;
            } else {
                throw new IllegalArgumentException(String.format(ILLEGAL_BUILDER_CLASS, getClass(), builderClass));
            }
        }

        /**
         * Returns the associated <em>subject</em>.
         */
        public final S subject() {
            return subject;
        }

        /**
         * Applies the associated <em>subject</em> to a given {@link Consumer} and returns {@code this}.
         */
        public final B setup(final Consumer<S> consumer) {
            consumer.accept(subject);
            return builderClass.cast(this);
        }
    }
}
