package de.team33.patterns.json.charon;

import de.team33.patterns.records.triton.RenderOption;

import java.util.Optional;
import java.util.Set;

public class Json {

    public static final Value NULL = JsonNull.INSTANCE;

    public static String render(final Value value, final Set<RenderOption> options) {
        return new Renderer(options).add(value, 0)
                                    .toString();
    }

    public static Value parse(final String source) {
        return new Parser(source).parseRoot();
    }

    /**
     * Identifies the type of a JSON value.
     */
    public enum Type {

        /**
         * Identifies a JSON null.
         */
        NULL,

        /**
         * Identifies a JSON boolean.
         */
        BOOLEAN,

        /**
         * Identifies a JSON number.
         */
        NUMBER,

        /**
         * Identifies a JSON string.
         */
        STRING,

        /**
         * Identifies a JSON array.
         */
        ARRAY,

        /**
         * Identifies a JSON object.
         */
        OBJECT;
    }

    public interface Value {

        /**
         * Determines whether <em>this</em> is an instance of the given <em>type</em>.
         */
        default boolean is(final Class<? extends Value> type) {
            return type.isInstance(this);
        }

        /**
         * Returns <em>this</em> as an instance of the given <em>type</em>,
         * provided that it is indeed an instance of that type.
         *
         * @param <T> The expected type.
         * @throws ClassCastException if <em>this</em> is not an instance of the given <em>type</em>.
         * @see #is(Class)
         */
        default <T extends Value> T as(final Class<T> type) {
            return type.cast(this);
        }

        /**
         * Returns <em>this</em> as an {@linkplain Optional optional} instance of the given <em>type</em>.
         * <p>
         * Returns {@link Optional#empty()} if <em>this</em> is not an instance of the given <em>type</em>.
         *
         * @param <T> The expected type.
         */
        @SuppressWarnings("BoundedWildcard")
        default <T extends Value> Optional<T> asOptional(final Class<T> type) {
            return Optional.of(this)
                           .filter(type::isInstance)
                           .map(type::cast);
        }

        /**
         * Returns the {@link Type} of <em>this</em> value.
         */
        Type type();
    }
}
