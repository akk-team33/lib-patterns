package de.team33.patterns.io.thalassa;

import de.team33.patterns.exceptional.dione.XBiConsumer;
import de.team33.patterns.exceptional.dione.XFunction;

import java.io.*;
import java.nio.charset.Charset;

final class Util {

    private Util() {}

    static <T> XFunction<InputStream, T, IOException>
    readMethod(final XFunction<? super BufferedReader, ? extends T, ? extends IOException> method,
               final Charset charset) {
        return in -> {
            try (final Reader reader = new InputStreamReader(in, charset);
                 final BufferedReader buffered = new BufferedReader(reader)) {
                return method.apply(buffered);
            }
        };
    }

    static <T> XBiConsumer<OutputStream, T, IOException>
    writeMethod(final XBiConsumer<? super BufferedWriter, ? super T, ? extends IOException> method,
                final Charset charset) {
        return (out, subject) -> {
            try (final Writer writer = new OutputStreamWriter(out, charset);
                 final BufferedWriter buffered = new BufferedWriter(writer)) {
                method.accept(buffered, subject);
            }
        };
    }

}
