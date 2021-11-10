/**
 * This package provides classes and mechanisms for assigning any <em>features</em> to appropriately prepared objects.
 * <p>
 * A <em>feature</em> in the sense of this definition is a property of basically any (but specific) type that can be
 * freely assigned to a suitable object.
 * <p>
 * An Object is suitable when it either <em>IS</em> (extends) a {@link de.team33.patterns.features.e1.FeatureSet}
 * or <em>HAS</em> a (field of) {@link de.team33.patterns.features.e1.FeatureSet} and offers a delegating method to
 * {@link de.team33.patterns.features.e1.FeatureSet#get(de.team33.patterns.features.e1.Key)} (of that field).
 * <p>
 * A {@link de.team33.patterns.features.e1.Key} is used to identify a particular <em>feature</em> and to describe how
 * the <em>feature</em> is to be instantiated.
 * <p>
 * A {@link de.team33.patterns.features.e1.FeatureSet} is used to manage the <em>features</em> that can be assigned to
 * an object. An object is suitable for assigning <em>features</em> to it if it has a
 * {@link de.team33.patterns.features.e1.FeatureSet}.
 */
package de.team33.patterns.features.e1;
