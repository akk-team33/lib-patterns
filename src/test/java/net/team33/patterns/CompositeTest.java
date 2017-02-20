package net.team33.patterns;

import net.team33.patterns.reflect.Fields;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

public class CompositeTest {

    private static final Fields<AnyClass> FIELDS = Fields.of(AnyClass.class);

    private AnyClass subject;

    @Before
    public final void setUp() {
        subject = new AnyClass();
    }

    @Test
    public final void testHashCode() {
        Assert.assertEquals(FIELDS.hashCode(subject), subject.hashCode());
    }

    @Test
    public final void testEquals() {
        Stream.of(subject, FIELDS.copy(subject).to(new AnyClass()), new AnyClass(), null)
                .forEach(any -> Assert.assertEquals(FIELDS.equals(subject, any), subject.equals(any)));
    }

    @Test
    public final void testToString() {
        Assert.assertEquals(FIELDS.toString(subject), subject.toString());
    }

    @SuppressWarnings({"unused", "UseOfObsoleteDateTimeApi"})
    private static class AnyClass extends Composite<AnyClass> {

        private final String aString = UUID.randomUUID().toString();
        private final Number aNumber = UUID.randomUUID().hashCode();
        private final Date aDate = new Date();

        @Override
        protected final <R> R apply(final Function<AnyClass, Function<Fields<AnyClass>, R>> function) {
            return function.apply(this).apply(FIELDS);
        }
    }
}