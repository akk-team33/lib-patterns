package de.team33.sample.patterns.building.elara;

import de.team33.patterns.building.elara.ProtoBuilder;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class Collecting {

    private Collecting() {
    }

    public static <E, C extends Collection<E>> ReBuilder<E, C, ?> reBuilder(final C subject) {
        return new ReBuilder<>(subject, ReBuilder.class);
    }

    public static class BuilderBase<E, C extends Collection<E>, B extends BuilderBase<E, C, B>>
            extends ProtoBuilder<C, B> {

        protected BuilderBase(final C container, final Class<B> builderClass) {
            super(container, builderClass);
        }

        public final B add(final E element) {
            return setup(container -> container.add(element));
        }

        @SafeVarargs
        public final B add(final E first, final E second, final E... more) {
            return addAll(Stream.concat(Stream.of(first, second), Stream.of(more)));
        }

        public final B addAll(final Stream<? extends E> elements) {
            return setup(container -> elements.forEach(container::add));
        }

        public final B addAll(final E[] elements) {
            return addAll(Stream.of(elements));
        }

        public final B addAll(final Collection<? extends E> elements) {
            return setup(container -> container.addAll(elements));
        }

        public final B addAll(final Iterable<? extends E> elements) {
            return Optional.of(elements)
                           .filter(Collection.class::isInstance)
                           .map(Collection.class::cast)
                           .map(this::addAll)
                           .orElseGet(() -> addAll(StreamSupport.stream(elements.spliterator(), false)));
        }

        public final B clear() {
            return setup(Collection::clear);
        }
    }

    public static final class ReBuilder<E, C extends Collection<E>, B extends ReBuilder<E, C, B>>
            extends BuilderBase<E, C, B> {

        private ReBuilder(final C container, final Class<B> builderClass) {
            super(container, builderClass);
        }

        public final C build() {
            return build(Function.identity());
        }
    }
}
