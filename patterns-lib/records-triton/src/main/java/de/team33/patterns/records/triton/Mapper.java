package de.team33.patterns.records.triton;

import de.team33.patterns.exceptional.dione.XFunction;

@SuppressWarnings("BoundedWildcard")
final class Mapper<S, T> implements Mapping<S, T> {

    private final XFunction<S, T, ?> mapping;
    private final Mapper<T, S> reverse;

    private Mapper(final XFunction<S, T, ?> mapping, final Mapper<T, S> reverse) {
        this.mapping = mapping;
        this.reverse = reverse;
    }

    Mapper(final XFunction<S, T, ?> forward, final XFunction<T, S, ?> backward) {
        this.mapping = forward;
        this.reverse = new Mapper<>(backward, this);
    }

    @SuppressWarnings("ReturnOfNull")
    private static String type(final Object source) {
        return (null == source) ? null : source.getClass().getCanonicalName();
    }

    @Override
    public final Mapper<S, T> forward(final XFunction<S, T, ?> method) {
        return new Mapper<>(method, reverse.mapping);
    }

    @Override
    public final Mapper<S, T> backward(final XFunction<T, S, ?> method) {
        return new Mapper<>(mapping, method);
    }

    final Mapper<T, S> reverse() {
        return reverse;
    }

    final T map(final S source) {
        checkMapping(source);
        try {
            return mapping.apply(source);
        } catch (final RuntimeException e) {
            //noinspection ProhibitedExceptionThrown
            throw e;
        } catch (final Exception e) {
            throw new IllegalArgumentException(("cannot apply mapping ...%n" +
                                                "    source: %s%n" +
                                                "    type:   %s%n").formatted(source, type(source)), e);
        }
    }

    final boolean isFullFeatured() {
        return (null != mapping) && (null != reverse.mapping);
    }

    private void checkMapping(final S source) {
        if (null == mapping) {
            throw new IllegalStateException(("mapping not defined (null) ...%n" +
                                             "    source: %s%n" +
                                             "    type:   %s%n").formatted(source, type(source)));
        }
    }
}
