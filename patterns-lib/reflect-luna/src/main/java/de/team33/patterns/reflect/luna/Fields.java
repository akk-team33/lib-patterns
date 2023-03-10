package de.team33.patterns.reflect.luna;

import de.team33.patterns.exceptional.e1.Converter;
import de.team33.patterns.exceptional.e1.XConsumer;
import de.team33.patterns.exceptional.e1.XFunction;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents an aggregate of {@link Field}s of a specific class and allows elementary operations over all these
 * {@link Field}s, with any {@link IllegalAccessException}s that may occur being encapsulated in unchecked exceptions.
 */
public class Fields {

    private static final int SYNTHETIC = 0x00001000;
    private static final int NOT_SIGNIFICANT = Modifier.STATIC | Modifier.TRANSIENT | SYNTHETIC;
    private static final Converter CNV = Converter.using(AccessException::new);

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

    /**
     * Returns a new instance that includes all significant* {@link Field}s declared by the given class.
     * <p>
     * *Significant fields are those fields that are not declared <em>static</em>, <em>transient</em>,
     * or <em>synthetic</em>**.
     * These are typically the fields that make up the "value" of a data object and as such should be considered when
     * implementing {@code equals()}, {@code hashCode()} and {@code toString()}.
     * <p>
     * **A synthetically declared field, for example, appears in a non-static inner class,
     * namely a reference to the instance of the outer class.
     */
    public static Fields of(final Class<?> subjectClass) {
        return new Fields(subjectClass);
    }

    /**
     * Performs a given operation on all contained fields.
     * If the operation causes an {@link IllegalAccessException}, it's caught, wrapped as an unchecked
     * {@link AccessException}, and rethrown.
     * <p>
     * In particular, this can be used to implement a copy operation or a copy constructor of a data object class,
     * example:
     * <pre>
     * public class DataObject {
     *
     *     private static final Fields FIELDS = Fields.of(DataObject.class);
     *
     *     // declare some instance fields ...
     *
     *     public DataObject(final DataObject source) {
     *         FIELDS.forEach(field -&gt; field.set(this, field.get(source)));
     *     }
     *
     *     // ...
     * }
     * </pre>
     * <p>
     * Note: Access to the value of a field using {@link Field#get(Object)} and {@link Field#set(Object, Object)}
     * is also possible without any problems for non-public fields if the accessing code resides in the context
     * of the field's declaring class.
     *
     * @throws AccessException If an {@link IllegalAccessException} is thrown during the given operation.
     */
    public final void forEach(final XConsumer<? super Field, IllegalAccessException> operation)
            throws AccessException {
        fields.forEach(CNV.consumer(operation));
    }


    /**
     * Returns a {@link Stream} of the results of a given mapping operation performed on each contained field.
     * If the operation throws an {@link IllegalAccessException}, it's caught, wrapped as an {@link AccessException}
     * (an unchecked exception), and rethrown.
     *
     * @throws AccessException If an {@link IllegalAccessException} is thrown during the operation.
     */
    @SuppressWarnings("BoundedWildcard")
    public final <R> Stream<R> map(final XFunction<? super Field, R, IllegalAccessException> operation)
            throws AccessException {
        return fields.stream().map(CNV.function(operation));
    }

    public static class AccessException extends IllegalStateException {

        private AccessException(final Throwable cause) {
            super(cause.getMessage(), cause);
        }
    }
}
