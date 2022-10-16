package de.team33.test.patterns.random.shared;

import java.util.Date;

public class SampleEx extends Sample {

    private Date dateValue;

    public Date getDateValue() {
        return dateValue;
    }

    public SampleEx setDateValue(Date dateValue) {
        this.dateValue = dateValue;
        return this;
    }
}
