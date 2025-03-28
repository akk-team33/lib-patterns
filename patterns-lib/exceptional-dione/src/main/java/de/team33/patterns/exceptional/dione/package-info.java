/**
 * This package contains variants of basic functional constructs that can throw checked exceptions,
 * for example {@link de.team33.patterns.exceptional.dione.XFunction} as a variant of {@link java.util.function.Function}.
 * <p>
 * In addition, it contains tools and utilities that enable the functional constructs defined here to be converted
 * into their counterparts. For example, an {@link de.team33.patterns.exceptional.dione.XFunction} can be converted
 * into a {@link java.util.function.Function}, with any <em>checked</em> exception being wrapped in a specific
 * <em>unchecked</em> exception.
 * <p>
 * The reverse conversion is trivial and does not require any utilities.
 * <p>
 * Finally, it contains tools and utilities for dealing with exceptions as well as exception types for occasional
 * special cases.
 *
 * @see <a href="https://de.wikipedia.org/wiki/Dione_(Mond)">Dione (Mond)</a>
 */
package de.team33.patterns.exceptional.dione;
