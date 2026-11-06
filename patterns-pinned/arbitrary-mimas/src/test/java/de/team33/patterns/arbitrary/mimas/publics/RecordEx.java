package de.team33.patterns.arbitrary.mimas.publics;

import de.team33.patterns.arbitrary.mimas.sample.Record;

import java.util.List;

@SuppressWarnings({
        "unused", "BooleanParameter", "ConstructorWithTooManyParameters", "PublicConstructorInNonPublicClass"})
class RecordEx extends Record {

    public RecordEx(final boolean booleanValue, final String stringValue, final int intValue, final Long longValue,
                    final List<String> stringList, final List<Long> longList) {
        super(booleanValue, stringValue, intValue, longValue, stringList, longList);
    }

    public RecordEx(final boolean booleanValue, final String stringValue, final int intValue, final Long longValue) {
        super(booleanValue, stringValue, intValue, longValue);
    }

    public RecordEx() {
    }
}
