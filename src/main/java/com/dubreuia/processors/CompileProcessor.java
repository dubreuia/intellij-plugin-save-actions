package com.dubreuia.processors;

import com.dubreuia.core.ExecutionMode;
import com.dubreuia.model.Storage;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;

import static com.dubreuia.core.component.SaveActionManager.LOGGER;
import static com.dubreuia.model.Action.compile;

class CompileProcessor implements Processor {

    private static final String NAME = "Compile";

    private final Project project;
    private final PsiFile psiFile;
    private final Storage storage;

    CompileProcessor(Project project, PsiFile psiFile, Storage storage) {
        this.project = project;
        this.psiFile = psiFile;
        this.storage = storage;
    }

    @Override
    public void run() {
        if (storage.isEnabled(compile)) {
            ApplicationManager.getApplication().invokeLater(() -> {
                try {
                    CompilerManager.getInstance(project).compile(new VirtualFile[]{psiFile.getVirtualFile()}, null);
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
        return toString(NAME, storage.isEnabled(compile));
    }

}
