package com.dubreuia.processors.java.inspection;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.localCanBeFinal.LocalCanBeFinal;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiTypeElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;

@SuppressWarnings("InspectionDescriptionNotFoundInspection")
public class CustomLocalCanBeFinal extends LocalCanBeFinal {

    @Override
    public ProblemDescriptor[] checkMethod(@NotNull PsiMethod method,
                                           @NotNull InspectionManager manager,
                                           boolean isOnTheFly) {
        return checkProblemDescriptors(super.checkMethod(method, manager, isOnTheFly));
    }

    @Override
    public ProblemDescriptor[] checkClass(@NotNull PsiClass aClass,
                                          @NotNull InspectionManager manager,
                                          boolean isOnTheFly) {
        return checkProblemDescriptors(super.checkClass(aClass, manager, isOnTheFly));
    }

    private ProblemDescriptor[] checkProblemDescriptors(@Nullable ProblemDescriptor[] descriptors) {
        return Arrays
                .stream(Optional.ofNullable(descriptors).orElse(new ProblemDescriptor[0]))
                .filter(descriptor -> isNotLombokVal(descriptor.getPsiElement()))
                .toArray(ProblemDescriptor[]::new);
    }

    private boolean isNotLombokVal(PsiElement element) {
        return Arrays
                .stream(element.getParent().getChildren())
                .noneMatch(child -> child instanceof PsiTypeElement && child.getText().equals("val"));
    }

}
