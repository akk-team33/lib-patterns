package de.team33.sample.patterns.building.elara;

import de.team33.patterns.building.elara.Mutable;

import java.util.Collection;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public final class MutableCollectionSample {

    private MutableCollectionSample() {
    }

    public static <E, L extends Collection<E>> Builder<E, L, ?> builder(final L subject) {
        return new Builder<>(subject, Builder.class);
    }

    public static class Builder<E, L extends Collection<E>, B extends Builder<E, L, B>> extends Mutable.Builder<L, B> {

        protected Builder(final L subject, final Class<B> builderClass) {
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