package com.dubreuia.processors.java;

import com.dubreuia.model.Storage;
import com.dubreuia.processors.Processor;
import com.dubreuia.processors.java.inspections.CustomUnqualifiedStaticUsageInspection;
import com.intellij.codeInspection.ExplicitTypeCanBeDiamondInspection;
import com.intellij.codeInspection.localCanBeFinal.LocalCanBeFinal;
import com.intellij.codeInspection.visibility.VisibilityInspection;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.siyeh.ig.classlayout.FinalPrivateMethodInspection;
import com.siyeh.ig.inheritance.MissingOverrideAnnotationInspection;
import com.siyeh.ig.maturity.SuppressionAnnotationInspection;
import com.siyeh.ig.performance.MethodMayBeStaticInspection;
import com.siyeh.ig.serialization.SerializableHasSerialVersionUIDFieldInspectionBase;
import com.siyeh.ig.style.ControlFlowStatementWithoutBracesInspection;
import com.siyeh.ig.style.FieldMayBeFinalInspection;
import com.siyeh.ig.style.UnnecessaryFinalOnLocalVariableOrParameterInspection;
import com.siyeh.ig.style.UnnecessarySemicolonInspection;
import com.siyeh.ig.style.UnnecessaryThisInspection;
import com.siyeh.ig.style.UnqualifiedFieldAccessInspection;
import com.siyeh.ig.style.UnqualifiedMethodAccessInspection;
import com.siyeh.ig.style.UnqualifiedStaticUsageInspection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.dubreuia.model.Action.accessCanBeTightened;
import static com.dubreuia.model.Action.customUnqualifiedStaticMemberAccess;
import static com.dubreuia.model.Action.explicitTypeCanBeDiamond;
import static com.dubreuia.model.Action.fieldCanBeFinal;
import static com.dubreuia.model.Action.finalPrivateMethod;
import static com.dubreuia.model.Action.generateSerialVersionUID;
import static com.dubreuia.model.Action.localCanBeFinal;
import static com.dubreuia.model.Action.methodMayBeStatic;
import static com.dubreuia.model.Action.missingOverrideAnnotation;
import static com.dubreuia.model.Action.suppressAnnotation;
import static com.dubreuia.model.Action.unnecessaryFinalOnLocalVariableOrParameter;
import static com.dubreuia.model.Action.unnecessarySemicolon;
import static com.dubreuia.model.Action.unnecessaryThis;
import static com.dubreuia.model.Action.unqualifiedFieldAccess;
import static com.dubreuia.model.Action.unqualifiedMethodAccess;
import static com.dubreuia.model.Action.unqualifiedStaticMemberAccess;
import static com.dubreuia.model.Action.useBlocks;

public enum ProcessorFactory {

    INSTANCE;

    public List<Processor> getSaveActionsProcessors(Project project, PsiFile psiFile, Storage storage) {
        List<Processor> processors = new ArrayList<>();
        // Adds stuff
        processors.add(getLocalCanBeFinalProcessor(project, psiFile, storage));
        processors.add(getFieldMayBeFinalProcessor(project, psiFile, storage));
        processors.add(getMethodMayBeStaticProcessor(project, psiFile, storage));
        processors.add(getUnqualifiedFieldAccessProcessor(project, psiFile, storage));
        processors.add(getUnqualifiedMethodAccessProcessor(project, psiFile, storage));
        processors.add(getUnqualifiedStaticUsageInspectionProcessor(project, psiFile, storage));
        processors.add(getCustomUnqualifiedStaticUsageInspectionProcessor(project, psiFile, storage));
        processors.add(getMissingOverrideAnnotationProcessor(project, psiFile, storage));
        processors.add(getControlFlowStatementWithoutBracesProcessor(project, psiFile, storage));
        processors.add(getGenerateSerialVersionUIDProcessor(project, psiFile, storage));
        // Removes stuff
        processors.add(getExplicitTypeCanBeDiamondProcessor(project, psiFile, storage));
        processors.add(getUnnecessaryThisProcessor(project, psiFile, storage));
        processors.add(getSuppressionAnnotationProcessor(project, psiFile, storage));
        processors.add(getFinalPrivateMethodProcessor(project, psiFile, storage));
        processors.add(getUnnecessarySemicolonProcessor(project, psiFile, storage));
        processors.add(getUnnecessaryFinalOnLocalVariableOrParameterProcessor(project, psiFile, storage));
        // Replaces stuff
        processors.add(getAccessCanBeTightenedProcessor(project, psiFile, storage));
        return processors;
    }


    @NotNull
    private InspectionProcessor getLocalCanBeFinalProcessor(
            Project project,
            PsiFile psiFile,
            Storage storage) {
        return new InspectionProcessor(
                project,
                psiFile,
                storage,
                localCanBeFinal,
                new LocalCanBeFinal());
    }


    @NotNull
    private InspectionProcessor getFieldMayBeFinalProcessor(
            Project project,
            PsiFile psiFile,
            Storage storage) {
        return new InspectionProcessor(
                project,
                psiFile,
                storage,
                fieldCanBeFinal,
                new FieldMayBeFinalInspection());
    }

