package de.team33.patterns.io.adrastea;

import java.io.IOException;
import java.nio.file.Path;

public record IOProblem(Path path, IOException cause) {
}
