/**
 * This module, consisting of this package and the classes defined in it, provides tools to generate random data
 * of virtually any type, mainly for testing purpose.
 * <p>
 * In a typical use case, a producer class is defined in the "test area" of a project, which is intended to
 * generate the data required in the tests, including complex ones, with random values.
 * The class can simply be derived from {@link java.util.Random}, for example, and also implements the
 * {@link de.team33.patterns.random.tarvos.Generator} interface, which makes other basic methods available.
 * Based on this, methods are then defined there in order to generate data of context-specific types. Example:
 * <pre>
 * public class Producer extends Random implements Generator {
 *
 *     private final Charger&lt;Producer&gt; charger = new Charger&lt;&gt;(Producer.class);
 *
 *     &#64;Override
 *     public final BigInteger nextBits(final int numBits) {
 *         return new BigInteger(numBits, this);
 *     }
 *
 *     public final Person nextPerson() {
 *         return charger.charge(new Person(), this);
 *     }
 *
 *     public final Customer nextCustomer() {
 *         return charger.charge(new Customer(), this);
 *     }
 *
 *     public final Employee nextEmployee() {
 *         return charger.charge(new Employee(), this);
 *     }
 * }
 * </pre>
 * <p>
 * In this example, a {@link de.team33.patterns.random.tarvos.Charger} is used to fill the complex, contextual types
 * (Person, Customer, Empoyee, ...) with random content.
 */
package de.team33.patterns.random.tarvos;