package de.team33.test.patterns.properties.methods;

import java.io.Serializable;
import java.util.List;

public interface X3 {

    List<String> getFirst();

    String getSecond();

    Serializable getThird();

    void setThird(Serializable value);
}
