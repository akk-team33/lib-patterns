/**
 * Provides tools for working with {@linkplain java.lang.Record records}.
 * <p>
 * In particular, the package supports conversions between:
 * <ul>
 *     <li>{@linkplain java.lang.Record Records} and JSON-formatted {@linkplain java.lang.String Strings}</li>
 *     <li>{@linkplain java.lang.Record Records} and {@linkplain java.util.Map Map} representations</li>
 * </ul>
 * <p>
 * For the sake of simplicity, JSON conversion supports only records whose native component types are:
 * <ul>
 *     <li>Primitive types</li>
 *     <li>Their wrapper types</li>
 *     <li>{@linkplain java.lang.String Strings}</li>
 *     <li>{@linkplain java.lang.Enum Enums}</li>
 *     <li>Records that comply with these restrictions</li>
 *     <li>Arrays whose elements comply with these restrictions</li>
 * </ul>
 *
 * @see de.team33.patterns.records.triton.Triton
 * @see <a href="https://de.wikipedia.org/wiki/Triton_(Mond)" target="_blank">Triton (Mond)</a>
 * @see <a href="https://de.wikipedia.org/wiki/Triton_(Mythologie)" target="_blank">Triton (Mythologie)</a>
 */
package de.team33.patterns.records.triton;
