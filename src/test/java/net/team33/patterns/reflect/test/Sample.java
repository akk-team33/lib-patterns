package net.team33.patterns.reflect.test;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings({
        "unused", "FieldMayBeStatic", "FieldMayBeFinal", "PublicField", "ProtectedField", "PackageVisibleField",
        "ConstantNamingConvention", "StaticVariableNamingConvention", "ClassWithTooManyFields",
        "RedundantStringConstructorCall"})
public class Sample {

    public static final String aStaticFinalField = "aStaticFinalField";
    private static String aStaticField = "aStaticField";

    public final String aPublicFinalField = new String("Sample.aPublicFinalField");
    public final transient String aPublicFinalTransientField = "Sample.aPublicFinalTransientField";
    protected final String aProtectedFinalField = new String("Sample.aProtectedFinalField");
    final String aPackageFinalField = new String("Sample.aPackageFinalField");

    private String aPrivateField = "Sample.aPrivateField";
    String aPackageField = "Sample.aPackageField";
    private final String aPrivateFinalField = new String("Sample.aPrivateFinalField");
    public String aPublicField = "Sample.aPublicField";
    protected String aProtectedField = "Sample.aProtectedField";
    public transient String aPublicTransientField = "Sample.aPublicTransientField";

    public static String getAStaticFinalField() {
        return aStaticFinalField;
    }

    public static String getAStaticField() {
        return aStaticField;
    }

    public static void setAStaticField(final String aStaticField) {
        Sample.aStaticField = aStaticField;
    }

    @SuppressWarnings("TypeMayBeWeakened")
    public static List<?> toList(final Sample subject) {
        return Arrays.asList(
                subject.aPrivateFinalField,
                subject.aProtectedFinalField,
                subject.aPackageFinalField,
                subject.aPublicFinalField,
                subject.aPrivateField,
                subject.aProtectedField,
                subject.aPackageField,
                subject.aPublicField
        );
    }

    public final String getAPrivateFinalField() {
        return aPrivateFinalField;
    }

    public final String getAProtectedFinalField() {
        return aProtectedFinalField;
    }

    public final String getAPackageFinalField() {
        return aPackageFinalField;
    }

    public final String getAPublicFinalField() {
        return aPublicFinalField;
    }

    public final String getAPublicFinalTransientField() {
        return aPublicFinalTransientField;
    }

    public final String getAPrivateField() {
        return aPrivateField;
    }

    public final Sample setAPrivateField(final String aPrivateField) {
        this.aPrivateField = aPrivateField;
        return this;
    }

    public final String getAProtectedField() {
        return aProtectedField;
    }

    public final Sample setAProtectedField(final String aProtectedField) {
        this.aProtectedField = aProtectedField;
        return this;
    }

    public final String getAPackageField() {
        return aPackageField;
    }

    public final Sample setAPackageField(final String aPackageField) {
        this.aPackageField = aPackageField;
        return this;
    }

    public final String getAPublicField() {
        return aPublicField;
    }

    public final Sample setAPublicField(final String aPublicField) {
        this.aPublicField = aPublicField;
        return this;
    }

    public final String getAPublicTransientField() {
        return aPublicTransientField;
    }

    public final Sample setAPublicTransientField(final String aPublicTransientField) {
        this.aPublicTransientField = aPublicTransientField;
        return this;
    }

    @SuppressWarnings("DesignForExtension")
    @Override
    public int hashCode() {
        return toList(this).hashCode();
    }

    @SuppressWarnings("DesignForExtension")
    @Override
    public boolean equals(final Object obj) {
        return (this == obj) || ((Sample.class == obj.getClass()) && toList(this).equals(toList((Sample) obj)));
    }

    @SuppressWarnings("DesignForExtension")
    @Override
    public String toString() {
        return toList(this).toString();
    }
}
