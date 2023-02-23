package de.team33.sample.patterns.building.elara;

import de.team33.patterns.building.elara.BuilderFrame;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public final class MutableCollection {

    private MutableCollection() {
    }

    public static <E, R extends Collection<E>> Builder<E, R, ?> builder(final Collection<E> subject,
                                                                        final Function<Collection<E>, R> mapping) {
        return new Builder<>(subject, mapping, Builder.class);
    }

    public static class Builder<E, R extends Collection<E>, B extends Builder<E, R, B>>
            extends BuilderBase<E, Collection<E>, B> {

        private final Function<Collection<E>, R> mapping;

        protected Builder(final Collection<E> container,
                          final Function<Collection<E>, R> mapping,
                          final Class<B> builderClass) {
            super(container, builderClass);
            this.mapping = mapping;
        }

        public final R build() {
            return build(mapping);
        }
    }

    public static class BuilderBase<E, C extends Collection<E>, B extends BuilderBase<E, C, B>>
            extends BuilderFrame<C, B> {

        protected BuilderBase(final C subject, final Class<B> builderClass) {
            super(subject, builderClass);
        }

        public final B add(final E element) {
            return setup(subject -> subject.add(element));
        }

        public final B remove(final Object element) {
            return setup(subject -> subject.remove(element));
        }

        public final B addAll(final Collection<? extends E> elements) {
            return setup(subject -> subject.addAll(elements));
        }

        public final B removeAll(final Collection<?> elements) {
            return setup(subject -> subject.removeAll(elements));
        }

        public final B removeIf(final Predicate<? super E> filter) {
            return setup(subject -> subject.removeIf(filter));
        }

        public final B retainAll(final Collection<?> elements) {
            return setup(subject -> subject.retainAll(elements));
        }

        public final B clear() {
            return setup(Collection::clear);
        }
    }
}
