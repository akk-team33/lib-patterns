package de.team33.patterns.exceptional.dione.publics;

import de.team33.patterns.exceptional.dione.ExpectationException;
import de.team33.patterns.exceptional.dione.Ignoring;
import de.team33.patterns.exceptional.dione.XSupplier;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class IgnoringTest {

    static final Ignoring<IOException> IGNORING_IO_EXCEPTION = Ignoring.any(IOException.class);
    static final Ignoring<IOException> IGNORING_IO_IA_IS_EXCEPTION = Ignoring.any(IOException.class,
                                                                                  IllegalArgumentException.class,
                                                                                  IllegalStateException.class);
    @SuppressWarnings("rawtypes")
    static final Ignoring IGNORING_MISLEADING = Ignoring.any(SQLException.class);

    private static String throwingAnIOException() throws IOException {
        throw new IOException("throwing an IOException");
    }

    private static String throwingAnIllegalArgumentException() {
        throw new IllegalArgumentException("throwing an IllegalArgumentException");
    }

    private static String throwingAnIllegalStateException() {
        throw new IllegalStateException("throwing an IllegalStateException");
    }

    @ParameterizedTest
    @EnumSource
    void run(final Case testCase) {
        final String[] result = {null};
        try {
            testCase.ignoring.run(() -> result[0] = testCase.supplier.get());
            assertNull(testCase.expectedException);
            assertEquals(testCase.rawExpected, result[0]);
        } catch (final RuntimeException e) {
            assertNotNull(testCase.expectedException);
            assertTrue(testCase.expectedException.isInstance(e),
                       () -> String.format("expected %s - but was %s",
                                           testCase.expectedException.getCanonicalName(),
                                           e.getClass().getCanonicalName()));
        }
    }

    @ParameterizedTest
    @EnumSource
    void get(final Case testCase) {
        try {
            final Optional<String> result = testCase.ignoring.get(testCase.supplier);
            assertNull(testCase.expectedException);
            assertEquals(testCase.expected(), result);
        } catch (final RuntimeException e) {
            assertNotNull(testCase.expectedException);
            assertTrue(testCase.expectedException.isInstance(e),
                       () -> String.format("expected %s - but was %s",
                                           testCase.expectedException.getCanonicalName(),
                                           e.getClass().getCanonicalName()));
        }
    }

    @SuppressWarnings("unused")
    enum Case {
        RETURNING_NULL(IGNORING_IO_EXCEPTION, () -> null, null, null),
        RETURNING_STRING(IGNORING_IO_EXCEPTION, () -> "a string", "a string", null),
        THROWING_CHECKED(IGNORING_IO_EXCEPTION,
                         IgnoringTest::throwingAnIOException,
                         null,
                         null),
        THROWING_MISLEADING(IGNORING_MISLEADING,
                            IgnoringTest::throwingAnIOException,
                            null,
                            ExpectationException.class),
        THROWING_IA_IGNORED(IGNORING_IO_IA_IS_EXCEPTION,
                            IgnoringTest::throwingAnIllegalArgumentException,
                            null,
                            null),
        THROWING_IS_IGNORED(IGNORING_IO_IA_IS_EXCEPTION,
                            IgnoringTest::throwingAnIllegalStateException,
                            null,
                            null),
        THROWING_IA(IGNORING_IO_EXCEPTION,
                    IgnoringTest::throwingAnIllegalArgumentException,
                    null,
                    IllegalArgumentException.class),
        THROWING_IS(IGNORING_IO_EXCEPTION,
                    IgnoringTest::throwingAnIllegalStateException,
                    null,
                    IllegalStateException.class);

        final Ignoring<IOException> ignoring;
        final XSupplier<String, IOException> supplier;
        final String rawExpected;
        final Class<? extends Exception> expectedException;

        Case(final Ignoring<IOException> ignoring,
             final XSupplier<String, IOException> supplier,
             final String rawExpected,
             final Class<? extends Exception> expectedException) {
            this.ignoring = ignoring;
            this.supplier = supplier;
            this.rawExpected = rawExpected;
            this.expectedException = expectedException;
        }

        final Optional<String> expected() {
            return Optional.ofNullable(rawExpected);
        }
    }
}