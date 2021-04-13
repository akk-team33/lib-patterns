package de.team33.patterns.exceptional.v1;

import java.util.function.Function;
import java.util.function.Predicate;

public class Cases<T extends Throwable> {

    private final T subject;

    private Cases(final T subject) {
        this.subject = subject;
    }

    public static <T extends Throwable> Cases<T> of(final T subject) {
        return new Cases<T>(subject);
    }

    public final T quit() {
        return subject;
    }

    public final <X> X quit(final Function<? super T, X> mapping) {
        return mapping.apply(subject);
    }

    public final <X extends Throwable> Cases<T> reThrow(final Class<X> xClass) throws X {
        return throwIf(xClass::isInstance, xClass::cast);
    }

    public final <X extends Throwable> Cases<T> throwIf(final Predicate<? super T> condition,
                                                        final Function<? super T, X> mapping) throws X {
        if (condition.test(subject)) {
            throw mapping.apply(subject);
        } else {
            return this;
        }
    }
}
