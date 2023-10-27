package de.team33.patterns.reflect.pandora;

import de.team33.patterns.reflect.pandora.testing.BeanClass;
import de.team33.patterns.reflect.pandora.testing.BeanInterface;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.lang.reflect.Modifier;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toCollection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MethodsTest {

    @ParameterizedTest
    @EnumSource
    final void publicOf_isPublic(final Case cs) {
        Methods.publicOf(cs.subjectClass)
               .forEach(method -> assertTrue(Modifier.isPublic(method.getModifiers())));
    }

    @ParameterizedTest
    @EnumSource
    final void publicOf(final Case cs) {
        final Set<String> result = Methods.publicOf(cs.subjectClass)
                                          .map(Methods::signatureOf)
                                          .collect(toCollection(TreeSet::new));

        assertEquals(cs.expectedPublicOf, result);
    }

    @ParameterizedTest
    @EnumSource
    final void publicInherentOf(final Case cs) {
        final Set<String> result = Methods.publicInherentOf(cs.subjectClass)
                                          .map(Methods::signatureOf)
                                          .collect(toCollection(TreeSet::new));

        assertEquals(cs.expectedInherentOf, result);
    }

    enum Case {

        OBJECT(Object.class,
               Collections.emptyList(),
               asList("equals(java.lang.Object)", "getClass()", "hashCode()", "notify()", "notifyAll()",
                      "toString()", "wait()", "wait(long)", "wait(long, int)")),
        PRIVATE_CLASS(PrivateClass.class,
                      asList("publicMethod()"),
                      asList("equals(java.lang.Object)", "getClass()", "hashCode()", "notify()", "notifyAll()",
                             "publicMethod()", "publicStaticMethod()", "toString()", "wait()", "wait(long)",
                             "wait(long, int)")),
        PACKAGE_CLASS(PackageClass.class,
                      asList("publicMethod()"),
                      asList("equals(java.lang.Object)", "getClass()", "hashCode()", "notify()", "notifyAll()",
                             "publicMethod()", "publicStaticMethod()", "toString()", "wait()", "wait(long)",
                             "wait(long, int)")),
        BEAN_INTERFACE(BeanInterface.class,
                       asList("getInstantValue()", "getIntValue()", "getLongValue()", "getStringValue()"),
                       asList("getInstantValue()", "getIntValue()", "getLongValue()", "getStringValue()")),
        BEAN_CLASS(BeanClass.class,
                   asList("getInstantValue()", "getIntValue()", "getLongValue()", "getStringValue()",
                          "setInstantValue(java.time.Instant)", "setIntValue(int)", "setLongValue(java.lang.Long)",
                          "setStringValue(java.lang.String)"),
                   asList("equals(java.lang.Object)", "getClass()", "getInstantValue()", "getIntValue()",
                          "getLongValue()", "getStringValue()", "hashCode()", "notify()", "notifyAll()",
                          "setInstantValue(java.time.Instant)", "setIntValue(int)", "setLongValue(java.lang.Long)",
                          "setStringValue(java.lang.String)", "toString()", "wait()", "wait(long)", "wait(long, int)")),
        INSTANT(Instant.class,
                asList("adjustInto(java.time.temporal.Temporal)", "atOffset(java.time.ZoneOffset)",
                       "atZone(java.time.ZoneId)", "compareTo(java.time.Instant)",
                       "get(java.time.temporal.TemporalField)", "getEpochSecond()",
                       "getLong(java.time.temporal.TemporalField)", "getNano()", "isAfter(java.time.Instant)",
                       "isBefore(java.time.Instant)", "isSupported(java.time.temporal.TemporalField)",
                       "isSupported(java.time.temporal.TemporalUnit)", "minus(java.time.temporal.TemporalAmount)",
                       "minus(long, java.time.temporal.TemporalUnit)", "minusMillis(long)", "minusNanos(long)",
                       "minusSeconds(long)", "plus(java.time.temporal.TemporalAmount)",
                       "plus(long, java.time.temporal.TemporalUnit)", "plusMillis(long)", "plusNanos(long)",
                       "plusSeconds(long)", "query(java.time.temporal.TemporalQuery)",
                       "range(java.time.temporal.TemporalField)", "toEpochMilli()",
                       "truncatedTo(java.time.temporal.TemporalUnit)",
                       "until(java.time.temporal.Temporal, java.time.temporal.TemporalUnit)",
                       "with(java.time.temporal.TemporalAdjuster)", "with(java.time.temporal.TemporalField, long)"),
                asList("adjustInto(java.time.temporal.Temporal)", "atOffset(java.time.ZoneOffset)",
                       "atZone(java.time.ZoneId)", "compareTo(java.lang.Object)", "compareTo(java.time.Instant)",
                       "equals(java.lang.Object)", "from(java.time.temporal.TemporalAccessor)",
                       "get(java.time.temporal.TemporalField)", "getClass()", "getEpochSecond()",
                       "getLong(java.time.temporal.TemporalField)", "getNano()", "hashCode()",
                       "isAfter(java.time.Instant)", "isBefore(java.time.Instant)",
                       "isSupported(java.time.temporal.TemporalField)", "isSupported(java.time.temporal.TemporalUnit)",
                       "minus(java.time.temporal.TemporalAmount)", "minus(long, java.time.temporal.TemporalUnit)",
                       "minusMillis(long)", "minusNanos(long)", "minusSeconds(long)", "notify()", "notifyAll()",
                       "now()", "now(java.time.Clock)", "ofEpochMilli(long)", "ofEpochSecond(long)",
                       "ofEpochSecond(long, long)", "parse(java.lang.CharSequence)",
                       "plus(java.time.temporal.TemporalAmount)", "plus(long, java.time.temporal.TemporalUnit)",
                       "plusMillis(long)", "plusNanos(long)", "plusSeconds(long)",
                       "query(java.time.temporal.TemporalQuery)", "range(java.time.temporal.TemporalField)",
                       "toEpochMilli()", "toString()", "truncatedTo(java.time.temporal.TemporalUnit)",
                       "until(java.time.temporal.Temporal, java.time.temporal.TemporalUnit)", "wait()", "wait(long)",
                       "wait(long, int)", "with(java.time.temporal.TemporalAdjuster)",
                       "with(java.time.temporal.TemporalField, long)"));

        final Class<?> subjectClass;
        final Set<String> expectedInherentOf;
        final Set<String> expectedPublicOf;

        Case(final Class<?> subjectClass, final List<String> expectedInherentOf, final List<String> expectedPublicOf) {
            this.subjectClass = subjectClass;
            this.expectedInherentOf = new TreeSet<>(expectedInherentOf);
            this.expectedPublicOf = new TreeSet<>(expectedPublicOf);
        }
    }

    @SuppressWarnings("unused")
    private static class PrivateClass {

        private static void privateStaticMethod() {
            throw new UnsupportedOperationException("not yet implemented");
        }

        static void packageStaticMethod() {
            throw new UnsupportedOperationException("not yet implemented");
        }

        protected static void protectedStaticMethod() {
            throw new UnsupportedOperationException("not yet implemented");
        }

        public static void publicStaticMethod() {
            throw new UnsupportedOperationException("not yet implemented");
        }

        private void privateMethod() {
            throw new UnsupportedOperationException("not yet implemented");
        }

        void packageMethod() {
            throw new UnsupportedOperationException("not yet implemented");
        }

        protected void protectedMethod() {
            throw new UnsupportedOperationException("not yet implemented");
        }

        public void publicMethod() {
            throw new UnsupportedOperationException("not yet implemented");
        }
    }

    @SuppressWarnings("unused")
    static class PackageClass {

        private static void privateStaticMethod() {
            throw new UnsupportedOperationException("not yet implemented");
        }

        static void packageStaticMethod() {
            throw new UnsupportedOperationException("not yet implemented");
        }

        protected static void protectedStaticMethod() {
            throw new UnsupportedOperationException("not yet implemented");
        }

        public static void publicStaticMethod() {
            throw new UnsupportedOperationException("not yet implemented");
        }

        private void privateMethod() {
            throw new UnsupportedOperationException("not yet implemented");
        }

        void packageMethod() {
            throw new UnsupportedOperationException("not yet implemented");
        }

        protected void protectedMethod() {
            throw new UnsupportedOperationException("not yet implemented");
        }

        public void publicMethod() {
            throw new UnsupportedOperationException("not yet implemented");
        }
    }
}
