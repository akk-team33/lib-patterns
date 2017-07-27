package net.team33.patterns.reflections.test;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings({
        "unused", "FieldMayBeStatic", "FieldMayBeFinal", "PublicField", "ProtectedField", "PackageVisibleField",
        "ConstantNamingConvention", "StaticVariableNamingConvention", "ClassWithTooManyFields"})
public class Sample {

    public static final String aStaticFinalField = "aStaticFinalField";
    private static String aStaticField = "aStaticField";

    private final String aPrivateFinalField = "Any.aPrivateFinalField";
    protected final String aProtectedFinalField = "Any.aProtectedFinalField";
    final String aPackageFinalField = "Any.aPackageFinalField";
    public final String aPublicFinalField = "Any.aPublicFinalField";
    public final transient String aPublicFinalTransientField = "Any.aPublicFinalTransientField";

    private String aPrivateField = "Any.aPrivateField";
    protected String aProtectedField = "Any.aProtectedField";
    String aPackageField = "Any.aPackageField";
    public String aPublicField = "Any.aPublicField";
    public transient String aPublicTransientField = "Any.aPublicTransientField";

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
