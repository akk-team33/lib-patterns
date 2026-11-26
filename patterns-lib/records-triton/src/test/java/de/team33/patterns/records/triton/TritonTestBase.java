package de.team33.patterns.records.triton;

public class TritonTestBase {

    static {
        Triton.setup(Class.class, mapping -> mapping.forward(Class::getName));
    }
}