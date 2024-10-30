package de.team33.patterns.io.phobos.testing;

import de.team33.testing.io.hydra.ZipIO;
import org.junit.jupiter.api.BeforeEach;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class IoTestBase {

    private String testID;
    private Path testPath;

    @BeforeEach
    public final void initModifyingTestBase() {
        testID = UUID.randomUUID().toString();
        testPath = Paths.get("target", "testing", getClass().getSimpleName(), testID);

        ZipIO.unzip(IoTestBase.class, "files.zip", testPath);
        //assertEquals(TextIO.read(IoTestBase.class, "files.txt"),
        //             FileInfo.of(leftPath).toString());
    }

    public final String testID() {
        return testID;
    }

    public final Path testPath() {
        return testPath;
    }
}
