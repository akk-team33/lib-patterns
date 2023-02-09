package de.team33.patterns.reflect.luna;

import de.team33.patterns.exceptional.e1.Converter;
import de.team33.patterns.exceptional.e1.Wrapping;
import de.team33.patterns.exceptional.e1.XConsumer;
import de.team33.patterns.exceptional.e1.XFunction;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Fields {

    private static final int SYNTHETIC = 0x00001000;
    private static final int NOT_SIGNIFICANT = Modifier.STATIC | Modifier.TRANSIENT | SYNTHETIC;
    private static final Converter CNV = Converter.using(Wrapping.method(IllegalStateException::new));

    private final List<Field> fields;

    private Fields(final Class<?> subjectClass) {
        this.fields = Stream.of(subjectClass.getDeclaredFields())
                            .filter(Fields::isSignificant)
                            .collect(Collectors.toList());
    }

    private static boolean isSignificant(final Member field) {
        return isSignificant(field.getModifiers());
    }

    private static boolean isSignificant(final int modifiers) {
        return 0 == (modifiers & NOT_SIGNIFICANT);
    }

    public static Fields of(final Class<?> subjectClass) {
        return new Fields(subjectClass);
    }

    public final void forEach(final XConsumer<? super Field, IllegalAccessException> consumer) {
        fields.forEach(CNV.consumer(consumer));
    }

    public final <R> Stream<R> map(final XFunction<? super Field, R, IllegalAccessException> function) {
        return fields.stream().map(CNV.function(function));
    }

    public final <R> List<R> mapToList(final XFunction<? super Field, R, IllegalAccessException> function) {
        return map(function).collect(Collectors.toList());
    }

    public final <R> Map<String, R> mapToMap(final XFunction<? super Field, R, IllegalAccessException> function) {
        return fields.stream()
                     .collect(Collectors.toMap(Field::getName, CNV.function(function)));
    }
}
