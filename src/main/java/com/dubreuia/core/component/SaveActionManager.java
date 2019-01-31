package com.dubreuia.core.component;

import com.dubreuia.core.ExecutionMode;
import com.dubreuia.model.Action;
import com.dubreuia.model.Storage;
import com.dubreuia.model.StorageFactory;
import com.dubreuia.processors.Processor;
import com.dubreuia.processors.Processor.OrderComparator;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileDocumentManagerAdapter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static com.dubreuia.core.ExecutionMode.saveAll;
import static com.dubreuia.model.Action.activate;
import static com.dubreuia.model.StorageFactory.DEFAULT;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

/**
 * <p>
 * Singleton event handler class, instanciated by {@link Component}. All actions are routed here.
 * <p>
 * The main method is {@link #guardedProcessPsiFiles(Project, Set, Action, ExecutionMode)} and will delegate to
 * {@link Engine#processPsiFilesIfNecessary()}. The method will check if the file needs to be processed and use the
 * processors to apply the modifications.
 * <p>
 * The psi files are ide wide, that means they are shared between projects (and editor windows), so we need to check if
 * the file is physically in that project before reformating, or else the file is formatted twice and intellij will ask
 * to confirm unlocking of non-project file in the other project, see {@link Engine} for more details.
 *
 * @see Engine
 */
public class SaveActionManager extends FileDocumentManagerAdapter {

    public static final Logger LOGGER = Logger.getInstance(SaveActionManager.class);

    private static SaveActionManager instance;

    public static SaveActionManager getInstance() {
        if (instance == null) {
            instance = new SaveActionManager();
        }
        return instance;
    }

    private final List<Processor> processors;
    private boolean running;
    private boolean javaAvailable;
    private final boolean compilingAvailable;
    private StorageFactory storageFactory;

    private SaveActionManager() {
        processors = new ArrayList<>();
        running = false;
        javaAvailable = false;
        compilingAvailable = initCompilingAvailable();
        storageFactory = DEFAULT;
    }

    private boolean initCompilingAvailable() {
        try {
            return Class.forName("com.intellij.openapi.compiler.CompilerManager") != null;
        } catch (Exception e) {
            return false;
        }
    }

    void addProcessors(Stream<Processor> processors) {
        processors.forEach(this.processors::add);
        this.processors.sort(new OrderComparator());
    }

    void enableJava() {
        javaAvailable = true;
    }

    void setStorageFactory(StorageFactory storageFactory) {
        this.storageFactory = storageFactory;
    }

    public boolean isCompilingAvailable() {
        return compilingAvailable;
    }

    public boolean isJavaAvailable() {
        return javaAvailable;
    }

    public Storage getStorage(Project project) {
        return storageFactory.getStorage(project);
    }

    @Override
    public void beforeAllDocumentsSaving() {
        LOGGER.info("[+] Start SaveActionManager#beforeAllDocumentsSaving");
        Document[] unsavedDocuments = FileDocumentManager.getInstance().getUnsavedDocuments();
        beforeDocumentsSaving(asList(unsavedDocuments));
        LOGGER.info("End SaveActionManager#beforeAllDocumentsSaving");
    }

    private void beforeDocumentsSaving(List<Document> documents) {
        LOGGER.info("Locating psi files for " + documents.size() + " documents: " + documents);

        Map<Project, Set<PsiFile>> projectPsiFiles = new HashMap<>();
        documents.forEach(document -> stream(ProjectManager.getInstance().getOpenProjects())
                .forEach(project -> ofNullable(PsiDocumentManager.getInstance(project).getPsiFile(document))
                        .map(psiFile -> {
                            Set<PsiFile> psiFiles = projectPsiFiles.getOrDefault(project, new HashSet<>());
                            projectPsiFiles.put(project, psiFiles);
                            return psiFiles.add(psiFile);
                        })));
        projectPsiFiles.forEach(((project, psiFiles) -> {
            guardedProcessPsiFiles(project, psiFiles, activate, saveAll);
        }));
    }

    public void guardedProcessPsiFiles(Project project, Set<PsiFile> psiFiles, Action activation, ExecutionMode mode) {
        if (ApplicationManager.getApplication().isDisposed()) {
            LOGGER.info("Application is closing, stopping invocation");
            return;
        }
        try {
            if (running) {
                LOGGER.info("Plugin already running, stopping invocation");
                return;
            }
            running = true;
            Engine engine = new Engine(getStorage(project), processors, project, psiFiles, activation, mode);
            engine.processPsiFilesIfNecessary();
        } finally {
            running = false;
        }
    }

}
