package de.team33.patterns.reflect.pandora.testing;

import java.time.Instant;

@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
public interface BeanInterface {

    int getIntValue();

    Long getLongValue();

    String getStringValue();

    Instant getInstantValue();
}
