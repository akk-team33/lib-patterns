package net.team33.patterns.reflect.test;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings({
        "unused", "FieldMayBeStatic", "FieldMayBeFinal", "PublicField", "ProtectedField", "PackageVisibleField",
        "ConstantNamingConvention", "StaticVariableNamingConvention", "ClassWithTooManyFields",
        "FieldNameHidesFieldInSuperclass"})
public class SampleEx extends Sample {

    public static final String aStaticFinalField = "aStaticFinalField";
    private static String aStaticField = "aStaticField";
    public final String aPublicFinalField = "Sample.aPublicFinalField";
    public final transient String aPublicFinalTransientField = "Sample.aPublicFinalTransientField";
    protected final String aProtectedFinalField = "Sample.aProtectedFinalField";
    final String aPackageFinalField = "Sample.aPackageFinalField";
    private final String aPrivateFinalField = "Sample.aPrivateFinalField";
    public String aPublicField = "Sample.aPublicField";
    public transient String aPublicTransientField = "Sample.aPublicTransientField";
    protected String aProtectedField = "Sample.aProtectedField";
    String aPackageField = "Sample.aPackageField";
    private String aPrivateField = "Sample.aPrivateField";

    @SuppressWarnings("TypeMayBeWeakened")
    public static List<?> toList(final SampleEx subject) {
        return Arrays.asList(
                Sample.toList(subject),
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
        return (this == obj) || ((SampleEx.class == obj.getClass()) && toList(this).equals(toList((SampleEx) obj)));
    }

    @SuppressWarnings("DesignForExtension")
    @Override
    public String toString() {
        return toList(this).toString();
    }
}
