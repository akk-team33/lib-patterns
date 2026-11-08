package de.team33.patterns.config.eunomia.publics;

import de.team33.patterns.config.eunomia.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ConfigRepoTest {

    private static final Path TEST_PATH =
            Path.of("target", "testing", ConfigRepoTest.class.getSimpleName());
    private static final Supply SUPPLY =
            new Supply();
    private static final SampleConfig DEFAULT_CONFIG =
            SUPPLY.anySampleConfig();

    private final Path testPath = TEST_PATH.resolve(SUPPLY.anyFileName());
    private final Path systemPath = testPath.resolve("system");
    private final Path userPath = testPath.resolve("user");
    private final Path cwdPath = testPath.resolve("cwd");
    private final ConfigRepo<SampleConfig> repo = new SampleRepo(DEFAULT_CONFIG, systemPath, userPath, cwdPath);

    private void logTestPath() {
        // on demand ...
        // System.out.println(testPath);
    }

    @Test
    final void path() {
        assertNull(repo.path(ConfigLevel.DEFAULT));
        assertTrue(repo.path(ConfigLevel.SYSTEM).startsWith(systemPath.toAbsolutePath().normalize()));
        assertTrue(repo.path(ConfigLevel.USER).startsWith(userPath.toAbsolutePath().normalize()));
        assertTrue(repo.path(ConfigLevel.CWD).startsWith(cwdPath.toAbsolutePath().normalize()));
    }

    @Test
    final void read_default() {
        final SampleConfig result = repo.read();
        assertEquals(DEFAULT_CONFIG, result);
        assertFalse(Files.exists(testPath, LinkOption.NOFOLLOW_LINKS));
    }

    @ParameterizedTest
    @EnumSource
    void roundTrip(final ConfigLevel level) {
        logTestPath();
        final var expected = SUPPLY.anySampleConfig();
        repo.write(level, expected);
        final SampleConfig result = repo.read(level);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @EnumSource
    void read_after_write(final ConfigLevel level) {
        logTestPath();
        final var expected = SUPPLY.anySampleConfig();
        repo.write(level, expected);

        final SampleConfig result = repo.read();
        assertEquals(expected, result);
    }

    @Test
    void read_after_write_partial() {
        logTestPath();
        final var expected = SUPPLY.anySampleConfig();
        repo.write(ConfigLevel.SYSTEM, new SampleConfig(expected.string(), null, null));
        repo.write(ConfigLevel.USER, new SampleConfig(null, expected.entry(), null));
        repo.write(ConfigLevel.CWD, new SampleConfig(null, null, expected.items()));

        final SampleConfig result = repo.read();
        assertEquals(expected, result);
    }

    @Test
    void reset() {
        logTestPath();
        repo.write(ConfigLevel.SYSTEM, SUPPLY.anySampleConfig());
        repo.write(ConfigLevel.USER, SUPPLY.anySampleConfig());
        repo.write(ConfigLevel.CWD, SUPPLY.anySampleConfig());

        repo.reset();
        assertEquals(DEFAULT_CONFIG, repo.read());
    }
}