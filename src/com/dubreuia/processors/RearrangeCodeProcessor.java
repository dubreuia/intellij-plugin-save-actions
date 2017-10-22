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
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import static com.dubreuia.core.SaveActionManager.LOGGER;
import static com.dubreuia.model.Action.rearrange;
import static com.dubreuia.processors.ProcessorMessage.toStringBuilder;

class RearrangeCodeProcessor extends com.intellij.codeInsight.actions.RearrangeCodeProcessor implements Processor {

    private static final String ID = "RearrangeCode";

    private final Storage storage;

    RearrangeCodeProcessor(Project project, PsiFile file, Storage storage) {
        super(project, new PsiFile[]{file}, COMMAND_NAME, null);
        this.storage = storage;
    }

    @NotNull
    @Override
    // com.intellij.codeInsight.actions.RearrangeCodeProcessor.prepareTask()
    protected FutureTask<Boolean> prepareTask(@NotNull final PsiFile file, final boolean processChangedTextOnly) {
        return new FutureTask<Boolean>(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
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

            }
        });
    }

    @NotNull
    // com.intellij.codeInsight.actions.RearrangeCodeProcessor#prepareRearrangeCommand
    private Runnable prepareRearrangeCommand(@NotNull final PsiFile file, @NotNull final Collection<TextRange> ranges) {
        final ArrangementEngine engine = ServiceManager.getService(myProject, ArrangementEngine.class);
        return new Runnable() {
            @Override
            public void run() {
                engine.arrange(file, ranges);
                if (getInfoCollector() != null) {
                    String info = engine.getUserNotificationInfo();
                    getInfoCollector().setRearrangeCodeNotification(info);
                }
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
    public int order() {
        return 0;
    }

    @Override
    public String toString() {
        return toStringBuilder(ID, storage.isEnabled(rearrange));
    }

}