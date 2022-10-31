package de.team33.patterns.testing.titan;

@FunctionalInterface
@SuppressWarnings("ProhibitedExceptionDeclared")
public interface Operation<R> {

    R operate(Indices indices) throws Exception;
}
