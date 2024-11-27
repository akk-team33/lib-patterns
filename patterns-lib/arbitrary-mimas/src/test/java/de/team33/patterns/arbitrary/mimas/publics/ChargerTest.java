package de.team33.patterns.arbitrary.mimas.publics;

import de.team33.patterns.arbitrary.mimas.UnfitConditionException;
import de.team33.patterns.arbitrary.mimas.sample.Buildable;
import de.team33.patterns.arbitrary.mimas.sample.Generic;
import de.team33.patterns.arbitrary.mimas.sample.Sample;
import de.team33.patterns.arbitrary.mimas.testing.ChargerSupply;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class ChargerTest extends ChargerSupply {

    @Test
    final void setterNotApplicable() {
        try {
            final Sample result = charge(new SampleEx());
            fail("should fail but was <" + result + ">");
        } catch (final UnfitConditionException e) {
            assertEquals("Method not applicable as setter!", e.getMessage().substring(0, 32));
            assertTrue(e.getMessage().contains(SampleEx.class.getSimpleName() + ".setDateValue"));
        }
    }

    @Test
    final void supplierNotFound() {
        try {
            final Sample result = charge(new Sample(), "anyStrings");
            fail("should fail but was <" + result + ">");
        } catch (final UnfitConditionException e) {
            assertEquals("No appropriate supplier method found!", e.getMessage().substring(0, 37));
            assertTrue(e.getMessage().contains(getClass().getSimpleName()));
            assertTrue(e.getMessage().contains(Sample.class.getSimpleName()));
        }
    }

    @Test
    final void charge_Sample() {
        final Sample result = charge(new Sample());
        assertEquals(sample, result);
    }

    @Test
    final void charge_SampleEx() {
        final Sample result = charge(new SampleEx(), "setDateValue");
        assertEquals(sample, result);
    }

    @Test
    final void charge_Buildable() {
        final Buildable result = charge(Buildable.builder()).build();
        assertEquals(buildable, result);
    }

    @Test
    final void charge_Generic() {
        final Generic<String> result = charge(new Generic<>(), "setTValue");
        assertEquals(new Generic<String>(), result);
    }
}

