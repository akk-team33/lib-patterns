package de.team33.patterns.reflect.beta;

import de.team33.patterns.exceptional.e1.Converter;
import de.team33.patterns.exceptional.e1.XBiFunction;
import de.team33.patterns.exceptional.e1.XConsumer;
import de.team33.patterns.exceptional.e1.XFunction;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Fields<T> {

    private static final int SYNTHETIC = 0x00001000;
    private static final int NOT_SIGNIFICANT = Modifier.STATIC | Modifier.TRANSIENT | SYNTHETIC;
    private static final Converter CNV = Converter.using(AccessException::new);

    private final List<Field> fields;
    private final XBiFunction<T, Field, Object, IllegalAccessException> read;

    @SuppressWarnings("BoundedWildcard")
    private Fields(final Class<T> subjectClass,
                   final XBiFunction<T, Field, Object, IllegalAccessException> read) {
        this.fields = Stream.of(subjectClass.getDeclaredFields())
                            .filter(Fields::isSignificant)
                            .collect(Collectors.toList());
        this.read = read;
    }

    private static boolean isSignificant(final Member field) {
        return isSignificant(field.getModifiers());
    }

    private static boolean isSignificant(final int modifiers) {
        return 0 == (modifiers & NOT_SIGNIFICANT);
    }

    public static <T> Fields<T> of(final Class<T> subjectClass,
                                   final XBiFunction<T, Field, Object, IllegalAccessException> read) {
        return new Fields<>(subjectClass, read);
    }

    public final void forEach(final XConsumer<? super Field, IllegalAccessException> consumer) {
        fields.forEach(CNV.consumer(consumer));
    }

    @SuppressWarnings("BoundedWildcard")
    public final <R> Stream<R> map(final XFunction<? super Field, R, IllegalAccessException> function) {
        return fields.stream().map(CNV.function(function));
    }

    public final List<Object> toList(final T source) {
        return fields.stream()
                     .map(CNV.function(field -> read.apply(source, field)))
                     .collect(Collectors.toList());
    }

    public final Map<String, Object> toMap(final T source) {
        return fields.stream()
                     .collect(Collectors.toMap(Field::getName,
                                               CNV.function(field -> read.apply(source, field))));
    }

    public static class AccessException extends IllegalStateException {

        private AccessException(final Throwable cause) {
            super(cause.getMessage(), cause);
        }
    }
}
