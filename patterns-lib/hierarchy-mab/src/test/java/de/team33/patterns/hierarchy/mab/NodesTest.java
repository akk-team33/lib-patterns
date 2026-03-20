package de.team33.patterns.hierarchy.mab;

import de.team33.patterns.hierarchy.mab.testing.PathProblem;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class NodesTest {

    @Test
    final void log() {
        Nodes.log(new PathProblem(Path.of("pom.xml"), new IOException()));
        assertTrue(true);
    }
}