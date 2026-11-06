package de.team33.patterns.typing.proteus;

import de.team33.patterns.lazy.janus.Features;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

abstract class TypeSupport {

    private final Features features = new Features();

    final Features features() {
        return features;
    }

    final TypeSupport actualParameter(final String name) {
        final List<String> formalParameters = formalParameters();
        return Optional.of(formalParameters.indexOf(name))
                       .filter(index -> 0 <= index)
                       .map(index -> actualParameterByIndex(name, index))
                       .orElseThrow(() -> new IllegalArgumentException(
                               String.format("formal parameter <%s> not found in %s", name, formalParameters)));
    }

    final TypeSupport memberType(final Type type) {
        return TypeCase.support(type, this);
    }

    final Optional<TypeSupport> superType() {
        return features.get(Key.SUPER_TYPE,
                            () -> Optional.ofNullable(core().getGenericSuperclass())
                                          .map(this::memberType));
    }

    final List<TypeSupport> interfaces() {
        return features.get(Key.INTERFACES,
                            () -> Stream.of(core().getGenericInterfaces())
                                        .map(this::memberType)
                                        .toList());
    }

    final List<TypeSupport> superTypes() {
        return features.get(Key.SUPER_TYPES,
                            () -> Stream.concat(superType().stream(), interfaces().stream())
                                        .toList());
    }

    final List<TypeSupport> typeHierarchy() {
        return features.get(Key.TYPE_HIERARCHY, () -> typeHierarchyStream().distinct().toList());
    }

    private Stream<TypeSupport> typeHierarchyStream() {
        final Stream<TypeSupport> head = Stream.of(this);
        if (superTypes().isEmpty()) {
            return head;
        } else {
            return Stream.concat(head, superTypes().stream().flatMap(TypeSupport::typeHierarchyStream));
        }
    }

    private TypeSupport actualParameterByIndex(final String name, final int index) {
        final List<TypeSupport> actualParameters = actualParameters();
        if (index < actualParameters.size()) {
            return actualParameters.get(index);
        } else {
            throw new IllegalStateException(
                    String.format("actual parameter for <%s> not found in %s", name, actualParameters));
        }
    }

    abstract Class<?> core();

    abstract List<String> formalParameters();

    abstract List<TypeSupport> actualParameters();

    abstract boolean isAssignableFrom(final TypeSupport other);

    @Override
    public abstract boolean equals(final Object obj);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();

    final boolean isRaw() {
        return (0 < formalParameters().size()) && actualParameters().isEmpty();
    }

    interface Key<T> extends Features.Key<T> {

        Key<List<Object>> TO_LIST = named("TO_LIST");
        Key<Integer> HASH_CODE = named("HASH_CODE");
        Key<String> TO_STRING = named("TO_STRING");
        Key<List<String>> FORMAL_PARAMETERS = named("FORMAL_PARAMETERS");
        Key<List<TypeSupport>> ACTUAL_PARAMETERS = named("ACTUAL_PARAMETERS");
        Key<Optional<TypeSupport>> SUPER_TYPE = named("SUPER_TYPE");
        Key<List<TypeSupport>> INTERFACES = named("INTERFACES");
        Key<List<TypeSupport>> SUPER_TYPES = named("SUPER_TYPES");
        Key<List<TypeSupport>> TYPE_HIERARCHY = named("TYPE_HIERARCHY");

        static <T> Key<T> named(final String name) {
            return new Key<T>() {
                @Override
                public String toString() {
                    return name;
                }
            };
        }
    }
}
