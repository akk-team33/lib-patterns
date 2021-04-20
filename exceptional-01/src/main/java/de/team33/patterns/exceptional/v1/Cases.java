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

    public final <R> R quit(final Function<? super T, R> mapping) {
        return mapping.apply(subject);
    }

    public final <X extends Throwable> Cases<T> reThrowIf(final Class<X> xClass) throws X {
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

    public final <TX extends Throwable> Conditional<T, TX> on(final Class<TX> txClass) {
        return txClass.isInstance(subject) ? new Positive<>(txClass.cast(subject)) : new Negative<>(this);
    }

    public final Conditional<T, T> on(final Predicate<? super T> condition) {
        return condition.test(subject) ? new Positive<>(subject) : new Negative<>(this);
    }

    public interface Conditional<T0 extends Throwable, TX extends Throwable> {

        Cases<T0> reThrow() throws TX;

        <X extends Throwable> Cases<T0> reThrow(Function<? super TX, X> mapping) throws X;
    }

    private static class Positive<T0 extends Throwable, TX extends Throwable> implements Conditional<T0, TX> {

        private final TX subject;

        private Positive(final TX subject) {
            this.subject = subject;
        }

        @Override
        public final Cases<T0> reThrow() throws TX {
            throw subject;
        }

        @Override
        public final <X extends Throwable> Cases<T0> reThrow(final Function<? super TX, X> mapping) throws X {
            throw mapping.apply(subject);
        }
    }

    private static class Negative<T0 extends Throwable, TX extends Throwable> implements Conditional<T0, TX> {

        private final Cases<T0> cases;

        private Negative(final Cases<T0> cases) {
            this.cases = cases;
        }

        @Override
        public final Cases<T0> reThrow() {
            return cases;
        }

        @Override
        public final <X extends Throwable> Cases<T0> reThrow(final Function<? super TX, X> mapping) {
            return cases;
        }
    }
}
