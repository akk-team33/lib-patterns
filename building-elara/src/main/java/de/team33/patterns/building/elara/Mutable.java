package de.team33.patterns.building.elara;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Utility class that supports a builder pattern for mutable types.
 */
public final class Mutable {

    private static final String ILLEGAL_BUILDER_CLASS =
            "<builderClass> is expected to represent <this> (%s) - but was %s";
    @SuppressWarnings("rawtypes")

    private Mutable() {
    }

    /**
     * Returns a new {@link Builder} that is tightly bound to a predetermined <em>subject</em>.
     * This means that the {@link Builder#build() build()} method always returns the same predetermined instance
     * when used multiple times and that {@link Builder#setup(Consumer) modifications} via the builder
     * always affect the bound subject and vice versa!
     *
     * @param <S> The subject type: the type of instance to be initialized.
     * @see #builder(Supplier)
     */
    public static <S> Builder<S, ?> builder(final S subject) {
        return builder(() -> subject);
    }

    /**
     * Returns a new {@link Builder} that uses a given {@link Supplier} on each {@link Builder#build() build()}
     * to create a (typically) new <em>subject</em> instance.
     *
     * @param <S> The subject type: the type of instance to be initialized.
     * @see #builder(Object)
     */
    public static <S> Builder<S, ?> builder(final Supplier<S> newSubject) {
        return new Builder<>(newSubject, Builder.class);
    }

    /**
     * Serves for the declarative initialization of instances (<em>subjects</em>) of any but specific type,
     * which are mutable and can therefore also be initialized step by step, but whose setter methods do not adhere
     * to the <em>builder pattern</em> and are therefore not suitable for declarative initialization.
     * <p>
     * An instance can be created and used directly using {@link Mutable#builder(Object)} or
     * {@link Mutable#builder(Supplier)}, but this class is intended primarily as a base class for
     * builder implementations specialized for initializing <em>subjects</em> of more specific types.
     *
     * @param <S> The subject type: the type of instance to be initialized.
     * @param <B> The builder type: the effective type of the derived builder implementation, at least this type itself.
     */
    public static class Builder<S, B extends Builder<S, B>> {

        private final Supplier<S> newSubject;
        private final Class<B> builderClass;
        private final List<Consumer<S>> setups;

        protected Builder(final Supplier<S> newSubject, final Class<B> builderClass) {
            if (builderClass.isAssignableFrom(getClass())) {
                this.newSubject = newSubject;
                this.builderClass = builderClass;
                this.setups = new LinkedList<>();
            } else {
                throw new IllegalArgumentException(String.format(ILLEGAL_BUILDER_CLASS, getClass(), builderClass));
            }
        }

        private S build(final S subject) {
            for (final Consumer<S> setup : setups) {
                setup.accept(subject);
            }
            return subject;
        }

        /**
         * Returns a (typically)* new instance of the mentioned subject type.
         */
        public final S build() {
            return build(newSubject.get());
        }

        public final B setup(final Consumer<S> consumer) {
            setups.add(consumer);
            return builderClass.cast(this);
        }
    }
}
