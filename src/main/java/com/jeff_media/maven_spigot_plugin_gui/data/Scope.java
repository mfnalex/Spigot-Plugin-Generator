package com.jeff_media.maven_spigot_plugin_gui.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
public enum Scope {

    SHADED("shaded", "Shaded"), PROVIDED("provided", "Provided"), SOFTDEPEND("soft-depend", "Provided (Soft-Depend"), HARDDEPEND("hard-depend", "Provided (Hard-Depend)"), ANNOTATION("annotation", "Provided (Annot. Proc.)"), TEST("test", "Test");

    @Getter
    private final String nameInArchetypeMetadata;
    @Getter
    private final String displayName;

    public static Scope fromString(String string) {
        if (string == null) return null;
        for (Scope scope : Scope.values()) {
            if (scope.nameInArchetypeMetadata.equalsIgnoreCase(string)) {
                return scope;
            }
        }
        throw new NoSuchElementException("Invalid scope: " + string);
    }


    @Override
    public String toString() {
        return getDisplayName();
    }
}
