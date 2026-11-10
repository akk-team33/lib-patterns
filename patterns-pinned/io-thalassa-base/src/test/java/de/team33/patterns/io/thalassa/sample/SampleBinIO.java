package de.team33.patterns.io.thalassa.sample;

import de.team33.patterns.io.thalassa.FileIO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@SuppressWarnings("unused")
public class SampleBinIO extends FileIO<Sample> {

    public SampleBinIO(final Path path, final Charset charset) {
        super(path, SampleBinIO::readSample, SampleBinIO::writeSample);
    }

    private static Sample readSample(final InputStream in) throws IOException {
        try (final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            in.transferTo(out);
            return Sample.parse(out.toString(StandardCharsets.UTF_8));
        }
    }

    private static void writeSample(final OutputStream out, final Sample sample) throws IOException {
        out.write(sample.toString().getBytes(StandardCharsets.UTF_8));
    }
}