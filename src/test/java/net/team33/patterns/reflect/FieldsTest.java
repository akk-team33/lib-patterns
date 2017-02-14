package net.team33.patterns.reflect;

import net.team33.patterns.Mapper;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@SuppressWarnings({"MultipleTopLevelClassesInFile", "Convert2Diamond"})
public class FieldsTest {

    private static final Fields<AnyClass> ANY_CLASS_FIELDS = Fields.of(AnyClass.class);
    private static final Fields<AnySubClass> ANY_SUB_CLASS_FIELDS = Fields.of(AnySubClass.class);
    private static final Map<String, Object> INITIAL_MAP = Mapper.wrap(new HashMap<String, Object>(3))
            .put("aPrivateField", "a private field")
            .put("aProtectedField", "a protected field")
            .put("aPublicField", "a public field")
            .unmodifiable();
    private static final Map<String, Object> INITIAL_SUB_MAP = Mapper.wrap(new TreeMap<String, Object>())
            .put("aPrivateField", "a private field")
            .put("aProtectedField", "a protected field in a subclass")
            .put("aPublicField", "a public field in a subclass")
            .put("anotherPrivateField", "another private field in a subclass")
            .put("anotherPublicField", "another public field in a subclass")
            .unmodifiable();
    private static final Map<String, Object> MODIFIED_MAP = Mapper.wrap(new HashMap<String, Object>(3))
            .put("aPrivateField", "a modified private field")
            .put("aProtectedField", "a modified protected field")
            .put("aPublicField", "a modified public field")
            .unmodifiable();
    private static final Map<String, Object> NULLED_MAP = Mapper.wrap(new HashMap<String, Object>(3))
            .put("aPrivateField", null)
            .put("aProtectedField", "a protected field")
            .put("aPublicField", "a public field")
            .unmodifiable();
    private static final Map<String, Object> NULL_MODIFIED_MAP = Mapper.wrap(new HashMap<String, Object>(3))
            .put("aPrivateField", null)
            .put("aProtectedField", "a modified protected field")
            .put("aPublicField", "a modified public field")
            .unmodifiable();
    private static final Map<String, Object> REDUCED_MODIFIED_MAP = Mapper.wrap(new HashMap<String, Object>(3))
            .put("aProtectedField", "a modified protected field")
            .put("aPublicField", "a modified public field")
            .unmodifiable();

    @Test
    public final void copyTo() {
        final AnyClass origin = ANY_CLASS_FIELDS.map(new AnyClass()).from(MODIFIED_MAP);
        final AnyClass result = ANY_CLASS_FIELDS.copy(origin).to(new AnyClass());
        Assert.assertEquals(MODIFIED_MAP, ANY_CLASS_FIELDS.map(result).to(new TreeMap<>()));
    }

    @Test
    public final void copyFrom() {
        final AnyClass origin = ANY_CLASS_FIELDS.map(new AnyClass()).from(MODIFIED_MAP);
        final AnyClass result = ANY_CLASS_FIELDS.copy(new AnyClass()).from(origin);
        Assert.assertEquals(MODIFIED_MAP, ANY_CLASS_FIELDS.map(result).to(new TreeMap<>()));
    }

    @Test
    public final void mapTo() {
        Assert.assertEquals(INITIAL_MAP, ANY_CLASS_FIELDS.map(new AnyClass()).to(new TreeMap<>()));
    }

    @Test
    public final void mapToAnySubClass() {
        Assert.assertEquals(INITIAL_SUB_MAP, ANY_SUB_CLASS_FIELDS.map(new AnySubClass()).to(new TreeMap<>()));
    }

    @Test
    public final void mapToAnySubClassToString() {
        Assert.assertEquals("{" +
                "aPrivateField=a private field," +
                " aProtectedField=a protected field in a subclass," +
                " aPublicField=a public field in a subclass," +
                " anotherPrivateField=another private field in a subclass," +
                " anotherPublicField=another public field in a subclass" +
                "}", ANY_SUB_CLASS_FIELDS.map(new AnySubClass()).to(new TreeMap<>()).toString());
    }

    @Test
    public final void mapToOneIsNull() {
        Assert.assertEquals(NULLED_MAP, ANY_CLASS_FIELDS.map(new AnyClass()
                .setAPrivateField(null)).to(new TreeMap<>()));
    }

    @Test
    public final void mapFrom() {
        final AnyClass result = ANY_CLASS_FIELDS.map(new AnyClass()).from(MODIFIED_MAP);
        Assert.assertEquals(MODIFIED_MAP, ANY_CLASS_FIELDS.map(result).to(new TreeMap<>()));
    }

    @Test
    public final void mapFromOneIsNull() {
        final AnyClass result = ANY_CLASS_FIELDS.map(new AnyClass()).from(NULL_MODIFIED_MAP);
        Assert.assertEquals(NULL_MODIFIED_MAP, ANY_CLASS_FIELDS.map(result).to(new TreeMap<>()));
    }

    @Test
    public final void mapFromOneIsMissing() {
        final AnyClass result = ANY_CLASS_FIELDS.map(new AnyClass()).from(REDUCED_MODIFIED_MAP);
        Assert.assertEquals(NULL_MODIFIED_MAP, ANY_CLASS_FIELDS.map(result).to(new TreeMap<>()));
    }

