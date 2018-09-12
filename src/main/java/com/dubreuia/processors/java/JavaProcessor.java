package com.dubreuia.processors.java;

import com.dubreuia.model.Action;
import com.dubreuia.model.Storage;
import com.dubreuia.processors.java.inspection.SerializableHasSerialVersionUIDFieldInspectionWrapper;
import com.intellij.codeInspection.ExplicitTypeCanBeDiamondInspection;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.localCanBeFinal.LocalCanBeFinal;
import com.intellij.codeInspection.visibility.CustomAccessCanBeTightenedInspection;
import com.intellij.codeInspection.visibility.VisibilityInspection;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.siyeh.ig.classlayout.FinalPrivateMethodInspection;
import com.siyeh.ig.inheritance.MissingOverrideAnnotationInspection;
import com.siyeh.ig.maturity.SuppressionAnnotationInspection;
import com.siyeh.ig.performance.MethodMayBeStaticInspection;
import com.siyeh.ig.style.ControlFlowStatementWithoutBracesInspection;
import com.siyeh.ig.style.CustomUnqualifiedStaticUsageInspection;
import com.siyeh.ig.style.FieldMayBeFinalInspection;
import com.siyeh.ig.style.UnnecessaryFinalOnLocalVariableOrParameterInspection;
import com.siyeh.ig.style.UnnecessarySemicolonInspection;
import com.siyeh.ig.style.UnnecessaryThisInspection;
import com.siyeh.ig.style.UnqualifiedFieldAccessInspection;
import com.siyeh.ig.style.UnqualifiedMethodAccessInspection;
import com.siyeh.ig.style.UnqualifiedStaticUsageInspection;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum JavaProcessor {

    fieldCanBeFinal(Action.fieldCanBeFinal,
            FieldMayBeFinalInspection::new),

    localCanBeFinal(Action.localCanBeFinal,
            LocalCanBeFinal::new),

    methodMayBeStatic(Action.methodMayBeStatic,
            MethodMayBeStaticInspection::new),

    unqualifiedFieldAccess(Action.unqualifiedFieldAccess,
            UnqualifiedFieldAccessInspection::new),

    unqualifiedMethodAccess(Action.unqualifiedMethodAccess,
            UnqualifiedMethodAccessInspection::new),

    unqualifiedStaticMemberAccess(Action.unqualifiedStaticMemberAccess,
            () -> {
                UnqualifiedStaticUsageInspection inspection = new UnqualifiedStaticUsageInspection();
                inspection.m_ignoreStaticFieldAccesses = false;
                inspection.m_ignoreStaticMethodCalls = false;
                inspection.m_ignoreStaticAccessFromStaticContext = false;
                return inspection;
            }),

    customUnqualifiedStaticMemberAccess(Action.customUnqualifiedStaticMemberAccess,
            CustomUnqualifiedStaticUsageInspection::new),

    missingOverrideAnnotation(Action.missingOverrideAnnotation,
            () -> {
                MissingOverrideAnnotationInspection inspection = new MissingOverrideAnnotationInspection();
                inspection.ignoreObjectMethods = false;
                return inspection;
            }),

    useBlocks(Action.useBlocks,
            ControlFlowStatementWithoutBracesInspection::new),

    generateSerialVersionUID(Action.generateSerialVersionUID,
            SerializableHasSerialVersionUIDFieldInspectionWrapper::get),

    unnecessaryThis(Action.unnecessaryThis,
            UnnecessaryThisInspection::new),

    finalPrivateMethod(Action.finalPrivateMethod,
            FinalPrivateMethodInspection::new),

    unnecessaryFinalOnLocalVariableOrParameter(Action.unnecessaryFinalOnLocalVariableOrParameter,
            UnnecessaryFinalOnLocalVariableOrParameterInspection::new),

    explicitTypeCanBeDiamond(Action.explicitTypeCanBeDiamond,
            ExplicitTypeCanBeDiamondInspection::new),

    suppressAnnotation(Action.suppressAnnotation,
            SuppressionAnnotationInspection::new),

    unnecessarySemicolon(Action.unnecessarySemicolon,
            UnnecessarySemicolonInspection::new),

    accessCanBeTightened(Action.accessCanBeTightened,
            () -> new CustomAccessCanBeTightenedInspection(new VisibilityInspection())),

    ;

    private final Action action;
    private final LocalInspectionTool inspection;

    JavaProcessor(Action action, Supplier<LocalInspectionTool> inspection) {
        this.action = action;
        this.inspection = inspection.get();
    }

    public Action getAction() {
        return action;
    }

    public LocalInspectionTool getInspection() {
        return inspection;
    }

    public InspectionProcessor getInspectionProcessor(Project project, PsiFile psiFile, Storage storage) {
        return new InspectionProcessor(project, psiFile, storage, action, inspection);
    }

    public static Optional<JavaProcessor> getJavaProcessorForAction(Action action) {
        return stream().filter(processor -> processor.action.equals(action)).findFirst();
    }

    public static Stream<JavaProcessor> stream() {
        return Arrays.stream(values());
    }

}