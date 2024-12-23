package de.team33.patterns.io.phobos.testing;

import de.team33.testing.io.hydra.ZipIO;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class IoTestBase {

    private String testID;
    private Path testPath;

    @BeforeEach
    public final void initModifyingTestBase() throws IOException {
        testID = UUID.randomUUID().toString();
        testPath = Paths.get("target", "testing", getClass().getSimpleName(), testID);

        ZipIO.unzip(IoTestBase.class, "files.zip", testPath);
        //assertEquals(TextIO.read(IoTestBase.class, "files.txt"),
        //             FileInfo.of(leftPath).toString());

        final Path dirPath = testPath.resolve("exceptional-dione");
        final Path dirLinkPath = testPath.resolve("directory.link");
        Files.createSymbolicLink(dirLinkPath, dirPath.getFileName());

        final Path regularPath = dirPath.resolve("pom.xml");
        final Path regLinkPath = dirPath.resolve("regular.link");
        Files.createSymbolicLink(regLinkPath, regularPath.getFileName());

        final Path missingPath = dirPath.resolve("missing.file");
        final Path missingLinkPath = dirPath.resolve("missing.link");
        Files.createSymbolicLink(missingLinkPath, missingPath.getFileName());

        final Path specPath = Paths.get("/dev/null");
        final Path specLinkPath = testPath.resolve("special.link");
        Files.createSymbolicLink(specLinkPath, specPath);

        final Path linkLinkPath = dirPath.resolve("indirect.link");
        Files.createSymbolicLink(linkLinkPath, regLinkPath.getFileName());
    }

    public final String testID() {
        return testID;
    }

    public final Path testPath() {
        return testPath;
    }
}
