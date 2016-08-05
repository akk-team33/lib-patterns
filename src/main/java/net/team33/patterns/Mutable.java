package net.team33.patterns;

import java.util.function.Consumer;

public class Mutable<T> {

    private T subject;

    public static <T> Mutable<T> of(final T initial) {
        return new Mutable<T>().set(initial);
    }

    public final T get() {
        return subject;
    }

    public final Mutable<T> set(final T subject) {
        this.subject = subject;
        return this;
    }

    public final Mutable<T> apply(final Consumer<Mutable<T>> consumer) {
        consumer.accept(this);
        return this;
    }
}
