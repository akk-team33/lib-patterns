/**
 * This package provides classes and mechanisms for assigning any <em>features</em> to appropriately prepared objects.
 * <p>
 * A <em>feature</em> in the sense of this definition is a property of basically any (but specific) type that can be
 * freely assigned to a suitable object.
 * <p>
 * An Object is suitable when it either <em>IS</em> (implements) a {@link de.team33.patterns.features.rhea.FeatureHub}
 * or <em>HAS</em> a (field of) {@link de.team33.patterns.features.rhea.FeatureHub} and offers a delegating method to
 * {@link de.team33.patterns.features.rhea.FeatureHub#get(java.util.function.Function)} (of that field).
 * <p>
 * A {@link java.util.function.Function} is used as a key to identify a particular <em>feature</em> and also to
 * describe how the <em>feature</em> is to be created.
 *
 * @see <a href="https://de.wikipedia.org/wiki/Rhea_(Mond)">Rhea</a>
 */
package de.team33.patterns.features.rhea;
