package com.dubreuia.core.component;

import com.dubreuia.core.ExecutionMode;
import com.dubreuia.core.action.ShortcutAction;
import com.dubreuia.model.Action;
import com.dubreuia.model.Storage;
import com.dubreuia.processors.Processor;
import com.dubreuia.processors.WriteCommandAction;
import com.intellij.openapi.application.RunResult;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileDocumentManagerAdapter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.dubreuia.core.ExecutionMode.normal;
import static com.dubreuia.model.Action.activate;
import static com.dubreuia.utils.PsiFiles.isPsiFileEligible;
import static java.util.stream.Collectors.toList;

/**
 * TODO doc
 * <p>
 * Event handler class, instanciated by {@link Component}. The {@link #getSaveActionsProcessors(Project, PsiFile)}
 * returns the global processors (not java specific). The list {@link #RUNNING_PROCESSORS} is shared between instances.
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

    @Override
    public void beforeAllDocumentsSaving() {
        LOGGER.info("[ENTRY POINT] " + getClass().getName());

        Document[] unsavedDocuments = FileDocumentManager.getInstance().getUnsavedDocuments();
        LOGGER.info("Unsaved documents (" + unsavedDocuments.length + "): " + Arrays.asList(unsavedDocuments));

        Map<Project, Set<PsiFile>> projectPsiFiles = new HashMap<>();
        for (Document unsavedDocument : unsavedDocuments) {
            for (Project openProject : ProjectManager.getInstance().getOpenProjects()) {
                PsiFile psiFile = PsiDocumentManager.getInstance(openProject).getPsiFile(unsavedDocument);
                if (psiFile != null) {
                    Set<PsiFile> psiFiles = projectPsiFiles.getOrDefault(openProject, new HashSet<>());
                    projectPsiFiles.put(openProject, psiFiles);
                    psiFiles.add(psiFile);
                }
            }
        }
        projectPsiFiles.forEach(((project, psiFiles) -> {
            PsiFile[] psiFilesArray = psiFiles.toArray(new PsiFile[0]);
            processPsiFileIfNecessary(project, psiFilesArray, activate, normal);
        }));

        LOGGER.info("[EXIT POINT] " + getClass().getName() + " processed " + unsavedDocuments.length);
    }

    // TODO array
    public void processPsiFileIfNecessary(Project project, PsiFile[] psiFiles, Action activation, ExecutionMode mode) {
        if (!getStorage(project).isEnabled(activation)) {
            LOGGER.info("Plugin not activated on " + project);
            return;
        }
        LOGGER.info("Processing " + project + " files " + Arrays.toString(psiFiles) + " mode " + mode);
        // TODO convert to class
        Set<String> inclusions = getStorage(project).getInclusions();
        Set<String> exclusions = getStorage(project).getExclusions();
        boolean noActionIfCompileErrors = getStorage(project).isEnabled(Action.noActionIfCompileErrors);
        Arrays.stream(psiFiles)
                .filter(psiFile -> isPsiFileEligible(project, psiFile, inclusions, exclusions, noActionIfCompileErrors))
                .forEach(psiFile -> processPsiFile(project, psiFiles, mode));
    }

    // TODO array
    private void processPsiFile(Project project, PsiFile[] psiFiles, ExecutionMode mode) {
        List<WriteCommandAction> processors = getSaveActionsProcessors(project, psiFiles);
        List<RunResult> results = processors.stream()
                .filter(processor -> getStorage(project).isEnabled(processor.getAction()))
                .peek(processor -> LOGGER.info("Running processor " + processor))
                .filter(processor -> processor.getModes().contains(mode))
                .map(processor -> new SimpleEntry<>(processor, processor.execute()))
                .peek(entry -> LOGGER.info("Exit processor " + entry.getKey()))
                .map(SimpleEntry::getValue)
                .collect(toList());
        // TODO test results
        LOGGER.info("Exit processors with result " + results);
    }

    protected Storage getStorage(Project project) {
        return ServiceManager.getService(project, Storage.class);
    }

    // TODO array
    protected List<WriteCommandAction> getSaveActionsProcessors(Project project, PsiFile[] psiFiles) {
        return Processor.stream().map(p -> p.getWriteCommandAction(project, psiFiles)).collect(toList());
    }

}
