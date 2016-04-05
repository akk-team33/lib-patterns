package net.team33.patterns;

import java.util.Collection;

public class Collector<E, C extends Collection<E>> {

    private final C subject;

    private Collector(final C subject) {
        this.subject = subject;
    }

    public static <E, C extends Collection<E>> Collector<E, C> on(final C subject) {
        return new Collector<>(subject);
    }

    public final C getSubject() {
        return subject;
    }
}
