package de.team33.patterns.files.styx.publics;

import de.team33.patterns.exceptional.dione.XConsumer;
import de.team33.patterns.files.pluto.FileEntry;
import de.team33.patterns.files.styx.Problem;
import de.team33.patterns.files.styx.Styx;
import de.team33.testing.io.hydra.ZipIO;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("FieldCanBeLocal")
class StyxTest {

    private static final String CLASS_NAME = StyxTest.class.getSimpleName();
    @SuppressWarnings("HardcodedFileSeparator")
    private static final Path TEST_PATH = Path.of("target", "testing", CLASS_NAME);
    @SuppressWarnings("HardcodedFileSeparator")
    private static final Path DEV_NULL = Paths.get("/dev/null"); // special file

    private final String uuid = UUID.randomUUID().toString();
    private final Path testPath = TEST_PATH.resolve(uuid);
    private final Path missingLink = testPath.resolve("missing.link");
    private final Path dirLink = testPath.resolve("directory.link");
    private final Path regularLink = testPath.resolve("regular.link");
    private final Path specialLink = testPath.resolve("special.link");
    private final Path linkLink = testPath.resolve("link.link");
    private final Path missingFile = testPath.resolve("file/is/missing");
    private final Path directory = testPath.resolve("de/team33");
    private final Path regularFile = directory.resolve("cmd/files/Main.java");

    StyxTest() throws IOException {
        Files.createDirectories(testPath);
        ZipIO.unzip(getClass(), "../files.zip", testPath);
        Files.createSymbolicLink(missingLink, missingFile.toAbsolutePath().normalize());
        Files.createSymbolicLink(dirLink, directory.toAbsolutePath().normalize());
        Files.createSymbolicLink(regularLink, regularFile.toAbsolutePath().normalize());
        Files.createSymbolicLink(specialLink, DEV_NULL);
        Files.createSymbolicLink(linkLink, regularLink.toAbsolutePath().normalize());
    }

    private static <X extends Exception> void forbidden(final Path path, final XConsumer<Path, X> method)
            throws IOException, X {
        final Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(path);
        Files.setPosixFilePermissions(path, Set.of());
        try {
            method.accept(path);
        } finally {
            Files.setPosixFilePermissions(path, permissions);
        }
    }

    @Test
    final void stream_forbidden() throws IOException {
        final List<Problem> problems = new LinkedList<>();
        forbidden(testPath, path -> {
            final Styx.Options options = Styx.Options.DEFAULT.onProblem(problems::add);
            final Styx.Streamer streamer = Styx.streamer(options);
            final FileEntry entry = FileEntry.original(path);

            final List<FileEntry> result = streamer.stream(entry)
                                                   .toList();

            assertEquals(List.of(entry), result);
        });
        assertEquals(1, problems.size());
        assertEquals(testPath.toAbsolutePath().normalize(), problems.get(0).entry().path());
    }

    @Test
    final void children() {
        final List<String> expected = List.of("de", "directory.link", "link.link",
                                              "missing.link", "regular.link", "special.link");
        final FileEntry entry = FileEntry.original(testPath);
        final List<String> result = Styx.children(entry)
                                        .map(FileEntry::name)
                                        .toList();
        assertEquals(expected, result);
    }

    @Test
    final void descendants() {
        final List<String> expected = List.of("de", "team33", "cmd", "files", "balancing", "Relative.java",
                                              "Relatives.java", "State.java", "cleaning", "DirDeletion.java", "common",
                                              "Counter.java", "FileType.java", "HashId.java", "Output.java",
                                              "RequestException.java", "TimeId.java", "job", "About.java",
                                              "Cleaning.java", "Command.java", "Comparing.java", "Copying.java",
                                              "Deduping.java", "Deletion.java", "DirCopying.java", "DirFinder.java",
                                              "FileType.java", "Finder.java", "IOFault.java", "Listing.java",
                                              "Moving.java", "Util.java", "Main.java", "matching",
                                              "CaseSensitivity.java", "InternalException.java", "Method.java",
                                              "NameMatcher.java", "TypeMatcher.java", "WildcardString.java", "moving",
                                              "FileInfo.java", "Fragment.java", "Guard.java", "Resolver.java",
                                              "ResolverException.java", "Rule.java", "Segment.java", "patterns",
                                              "hierarchy", "mab", "Nodes.java", "package-info.java", "io", "adrastea",
                                              "FileEntry.java", "LinkAttributes.java", "LinkHandling.java",
                                              "Normality.java", "package-info.java", "Util.java", "tools", "io",
                                              "Bytes.java", "FileHashing.java", "LazyHashing.java", "LazyTiming.java",
                                              "StrictHashing.java", "directory.link", "link.link", "missing.link",
                                              "regular.link", "special.link");
        final FileEntry entry = FileEntry.original(testPath);
        final List<String> result = Styx.descendants(entry)
                                        .map(FileEntry::name)
                                        .toList();
        assertEquals(expected, result);
    }

