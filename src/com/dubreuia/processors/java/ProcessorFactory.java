package com.dubreuia.processors.java;

import com.dubreuia.model.Storage;
import com.dubreuia.processors.Processor;
import com.intellij.codeInspection.ExplicitTypeCanBeDiamondInspection;
import com.intellij.codeInspection.localCanBeFinal.LocalCanBeFinal;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.siyeh.ig.classlayout.FinalPrivateMethodInspection;
import com.siyeh.ig.inheritance.MissingOverrideAnnotationInspection;
import com.siyeh.ig.maturity.SuppressionAnnotationInspection;
import com.siyeh.ig.style.ControlFlowStatementWithoutBracesInspection;
import com.siyeh.ig.style.FieldMayBeFinalInspection;
import com.siyeh.ig.style.UnnecessaryFinalOnLocalVariableOrParameterInspection;
import com.siyeh.ig.style.UnnecessarySemicolonInspection;
import com.siyeh.ig.style.UnnecessaryThisInspection;
import com.siyeh.ig.style.UnqualifiedFieldAccessInspection;

import java.util.ArrayList;
import java.util.List;

import static com.dubreuia.model.Action.explicitTypeCanBeDiamond;
import static com.dubreuia.model.Action.fieldCanBeFinal;
import static com.dubreuia.model.Action.finalPrivateMethod;
import static com.dubreuia.model.Action.localCanBeFinal;
import static com.dubreuia.model.Action.missingOverrideAnnotation;
import static com.dubreuia.model.Action.suppressAnnotation;
import static com.dubreuia.model.Action.unnecessaryFinalOnLocalVariableOrParameter;
import static com.dubreuia.model.Action.unnecessarySemicolon;
import static com.dubreuia.model.Action.unnecessaryThis;
import static com.dubreuia.model.Action.unqualifiedFieldAccess;
import static com.dubreuia.model.Action.useBlocks;

public enum ProcessorFactory {

    INSTANCE;

    public List<Processor> getSaveActionsProcessors(Project project, PsiFile psiFile, Storage storage) {
        List<Processor> processors = new ArrayList<Processor>();
        // Add stuff
        processors.add(new InspectionProcessor(project, psiFile, storage, localCanBeFinal,
                new LocalCanBeFinal()));
        processors.add(new InspectionProcessor(project, psiFile, storage, unqualifiedFieldAccess,
                new UnqualifiedFieldAccessInspection()));
        processors.add(new InspectionProcessor(project, psiFile, storage, fieldCanBeFinal,
                new FieldMayBeFinalInspection()));
        processors.add(new InspectionProcessor(project, psiFile, storage, missingOverrideAnnotation,
                new MissingOverrideAnnotationInspection()));
        processors.add(new InspectionProcessor(project, psiFile, storage, useBlocks,
                new ControlFlowStatementWithoutBracesInspection()));

        // Removes stuff
        processors.add(new InspectionProcessor(project, psiFile, storage, explicitTypeCanBeDiamond,
                new ExplicitTypeCanBeDiamondInspection()));
        processors.add(new InspectionProcessor(project, psiFile, storage, unnecessaryThis,
                new UnnecessaryThisInspection()));
        processors.add(new InspectionProcessor(project, psiFile, storage, suppressAnnotation,
                new SuppressionAnnotationInspection()));
        processors.add(new InspectionProcessor(project, psiFile, storage, finalPrivateMethod,
                new FinalPrivateMethodInspection()));
        processors.add(new InspectionProcessor(project, psiFile, storage, unnecessarySemicolon,
                new UnnecessarySemicolonInspection()));
        processors.add(new InspectionProcessor(project, psiFile, storage, unnecessaryFinalOnLocalVariableOrParameter,
                new UnnecessaryFinalOnLocalVariableOrParameterInspection()));
        return processors;
    }

}
