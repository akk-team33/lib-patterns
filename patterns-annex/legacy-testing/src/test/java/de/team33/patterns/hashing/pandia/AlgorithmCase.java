package de.team33.patterns.hashing.pandia;

import de.team33.testing.io.hydra.FileIO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public enum AlgorithmCase {

    MD5_EMPTY(Algorithm.MD5, "empty.txt"),
    MD5_SHORT(Algorithm.MD5, "short.txt"),
    MD5_LONG(Algorithm.MD5, "long.txt"),
    SHA1_EMPTY(Algorithm.SHA_1, "empty.txt"),
    SHA256_SHORT(Algorithm.SHA_256, "short.txt"),
    SHA512_LONG(Algorithm.SHA_512, "long.txt");

    public final Algorithm algorithm;
    public final Hash expected;
    public final String string;
    public final Path path;
    public final byte[] bytes;

    AlgorithmCase(final Algorithm algorithm, final String rsrcName) {
        this.algorithm = algorithm;
        this.path = newPath(rsrcName);
        this.bytes = readBytes(path);
        this.string = new String(bytes, StandardCharsets.UTF_8);
        this.expected = new Hash(expected(algorithm.ident(), bytes));
    }

    private static byte[] expected(final String ident, final byte[] bytes) {
        try {
            final MessageDigest md = MessageDigest.getInstance(ident);
            md.update(bytes, 0, bytes.length);
            return md.digest();
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private static byte[] readBytes(final Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private static Path newPath(final String rsrcName) {
        final Path path = Paths.get("target", "testing", AlgorithmCase.class.getSimpleName(), rsrcName);
        FileIO.copy(AlgorithmCase.class, rsrcName, path);
        return path;
    }
}
