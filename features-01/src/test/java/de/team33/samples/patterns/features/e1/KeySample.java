package de.team33.samples.patterns.features.e1;

import de.team33.patterns.features.e1.Key;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

class KeySample {

    static final Key<Random> RANDOM = Random::new;
    static final Key<Date> DATE = Date::new;
    static final Key<List<Throwable>> PROBLEMS = LinkedList::new;

    // ...
}
