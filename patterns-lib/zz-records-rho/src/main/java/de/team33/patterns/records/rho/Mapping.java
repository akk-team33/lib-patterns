package de.team33.patterns.records.rho;

import de.team33.patterns.exceptional.dione.XFunction;

import static de.team33.patterns.records.rho.Util.typeName;

/**
 * Abstracts a customizable bidirectional mapping between a source type and a target type.
 *
 * @param <S> the source type.
 * @param <T> the target type.
 */
public final class Mapping<S, T> {

    private final XFunction<S, T, ?> mapping;
    private final Mapping<T, S> reverse;

    private Mapping(final XFunction<S, T, ?> mapping, final Mapping<T, S> reverse) {
        this.mapping = mapping;
        this.reverse = reverse;
    }

    @SuppressWarnings("BoundedWildcard")
    Mapping(final XFunction<S, T, ?> forward, final XFunction<T, S, ?> backward) {
        this.mapping = forward;
        this.reverse = new Mapping<>(backward, this);
    }

    /**
     * Returns a mapping that combines the properties of <em>this</em> instance with the given
     * forward mapping <em>method</em>.
     */
    public final Mapping<S, T> forward(final XFunction<S, T, ?> method) {
        // currently undocumented feature:
        // (method == null) => result no longer isFeatured()
        return new Mapping<>(method, reverse.mapping);
    }

    /**
     * Returns a mapping that combines the properties of <em>this</em> instance with the given
     * backward mapping <em>method</em>.
     */
    public final Mapping<S, T> backward(final XFunction<T, S, ?> method) {
        // currently undocumented feature:
        // (method == null) => result no longer isFeatured()
        return new Mapping<>(mapping, method);
    }

    final Mapping<T, S> reverse() {
        return reverse;
    }

    @SuppressWarnings("ProhibitedExceptionThrown")
    final T map(final S source) {
        if (null != mapping) {
            try {
                return mapping.apply(source);
            } catch (final RuntimeException e) {
                throw e;
            } catch (final Exception e) {
                throw new IllegalStateException(("cannot apply mapping ...%n" +
                                                 "    source: %s%n" +
                                                 "    type:   %s%n").formatted(source, typeName(source)), e);
            }
        }
        throw new IllegalStateException(("mapping not available for ...%n" +
                                         "    source: %s%n" +
                                         "    type:   %s%n").formatted(source, typeName(source)));
    }

    final boolean isFeatured() {
        return (null != mapping) && (null != reverse.mapping);
    }
}
