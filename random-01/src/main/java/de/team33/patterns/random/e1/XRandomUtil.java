package de.team33.patterns.random.e1;

final class XRandomUtil {

    static final int FLOAT_RESOLUTION = Float.SIZE;
    static final int DOUBLE_RESOLUTION = Double.SIZE;
    static final int BIG_RESOLUTION = Long.SIZE;

    private XRandomUtil() {
    }

    @SuppressWarnings("NumericCastThatLosesPrecision")
    static char anyRawChar(final BitFactory bitFactory) {
        return (char) bitFactory.anyBits(Character.SIZE).intValue();
    }
}
