package de.team33.test.patterns.properties.methods;

import java.io.Serializable;
import java.util.Collection;

public interface X2 {

    Collection<String> getFirst();

    String getSecond();

    Number getThird();

    void setThird(Number value);
}
