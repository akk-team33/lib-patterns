package de.team33.patterns.reflect.pandora;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.stream.Stream;

public class Properties<T> {

    public static <T> Properties<T> of(final Class<T> subjectClass) {
        Stream.of(subjectClass.getMethods())
              .filter(Util::isNoObjectMethod)
              .filter(method -> 0 == method.getParameterCount())
              .map(Primary::new)
              .filter(method -> method.prefix.isGetter);
        throw new UnsupportedOperationException("not yet implemented");
    }

    public final Map<String, Object> toMap(final T subject) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    private static class Primary {

        final Prefix prefix;
        final Method method;

        Primary(final Method method) {
            this.prefix = Prefix.of(method.getName());
            this.method = method;
        }
    }
}
