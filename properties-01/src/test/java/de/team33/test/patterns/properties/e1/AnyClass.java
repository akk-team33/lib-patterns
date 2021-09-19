package de.team33.test.patterns.properties.e1;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static java.lang.System.identityHashCode;
import static java.lang.System.nanoTime;

public class AnyClass extends AnyBaseClass {

    private int anInt;
    private Double aDouble;
    private String aString;
    private Date aDate;

    private transient List<Object> aTransient = new LinkedList<>(Arrays.asList(identityHashCode(this), nanoTime()));

    public AnyClass() {
    }

    public AnyClass(final Random random) {
        super((random));
        anInt = random.nextInt();
        aDouble = (0 == random.nextInt(100)) ? null : random.nextDouble();
        aString = (0 == random.nextInt(100)) ? null : new BigInteger(100, random).toString(Character.MAX_RADIX);
        aDate = (0 == random.nextInt(100)) ? null : new Date(System.currentTimeMillis() + random.nextInt());
    }

    public AnyClass(final AnyClass origin) {
        super(origin);
        anInt = origin.anInt;
        aDouble = origin.aDouble;
        aString = origin.aString;
        setADate(origin.getADate());
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

    public int getAnInt() {
        return anInt;
    }

    public AnyClass setAnInt(final int anInt) {
        this.anInt = anInt;
        return this;
    }

    public Double getADouble() {
        return aDouble;
    }

    public AnyClass setADouble(final Double aDouble) {
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
        return (null == aDate) ? null : new Date(aDate.getTime());
    }

    public AnyClass setADate(final Date aDate) {
        this.aDate = (null == aDate) ? null : new Date(aDate.getTime());
        return this;
    }
}
