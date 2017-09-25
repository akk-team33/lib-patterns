package net.team33.patterns;

import org.junit.Assert;
import org.junit.Test;

public class AsciiCodecTest {

    private static final byte[] SAMPLE = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    @Test
    public void encode() {
        final AsciiCodec codec = new AsciiCodec();
        final String result = codec.encode(SAMPLE);
        Assert.assertEquals("", result);
    }

    @Test
    public void decode() {
        final AsciiCodec codec = new AsciiCodec();
        final String sample = "0123456789";
        final byte[] result = codec.decode(sample);
        Assert.assertArrayEquals(SAMPLE, result);
    }
}