package de.team33.patterns.building.elara;

import de.team33.patterns.building.anthe.SelfReferring;

/**
 * @deprecated Use {@link SelfReferring} instead!
 */
@SuppressWarnings("DeprecatedIsStillUsed")
@Deprecated(forRemoval = true)
public class BuilderBase<B extends BuilderBase<B>> extends SelfReferring<B> {

    /**
     * @deprecated Use {@link SelfReferring#SelfReferring(Class)} instead!
     */
    @Deprecated
    protected BuilderBase(final Class<B> thisClass) {
        super(thisClass);
    }
}
