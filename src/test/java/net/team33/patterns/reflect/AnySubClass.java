package net.team33.patterns.reflect;

@SuppressWarnings({
        "unused", "FieldCanBeLocal", "ProtectedField", "FieldMayBeStatic", "PublicField",
        "FieldNameHidesFieldInSuperclass", "TransientFieldInNonSerializableClass", "FieldHasSetterButNoGetter"})
class AnySubClass extends AnyClass {

    private static final String A_STATIC_FIELD = "a static field in a subclass";

    public final String aPublicField = "a public field in a subclass";
    public final String anotherPublicField = "another public field in a subclass";
    protected final String aProtectedField = "a protected field in a subclass";
    public transient String anotherTransientField = "another transient field in a subclass";
    private String anotherPrivateField = "another private field in a subclass";

    public final AnySubClass setAnotherPrivateField(final String value) {
        anotherPrivateField = value;
        return this;
    }
}
