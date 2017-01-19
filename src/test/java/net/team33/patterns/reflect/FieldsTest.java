package net.team33.patterns.reflect;

import net.team33.patterns.Mapper;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings({"MultipleTopLevelClassesInFile", "Convert2Diamond"})
public class FieldsTest {

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
    public final void mapTo() {
        Assert.assertEquals(INITIAL_MAP, Fields.of(AnyClass.class).map(new AnyClass()).to(new TreeMap<>()));
    }

    @Test
    public final void mapToAnySubClass() {
        Assert.assertEquals(INITIAL_SUB_MAP, Fields.of(AnySubClass.class).map(new AnySubClass()).to(new TreeMap<>()));
    }

    @Test
    public final void mapToAnySubClassToString() {
        Assert.assertEquals("{" +
                "aPrivateField=a private field," +
                " aProtectedField=a protected field in a subclass," +
                " aPublicField=a public field in a subclass," +
                " anotherPrivateField=another private field in a subclass," +
                " anotherPublicField=another public field in a subclass" +
                "}", Fields.of(AnySubClass.class).map(new AnySubClass()).to(new TreeMap<>()).toString());
    }

    @Test
    public final void mapToOneIsNull() {
        Assert.assertEquals(NULLED_MAP, Fields.of(AnyClass.class).map(new AnyClass()
                .setAPrivateField(null)).to(new TreeMap<>()));
    }

    @Test
    public final void mapFrom() {
        final AnyClass result = Fields.of(AnyClass.class).map(new AnyClass()).from(MODIFIED_MAP);
        Assert.assertEquals(MODIFIED_MAP, Fields.of(AnyClass.class).map(result).to(new TreeMap<>()));
    }

    @Test
    public final void mapFromOneIsNull() {
        final AnyClass result = Fields.of(AnyClass.class).map(new AnyClass()).from(NULL_MODIFIED_MAP);
        Assert.assertEquals(NULL_MODIFIED_MAP, Fields.of(AnyClass.class).map(result).to(new TreeMap<>()));
    }

    @Test
    public final void mapFromOneIsMissing() {
        final AnyClass result = Fields.of(AnyClass.class).map(new AnyClass()).from(REDUCED_MODIFIED_MAP);
        Assert.assertEquals(NULL_MODIFIED_MAP, Fields.of(AnyClass.class).map(result).to(new TreeMap<>()));
    }
}
