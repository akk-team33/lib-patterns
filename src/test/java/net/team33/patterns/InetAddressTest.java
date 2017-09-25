package net.team33.patterns;

import org.junit.Assert;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Base64;
import java.util.stream.Stream;

public class InetAddressTest {

    public static final Base64.Encoder ENCODER = Base64.getEncoder();
    public static final Base64.Decoder DECODER = Base64.getDecoder();

    private static byte[] rawIpAddress(final String ip) {
        try {
            return InetAddress.getByName(ip).getAddress();
        } catch (final UnknownHostException caught) {
            throw new IllegalArgumentException(caught.getMessage(), caught);
        }
    }

    @Test
    public final void testIpV6() {
        final byte[] result = rawIpAddress("1234:5678:9abc:def0:1234:5678:9abc:def0");
        Assert.assertEquals(16, result.length);
    }

    @Test
    public final void encode() {
        Stream.of(
                "89.204.139.45"/*,
                "192.168.100.1",
                "0.0.100.1",
                "192.168.0.0",
                "1234:5678:9abc:def0:1234:5678:9abc:def0",
                "::def0:1234:5678:9abc:def0",
                "1234:5678:9abc:def0:1234::"*/
        ).forEach(ip -> {
            final byte[] input = rawIpAddress(ip);
            final String encoded = ENCODER.encodeToString(input);
            final byte[] decoded = DECODER.decode(encoded);
            Assert.assertArrayEquals(input, decoded);
        });
    }
}
