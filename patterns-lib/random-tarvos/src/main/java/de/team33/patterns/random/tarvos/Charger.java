package de.team33.patterns.random.tarvos;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * A utility interface:
 * can extend producer classes with the functionality to fill classic mutable Java data objects* with (typically)
 * random values.
 * <p>
 * In more general terms, all target instances can be filled with values via reflection if they have suitable
 * methods for receiving these values: so-called setters**.
 * Within certain limits, this can also apply, for example, to target instances according to the builder pattern.
 * <p>
 * The producer class that implements this interface must provide appropriate getters*** to supply the values
 * that can be passed to the target instance's setters.
 * <p>
 * Static, synthetic and native methods are generally ignored.
 * <p>
 * *<em>Classic Java data objects</em> in this context means objects that are (essentially) made up of properties
 * and whose property values can be determined or set using so-called getters and setters**.
 * <p>
 * **<em>Setters</em> in this context are public instance methods that take exactly one argument as a parameter
 * and return nothing (void) or, according to the builder pattern, the target instance itself as the result.
 * <p>
 * ***<em>Getters</em> in this context are public instance methods that take no parameters and return a result
 * of a specific type (not void). The {@link Object#hashCode() hashCode()} and {@link Object#toString() toString()}
 * methods are ignored.
 *
 * @see de.team33.patterns.random.tarvos package
 */
public interface Charger {

    /**
     * Initializes the properties of a given {@code target} instance with values provided by this Charger instance
     * itself and returns the initialized target.
     * <p>
     * In order for a property to be initialized, the {@code target}'s class must provide a corresponding setter
     * that is actually accessible from this {@link Charger}. In addition, this {@link Charger}'s implementation class
     * must provide a parameterless method that can return an appropriate value for the property in question.
     * <p>
     * The default implementation provides the intended functionality.
     * It makes little sense to override this method or then even use this interface at all.
     *
     * @param target The target of the operation.
     * @param ignore The names of methods that shell be ignored.
     * @param <T> The target type.
     * @return The target.
     */
    default <T> T charge(final T target, final String... ignore) {
        return new Charging<>(this, target, Arrays.asList(ignore)).result();
    }
}
