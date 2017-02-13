package net.team33.patterns;

import java.util.function.Consumer;

public final class Smart {

    private Smart() {
    }

    public static <T> T init(final T subject, final Consumer<T> consumer) {
        return edit(subject, consumer);
    }

    public static <T> T prepare(final T subject, final Consumer<T> consumer) {
        return edit(subject, consumer);
    }

    public static <T> T update(final T subject, final Consumer<T> consumer) {
        return edit(subject, consumer);
    }

    public static <T> T edit(final T subject, final Consumer<T> consumer) {
        consumer.accept(subject);
        return subject;
    }
}
