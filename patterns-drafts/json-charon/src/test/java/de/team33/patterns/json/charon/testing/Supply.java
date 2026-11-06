package de.team33.patterns.json.charon.testing;

import de.team33.patterns.arbitrary.mimas.Generator;

import java.math.BigDecimal;

public class Supply extends Generator.Basic {

    public BigDecimal anyBigDecimal() {
        return new BigDecimal(Double.toString(anyDouble()));
    }
}
