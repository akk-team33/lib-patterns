package de.team33.sample.patterns.building.elara;

import de.team33.patterns.building.elara.BuilderFrame;
import de.team33.patterns.building.elara.Mutable;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public final class MutableCollection {

    private MutableCollection() {
    }

    public static <E, C extends Collection<E>> Builder<E, C, ?> builder(final C subject) {
        return new Builder<>(subject, Builder.class);
    }

    public static class Builder<E, C extends Collection<E>, B extends Builder<E, C, B>> extends BuilderFrame<C, B> {

        protected Builder(final C subject, final Class<B> builderClass) {
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

        public final C build() {
            return build(Function.identity());
        }
    }
}
