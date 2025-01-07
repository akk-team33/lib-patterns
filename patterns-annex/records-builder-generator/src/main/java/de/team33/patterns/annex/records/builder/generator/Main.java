package de.team33.patterns.annex.records.builder.generator;

import de.team33.patterns.annex.records.builder.generating.ClassCode;

public class Main {

    private final ClassCode code;

    private Main(final String[] args) throws ClassNotFoundException {
        this.code = new ClassCode(Class.forName(args[0]));
    }

    public static void main(String[] args) throws ClassNotFoundException {
        new Main(args).run();
    }

    private void run() {
        System.out.println(code);
    }
}
