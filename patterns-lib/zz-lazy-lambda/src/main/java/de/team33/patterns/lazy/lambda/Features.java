package de.team33.patterns.lazy.lambda;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class Features {

    @SuppressWarnings("rawtypes")
    private final Map<Key, Lazy> backing = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public final <T> T get(final Key<T> key, final Supplier<T> supplier) {
        return (T) backing.computeIfAbsent(key, any -> Lazy.init(supplier))
                          .get();
    }

    @SuppressWarnings("unchecked")
    public final <T> Optional<T> peek(final Key<T> key) {
        return (Optional<T>) Optional.ofNullable(backing.get(key))
                                     .map(Lazy::get);
    }

    public final void reset() {
        backing.clear();
    }

    @SuppressWarnings("MarkerInterface")
    public interface Key<T> {}
}
