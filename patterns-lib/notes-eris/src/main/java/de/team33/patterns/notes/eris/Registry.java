package de.team33.patterns.notes.eris;

import java.util.function.Consumer;

/**
 * @deprecated in this form, it appears largely useless.
 */
@Deprecated
public interface Registry {

    /**
     * @deprecated see {@link Registry}.
     */
    @Deprecated
    <M> void add(Channel<M> channel, Consumer<? super M> listener);
}
