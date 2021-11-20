package de.team33.patterns.random.e1;

final class XFactoryUtil {

    static final int FLOAT_RESOLUTION = Float.SIZE;
    static final int DOUBLE_RESOLUTION = Double.SIZE;
    static final int BIG_RESOLUTION = Long.SIZE;

    private XFactoryUtil() {
    }

    @SuppressWarnings("NumericCastThatLosesPrecision")
    static char anyRawChar(final BitFactory bitFactory) {
        return (char) bitFactory.anyBits(Character.SIZE).intValue();
    }
}
