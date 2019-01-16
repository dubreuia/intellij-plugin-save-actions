package com.dubreuia.processors;

import com.dubreuia.core.ExecutionMode;
import com.dubreuia.model.Action;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import java.util.Set;

public interface Processor {

    Action getAction();

    Set<ExecutionMode> getModes();

    WriteCommandAction getWriteCommandAction(Project project, PsiFile[] psiFiles);

}
