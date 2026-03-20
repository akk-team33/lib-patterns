package de.team33.patterns.hierarchy.mab.testing;

import de.team33.patterns.hierarchy.mab.Nodes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class PathLister implements Nodes.Lister<Path, PathProblem> {

    private static final List<Path> EMPTY_LIST = List.of();

    @Override
    public List<Path> list(final Path path, final Consumer<? super PathProblem> onProblem) {
        if (Files.isDirectory(path)) {
            try (final Stream<Path> paths = Files.list(path)) {
                return paths.sorted(Comparator.comparing(Path::getFileName)).toList();
            } catch (final IOException e) {
                onProblem.accept(new PathProblem(path, e));
            }
        }
        return EMPTY_LIST;
    }
}
