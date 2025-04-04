package de.team33.patterns.building.elara.sample;

import de.team33.patterns.building.elara.LateBuilder;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class Collecting {

    private Collecting() {
    }

    @SuppressWarnings({"unused", "ClassNameSameAsAncestorName"})
    @FunctionalInterface
    public interface Setup<E, C extends Collection<E>, B>
            extends de.team33.patterns.building.elara.Setup<C, B> {

        default B add(final E element) {
            return setup(container -> container.add(element));
        }

        @SuppressWarnings("unchecked")
        default B add(final E first, final E second, final E... more) {
            return addAll(Stream.concat(Stream.of(first, second), Stream.of(more)));
        }

        default B addAll(final Stream<? extends E> elements) {
            return setup(container -> elements.forEach(container::add));
        }

        default B addAll(final E[] elements) {
            return addAll(Stream.of(elements));
        }

        default B addAll(final Collection<? extends E> elements) {
            return setup(container -> container.addAll(elements));
        }

        @SuppressWarnings("unchecked")
        default B addAll(final Iterable<? extends E> elements) {
            return Optional.of(elements)
                           .filter(iterable -> iterable instanceof Collection)
                           .map(iterable -> (Collection<E>) iterable)
                           .map(this::addAll)
                           .orElseGet(() -> addAll(StreamSupport.stream(elements.spliterator(), false)));
        }

        default B clear() {
            return setup(Collection::clear);
        }
    }

    public static <E, S extends Collection<E>> Charger<E, S, ?> charger(final S subject) {
        return new Charger<>(subject, Charger.class);
    }

    @SuppressWarnings({"ClassNameSameAsAncestorName", "WeakerAccess"})
    public static class Charger<E, S extends Collection<E>, B extends Charger<E, S, B>>
            extends de.team33.patterns.building.elara.Charger<S, B> implements Setup<E, S, B> {

        protected Charger(final S subject, final Class<B> builderClass) {
            super(subject, builderClass);
        }
    }

    public static <E, S extends Collection<E>> Builder<E, S, ?> builder(final Supplier<? extends S> newResult) {
        return new Builder<>(newResult, Builder.class);
    }

    @SuppressWarnings("WeakerAccess")
    public static class Builder<E, R extends Collection<E>, B extends Builder<E, R, B>>
            extends LateBuilder<R, B> implements Setup<E, R, B> {

        protected Builder(final Supplier<? extends R> newResult, final Class<B> builderClass) {
            super(newResult, builderClass);
        }
    }
}
