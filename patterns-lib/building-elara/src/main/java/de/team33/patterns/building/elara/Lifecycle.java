package de.team33.patterns.building.elara;

/**
 *
 */
public interface Lifecycle {

    Lifecycle INFINITE = new Lifecycle() {
        @Override
        public void check() {
        }

        @Override
        public void increment() {
        }
    };

    /**
     * Will be called on any setup step.
     *
     * @throws IllegalStateException if the lifecycle does not allow further setup on the associated <em>core</em>
     *                               instance.
     */
    void check();

    /**
     * Will be called on any final build step.
     */
    void increment();
}
