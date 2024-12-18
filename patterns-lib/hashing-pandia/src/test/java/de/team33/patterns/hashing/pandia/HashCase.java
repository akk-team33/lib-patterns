package de.team33.patterns.hashing.pandia;

import java.math.BigInteger;

enum HashCase {

    ZERO_1(new byte[]{0}, BigInteger.ZERO);

    final byte[] bytes;
    final BigInteger bigInteger;

    HashCase(final byte[] bytes, final BigInteger bigInteger) {
        this.bytes = bytes;
        this.bigInteger = bigInteger;
    }
}
