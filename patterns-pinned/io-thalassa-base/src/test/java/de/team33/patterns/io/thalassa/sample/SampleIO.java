package de.team33.patterns.io.thalassa.sample;

import de.team33.patterns.io.thalassa.FileIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Path;

@SuppressWarnings("unused")
public class SampleIO extends FileIO<Sample> {

    public SampleIO(final Path path, final Charset charset) {
        super(path, charset, SampleIO::readSample, SampleIO::writeSample);
    }

    private static Sample readSample(final BufferedReader reader) throws IOException {
        try (final StringWriter writer = new StringWriter()) {
            reader.transferTo(writer);
            return Sample.parse(writer.toString());
        }
    }

    private static void writeSample(final Writer writer, final Sample sample) throws IOException {
        writer.write(sample.toString());
    }
}