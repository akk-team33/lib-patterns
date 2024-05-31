package de.team33.patterns.arbitrary.mimas;

import java.math.BigInteger;

final class Generating {

    private Generating() {
    }

    static boolean anyBoolean(final BitGenerator generator) {
        return generator.anyBits(1).equals(BigInteger.ONE);
    }

    static byte anyByte(BitGenerator generator) {
        return generator.anyBits(Byte.SIZE).byteValue();
    }

    static short anyShort(BitGenerator generator) {
        return generator.anyBits(Short.SIZE).shortValue();
    }

    static int anyInt(BitGenerator generator) {
        return generator.anyBits(Integer.SIZE).intValue();
    }
}
