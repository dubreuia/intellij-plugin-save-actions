package com.dubreuia;

import com.dubreuia.processors.ProcessorFactory;
import com.intellij.codeInsight.actions.AbstractLayoutCodeProcessor;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManagerAdapter;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.dubreuia.utils.Documents.isDocumentActive;
import static com.dubreuia.utils.PsiFiles.isPsiFileExcluded;

public class SaveActionFileDocumentManager extends FileDocumentManagerAdapter {

    private final Settings settings = ServiceManager.getService(Settings.class);

    @Override
    public void beforeDocumentSaving(@NotNull Document document) {
        if (!SaveAllAction.TRIGGERED && isDocumentActive(document)) {
            return;
        }
        final DataContext dataContext = DataManager.getInstance().getDataContextFromFocus().getResult();
        final Project project = DataKeys.PROJECT.getData(dataContext);
        if (project != null) {
            final PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
            if (isPsiFileEligible(project, psiFile)) {
                processPsiFile(project, psiFile);
            }
        }
    }

    private boolean isPsiFileEligible(Project project, PsiFile psiFile) {
        return psiFile != null && !isPsiFileExcluded(project, psiFile, settings.getExclusions());
    }

    private void processPsiFile(final Project project, final PsiFile psiFile) {
        final List<AbstractLayoutCodeProcessor> processors =
                ProcessorFactory.INSTANCE.getSaveActionsProcessors(project, psiFile, settings);
        for (AbstractLayoutCodeProcessor processor : processors) {
            if (processor != null) {
                processor.run();
            }
        }
    }

}
