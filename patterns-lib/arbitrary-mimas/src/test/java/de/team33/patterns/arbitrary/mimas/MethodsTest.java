package de.team33.patterns.arbitrary.mimas;

import de.team33.patterns.arbitrary.mimas.sample.Buildable;
import de.team33.patterns.arbitrary.mimas.sample.Generic;
import de.team33.patterns.arbitrary.mimas.sample.Record;
import de.team33.patterns.arbitrary.mimas.sample.Sample;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MethodsTest {

    @ParameterizedTest
    @EnumSource(Case.class)
    final void publicSetters(final Case c) {
        final Set<String> expected = c.setters;
        final Set<String> result = Methods.publicSetters(c.subject)
                                          .map(Method::toGenericString)
                                          .map(s -> s.replace(c.subject.getName(), c.subject.getSimpleName()))
                                          .collect(Collectors.toCollection(TreeSet::new));
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @EnumSource(Case.class)
    final void publicGetters(final Case c) {
        final Set<String> expected = c.getters;
        final Set<String> result = Methods.publicGetters(c.subject)
                                          .map(Method::toGenericString)
                                          .map(s -> s.replace(c.subject.getName(), c.subject.getSimpleName()))
                                          .collect(Collectors.toCollection(TreeSet::new));
        assertEquals(expected, result);
    }

    enum Case {

        SAMPLE(Sample.class, List.of(
                "public final Sample Sample.setBooleanValue(boolean)",
                "public final Sample Sample.setIntValue(int)",
                "public final Sample Sample.setLongList(java.util.List<java.lang.Long>)",
                "public final Sample Sample.setLongValue(java.lang.Long)",
                "public final Sample Sample.setStringList(java.util.List<java.lang.String>)",
                "public final Sample Sample.setStringValue(java.lang.String)"
        ), List.of(
                "public final boolean Sample.isBooleanValue()",
                "public final int Sample.getIntValue()",
                "public final java.lang.Long Sample.getLongValue()",
                "public final java.lang.String Sample.getStringValue()",
                "public final java.util.List<java.lang.Long> Sample.getLongList()",
                "public final java.util.List<java.lang.String> Sample.getStringList()"
        )),

        BUILDABLE(Buildable.class, emptyList(), List.of(
                "public final int Buildable.getIntValue()",
                "public final java.lang.Long Buildable.getLongValue()",
                "public final java.lang.String Buildable.getStringValue()",
                "public final java.util.List<java.lang.Long> Buildable.getLongList()",
                "public final java.util.List<java.lang.String> Buildable.getStringList()"
        )),

        BUILDABLE_BUILDER(Buildable.Builder.class, List.of(
                "public final Builder Builder.setIntValue(int)",
                "public final Builder Builder.setLongList(java.util.List<java.lang.Long>)",
                "public final Builder Builder.setLongValue(java.lang.Long)",
                "public final Builder Builder.setStringList(java.util.List<java.lang.String>)",
                "public final Builder Builder.setStringValue(java.lang.String)"
        ), singletonList(
                "public final de.team33.patterns.arbitrary.mimas.sample.Buildable Builder.build()"
        )),

        GENERIC(Generic.class, singletonList(
                "public final Generic<T> Generic.setTValue(T)"
                                            ), singletonList(
                "public final T Generic.getTValue()"
        )),

        RECORD(Record.class, emptyList(), List.of(
                "public final boolean Record.booleanValue()",
                "public final java.util.List<java.lang.Object> Record.toList()",
                "public final int Record.intValue()",
                "public final java.lang.Long Record.longValue()",
                "public final java.lang.String Record.stringValue()",
                "public final java.util.List<java.lang.Long> Record.longList()",
                "public final java.util.List<java.lang.String> Record.stringList()"
        ));

        private final Class<?> subject;
        private final Set<String> setters;
        private final Set<String> getters;

        Case(final Class<?> subject, final List<String> setters, final List<String> getters) {
            this.subject = subject;
            this.setters = new TreeSet<>(setters);
            this.getters = new TreeSet<>(getters);
        }
    }
}
