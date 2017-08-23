package net.team33.patterns;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class TextTemplate {

    private static final Comparator<PlaceHolder> PLACE_HOLDER_SYMBOL_ORDER =
            Comparator.comparing(subject -> subject.symbol);
    private static final Comparator<PlaceHolder> PLACE_HOLDER_LIMIT_ORDER = (left, right) -> {
        final int result = Integer.compare(right.limit, left.limit);
        return (0 == result) ? PLACE_HOLDER_SYMBOL_ORDER.compare(left, right) : result;
    };
    private static final Comparator<PlaceHolder> PLACE_HOLDER_ORDER = (left, right) -> {
        final int result = Integer.compare(left.start, right.start);
        return (0 == result) ? PLACE_HOLDER_LIMIT_ORDER.compare(left, right) : result;
    };
    private static final Supplier<Set<PlaceHolder>> NEW_PLACE_HOLDERS =
            () -> new TreeSet<>(PLACE_HOLDER_ORDER);

    private final String template;

    public TextTemplate(final String template) {
        this.template = template;
    }

    public final String resolve(final Map<String, ?> data) {
        return fragments(data)
                .map(fragment -> fragment.resolve(data))
                .collect(Collectors.joining());
    }

    private Stream<Fragment> fragments(final Map<String, ?> data) {
        return placeHolders(data)
                .collect(Fragments::new, Fragments::add, Fragments::addAll)
                .stream();
    }

    private Stream<PlaceHolder> placeHolders(final Map<String, ?> data) {
        return data.keySet().stream()
                .map(this::symbolPlaceHolders)
                .collect(NEW_PLACE_HOLDERS, Set::addAll, Set::addAll)
                .stream();
    }

    private Collection<PlaceHolder> symbolPlaceHolders(final String symbol) {
        final Collection<PlaceHolder> result = new LinkedList<>();
        int start = template.indexOf(symbol);
        while (0 <= start) {
            final int limit = start + symbol.length();
            result.add(new PlaceHolder(symbol, start, limit));
            start = template.indexOf(symbol, limit);
        }
        return result;
    }

    @FunctionalInterface
    private interface Fragment {
        String resolve(Map<String, ?> data);
    }

    private static class PlaceHolder {
        private final String symbol;
        private final int start;
        private final int limit;

        private PlaceHolder(final String symbol, final int start, final int limit) {
            assert 0 <= start;
            assert start <= limit;
            this.symbol = symbol;
            this.start = start;
            this.limit = limit;
        }
    }

    private class Fragments {
        private final Collection<Fragment> result = new LinkedList<>();
        private int last = 0;

        private void add(final PlaceHolder placeHolder) {
            if (placeHolder.start >= last) {
                final String prefix = template.substring(last, placeHolder.start);
                result.add(map -> prefix);
                result.add(map -> map.get(placeHolder.symbol).toString());
                last = placeHolder.limit;
            }
        }

        private void addAll(final Fragments other) {
            throw new UnsupportedOperationException("This method is not expected to be called at all");
        }

        private Stream<Fragment> stream() {
            final String postfix = template.substring(last);
            result.add(map -> postfix);
            return result.stream();
        }
    }
}
