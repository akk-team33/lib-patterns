package de.team33.patterns.testing.v1;

import de.team33.patterns.exceptional.v1.XBiConsumer;

import java.util.LinkedList;
import java.util.List;

public class Repetition {

    public static Builder add(final int parallel, final int serial, final XBiConsumer<Integer, Integer, ?> code) {
        return new Builder().add(parallel, serial, code);
    }

    private static Runnable runnable(final int i, final int serial, final XBiConsumer<Integer, Integer, ?> code) {
        return () -> {
            for (int k = 0; k < serial; ++k) {
                try {
                    code.accept(i, k);
                } catch (Exception e) {
                    throw new UnsupportedOperationException("not yet implemented", e);
                }
            }
        };
    }

    public static class Builder {

        private final List<Thread> threads = new LinkedList<>();

        public Builder add(final int parallel, final int serial, final XBiConsumer<Integer, Integer, ?> code) {
            for (int i = 0; i < parallel; ++i) {
                threads.add(new Thread(runnable(i, serial, code), "BatchJob[" + i + "]"));
            }
            return this;
        }
    }

    public static class Report {

    }
}
