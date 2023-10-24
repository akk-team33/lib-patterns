package de.team33.patterns.reflect.pandora.testing;

import de.team33.patterns.reflect.pandora.BeanMapper;

import java.time.Instant;
import java.util.Map;

public class BeanClass implements BeanInterface {

    private static final BeanMapper<BeanInterface, BeanClass> MAPPER = BeanMapper.mapping(BeanInterface.class,
                                                                                          BeanClass.class);

    private int intValue;
    private Long longValue;
    private String stringValue;
    private Instant instantValue;

    @Override
    public int getIntValue() {
        return intValue;
    }

    public BeanClass setIntValue(int intValue) {
        this.intValue = intValue;
        return this;
    }

    @Override
    public Long getLongValue() {
        return longValue;
    }

    public BeanClass setLongValue(Long longValue) {
        this.longValue = longValue;
        return this;
    }

    @Override
    public String getStringValue() {
        return stringValue;
    }

    public BeanClass setStringValue(String stringValue) {
        this.stringValue = stringValue;
        return this;
    }

    @Override
    public Instant getInstantValue() {
        return instantValue;
    }

    public BeanClass setInstantValue(Instant instantValue) {
        this.instantValue = instantValue;
        return this;
    }

    private Map<String, Object> toMap() {
        return MAPPER.toMap(this);
    }

    @Override
    public final int hashCode() {
        return toMap().hashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        return this == obj || (obj instanceof BeanClass && toMap().equals(((BeanClass)obj).toMap()));
    }

    @Override
    public final String toString() {
        return toMap().toString();
    }
}
