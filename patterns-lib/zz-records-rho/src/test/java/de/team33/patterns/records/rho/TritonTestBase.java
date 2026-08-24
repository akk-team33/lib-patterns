package de.team33.patterns.records.rho;

public class TritonTestBase {

    static {
        Triton.setup(Class.class, mapping -> mapping.forward(Class::getName));
    }
}