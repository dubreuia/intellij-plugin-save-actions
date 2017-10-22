package com.dubreuia.core;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.dubreuia.model.Action.activate;
import static com.dubreuia.model.Action.noActionIfCompileErrors;
import static com.dubreuia.utils.PsiFiles.isIncludedAndNotExcluded;
import static com.dubreuia.utils.PsiFiles.isPsiFileInProject;
import static java.util.Collections.sort;
import static java.util.Collections.synchronizedList;

/**
 * Event handler class, instanciated by {@link Component}. The {@link #getSaveActionsProcessors(Project, PsiFile)}
 * returns the global processors (not java specific). The list {@link #runningProcessors} is shared between instances
 */
public class SaveActionManager extends FileDocumentManagerAdapter {

    public static final Logger LOGGER = Logger.getInstance(SaveActionManager.class);

    private static List<Processor> runningProcessors = synchronizedList(new ArrayList<Processor>());

    static {
        LOGGER.setLevel(Level.DEBUG);
    }

    @Override
    public void beforeDocumentSaving(@NotNull Document document) {
        LOGGER.debug("Running SaveActionManager on " + document);
        for (Project project : ProjectManager.getInstance().getOpenProjects()) {
            PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
            if (getStorage(project).isEnabled(activate)) {
                checkAndProcessPsiFile(project, psiFile);
            }
        }
    }

    void checkAndProcessPsiFile(Project project, PsiFile psiFile) {
        if (isPsiFileEligible(project, psiFile)) {
            processPsiFile(project, psiFile);
        }
    }

    /**
     * The psi files seems to be shared between projects, so we need to check if the file is physically
     * in that project before reformating, or else the file is formatted twice and intellij will ask to
     * confirm unlocking of non-project file in the other project.
     */
    private boolean isPsiFileEligible(Project project, PsiFile psiFile) {
        return psiFile != null
                && isProjectValid(project)
                && isPsiFileInProject(project, psiFile)
                && isPsiFileHasErrors(project, psiFile)
                && isPsiFileIncluded(project, psiFile)
                && isPsiFileFresh(psiFile)
                && isPsiFileValid(psiFile);
    }

    private boolean isProjectValid(Project project) {
        return project.isInitialized()
                && !project.isDisposed();
    }

    private boolean isPsiFileHasErrors(Project project, PsiFile psiFile) {
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

    private boolean isPsiFileValid(PsiFile psiFile) {
        return psiFile.isValid();
    }

    private void processPsiFile(Project project, PsiFile psiFile) {
        List<Processor> processors = getSaveActionsProcessors(project, psiFile);
        LOGGER.debug("Running processors " + processors + ", file " + psiFile + ", project " + project);
        for (Processor processor : processors) {
            runProcessor(processor);
        }
    }

    private void runProcessor(Processor processor) {
        if (runningProcessors.contains(processor)) {
            return;
        }
        try {
            runningProcessors.add(processor);
            processor.run();
        } finally {
            runningProcessors.remove(processor);
        }
    }

    protected Storage getStorage(Project project) {
        return ServiceManager.getService(project, Storage.class);
    }

    protected List<Processor> getSaveActionsProcessors(Project project, PsiFile psiFile) {
        List<Processor> processors = ProcessorFactory.INSTANCE
                .getSaveActionsProcessors(project, psiFile, getStorage(project));
        sort(processors, new Processor.ProcessorComparator());
        return processors;
    }

}
