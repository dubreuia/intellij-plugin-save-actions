package com.dubreuia.processors;

import com.dubreuia.core.ExecutionMode;
import com.dubreuia.model.Action;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import java.util.Comparator;
import java.util.Set;

/**
 * Processor interface contains the provider method, the action that enables the processors, and the modes for which
 * the processor is available.
 */
public interface Processor {

    Action getAction();

    Set<ExecutionMode> getModes();

    int getOrder();

    SaveCommand getSaveCommand(Project project, Set<PsiFile> psiFiles);

    class OrderComparator implements Comparator<Processor> {

        @Override
        public int compare(Processor o1, Processor o2) {
            return Integer.compare(o1.getOrder(), o2.getOrder());
        }

    }

}
