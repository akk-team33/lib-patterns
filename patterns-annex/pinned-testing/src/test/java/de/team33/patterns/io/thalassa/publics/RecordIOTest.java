package de.team33.patterns.io.thalassa.publics;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.io.thalassa.Input;
import de.team33.patterns.io.thalassa.RecordIO;
import de.team33.patterns.typing.proteus.Type;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RecordIOTest {

    private static final Generator GENERATOR = Generator.of(new SecureRandom());
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final Path PATH = Path.of("target", "testing", RecordIOTest.class.getSimpleName());

    static <R extends Record> WriteCase<R> writeCase(final String prefix,
                                                     final R sample,
                                                     final Function<? super Path, RecordIO<R>> toRecordIO) {
        final String name = "%s.json".formatted(GENERATOR.anyString(8, CHARACTERS));
        final Path path = PATH.resolve(name);
        return new WriteCase<>("%s: %s".formatted(prefix, name), sample, toRecordIO.apply(path));
    }

    static Stream<WriteCase<?>> writeCases() {
        return Stream.of(writeCase("by(Class, Path)", anySample(),
                                   path -> RecordIO.by(Sample.class, path)),
                         writeCase("by(Class, Path, Charset)", anySample(),
                                   path -> RecordIO.by(Sample.class, path, StandardCharsets.UTF_8)),
                         writeCase("by(Type, Path)", anyGeneric(),
                                   path -> RecordIO.by(new Type<>() {}, path)),
                         writeCase("by(Type, Path, Charset)", anyGeneric(),
                                   path -> RecordIO.by(new Type<>() {}, path, StandardCharsets.UTF_8)));
    }

    static <R extends Record> ReadCase<R> readCase(final WriteCase<R> wc) {
        wc.recordIO.writeUnchecked(wc.sample);
        return new ReadCase<>(wc.name, wc.sample, wc.recordIO);
    }

    static Stream<ReadCase<?>> readCases() {
        final Sample sample =
                new Sample("string value", 278, 1L + Integer.MAX_VALUE, SampleEnum.V2);
        final Generic<Long, SampleEnum> generic =
                new Generic<>("string value", 278, 1L + Integer.MAX_VALUE, SampleEnum.V2);
        return Stream.concat(writeCases().map(RecordIOTest::readCase),
                             Stream.of(new ReadCase<>("by(Class, Class, String)", sample,
                                                      RecordIO.by(Sample.class,
                                                                  RecordIOTest.class,
                                                                  "ResourceTest.json")),
                                       new ReadCase<>("by(Class, Class, String, Charset)", sample,
                                                      RecordIO.by(Sample.class,
                                                                  RecordIOTest.class,
                                                                  "ResourceTest.json",
                                                                  StandardCharsets.UTF_8)),
                                       new ReadCase<>("by(Type, Class, String)", generic,
                                                      RecordIO.by(new Type<>() {},
                                                                  RecordIOTest.class,
                                                                  "ResourceTest.json")),
                                       new ReadCase<>("by(Type, Class, String, Charset)", generic,
                                                      RecordIO.by(new Type<>() {},
                                                                  RecordIOTest.class,
                                                                  "ResourceTest.json",
                                                                  StandardCharsets.UTF_8))));
    }

    static Sample anySample() {
        return new Sample(GENERATOR.anyString(),
                          GENERATOR.anyInt(),
                          GENERATOR.anyLong(),
                          GENERATOR.anyOf(SampleEnum.class));
    }

    static Generic<Long, SampleEnum> anyGeneric() {
        return new Generic<>(GENERATOR.anyString(),
                             GENERATOR.anyInt(),
                             GENERATOR.anyLong(),
                             GENERATOR.anyOf(SampleEnum.class));
    }

    @ParameterizedTest
    @MethodSource("readCases")
    final <R extends Record> void read(final ReadCase<R> given) throws IOException {
        final R result = given.recordIO.read();
        assertEquals(given.expected, result);
    }

    @ParameterizedTest
    @MethodSource("writeCases")
    final <R extends Record> void write(final WriteCase<R> given) throws IOException {
        given.recordIO.write(given.sample);
        assertEquals(given.sample, given.recordIO.read());
    }

    @Test
    final void read_write_read_sample() {
        final Sample expected = RecordIO.read(Sample.class, RecordIOTest.class, "ResourceTest.json");
        final Path path = PATH.resolve("%s.json".formatted(GENERATOR.anyString(8, CHARACTERS)));
        RecordIO.write(expected, path);
        final Sample result = RecordIO.read(Sample.class, path);
        assertEquals(expected, result);
    }

    @Test
    final void read_write_read_generic() {
        final Generic<Long, SampleEnum> expected =
                RecordIO.read(new Type<>() {}, RecordIOTest.class, "ResourceTest.json");
        final Path path = PATH.resolve("%s.json".formatted(GENERATOR.anyString(8, CHARACTERS)));
        RecordIO.write(expected, path);
        final Generic<Long, SampleEnum> result =
                RecordIO.read(new Type<>() {}, path);
        assertEquals(expected, result);
    }

    enum SampleEnum {
        V1,
        V2,
        V3
    }

    record WriteCase<R extends Record>(String name, R sample, RecordIO<R> recordIO) {

        @Override
        public String toString() {
            return name;
        }
    }

    record ReadCase<R extends Record>(String name, R expected, Input<R> recordIO) {

        @Override
        public String toString() {
            return name;
        }
    }

    record Sample(String stringValue, int intValue, Long longValue, SampleEnum enumValue) {}

    record Generic<L, E>(String stringValue, int intValue, L longValue, E enumValue) {}
}
