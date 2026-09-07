package de.team33.patterns.config.eunomia;

import java.nio.file.Path;

record Pathing(Path system, Path user, Path cwd) {

    @SuppressWarnings("unused")
    static final Pathing DEFAULT = new Pathing(Path.of("/etc"),
                                               Path.of(System.getProperty("user.home")).resolve(".etc"),
                                               Path.of("."));

    Pathing {
        system = system.toAbsolutePath().normalize();
        user = user.toAbsolutePath().normalize();
        cwd = cwd.toAbsolutePath().normalize();
    }
}
