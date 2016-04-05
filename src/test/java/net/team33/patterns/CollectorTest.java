package net.team33.patterns;

import org.junit.Test;

import java.util.HashSet;

public class CollectorTest {

    @Test
    public void test() {
        final HashSet<Integer> subject = Collector.on(new HashSet<Integer>(0)).getSubject();
    }
}