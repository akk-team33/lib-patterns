package de.team33.test.patterns.properties.e1;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class AnyClass extends AnyBaseClass {

    private int anInt;
    private double aDouble;
    private String aString;
    private Date aDate;

    public AnyClass() {
    }

    @Override
    public AnyClass setALong(final long aLong) {
        return (AnyClass) super.setALong(aLong);
    }

    @Override
    public AnyClass setABigDecimal(final BigDecimal aBigDecimal) {
        return (AnyClass) super.setABigDecimal(aBigDecimal);
    }

    @Override
    public AnyClass setAList(final List<?> aList) {
        return (AnyClass) super.setAList(aList);
    }

    public AnyClass(final Random random) {
        super((random));
        anInt = random.nextInt();
        aDouble = random.nextDouble();
        aString = new BigInteger(100, random).toString(Character.MAX_RADIX);
        aDate = new Date(System.currentTimeMillis() + random.nextInt());
    }

    public AnyClass(final AnyClass origin) {
        super(origin);
        anInt = origin.anInt;
        aDouble = origin.aDouble;
        aString = origin.aString;
        aDate = new Date(origin.aDate.getTime());
    }

    public int getAnInt() {
        return anInt;
    }

    public AnyClass setAnInt(final int anInt) {
        this.anInt = anInt;
        return this;
    }

    public double getADouble() {
        return aDouble;
    }

    public AnyClass setADouble(final double aDouble) {
        this.aDouble = aDouble;
        return this;
    }

    public String getAString() {
        return aString;
    }

    public AnyClass setAString(final String aString) {
        this.aString = aString;
        return this;
    }

    public Date getADate() {
        return aDate;
    }

    public AnyClass setADate(final Date aDate) {
        this.aDate = aDate;
        return this;
    }
}
