package de.team33.patterns.beans.hermes;

import de.team33.patterns.typing.proteus.Type;

import java.util.Map;

/**
 * A utility that provides conversions between JavaBeans and {@linkplain Map Map} representations.
 */
public final class Hermes {

    private Hermes() {}

    /**
     * Returns an immutable {@link Map} describing the given <em>beanClass</em>.
     * <p>
     * Each entry of the {@link Map} describes a single bean property as follows:
     * <table>
     *     <caption></caption>
     *     <tr>
     *         <th>key</th>
     *         <th>value</th>
     *     </tr>
     *     <tr>
     *         <td>{@link String}</td>
     *         <td>{@link Class}{@code <?>}</td>
     *     </tr>
     *     <tr>
     *         <td>The name of the bean property</td>
     *         <td>The type of the bean property</td>
     *     </tr>
     * </table>
     */
    public static <T> Map<String, Class<?>> description(final Class<T> beanClass) {
        return Reflector.of(beanClass).description();
    }

    /**
     * Returns an immutable {@link Map} describing the given <em>beanType</em>.
     * <p>
     * Each entry of the {@link Map} describes a single bean property as follows:
     * <table>
     *     <caption></caption>
     *     <tr>
     *         <th>key</th>
     *         <th>value</th>
     *     </tr>
     *     <tr>
     *         <td>{@link String}</td>
     *         <td>{@link Type}{@code <?>}</td>
     *     </tr>
     *     <tr>
     *         <td>The name of the bean property</td>
     *         <td>The type of the bean property</td>
     *     </tr>
     * </table>
     */
    public static <T> Map<String, Type<?>> description(final Type<T> beanType) {
        return Refractor.of(beanType).description();
    }

    /**
     * Returns a {@link Map} containing the property values of the given <em>bean</em>.
     * <p>
     * The returned {@link Map} is independent of the supplied bean instance and is mutable.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map<String, Object> toMap(final Object bean) {
        return Reflector.of((Class) bean.getClass())
                        .toMap(bean);
    }

    /**
     * Returns a new instance of the given <em>beanClass</em>, mapped from the given <em>source</em>.
     * <p>
     * Missing property values are treated as {@code null}. This may fail for primitive bean properties.
     * Source values for unknown bean properties are ignored.
     *
     * @param <T> The bean type.
     */
    public static <T> T toBean(final Class<T> beanClass, final Map<String, Object> source) {
        return Reflector.of(beanClass)
                        .toBean(source);
    }

    /**
     * Returns a new instance of the given <em>beanType</em>, mapped from the given <em>source</em>.
     * <p>
     * Missing property values are treated as {@code null}. This may fail for primitive bean properties.
     * Source values for unknown bean properties are ignored.
     *
     * @param <T> The bean type.
     */
    public static <T> T toBean(final Type<T> beanType, final Map<String, Object> source) {
        return Refractor.of(beanType)
                        .toBean(source);
    }
}
