package de.team33.patterns.building.elara.sample;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.UnaryOperator;

@SuppressWarnings("unused")
public final class MutableList {

    private MutableList() {
    }

    public static <E, L extends List<E>> Builder<E, L, ?> builder(final L subject) {
        return new Builder<>(subject, Builder.class);
    }

    public static <E> Builder<E, List<E>, ?> add(final E element) {
        return builder(MutableList.<E>newList()).add(element);
    }

    private static <E> List<E> newList() {
        return new LinkedList<>();
    }

    @SuppressWarnings("ClassTooDeepInInheritanceTree")
    public static class Builder<E, L extends List<E>, B extends Builder<E, L, B>>
            extends MutableCollection.BuilderBase<E, L, B> {

        protected Builder(final L subject, final Class<B> builderClass) {
            super(subject, builderClass);
        }

        public final B addAll(final int index, final Collection<? extends E> c) {
            return setup(subject -> subject.addAll(index, c));
        }

        public final B replaceAll(final UnaryOperator<E> operator) {
            return setup(subject -> subject.replaceAll(operator));
        }

        public final B sort(final Comparator<? super E> c) {
            return setup(subject -> subject.sort(c));
        }

        public final B set(final int index, final E element) {
            return setup(subject -> subject.set(index, element));
        }

        public final B add(final int index, final E element) {
            return setup(subject -> subject.add(index, element));
        }
    }
}
