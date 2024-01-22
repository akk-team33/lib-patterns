package de.team33.patterns.normal.iocaste;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Normal {

    public static final Comparator<Normal> ORDER = Comparator.comparing(Normal::type)
                                                             .thenComparing(Normal::compareSameType);

    private static int compareSameType(final Normal left, final Normal right) {
        return left.type().order.compare(left, right);
    }

    public static Normal of(final CharSequence value) {
        return new Simple(value);
    }

    public static Normal of(final Collection<? extends Normal> value) {
        return new Aggregate(value);
    }

    public static Normal of(final Map<? extends Normal, ? extends Normal> value) {
        return new Composite(value);
    }

    private static String indent(final int i) {
        return Stream.generate(() -> "    ").limit(i).collect(Collectors.joining());
    }

    private static String newLine(final int indent) {
        return String.format(",%n%s", indent(indent));
    }

    public abstract Type type();

    public final boolean isSimple() {
        return Type.SIMPLE == type();
    }

    public final boolean isAggregate() {
        return Type.AGGREGATE == type();
    }

    public final boolean isComposite() {
        return Type.COMPOSITE == type();
    }

    public abstract String asSimple();

    public abstract Collection<Normal> asAggregate();

    public abstract Map<Normal, Normal> asComposite();

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof Normal) && equals((Normal) obj));
    }

    private boolean equals(final Normal obj) {
        return type().primary.apply(this).equals(obj.type().primary.apply(obj));
    }

    @Override
    public final int hashCode() {
        return type().primary.apply(this).hashCode();
    }

    @Override
    public final String toString() {
        return toString(0);
    }

    abstract String toString(final int indent);

    public enum Type {

        SIMPLE(Normal::asSimple, Simple.ORDER),
        AGGREGATE(Normal::asAggregate, Aggregate.ORDER),
        COMPOSITE(Normal::asComposite, Composite.ORDER);

        private final Function<Normal, Object> primary;
        private final Comparator<Normal> order;

        Type(final Function<Normal, Object> primary, final Comparator<Normal> order) {
            this.primary = primary;
            this.order = order;
        }
    }

    private static class Composite extends Base {

        private static final Comparator<Normal> ORDER = Comparator.comparing(normal -> normal.asComposite()
                                                                                             .entrySet()
                                                                                             .iterator(),
                                                                             Composite::compare);

        private static int compare(final Iterator<Map.Entry<Normal, Normal>> left,
                                   final Iterator<Map.Entry<Normal, Normal>> right) {
            int result = 0;
            while ((result == 0) && left.hasNext() && right.hasNext()) {
                result = Normal.ORDER.compare(left.next().getKey(), right.next().getKey());
            }
            if (0 != result) {
                return result;
            } else if (left.hasNext()) {
                return 1;
            } else if (right.hasNext()) {
                return -1;
            } else {
                return 0;
            }
        }

        private final Map<Normal, Normal> value;

        private Composite(final Map<? extends Normal, ? extends Normal> value) {
            final Map<Normal, Normal> stage = new TreeMap<>(Normal.ORDER);
            stage.putAll(value);
            this.value = Collections.unmodifiableMap(stage);
        }

        @Override
        public final Type type() {
            return Type.COMPOSITE;
        }

        @Override
        public final Map<Normal, Normal> asComposite() {
            //noinspection AssignmentOrReturnOfFieldWithMutableType
            return value;
        }

        @Override
        final String toString(final int indent) {
            if (value.isEmpty()) {
                return "{}";
            } else {
                final int nextIndent = indent + 1;
                final String nextLine = newLine(nextIndent);
                return value.entrySet()
                            .stream()
                            .map(normal -> String.join("",
                                                       normal.getKey().toString(nextIndent),
                                                       " -> ",
                                                       normal.getValue().toString(nextIndent)))
                            .collect(Collectors.joining(nextLine, "{" + nextLine, newLine(indent) + "}"));
            }
        }
    }

    private static class Aggregate extends Base {

        private static final Comparator<Normal> ORDER = Comparator.comparing(normal -> normal.asAggregate().iterator(),
                                                                             Aggregate::compare);

        private static int compare(final Iterator<Normal> left, final Iterator<Normal> right) {
            int result = 0;
            while ((result == 0) && left.hasNext() && right.hasNext()) {
                result = Normal.ORDER.compare(left.next(), right.next());
            }
            if (0 != result) {
                return result;
            } else if (left.hasNext()) {
                return 1;
            } else if (right.hasNext()) {
                return -1;
            } else {
                return 0;
            }
        }

        private final Collection<Normal> value;

        private Aggregate(final Collection<? extends Normal> value) {
            this.value = (value instanceof Set<?>) ? newSet(value) : newList(value);
        }

        private static List<Normal> newList(final Collection<? extends Normal> value) {
            return Collections.unmodifiableList(new ArrayList<>(value));
        }

        private static Set<Normal> newSet(final Collection<? extends Normal> value) {
            final Set<Normal> result = new TreeSet<Normal>(Normal.ORDER);
            result.addAll(value);
            return Collections.unmodifiableSet(result);
        }

        @Override
        public final Type type() {
            return Type.AGGREGATE;
        }

        @Override
        public final Collection<Normal> asAggregate() {
            //noinspection AssignmentOrReturnOfFieldWithMutableType
            return value;
        }

        @Override
        final String toString(final int indent) {
            if (value.isEmpty()) {
                return "[]";
            } else {
                final int nextIndent = indent + 1;
                final String nextLine = newLine(nextIndent);
                return value.stream()
                            .map(normal -> normal.toString(nextIndent))
                            .collect(Collectors.joining(nextLine, "[" + nextLine, newLine(indent) + "]"));
            }
        }
    }

    private static class Simple extends Base {

        private static final Comparator<Normal> ORDER = Comparator.comparing(Normal::asSimple);

        private static final String QUOTE = "\"";
        private static final String DOUBLE_QUOTE = "\"\"";
        private static final Pattern P_RPLC_QUOTE = Pattern.compile(QUOTE, Pattern.LITERAL);
        private static final Pattern P_TO_BE_QUOTED = Pattern.compile("[^\\s\"]*[\\s\"].*");

        private final String value;

        private Simple(final CharSequence value) {
            this.value = value.toString();
        }

        @Override
        public final Type type() {
            return Type.SIMPLE;
        }

        @Override
        public final String asSimple() {
            return value;
        }

        @Override
        final String toString(final int indent) {
            if (value.isEmpty() || P_TO_BE_QUOTED.matcher(value).matches()) {
                return String.join("", QUOTE, P_RPLC_QUOTE.matcher(value).replaceAll(DOUBLE_QUOTE), QUOTE);
            } else {
                return value;
            }
        }
    }

    private abstract static class Base extends Normal {

        @SuppressWarnings("DesignForExtension")
        @Override
        public String asSimple() {
            throw new UnsupportedOperationException("<this> is expected to be SIMPLE - but was " + type());
        }

        @SuppressWarnings("DesignForExtension")
        @Override
        public Collection<Normal> asAggregate() {
            throw new UnsupportedOperationException("<this> is expected to be AGGREGATE - but was " + type());
        }

        @SuppressWarnings("DesignForExtension")
        @Override
        public Map<Normal, Normal> asComposite() {
            throw new UnsupportedOperationException("<this> is expected to be COMPOSITE - but was " + type());
        }
    }
}
