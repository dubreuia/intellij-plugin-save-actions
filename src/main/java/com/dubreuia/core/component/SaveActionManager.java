package com.dubreuia.core.component;

import com.dubreuia.core.ExecutionMode;
import com.dubreuia.core.action.ShortcutAction;
import com.dubreuia.model.Action;
import com.dubreuia.model.Storage;
import com.dubreuia.processors.Processor;
import com.dubreuia.processors.Processor.ProcessorComparator;
import com.dubreuia.processors.ProcessorFactory;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManagerAdapter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.dubreuia.core.ExecutionMode.normal;
import static com.dubreuia.model.Action.activate;
import static com.dubreuia.utils.PsiFiles.isPsiFileEligible;
import static java.util.Collections.synchronizedList;

/**
 * Event handler class, instanciated by {@link Component}. The {@link #getSaveActionsProcessors(Project, PsiFile)}
 * returns the global processors (not java specific). The list {@link #runningProcessors} is shared between instances.
 * <p>
 * The main method is {@link #processPsiFileIfNecessary(Project, PsiFile, ExecutionMode)}. Make sure the action is
 * activated before calling the method.
 * <p>
 * The psi files seems to be shared between projects, so we need to check if the file is physically
 * in that project before reformating, or else the file is formatted twice and intellij will ask to
 * confirm unlocking of non-project file in the other project,
 * see {@link com.dubreuia.utils.PsiFiles#isPsiFileEligible(Project, PsiFile, Set, Set, boolean)}.
 *
 * @see ShortcutAction
 */
public class SaveActionManager extends FileDocumentManagerAdapter {

    public static final Logger LOGGER = Logger.getInstance(SaveActionManager.class);

    private static List<Processor> runningProcessors = synchronizedList(new ArrayList<>());

    @Override
    public void beforeDocumentSaving(@NotNull Document document) {
        LOGGER.info("Running SaveActionManager on " + document);
        for (Project project : ProjectManager.getInstance().getOpenProjects()) {
            PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
            if (getStorage(project).isEnabled(activate)) {
                processPsiFileIfNecessary(project, psiFile, normal);
            }
        }
    }

    public void processPsiFileIfNecessary(Project project, PsiFile psiFile, ExecutionMode mode) {
        Set<String> inclusions = getStorage(project).getInclusions();
        Set<String> exclusions = getStorage(project).getExclusions();
        boolean noActionIfCompileErrors = getStorage(project).isEnabled(Action.noActionIfCompileErrors);
        if (isPsiFileEligible(project, psiFile, inclusions, exclusions, noActionIfCompileErrors)) {
            processPsiFile(project, psiFile, mode);
        }
    }

    private void processPsiFile(Project project, PsiFile psiFile, ExecutionMode mode) {
        List<Processor> processors = getSaveActionsProcessors(project, psiFile);
        LOGGER.info("Running processors " + processors + ", file " + psiFile + ", project " + project);
        processors.stream()
                .filter(processor -> processor.canRun(mode))
                .forEach(this::runProcessor);
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

    public Storage getStorage(Project project) {
        return ServiceManager.getService(project, Storage.class);
    }

    protected List<Processor> getSaveActionsProcessors(Project project, PsiFile psiFile) {
        List<Processor> processors = ProcessorFactory.INSTANCE
                .getSaveActionsProcessors(project, psiFile, getStorage(project));
        processors.sort(new ProcessorComparator());
        return processors;
    }

}
