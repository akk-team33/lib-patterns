package de.team33.patterns.production.narvi;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Collections.emptyMap;

public class Charger {

    @SuppressWarnings("rawtypes")
    private final Map<Class, Supplier> classMethodMap;
    @SuppressWarnings("rawtypes")
    private final Map<Object, Supplier> templateMethodMap;

    private Charger(final Builder builder) {
        classMethodMap = new HashMap<>(builder.classMethodMap);
        templateMethodMap = new HashMap<>(builder.templateMethodMap);
    }

    public static Builder builder() {
        return new Builder(emptyMap(), emptyMap());
    }

    public final <T> T get(final Class<T> type) {
        return getMethod(type).get();
    }

    public final <T> T get(final T template) {
        return getMethod(template).get();
    }

    private <T> Supplier<T> getMethod(final T template) {
        //noinspection unchecked
        return Optional.ofNullable(templateMethodMap.get(template))
                       .orElseThrow(() -> new NoSuchElementException(
                               "no method found for template <" + template + ">"));
    }

    private <T> Supplier<T> getMethod(final Class<T> type) {
        //noinspection unchecked
        return Optional.ofNullable(classMethodMap.get(type))
                       .orElseThrow(() -> new NoSuchElementException("no method found for " + type));
    }

    /**
     * Like {@link #initBean(Object, Object)}, where the instance to be initialized is also used as a template.
     *
     * @param bean The instance to be initialized.
     * @param <B>  The type of the instance to be initialized.
     * @return The given instance after it has been initialized.
     */
    public final <B> B initBean(final B bean) {
        return initBean(bean, bean);
    }

    /**
     * Initializes a given bean instance. It is assumed that the bean instance is made up of properties
     * that can be assigned values using appropriate setters*.
     * <p>
     * To determine how each property of the given bean should be initialized, an attempt is made to find an
     * appropriate getter** in the given template. If no suitable getter** is found or the value from the template
     * determined using the getter** corresponds to the default value*** (NULL, zero, false etc.),
     * the corresponding property is initialized solely on the basis of its type.
     * Otherwise, the value of the property is initialized based on the found template value
     * (see also {@link #get(Class)} and {@link #get(Object)}).
     * <p>
     * *<em>Setter:</em> a non-static method that takes exactly one parameter, has no result type (void)
     * or the type of the instance in question itself (builder pattern), and whose name starts with "set". Examples:
     * <pre>
     * public final void setProperty(final String value) {
     *     this.property = value;
     * }
     * </pre>
     * or
     * <pre>
     * public final Builder setProperty(final String value) {
     *     this.property = value;
     *     return this;
     * }
     * </pre>
     * <p>
     * **<em>Getter:</em> a non-static method that takes no parameters, has a specific result type (non-void),
     * and whose name starts with "get" or "is". Examples:
     * <pre>
     * public final String getProperty() {
     *     return this.property;
     * }
     * </pre>
     * or
     * <pre>
     * public final boolean isBooleanProperty() {
     *     return this.booleanProperty;
     * }
     * </pre>
     * <p>
     * ***<em>Default value:</em> The value assigned to an instance field when not explicitly initialized.
     * These are, for example, {@code zero} for fields of type {@code int}, {@code false} for fields of type
     * {@code boolean} or {@code null} for fields of any object type.
     *
     * @param bean     The bean instance to be initialized.
     * @param template A template. May or may not be of the same type as the bean to be initialized.
     *                 Can even be identical to the bean to be initialized.
     * @param <B>      The type of the instance to be initialized.
     * @return The given bean instance after it has been initialized.
     */
    public final <B> B initBean(final B bean, final Object template) {
        for (final Beans.Property property : Beans.getProperties(bean.getClass())) {
            final Object propertyTemplate = property.getterOf(template.getClass()).apply(template);
            final Implicit implicit = Implicit.of(property.type);
            final Object value = implicit.is(propertyTemplate) ? get(property.type) : get(propertyTemplate);
            property.setter.accept(bean, value);
        }
        return bean;
    }

    public final Builder toBuilder() {
        return new Builder(classMethodMap, templateMethodMap);
    }

    public static class Builder {

        @SuppressWarnings("rawtypes")
        private final Map<Class, Supplier> classMethodMap;
        @SuppressWarnings("rawtypes")
        private final Map<Object, Supplier> templateMethodMap;

        @SuppressWarnings("rawtypes")
        public Builder(final Map<Class, Supplier> classMethodMap, final Map<Object, Supplier> templateMethodMap) {
            this.classMethodMap = new HashMap<>(classMethodMap);
            this.templateMethodMap = new HashMap<>(templateMethodMap);
        }

        public final Charger build() {
            return new Charger(this);
        }

        public final <T> Function<Supplier<? extends T>, Builder> on(final Class<T> type) {
            return method -> {
                classMethodMap.put(type, method);
                return this;
            };
        }

        public final <T> Function<Supplier<? extends T>, Builder> on(final T template) {
            return method -> {
                templateMethodMap.put(template, method);
                return this;
            };
        }
    }
}