    @NotNull
    private InspectionProcessor getAccessCanBeTightenedProcessor(
            Project project,
            PsiFile psiFile,
            Storage storage) {
        return new InspectionProcessor(
                project,
                psiFile,
                storage,
                accessCanBeTightened,
                new AccessCanBeTightenedInspection(new VisibilityInspection()));
    }

    @NotNull
    private InspectionProcessor getMethodMayBeStaticProcessor(
            Project project,
            PsiFile psiFile,
            Storage storage) {
        return new InspectionProcessor(
                project,
                psiFile,
                storage,
                methodMayBeStatic,
                new MethodMayBeStaticInspection());
    }

    @NotNull
    private InspectionProcessor getUnqualifiedFieldAccessProcessor(
            Project project,
            PsiFile psiFile,
            Storage storage) {
        return new InspectionProcessor(
                project,
                psiFile,
                storage,
                unqualifiedFieldAccess,
                new UnqualifiedFieldAccessInspection());
    }

    @NotNull
    private InspectionProcessor getUnqualifiedMethodAccessProcessor(
            Project project,
            PsiFile psiFile,
            Storage storage) {
        return new InspectionProcessor(
                project,
                psiFile,
                storage,
                unqualifiedMethodAccess,
                new UnqualifiedMethodAccessInspection());
    }

    @NotNull
    private InspectionProcessor getUnqualifiedStaticUsageInspectionProcessor(
            Project project,
            PsiFile psiFile,
            Storage storage) {
        UnqualifiedStaticUsageInspection inspection = new UnqualifiedStaticUsageInspection();
        inspection.m_ignoreStaticFieldAccesses = false;
        inspection.m_ignoreStaticMethodCalls = false;
        inspection.m_ignoreStaticAccessFromStaticContext = false;
        return new InspectionProcessor(
                project,
                psiFile,
                storage,
                unqualifiedStaticMemberAccess,
                inspection);
    }

    @NotNull
    private InspectionProcessor getCustomUnqualifiedStaticUsageInspectionProcessor(
            Project project,
            PsiFile psiFile,
            Storage storage) {
        CustomUnqualifiedStaticUsageInspection inspection = new CustomUnqualifiedStaticUsageInspection();
        return new InspectionProcessor(
                project,
                psiFile,
                storage,
                customUnqualifiedStaticMemberAccess,
                inspection);
    }

    @NotNull
    private InspectionProcessor getMissingOverrideAnnotationProcessor(
            Project project,
            PsiFile psiFile,
            Storage storage) {
        MissingOverrideAnnotationInspection inspection = new MissingOverrideAnnotationInspection();
        inspection.ignoreObjectMethods = false;
        return new InspectionProcessor(
                project,
                psiFile,
                storage,
                missingOverrideAnnotation,
                inspection);
    }

    @NotNull
    private InspectionProcessor getControlFlowStatementWithoutBracesProcessor(
            Project project,
            PsiFile psiFile,
            Storage storage) {
        return new InspectionProcessor(
                project,
                psiFile,
                storage,
                useBlocks,
                new ControlFlowStatementWithoutBracesInspection());
    }

    @NotNull
    private InspectionProcessor getGenerateSerialVersionUIDProcessor(
            Project project,
            PsiFile psiFile,
            Storage storage) {
        return new InspectionProcessor(
                project,
                psiFile,
                storage,
                generateSerialVersionUID,
                new SerializableHasSerialVersionUIDFieldInspectionBase());
    }

    @NotNull
    private InspectionProcessor getExplicitTypeCanBeDiamondProcessor(
            Project project,
            PsiFile psiFile,
            Storage storage) {
        return new InspectionProcessor(
                project,
                psiFile,
                storage,
                explicitTypeCanBeDiamond,
                new ExplicitTypeCanBeDiamondInspection());
    }

    @NotNull
    private InspectionProcessor getUnnecessaryThisProcessor(
            Project project,
            PsiFile psiFile,
            Storage storage) {
        return new InspectionProcessor(
                project,
                psiFile,
                storage,
                unnecessaryThis,
                new UnnecessaryThisInspection());
    }

    @NotNull
    private InspectionProcessor getSuppressionAnnotationProcessor(
            Project project,
            PsiFile psiFile,
            Storage storage) {
        return new InspectionProcessor(
                project,
                psiFile,
                storage,
                suppressAnnotation,
                new SuppressionAnnotationInspection());
    }

    @NotNull
    private InspectionProcessor getFinalPrivateMethodProcessor(
            Project project,
            PsiFile psiFile,
            Storage storage) {
        return new InspectionProcessor(
                project,
                psiFile,
                storage,
                finalPrivateMethod,
                new FinalPrivateMethodInspection());
    }

    @NotNull
    private InspectionProcessor getUnnecessarySemicolonProcessor(
            Project project,
            PsiFile psiFile,
            Storage storage) {
        return new InspectionProcessor(
                project,
                psiFile,
                storage,
                unnecessarySemicolon,
                new UnnecessarySemicolonInspection());
    }

    @NotNull
    private InspectionProcessor getUnnecessaryFinalOnLocalVariableOrParameterProcessor(
            Project project,
            PsiFile psiFile,
            Storage storage) {
        return new InspectionProcessor(
                project,
                psiFile,
                storage,
                unnecessaryFinalOnLocalVariableOrParameter,
                new UnnecessaryFinalOnLocalVariableOrParameterInspection());
    }

}
