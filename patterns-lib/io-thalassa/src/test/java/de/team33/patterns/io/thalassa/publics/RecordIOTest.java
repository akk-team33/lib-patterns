package de.team33.patterns.io.thalassa.publics;

import de.team33.patterns.io.thalassa.Input;
import de.team33.patterns.io.thalassa.RecordIO;
import de.team33.testing.Supply;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RecordIOTest extends Supply {

    private static final Path PATH = Path.of("target", "testing", RecordIOTest.class.getSimpleName());
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final Input<Sample> CLASSPATH_INPUT =
            RecordIO.by(Sample.class, RecordIOTest.class, "ResourceTest.json");

    private final RecordIO<Sample> io;
    private final Path path;

    RecordIOTest() throws IOException {
        this.path = PATH.resolve("%s.txt".formatted(anyString(8, CHARACTERS)));
        this.io = RecordIO.by(Sample.class, path);
    }

    @Test
    final void by_classpath_read() throws IOException {
        final Sample expected = new Sample("string value", 278,
                                           1L + Integer.MAX_VALUE, SampleEnum.V2);
        final Sample result = CLASSPATH_INPUT.read();
        assertEquals(expected, result);
    }

    @Test
    final void read() throws IOException {
        final Sample original = anySample();
        final String json = "{\n" +
                            "  \"stringValue\": \"%s\",\n" +
                            "  \"intValue\": %d,\n" +
                            "  \"longValue\": %d,\n" +
                            "  \"enumValue\": \"%s\"\n" +
                            "}";
        Files.createDirectories(path.getParent());
        Files.writeString(path, json.formatted(original.stringValue,
                                               original.intValue,
                                               original.longValue,
                                               original.enumValue));

        final Sample result = io.read();
        assertEquals(original, result);
    }

    @Test
    final void write() throws IOException {
        final Sample original = anySample();
        io.write(original);
        assertEquals(original, io.read());
    }

    final Sample anySample() {
        return new Sample(anyString(), anyInt(), anyLong(), anyOf(SampleEnum.class));
    }

    enum SampleEnum {
        V1,
        V2,
        V3
    }

    record Sample(String stringValue, int intValue, Long longValue, SampleEnum enumValue) {
    }
}
