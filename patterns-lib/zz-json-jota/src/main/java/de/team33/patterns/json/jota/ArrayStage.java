package de.team33.patterns.json.jota;

import de.team33.patterns.building.elara.DataBuilder;

import java.util.*;
import java.util.function.UnaryOperator;

public final class ArrayStage extends AbstractList<Object> {

    private final List<Object> core;

    @SuppressWarnings("Java9CollectionFactory")
    private ArrayStage(final List<Object> core) {
        this.core = Collections.unmodifiableList(new ArrayList<>(core));
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public final Object get(final int index) {
        return core.get(index);
    }

    @Override
    public final int size() {
        return core.size();
    }

    public static final class Builder extends DataBuilder<List<Object>, ArrayStage, Builder> {

        private Builder() {
            super(new ArrayList<>(), ArrayStage::new, Builder.class);
        }

        public final Builder add(final Object element) {
            return setup(list -> list.add(element));
        }

        public final Builder add(final int index, final Object element) {
            return setup(list -> list.add(index, element));
        }

        public final Builder addAll(final Collection<?> other) {
            return setup(list -> list.addAll(other));
        }

        public final Builder addAll(final int index, final Collection<?> other) {
            return setup(list -> list.addAll(index, other));
        }

        public final Builder remove(final Object element) {
            return setup(list -> list.remove(element));
        }

        public final Builder remove(final int index) {
            return setup(list -> list.remove(index));
        }

        public final Builder removeAll(final Collection<?> candidates) {
            return setup(list -> list.removeAll(candidates));
        }

        public final Builder replaceAll(final UnaryOperator<Object> operator) {
            return setup(list -> list.replaceAll(operator));
        }

        public final Builder retainAll(final Collection<?> candidates) {
            return setup(list -> list.retainAll(candidates));
        }

        public final Builder set(final int index, final Object element) {
            return setup(list -> list.set(index, element));
        }

        public final Builder sort(final Comparator<? super Object> comparator) {
            return setup(list -> list.sort(comparator));
        }

        public final Builder clear() {
            return setup(List::clear);
        }
    }
}