    @Test
    public final void hashCodes() {
        final AtomicInteger skipped = new AtomicInteger(0);
        final int limit = 100;
        Stream.generate(Subject::new).limit(limit).forEach(left ->
                Stream.generate(Subject::new).limit(limit).forEach(right -> {
                    if (Subject.FIELDS.equals(left, right)) {
                        Assert.assertEquals(
                                Subject.FIELDS.hashCode(left),
                                Subject.FIELDS.hashCode(right)
                        );
                    } else {
                        skipped.incrementAndGet();
                    }
                }));
        // enough relevant test cases?
        Assert.assertTrue(((limit * limit) - limit) > skipped.get());
    }

    @Test
    public final void equals() {
        final AtomicInteger skipped = new AtomicInteger(0);
        final int limit = 50;
        Stream.generate(Subject::new).limit(limit).forEach(left -> {

            // [Object.equals()]
            // It is reflexive: for any non-null reference value x, x.equals(x) should return true.
            Assert.assertTrue(Subject.FIELDS.equals(left, left));

            Stream.generate(Subject::new).limit(limit).forEach(right -> {
                final boolean result = Subject.FIELDS.equals(left, right);

                // Must match manual equals()-implementation
                Assert.assertEquals(left.equals(right), result);

                // [Object.equals()]
                // It is symmetric: for any non-null reference values x and y, x.equals(y) should return true
                // if and only if y.equals(x) returns true.
                Assert.assertEquals(Subject.FIELDS.equals(right, left), result);

                if (result) {
                    Stream.generate(Subject::new).limit(limit).forEach(third -> {
                        // [Object.equals()]
                        // It is transitive: for any non-null reference values x, y, and z,
                        // if x.equals(y) returns true and y.equals(z) returns true, then x.equals(z) should return true.
                        Assert.assertEquals(
                                Subject.FIELDS.equals(left, third),
                                Subject.FIELDS.equals(right, third));
                    });
                } else {
                    skipped.incrementAndGet();
                }
            });
        });
        // enough relevant test cases?
        Assert.assertTrue(((limit * limit) - limit) > skipped.get());
    }

    @Test
    public final void toStrings() {
        final AtomicInteger skipped = new AtomicInteger(0);
        final Set<String> strings = new TreeSet<>();
        final int limit = 100;
        Stream.generate(Subject::new).limit(limit).forEach(left -> {
            final String leftString = Subject.FIELDS.toString(left);
            strings.add(leftString);
            Stream.generate(Subject::new).limit(limit).forEach(right -> {
                final String rightString = Subject.FIELDS.toString(right);
                strings.add(rightString);
                if (Subject.FIELDS.equals(left, right)) {
                    Assert.assertEquals(
                            leftString,
                            rightString
                    );
                } else {
                    skipped.incrementAndGet();
                }
            });
        });
        // right number of different strings?
        Assert.assertEquals(Subject.SIZE, strings.size());
        // enough relevant test cases?
        Assert.assertTrue(((limit * limit) - limit) > skipped.get());
    }

    private static class Subject {
        public static final int SIZE;
        private static final List<String> STRINGS;
        private static final List<BigInteger> BIG_INTEGERS;
        private static final List<Integer> INTEGERS;
        private static final Random RANDOM;
        private static final Fields<Subject> FIELDS;

        static {
            RANDOM = new Random();
            INTEGERS = Arrays.asList(
                    RANDOM.nextInt(),
                    RANDOM.nextInt(),
                    RANDOM.nextInt()
            );
            STRINGS = Arrays.asList(
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString()
            );
            BIG_INTEGERS = Arrays.asList(
                    new BigInteger(128, RANDOM),
                    new BigInteger(128, RANDOM),
                    new BigInteger(128, RANDOM)
            );
            FIELDS = Fields.of(Subject.class);
            SIZE = INTEGERS.size() * STRINGS.size() * BIG_INTEGERS.size();
        }

        private final int intValue;
        private final BigInteger bigValue;
        private final String strValue;
        private final transient double dblValue;

        private Subject() {
            synchronized (RANDOM) {
                this.intValue = INTEGERS.get(RANDOM.nextInt(INTEGERS.size()));
                this.bigValue = BIG_INTEGERS.get(RANDOM.nextInt(BIG_INTEGERS.size()));
                this.strValue = STRINGS.get(RANDOM.nextInt(STRINGS.size()));
                dblValue = RANDOM.nextDouble();
            }
        }

        private static List<?> toList(final Subject s) {
            return Arrays.asList(s.intValue, s.bigValue, s.strValue);
        }

        @Override
        public int hashCode() {
            return toList(this).hashCode();
        }

        @Override
        public boolean equals(final Object obj) {
            return (this == obj) || ((obj instanceof Subject) && toList(this).equals(toList((Subject) obj)));
        }

        @Override
        public String toString() {
            return Mapper.wrap(new TreeMap<String, Object>())
                    .put("intValue", intValue)
                    .put("bigValue", bigValue)
                    .put("strValue", strValue)
                    .unwrap()
                    .toString();
        }
    }
}
