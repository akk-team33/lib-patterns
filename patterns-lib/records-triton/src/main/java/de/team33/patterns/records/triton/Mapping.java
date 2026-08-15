package de.team33.patterns.records.triton;

import de.team33.patterns.exceptional.dione.XFunction;

/**
 * Abstracts a customizable bidirectional mapping between a source type and a target type.
 *
 * @param <S> the source type.
 * @param <T> the target type.
 */
public interface Mapping<S, T> {

    /**
     * Returns a mapping that combines the properties of <em>this</em> instance with the given
     * forward mapping <em>method</em>.
     */
    Mapping<S, T> forward(final XFunction<S, T, ?> method);

    /**
     * Returns a mapping that combines the properties of <em>this</em> instance with the given
     * backward mapping <em>method</em>.
     */
    Mapping<S, T> backward(final XFunction<T, S, ?> method);
}
