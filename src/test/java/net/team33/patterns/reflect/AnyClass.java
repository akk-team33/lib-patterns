package net.team33.patterns.reflect;

@SuppressWarnings({"unused", "FieldCanBeLocal", "ProtectedField", "FieldMayBeStatic", "PublicField"})
class AnyClass {

    private static final String A_STATIC_FIELD = "a static field";
    public final String aPublicField = "a public field";
    protected final String aProtectedField = "a protected field";
    public transient String aTransientField = "a transient field";
    private String aPrivateField = "a private field";

    public final AnyClass setAPrivateField(final String value) {
        this.aPrivateField = value;
        return this;
    }
}
