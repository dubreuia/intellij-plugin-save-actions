package com.dubreuia.processors;

import com.dubreuia.model.Storage;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.arrangement.Rearranger;
import com.intellij.psi.codeStyle.arrangement.engine.ArrangementEngine;
import com.intellij.util.diff.FilesTooBigForDiffException;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.FutureTask;

import static com.dubreuia.core.component.SaveActionManager.LOGGER;
import static com.dubreuia.model.Action.rearrange;

class RearrangeCodeProcessor extends com.intellij.codeInsight.actions.RearrangeCodeProcessor implements Processor {

    private static final String NAME = "RearrangeCode";

    private final Storage storage;

    RearrangeCodeProcessor(Project project, PsiFile psiFile, Storage storage) {
        super(project, new PsiFile[]{psiFile}, COMMAND_NAME, null);
        this.storage = storage;
    }

    @NotNull
    @Override
    // com.intellij.codeInsight.actions.RearrangeCodeProcessor.prepareTask()
    protected FutureTask<Boolean> prepareTask(@NotNull PsiFile file, boolean processChangedTextOnly) {
        return new FutureTask<>(() -> {
            try {
                Collection<TextRange> ranges = getRangesToFormat(file, processChangedTextOnly);
                Document document = PsiDocumentManager.getInstance(myProject).getDocument(file);

                if (document != null && Rearranger.EXTENSION.forLanguage(file.getLanguage()) != null) {
                    PsiDocumentManager.getInstance(myProject).doPostponedOperationsAndUnblockDocument(document);
                    Runnable command = prepareRearrangeCommand(file, ranges);
                    CommandProcessor.getInstance().executeCommand(myProject, command, COMMAND_NAME, null);
                }

                return true;
            } catch (FilesTooBigForDiffException e) {
                handleFileTooBigException(LOGGER, e, file);
                return false;
            }

        });
    }

    @NotNull
    // com.intellij.codeInsight.actions.RearrangeCodeProcessor#prepareRearrangeCommand
    private Runnable prepareRearrangeCommand(@NotNull PsiFile file, @NotNull Collection<TextRange> ranges) {
        ArrangementEngine engine = ServiceManager.getService(myProject, ArrangementEngine.class);
        return () -> {
            engine.arrange(file, ranges);
            if (getInfoCollector() != null) {
                String info = engine.getUserNotificationInfo();
                getInfoCollector().setRearrangeCodeNotification(info);
            }
        };
    }

    @Override
    public void run() {
        if (storage.isEnabled(rearrange)) {
            try {
                super.run();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String toString() {
        return toString(NAME, storage.isEnabled(rearrange));
    }

}