package de.team33.patterns.hierarchy.mab.testing;

import de.team33.patterns.hierarchy.mab.Nodes;

import java.io.IOException;
import java.nio.file.Path;

public record PathProblem(Path node, IOException cause) implements Nodes.Problem<Path> {
}
