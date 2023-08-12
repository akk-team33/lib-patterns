package de.team33.patterns.properties.e1;

import java.util.Map;
import java.util.TreeMap;

@Deprecated
class ComboMapping<T> implements BiMapping<T> {

    private final Mapping<? super T> mapping;
    private final ReMapping<T> reMapping;

    ComboMapping(final Mapping<? super T> mapping, final ReMapping<T> reMapping) {
        this.mapping = mapping;
        this.reMapping = reMapping;
    }

    @Override
    public final TargetOperation<T> copy(final T origin) {
        return new Operation(origin);
    }

    @Override
    public final TargetOperation<Map<String, Object>> map(final T origin) {
        return mapping.map(origin);
    }

    @Override
    public final TargetOperation<T> remap(final Map<?, ?> origin) {
        return reMapping.remap(origin);
    }

    private final class Operation implements TargetOperation<T> {
        private final T origin;

        Operation(final T origin) {
            this.origin = origin;
        }

        @Override
        public final <TX extends T> TX to(final TX target) {
            return remap(map(origin).to(new TreeMap<>())).to(target);
        }
    }
}
