package de.team33.patterns.features.rhea;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface Feature {

    interface Key<H, R> {

        R init(H host);
    }

    interface Hub<H> {

        <R> R get(Key<? super H, ? extends R> key);
    }

    abstract class Holder<H> implements Hub<H> {

        @SuppressWarnings("rawtypes")
        private final Map<Key, Object> backing = new ConcurrentHashMap<>(0);

        protected abstract H host();

        @Override
        public <R> R get(final Key<? super H, ? extends R> key) {
            // This is the only place where a feature is created and associated with the key
            // and this feature is clearly of type <R>, so that a cast can take place without any problems ...
            // noinspection unchecked
            return (R) backing.computeIfAbsent(key, this::create);
        }

        private <R> R create(final Key<? super H, ? extends R> key) {
            return key.init(host());
        }
    }
}
