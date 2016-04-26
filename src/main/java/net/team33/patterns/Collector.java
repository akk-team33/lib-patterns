package net.team33.patterns;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class Collector<E, C extends Collection<E>> {

    private final C subject;

    private Collector(final C subject) {
        this.subject = subject;
    }

    public static <E, C extends Collection<E>> Collector<E, C> collect(final C subject) {
        return new Collector<>(subject);
    }

    public final C getSubject() {
        return subject;
    }

    public final Collector<E, C> add(final E element) {
        subject.add(element);
        return this;
    }

    public final Collector<E, C> add(final E element1, final E element2) {
        return add(element1).add(element2);
    }

    @SuppressWarnings("OverloadedVarargsMethod")
    @SafeVarargs
    public final Collector<E, C> add(final E... elements) {
        return addAll(Arrays.asList(elements));
    }

    public final Collector<E, C> addAll(final Collection<? extends E> elements) {
        subject.addAll(elements);
        return this;
    }

    public final Collector<E, C> addAll(final Iterable<? extends E> elements) {
        return addAll(elements.iterator());
    }

    public final Collector<E, C> addAll(final Iterator<? extends E> elements) {
        while (elements.hasNext()) {
            add(elements.next());
        }
        return this;
    }

    public final Collector<E, C> remove(final E element) {
        while (subject.remove(element)) {
        }
        return this;
    }

    public final Collector<E, C> remove(final E element1, final E element2) {
        return remove(element1).remove(element2);
    }

    @SuppressWarnings("OverloadedVarargsMethod")
    @SafeVarargs
    public final Collector<E, C> remove(final E... elements) {
        return removeAll(Arrays.asList(elements));
    }

    public final Collector<E, C> removeAll(final Collection<? extends E> elements) {
        subject.removeAll(elements);
        return this;
    }

    public final Collector<E, C> removeAll(final Iterable<? extends E> elements) {
        return removeAll(elements.iterator());
    }

    public final Collector<E, C> removeAll(final Iterator<? extends E> elements) {
        while (elements.hasNext()) {
            remove(elements.next());
        }
        return this;
    }

    @SuppressWarnings("OverloadedVarargsMethod")
    @SafeVarargs
    public final Collector<E, C> retain(final E... elements) {
        return retainAll(Arrays.asList(elements));
    }

    public final Collector<E, C> retainAll(final Collection<? extends E> elements) {
        subject.retainAll(elements);
        return this;
    }

    public final Collector<E, C> retainAll(final Iterable<? extends E> elements) {
        return retainAll(elements.iterator());
    }

    public final Collector<E, C> retainAll(final Iterator<? extends E> elements) {
        return removeAll(collect(new HashSet<E>(subject)).removeAll(elements).subject);
    }

    public final Collector<E, C> clear() {
        subject.clear();
        return this;
    }
}
