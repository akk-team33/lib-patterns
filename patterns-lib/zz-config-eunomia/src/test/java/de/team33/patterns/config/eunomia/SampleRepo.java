package de.team33.patterns.config.eunomia;

import de.team33.patterns.typing.proteus.Type;

import java.nio.file.Path;

public class SampleRepo extends ConfigRepo<SampleConfig> {

    private static final Naming NAMING = new Naming(SampleConfig.class.getPackageName(),
                                                    SampleConfig.class.getSimpleName());

    public SampleRepo(final SampleConfig defaultConfig, final Path system, final Path user, final Path cwd) {
        super(new IOMapping<>(new Type<>() {}, new Pathing(system, user, cwd), NAMING), defaultConfig);
    }
}
