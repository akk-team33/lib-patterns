package de.team33.patterns.arbitrary.mimas.sample;

import java.time.Instant;

public record Person(String firstName, String lastName, Instant birth, String location, Person mother, Person father) {
}
