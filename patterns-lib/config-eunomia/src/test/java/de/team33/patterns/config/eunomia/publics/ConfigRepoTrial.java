package de.team33.patterns.config.eunomia.publics;

import de.team33.patterns.config.eunomia.ConfigLevel;
import de.team33.patterns.config.eunomia.ConfigRepo;
import de.team33.patterns.config.eunomia.SampleConfig;
import de.team33.patterns.config.eunomia.Supply;

public class ConfigRepoTrial {

    private static final Supply SUPPLY =
            new Supply();
    private static final SampleConfig DEFAULT_CONFIG =
            SUPPLY.anySampleConfig();
    private static final ConfigRepo<SampleConfig> REPO = ConfigRepo.by(SampleConfig.class, DEFAULT_CONFIG);

    public static void main(String[] args) {
        System.out.printf("REPO/DEFAULT : %s%n", REPO.path(ConfigLevel.DEFAULT));
        System.out.printf("REPO/SYSTEM  : %s%n", REPO.path(ConfigLevel.SYSTEM));
        System.out.printf("REPO/USER    : %s%n", REPO.path(ConfigLevel.USER));
        System.out.printf("REPO/CWD     : %s%n", REPO.path(ConfigLevel.CWD));

        System.out.println();
        System.out.println("reading default ...");
        System.out.println(REPO.read());

        REPO.write(ConfigLevel.USER, new SampleConfig(SUPPLY.anyString(), null, null));
        System.out.println();
        System.out.println("reading after writing USER ...");
        System.out.println(REPO.read());

        REPO.write(ConfigLevel.CWD, new SampleConfig(SUPPLY.anyString(), SUPPLY.anySampleConfig().entry(), null));
        System.out.println();
        System.out.println("reading after writing CWD ...");
        System.out.println(REPO.read());

        // should fail unless sudo ...
        // REPO.write(ConfigLevel.SYSTEM, new SampleConfig(null, null, SUPPLY.anySampleConfig().items()));

        REPO.reset();
        System.out.println();
        System.out.println("reading after reset ...");
        System.out.println(REPO.read());
    }
}
