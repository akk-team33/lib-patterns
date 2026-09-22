package de.team33.patterns.arbitrary.mimas;

import java.util.List;

/**
 * A utility interface:
 * can extend producer classes with the functionality to fill <em>classic mutable data objects</em>* with (typically)
 * arbitrary values.
 * <p>
 * In more general terms, all target instances can be filled with values via reflection if they have suitable
 * methods for receiving these values: so-called <em>setters</em>**.
 * Within certain limits, this can also apply to target instances according to the builder pattern.
 * <p>
 * The producer class itself, which implements this interface, must provide suitable <em>getters</em>*** to supply
 * the values that can be passed to the setters of the target instance.
 * <p>
 * Static, synthetic and native methods, as well as those defined by the class {@link Object}, are generally ignored.
 * <p>
 * *<b><em>Classic mutable data objects</em></b> in this context means objects that are (essentially) made up of
 * properties and whose property values can be determined or updated using so-called <em>getters</em> and
 * <em>setters</em>**.
 * <p>
 * **<b><em>Setters</em></b> in this context are public instance methods that take exactly one argument as a parameter
 * and return nothing (void) or, according to the builder pattern, the target instance itself as the result.
 * <p>
 * ***<b><em>Getters</em></b> in this context are public instance methods that take no parameters and return a result
 * of a specific type (not void). The {@link Object#hashCode() hashCode()} and {@link Object#toString() toString()}
 * methods are ignored.
 *
 * @see de.team33.patterns.arbitrary.mimas package
 */
public interface Charger {

    /**
     * Initializes the properties of a given {@code target} instance with values provided by <em>this</em> Charger
     * instance itself and returns the initialized target.
     * <p>
     * In order for a property to be initialized, the {@code target}'s class must provide a corresponding setter
     * that is actually accessible from <em>this</em>. In addition, <em>this</em> Charger's implementation class
     * must provide a parameterless method that can return an appropriate value for the property in question.
     * <p>
     * The default implementation provides the intended functionality.
     * It makes little sense to override <em>this</em> method or then even use <em>this</em> interface at all.
     *
     * @param target The target of the operation.
     * @param ignore The names of setters that shell be ignored.
     * @param <T>    The target type.
     * @return The target.
     */
    default <T> T charge(final T target, final String... ignore) {
        return new Charging<>(this, target, List.of(ignore)).result();
    }
}
