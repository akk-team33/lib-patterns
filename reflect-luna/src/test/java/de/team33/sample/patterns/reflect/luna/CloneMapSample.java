package de.team33.sample.patterns.reflect.luna;

import de.team33.patterns.reflect.luna.CloneMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CloneMapSample {

    public static final CloneMap CLONE_MAP = CloneMap.builder()
                                                     .on(Date.class).apply(date -> new Date(date.getTime()))
                                                     .on(List.class).apply(ArrayList::new)
                                                     .on(Map.class).apply(HashMap::new)
                                                     .build();

}
