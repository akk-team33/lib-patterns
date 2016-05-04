package net.team33.patterns;

import java.util.Collections;
import java.util.Map;

public class Mapper<K, V, M extends Map<K, V>> {

    private final M subject;

    @SuppressWarnings("AssignmentToCollectionOrArrayFieldFromParameter")
    Mapper(final M subject) {
        this.subject = subject;
    }

    public static <K, V, M extends Map<K, V>> Mapper<K, V, M> map(final M subject) {
        return new Mapper<>(subject);
    }

    public final Mapper<K, V, M> put(final K key, final V value) {
        subject.put(key, value);
        return this;
    }

    public final Mapper<K, V, M> putAll(final Map<? extends K, ? extends V> origin) {
        subject.putAll(origin);
        return this;
    }

    public final Mapper<K, V, M> remove(final Object key) {
        subject.remove(key);
        return this;
    }

    public final Mapper<K, V, M> clear() {
        subject.clear();
        return this;
    }

    public final M getSubject() {
        return subject;
    }

    @SuppressWarnings("DesignForExtension")
    public Map<K, V> unmodifiable() {
        return Collections.unmodifiableMap(subject);
    }

}
