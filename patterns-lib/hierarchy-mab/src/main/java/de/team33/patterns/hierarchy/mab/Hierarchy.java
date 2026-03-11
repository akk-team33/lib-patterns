package de.team33.patterns.hierarchy.mab;

import java.util.List;

public final class Hierarchy {

    private Hierarchy() {
    }

    /**
     * Represents an item within a hierarchical structure.
     *
     * @param <E> The final type of item.
     */
    public interface Item<E extends Item<E>> {

        /**
         * Returns the immediate content of <em>this</em> {@link Item}.
         */
        List<E> list();
    }
}
