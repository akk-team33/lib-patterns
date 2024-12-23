package de.team33.patterns.hashing.pandia;

import de.team33.patterns.exceptional.dione.XSupplier;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public enum Algorithm {

    MD5("MD5"),
    SHA_1("SHA-1"),
    SHA_256("SHA-256"),
    SHA_512("SHA-512");

    private static final int ONE_KB = 1024;

    private final String ident;

    Algorithm(final String ident) {
        this.ident = ident;
    }

    public final String ident() {
        return ident;
    }

    public final Hash hash(final String origin) {
        return hash(origin, StandardCharsets.UTF_8);
    }

    public final Hash hash(final String origin, final Charset charset) {
        return hash(origin.getBytes(charset));
    }

    public final Hash hash(final byte[] bytes) {
        return hash(() -> new ByteArrayInputStream(bytes));
    }

    public final Hash hash(final Path path) {
        return hash(() -> Files.newInputStream(path));
    }

    public final Hash hash(final XSupplier<InputStream, IOException> streamable) {
        try (final InputStream in = streamable.get()) {
            return hash(in);
        } catch (final IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public final Hash hash(final InputStream in) throws IOException {
        final MessageDigest md = getMessageDigest();
        final byte[] buffer = new byte[ONE_KB];
        for (int read = in.read(buffer, 0, ONE_KB); 0 < read; read = in.read(buffer, 0, ONE_KB)) {
            md.update(buffer, 0, read);
        }
        return new Hash(md.digest());
    }

    private MessageDigest getMessageDigest() {
        try {
            return MessageDigest.getInstance(ident);
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalStateException("Message digest algorithm <" + ident + "> is not supported", e);
        }
    }
}