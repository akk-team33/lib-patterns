package de.team33.patterns.tree.styx;

import de.team33.patterns.tree.styx.publics.TestGenerator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class PathTest {

    private static final TestGenerator GEN = new TestGenerator();

    @Disabled
    @Test
    void of() {
        final Path<String> path = Path.<String>of(Path.slice(GEN.anyString()),
                                                  Path.slice(GEN.anyInt()),
                                                  Path.slice(GEN.anyString()));
    }
}