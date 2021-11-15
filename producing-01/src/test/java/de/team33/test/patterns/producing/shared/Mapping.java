package de.team33.test.patterns.producing.shared;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Mapping<T> {

    private final Map<String, Field> fieldMap;

    Mapping(final Stream<Field> fields) {
        this.fieldMap = fields.collect(Collectors.toMap(Field::getName, field -> field));
    }

    final T remap(final Map<String, ?> origin, final T target) {
        fieldMap.forEach((name, field) -> {
            try {
                field.set(target, origin.get(name));
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        });
        return target;
    }

    final Map<String, Object> map(final T origin, final Map<String, Object> target) {
        fieldMap.forEach((name, field) -> {
            try {
                target.put(name, field.get(origin));
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        });
        return target;
    }
}
