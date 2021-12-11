package de.team33.patterns.production.e1;

/**
 * Plain implementation of a {@link FactoryHub}.
 */
public final class PlainFactoryHub extends FactoryHub<PlainFactoryHub> {

    private PlainFactoryHub(final Collector<PlainFactoryHub, ?> collector) {
        super(collector);
    }

    /**
     * Returns a new {@link Builder}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * @return {@code this}
     */
    @Override
    protected final PlainFactoryHub getContext() {
        return this;
    }

    /**
     * A Builder for a new instance of {@link PlainFactoryHub}.
     *
     * @see #builder()
     */
    public static final class Builder extends FactoryHub.Collector<PlainFactoryHub, Builder> {

        private Builder() {
        }

        /**
         * @return {@code this}
         */
        @Override
        protected final Builder getBuilder() {
            return this;
        }

        /**
         * Returns a new instance of {@link PlainFactoryHub}.
         */
        public final PlainFactoryHub build() {
            return new PlainFactoryHub(this);
        }
    }
}
