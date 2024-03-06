package de.team33.patterns.typing.atlas.sample.publics;

import de.team33.patterns.typing.atlas.sample.testing.Sample;

import java.util.Date;

@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
class SampleEx extends Sample {

    private Date dateValue;

    public final Date getDateValue() {
        return dateValue;
    }

    public final SampleEx setDateValue(final Date dateValue) {
        this.dateValue = dateValue;
        return this;
    }
}
