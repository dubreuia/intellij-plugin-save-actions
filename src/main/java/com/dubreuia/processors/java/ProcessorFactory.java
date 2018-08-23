package com.dubreuia.processors.java;

import com.dubreuia.model.Storage;
import com.dubreuia.processors.Processor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import java.util.List;

import static java.util.stream.Collectors.toList;

public enum ProcessorFactory {

    INSTANCE;

    public List<Processor> getSaveActionsProcessors(Project project, PsiFile psiFile, Storage storage) {
        return JavaProcessor.stream()
                .map(processor -> processor.getInspectionProcessor(project, psiFile, storage))
                .collect(toList());
    }

}
