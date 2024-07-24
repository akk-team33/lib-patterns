/**
 * This module, consisting of this package and the classes/interfaces defined in it, provides tools to generate
 * arbitrary data of virtually any type, mainly for testing purpose.
 * <p>
 * In a typical use case, a producer class is defined in the "test area" of a project, which is intended to
 * generate the data required in the tests, including complex ones, with arbitrary values.
 * The class can simply use or extend {@link java.util.Random}*, for example, and also implements the
 * {@link de.team33.patterns.arbitrary.mimas.Generator} interface, which makes other basic methods available.
 * Based on this, methods are then defined there in order to generate data of context-specific types. Example:
 * <pre>
 * public class Producer implements Generator, Charger, Initiator {
 *
 *     private final Random random = new Random();
 *
 *     &#64;Override
 *     public final BigInteger anyBits(final int numBits) {
 *         return new BigInteger(numBits, random);
 *     }
 *
 *     public final Person anyPerson() {
 *         return initiate(Person.class);
 *     }
 *
 *     public final Customer anyCustomer() {
 *         return charge(new Customer());
 *     }
 *
 *     public final Employee anyEmployee() {
 *         return charge(new Employee());
 *     }
 * }
 * </pre>
 * <p>
 * In this example, the {@link de.team33.patterns.arbitrary.mimas.Charger} and
 * {@link de.team33.patterns.arbitrary.mimas.Initiator} interfaces are used to charge/initiate the complex,
 * contextual types (Person, Customer, Employee, ...) with random content.
 *
 * @see <a href="https://de.wikipedia.org/wiki/Mimas_(Mond)">Mimas</a>
 */
package de.team33.patterns.arbitrary.mimas;
