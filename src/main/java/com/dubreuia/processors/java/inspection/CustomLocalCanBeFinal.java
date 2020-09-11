/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 Alexandre DuBreuil
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

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
