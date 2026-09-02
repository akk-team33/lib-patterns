package de.team33.patterns.typing.proteus;

import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
record TypeRange(List<TypeSupport> upperBounds, List<TypeSupport> lowerBounds) {

    private static final TypeSupport OBJECT_SUPPORT = ClassCase.support(Object.class);
    private static final List<TypeSupport> MAX_BOUNDS = List.of(OBJECT_SUPPORT);

    static TypeRange by(final WildcardType type, final TypeSupport context) {
        final List<TypeSupport> upperBounds = Stream.of(type.getUpperBounds())
                                                    .map(bound -> TypeCase.support(bound, context))
                                                    .toList();
        final List<TypeSupport> lowerBounds = Stream.of(type.getLowerBounds())
                                                    .map(bound -> TypeCase.support(bound, context))
                                                    .toList();
        return new TypeRange(upperBounds, lowerBounds);
    }

    private static String toString(final List<? extends TypeSupport> bounds) {
        return bounds.stream()
                     .map(TypeSupport::toString)
                     .collect(Collectors.joining("|"));
    }

    @Override
    public String toString() {
        if (lowerBounds.isEmpty()) {
            if (upperBounds.equals(MAX_BOUNDS)) {
                return "?";
            } else if (0 < upperBounds.size()) {
                return "? extends %s".formatted(toString(upperBounds));
            }
        } else if (upperBounds.equals(MAX_BOUNDS)) {
            return "? super %s".formatted(toString(lowerBounds));
        }
        throw new IllegalStateException(("illegal state:%n" +
                                         "   upperBounds: %s%n" +
                                         "   lowerBounds: %s%n").formatted(upperBounds, lowerBounds));
    }
}
