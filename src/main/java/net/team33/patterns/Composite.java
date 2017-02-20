package net.team33.patterns;

import net.team33.patterns.reflect.Fields;

import java.util.function.Function;

public abstract class Composite<C extends Composite<C>> {

    protected abstract <R> R apply(final Function<C, Function<Fields<C>, R>> function);

    @Override
    public final int hashCode() {
        return apply(self -> fields -> fields.hashCode(self));
    }

    @Override
    public final boolean equals(final Object obj) {
        return apply(self -> fields -> fields.equals(self, obj));
    }

    @Override
    public final String toString() {
        return apply(self -> fields -> fields.toString(self));
    }
}
