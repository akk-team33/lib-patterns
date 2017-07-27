package net.team33.patterns.reflections.test;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings({
        "unused", "FieldMayBeStatic", "FieldMayBeFinal", "PublicField", "ProtectedField", "PackageVisibleField",
        "ConstantNamingConvention", "StaticVariableNamingConvention", "ClassWithTooManyFields"})
public class Sample {

    public static final String aStaticFinalField = "aStaticFinalField";
    private static String aStaticField = "aStaticField";

    private final String aPrivateFinalField = "Sample.aPrivateFinalField";
    protected final String aProtectedFinalField = "Sample.aProtectedFinalField";
    final String aPackageFinalField = "Sample.aPackageFinalField";
    public final String aPublicFinalField = "Sample.aPublicFinalField";
    public final transient String aPublicFinalTransientField = "Sample.aPublicFinalTransientField";

    private String aPrivateField = "Sample.aPrivateField";
    protected String aProtectedField = "Sample.aProtectedField";
    String aPackageField = "Sample.aPackageField";
    public String aPublicField = "Sample.aPublicField";
    public transient String aPublicTransientField = "Sample.aPublicTransientField";

    @SuppressWarnings("TypeMayBeWeakened")
    private static List<?> toList(final Sample subject) {
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
