package com.dubreuia;

import com.dubreuia.model.Storage;
import com.dubreuia.processors.Processor;
import com.dubreuia.processors.ProcessorFactory;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManagerAdapter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.util.PsiErrorElementUtil;
import org.apache.log4j.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

import static com.dubreuia.model.Action.noActionIfCompileErrors;
import static com.dubreuia.utils.PsiFiles.isIncludedAndNotExcluded;
import static com.dubreuia.utils.PsiFiles.isPsiFileInProject;

public class SaveActionManager extends FileDocumentManagerAdapter {

    public static final Logger LOGGER = Logger.getInstance(SaveActionManager.class);

    static {
        LOGGER.setLevel(Level.DEBUG);
    }

    @Override
    public void beforeDocumentSaving(@NotNull Document document) {
        for (Project project : ProjectManager.getInstance().getOpenProjects()) {
            PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
            if (isPsiFileEligible(project, psiFile)) {
                processPsiFile(project, psiFile);
            }
        }
    }

    /**
     * The psi files seems to be shared between projects, so we need to check if the file is physically
     * in that project before reformating, or else the file is formatted twice and intellij will ask to
     * confirm unlocking of non-project file in the other project.
     */
    private boolean isPsiFileEligible(Project project, PsiFile psiFile) {
        return psiFile != null &&
                isProjectValid(project) &&
                isPsiFileInProject(project, psiFile) &&
                isPsiFileValid(project, psiFile) &&
                isPsiFileIncluded(project, psiFile) &&
                isPsiFileFresh(psiFile);
    }

    private boolean isProjectValid(Project project) {
        return project.isInitialized() &&
                !project.isDisposed();
    }

    private boolean isPsiFileValid(Project project, PsiFile psiFile) {
        if (getStorage(project).isEnabled(noActionIfCompileErrors)) {
            return !PsiErrorElementUtil.hasErrors(project, psiFile.getVirtualFile());
        }
        return true;
    }

    private boolean isPsiFileIncluded(Project project, PsiFile psiFile) {
        String canonicalPath = psiFile.getVirtualFile().getCanonicalPath();
        Set<String> inclusions = getStorage(project).getInclusions();
        Set<String> exclusions = getStorage(project).getExclusions();
        return isIncludedAndNotExcluded(canonicalPath, inclusions, exclusions);
    }

    private boolean isPsiFileFresh(PsiFile psiFile) {
        return psiFile.getModificationStamp() != 0;
    }

    private void processPsiFile(Project project, PsiFile psiFile) {
        List<Processor> processors = ProcessorFactory.INSTANCE
                .getSaveActionsProcessors(project, psiFile, getStorage(project));
        LOGGER.debug("Running processors " + processors + ", file " + psiFile + ", project " + project);
        for (Processor processor : processors) {
            processor.writeToFile();
        }
    }

    private Storage getStorage(Project project) {
        return ServiceManager.getService(project, Storage.class);
    }

}
