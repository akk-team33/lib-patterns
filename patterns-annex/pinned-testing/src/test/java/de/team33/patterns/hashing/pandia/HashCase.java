package de.team33.patterns.hashing.pandia;

import java.math.BigInteger;

enum HashCase {

    ZERO_1(new byte[]{0}, BigInteger.ZERO),
    ZERO_2(new byte[]{0, 0, 0, 0}, BigInteger.ZERO),
    ONE_1(new byte[]{1}, BigInteger.ONE),
    ONE_2(new byte[]{0, 0, 0, 1}, BigInteger.ONE),
    ALL_BITS_1(new byte[]{-1}, BigInteger.valueOf(255)),
    ALL_BITS_3(new byte[]{-1, -1, -1}, BigInteger.valueOf(0xffffffL)),
    ALL_BITS_7(new byte[]{-1, -1, -1, -1, -1, -1, -1}, BigInteger.valueOf(0xffffffffffffffL)),
    MAX_BIT_1(new byte[]{-128}, BigInteger.valueOf(128)),
    MAX_BIT_4(new byte[]{-128, 0, 0, 0}, BigInteger.valueOf(0x80000000L)),
    MAX_BIT_7(new byte[]{-128, 0, 0, 0, 0, 0, 0}, BigInteger.valueOf(0x80000000000000L)),
    OTHER(Extra.OTHER_VALUE.toByteArray(), Extra.OTHER_VALUE);

    final byte[] bytes;
    final BigInteger bigInteger;
    final String hexString;
    final String base36String;

    HashCase(final byte[] bytes, final BigInteger bigInteger) {
        this.bytes = bytes;
        this.bigInteger = bigInteger;
        this.hexString = bigInteger.toString(16);
        this.base36String = bigInteger.toString(36);
    }

    private static class Extra {

        private static final BigInteger OTHER_VALUE = BigInteger.valueOf(0x13579ace24680bdfL);
    }
}
