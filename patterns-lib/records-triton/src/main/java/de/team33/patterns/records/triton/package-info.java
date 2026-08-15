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
 *     <li>{@linkplain java.math.BigInteger BigInteger} and {@linkplain java.math.BigDecimal BigDecimal}</li>
 *     <li>{@linkplain java.lang.String Strings}</li>
 *     <li>{@linkplain java.lang.Enum Enums}</li>
 *     <li>Records that comply with these restrictions</li>
 *     <li>Arrays whose elements comply with these restrictions</li>
 *     <li><em>Stringable</em>* types</li>
 * </ul>
 * <p>
 * A <em>Stringable</em>* type in this context is a type that declares either ...
 * <ul>
 *     <li>a public constructor that takes a single {@link java.lang.String} or {@link java.lang.CharSequence}
 *     as parameter, or</li>
 *     <li>a public static factory method that takes a single {@link java.lang.String} or
 *     {@link java.lang.CharSequence} as parameter and returns an instance of the type itself.</li>
 * </ul>
 * <p>
 * Furthermore, the type must have an implementation of {@link java.lang.Object#toString() toString()}
 * whose result is applicable in the method described above, which then leads to an equivalent result.
 * <p>
 * <b>NOTE</b> that you need to check whether a particular type is actually suitable for JSON conversion in this context,
 * in both directions, before applying it.
 * <p>
 * Suitable types in this sense would be, for example, {@link java.time.Instant Instant}, {@link java.util.UUID UUID}
 * or {@link java.math.BigInteger BigInteger}, although the latter is treated differently in this context.
 * However, types such as {@link java.util.Date Date}, {@link java.text.SimpleDateFormat SimpleDateFormat}
 * or {@link java.lang.Class Class} would be unsuitable, since the result of their
 * {@link java.lang.Object#toString() toString()} implementation cannot actually be applied to their respective
 * initialization method.
 * <p>
 * But there's a solution for these cases too: you can set up support for virtually any type using
 * {@link de.team33.patterns.records.triton.Triton#setup(java.lang.Class, java.util.function.UnaryOperator)}.
 *
 * @see de.team33.patterns.records.triton.Triton
 * @see <a href="https://de.wikipedia.org/wiki/Triton_(Mond)" target="_blank">Triton (Mond)</a>
 * @see <a href="https://de.wikipedia.org/wiki/Triton_(Mythologie)" target="_blank">Triton (Mythologie)</a>
 */
package de.team33.patterns.records.triton;
