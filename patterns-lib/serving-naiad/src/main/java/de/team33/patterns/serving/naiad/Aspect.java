package de.team33.patterns.serving.naiad;

/**
 * Abstracts an <em>"aspect"</em>: a kind of property that can be useful as part of an interactive application's
 * backend service. While a property in the common sense merely represents a value of a certain type
 * and in particular can be readable and/or modifiable, an <em>"aspect"</em> is additionally able to provide an
 * interface to notify an audience about changes of its value.
 *
 * @param <T> The type of the provided value.
 */
public interface Aspect<T> {

}
