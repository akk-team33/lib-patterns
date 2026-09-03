package de.team33.patterns.io.thalassa.sample;

@SuppressWarnings("WeakerAccess")
public record Sample(String string) {

    public static Sample parse(final String string) {
        return new Sample(string);
    }
}
