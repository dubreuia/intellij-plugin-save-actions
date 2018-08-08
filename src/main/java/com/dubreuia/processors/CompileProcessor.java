package com.dubreuia.processors;

import com.dubreuia.core.ExecutionMode;
import com.dubreuia.model.Storage;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;

import static com.dubreuia.core.SaveActionManager.LOGGER;
import static com.dubreuia.model.Action.compile;
import static com.dubreuia.processors.ProcessorMessage.toStringBuilder;

class CompileProcessor implements Processor {

    private static final String NAME = "Compile";

    private final Project project;

    private final PsiFile file;

    private final Storage storage;

    CompileProcessor(Project project, PsiFile file, Storage storage) {
        this.project = project;
        this.file = file;
        this.storage = storage;
    }

    @Override
    public void run() {
        if (storage.isEnabled(compile)) {
            ApplicationManager.getApplication().invokeLater(() -> {
                try {
                    CompilerManager.getInstance(project).compile(new VirtualFile[]{file.getVirtualFile()}, null);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            });
        }
    }

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public boolean canRun(ExecutionMode mode) {
        return !ExecutionMode.batch.equals(mode);
    }

    @Override
    public String toString() {
        return toStringBuilder(NAME, storage.isEnabled(compile));
    }

}
