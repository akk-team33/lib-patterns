package de.team33.patterns.arbitrary.mimas;

import java.util.Random;
import java.util.random.RandomGenerator;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

class Proxy extends Random {

    private final RandomGenerator backing;

    Proxy(final RandomGenerator backing) {
        this.backing = backing;
    }

    @Override
    public final boolean isDeprecated() {
        return backing.isDeprecated();
    }

    @Override
    public final DoubleStream doubles() {
        return backing.doubles();
    }

    @Override
    public final DoubleStream doubles(double randomNumberOrigin, double randomNumberBound) {
        return backing.doubles(randomNumberOrigin, randomNumberBound);
    }

    @Override
    public final DoubleStream doubles(long streamSize) {
        return backing.doubles(streamSize);
    }

    @Override
    public final DoubleStream doubles(long streamSize, double randomNumberOrigin, double randomNumberBound) {
        return backing.doubles(streamSize, randomNumberOrigin, randomNumberBound);
    }

    @Override
    public final IntStream ints() {
        return backing.ints();
    }

    @Override
    public final IntStream ints(int randomNumberOrigin, int randomNumberBound) {
        return backing.ints(randomNumberOrigin, randomNumberBound);
    }

    @Override
    public final IntStream ints(long streamSize) {
        return backing.ints(streamSize);
    }

    @Override
    public final IntStream ints(long streamSize, int randomNumberOrigin, int randomNumberBound) {
        return backing.ints(streamSize, randomNumberOrigin, randomNumberBound);
    }

    @Override
    public final LongStream longs() {
        return backing.longs();
    }

    @Override
    public final LongStream longs(long randomNumberOrigin, long randomNumberBound) {
        return backing.longs(randomNumberOrigin, randomNumberBound);
    }

    @Override
    public final LongStream longs(long streamSize) {
        return backing.longs(streamSize);
    }

    @Override
    public final LongStream longs(long streamSize, long randomNumberOrigin, long randomNumberBound) {
        return backing.longs(streamSize, randomNumberOrigin, randomNumberBound);
    }

    @Override
    public final boolean nextBoolean() {
        return backing.nextBoolean();
    }

    @Override
    public final void nextBytes(byte[] bytes) {
        backing.nextBytes(bytes);
    }

    @Override
    public final float nextFloat() {
        return backing.nextFloat();
    }

    @Override
    public final float nextFloat(float bound) {
        return backing.nextFloat(bound);
    }

    @Override
    public final float nextFloat(float origin, float bound) {
        return backing.nextFloat(origin, bound);
    }

    @Override
    public final double nextDouble() {
        return backing.nextDouble();
    }

    @Override
    public final double nextDouble(double bound) {
        return backing.nextDouble(bound);
    }

    @Override
    public final double nextDouble(double origin, double bound) {
        return backing.nextDouble(origin, bound);
    }

    @Override
    public final int nextInt() {
        return backing.nextInt();
    }

    @Override
    public final int nextInt(int bound) {
        return backing.nextInt(bound);
    }

    @Override
    public final int nextInt(int origin, int bound) {
        return backing.nextInt(origin, bound);
    }

    @Override
    public final long nextLong() {
        return backing.nextLong();
    }

    @Override
    public final long nextLong(long bound) {
        return backing.nextLong(bound);
    }

    @Override
    public final long nextLong(long origin, long bound) {
        return backing.nextLong(origin, bound);
    }

    @Override
    public final double nextGaussian() {
        return backing.nextGaussian();
    }

    @Override
    public final double nextGaussian(double mean, double stddev) {
        return backing.nextGaussian(mean, stddev);
    }

    @Override
    public final double nextExponential() {
        return backing.nextExponential();
    }
}
