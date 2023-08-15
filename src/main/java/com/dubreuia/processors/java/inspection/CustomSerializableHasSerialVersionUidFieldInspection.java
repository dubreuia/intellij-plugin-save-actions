package com.dubreuia.processors.java.inspection;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.SerializableHasSerialVersionUidFieldInspection;
import com.intellij.codeInspection.USerializableInspectionBase;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.UClass;

/**
 * Wrapper for the SerializableHasSerialVersionUidFieldInspection class.
 * <p>
 * We have to set isOnTheFly to true. Otherwise, the inspection will not be applied.
 *
 * @since IDEA 2021.3
 */
public class CustomSerializableHasSerialVersionUidFieldInspection extends USerializableInspectionBase {


    @SuppressWarnings("unchecked")
    public CustomSerializableHasSerialVersionUidFieldInspection() {
        super(new Class[]{UClass.class});
    }

    @Override
    public @NonNls @NotNull String getID() {
        return "serial";
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String getDisplayName() {

        return this.getClass().getSimpleName();
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String getGroupDisplayName() {

        return "SaveActionsInternal";
    }

    @Override
    public ProblemDescriptor @Nullable [] checkClass(@NotNull UClass aClass, @NotNull InspectionManager manager, boolean isOnTheFly) {
        SerializableHasSerialVersionUidFieldInspection inspection = new SerializableHasSerialVersionUidFieldInspection();
        return inspection.checkClass(aClass, manager, true);
    }
}