    @Test
    final void stream() {
        final List<String> expected = List.of(uuid, "de", "team33", "cmd", "files", "balancing", "Relative.java",
                                              "Relatives.java", "State.java", "cleaning", "DirDeletion.java", "common",
                                              "Counter.java", "FileType.java", "HashId.java", "Output.java",
                                              "RequestException.java", "TimeId.java", "job", "About.java",
                                              "Cleaning.java", "Command.java", "Comparing.java", "Copying.java",
                                              "Deduping.java", "Deletion.java", "DirCopying.java", "DirFinder.java",
                                              "FileType.java", "Finder.java", "IOFault.java", "Listing.java",
                                              "Moving.java", "Util.java", "Main.java", "matching",
                                              "CaseSensitivity.java", "InternalException.java", "Method.java",
                                              "NameMatcher.java", "TypeMatcher.java", "WildcardString.java", "moving",
                                              "FileInfo.java", "Fragment.java", "Guard.java", "Resolver.java",
                                              "ResolverException.java", "Rule.java", "Segment.java", "patterns",
                                              "hierarchy", "mab", "Nodes.java", "package-info.java", "io", "adrastea",
                                              "FileEntry.java", "LinkAttributes.java", "LinkHandling.java",
                                              "Normality.java", "package-info.java", "Util.java", "tools", "io",
                                              "Bytes.java", "FileHashing.java", "LazyHashing.java", "LazyTiming.java",
                                              "StrictHashing.java", "directory.link", "link.link", "missing.link",
                                              "regular.link", "special.link");
        final FileEntry entry = FileEntry.original(testPath);
        final List<String> result = Styx.stream(entry)
                                        .map(FileEntry::name)
                                        .toList();
        assertEquals(expected, result);
    }

    @Test
    final void stream_skip() {
        final List<String> expected = List.of(uuid, "de", "team33", "cmd", "files", "balancing", "cleaning",
                                              "common", "Counter.java", "FileType.java", "HashId.java", "Output.java",
                                              "RequestException.java", "TimeId.java", "job", "Main.java", "matching",
                                              "CaseSensitivity.java", "InternalException.java", "Method.java",
                                              "NameMatcher.java", "TypeMatcher.java", "WildcardString.java", "moving",
                                              "patterns", "tools", "io", "Bytes.java", "FileHashing.java",
                                              "LazyHashing.java", "LazyTiming.java", "StrictHashing.java",
                                              "directory.link", "link.link", "missing.link", "regular.link",
                                              "special.link");
        final List<Problem> problems = new LinkedList<>();

        final Styx.Options options = Styx.Options.DEFAULT.onProblem(problems::add)
                                                         .skip(entry -> entry.path().endsWith("balancing"))
                                                         .skip(entry -> entry.path().endsWith("cleaning"))
                                                         .skip(entry -> entry.path().endsWith("job"))
                                                         .skip(entry -> entry.path().endsWith("moving"))
                                                         .skip(entry -> entry.path().endsWith("patterns"));
        final FileEntry entry = FileEntry.original(testPath);
        final List<String> result = Styx.stream(entry, options)
                                        .map(FileEntry::name)
                                        .toList();

        assertEquals(expected, result);
        assertTrue(problems.isEmpty());
    }

    @Test
    final void stream_skip_head() {
        final List<String> expected = List.of(uuid);
        final Styx.Options options = Styx.Options.RESOLVE.skip(FileEntry::isDirectory);
        final FileEntry entry = FileEntry.resolved(testPath);

        final List<String> result = Styx.stream(entry, options)
                                        .map(FileEntry::name)
                                        .toList();

        assertEquals(expected, result);
    }
}