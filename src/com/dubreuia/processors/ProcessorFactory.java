package com.dubreuia.processors;

import com.dubreuia.model.StorageRO;
import com.intellij.codeInspection.ExplicitTypeCanBeDiamondInspection;
import com.intellij.codeInspection.localCanBeFinal.LocalCanBeFinal;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.siyeh.ig.classlayout.FinalPrivateMethodInspection;
import com.siyeh.ig.maturity.SuppressionAnnotationInspection;
import com.siyeh.ig.style.FieldMayBeFinalInspection;
import com.siyeh.ig.style.UnnecessarySemicolonInspection;
import com.siyeh.ig.style.UnqualifiedFieldAccessInspection;

import java.util.ArrayList;
import java.util.List;

import static com.dubreuia.model.Action.activate;
import static com.dubreuia.model.Action.explicitTypeCanBeDiamond;
import static com.dubreuia.model.Action.fieldCanBeFinal;
import static com.dubreuia.model.Action.finalPrivateMethod;
import static com.dubreuia.model.Action.localCanBeFinal;
import static com.dubreuia.model.Action.suppressAnnotation;
import static com.dubreuia.model.Action.unnecessarySemicolon;
import static com.dubreuia.model.Action.unqualifiedFieldAccess;

public enum ProcessorFactory {

    INSTANCE;

    public List<Processor> getSaveActionsProcessors(Project project, PsiFile psiFile, StorageRO storage) {
        final ArrayList<Processor> processors = new ArrayList<Processor>();
        if (storage.isEnabled(activate)) {
            processors.add(new OptimizeImportsProcessor(project, psiFile, storage));
            processors.add(new ReformatCodeProcessor(project, psiFile, storage));
            processors.add(new RearrangeCodeProcessor(project, psiFile, storage));
            processors.add(new InspectionProcessor(project, psiFile, storage, localCanBeFinal, new LocalCanBeFinal()));
            processors.add(new InspectionProcessor(project, psiFile, storage, explicitTypeCanBeDiamond, new ExplicitTypeCanBeDiamondInspection()));
            processors.add(new InspectionProcessor(project, psiFile, storage, unqualifiedFieldAccess, new UnqualifiedFieldAccessInspection()));
            processors.add(new InspectionProcessor(project, psiFile, storage, suppressAnnotation, new SuppressionAnnotationInspection()));
            processors.add(new InspectionProcessor(project, psiFile, storage, finalPrivateMethod, new FinalPrivateMethodInspection()));
            processors.add(new InspectionProcessor(project, psiFile, storage, unnecessarySemicolon, new UnnecessarySemicolonInspection()));
            processors.add(new InspectionProcessor(project, psiFile, storage, fieldCanBeFinal, new FieldMayBeFinalInspection()));
        }
        return processors;
    }

}
