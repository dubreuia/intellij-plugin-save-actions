package com.dubreuia.integration;

import com.dubreuia.core.SaveActionManager;
import com.dubreuia.core.SaveActionShortcutManager;
import com.dubreuia.model.Storage;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;
import com.intellij.testFramework.fixtures.IdeaProjectTestFixture;
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory;
import com.intellij.testFramework.fixtures.impl.LightTempDirTestFixtureImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.intellij.openapi.actionSystem.CommonDataKeys.PROJECT;
import static com.intellij.openapi.actionSystem.CommonDataKeys.PSI_FILE;
import static com.intellij.openapi.actionSystem.impl.SimpleDataContext.getSimpleContext;
import static com.intellij.testFramework.LightProjectDescriptor.EMPTY_PROJECT_DESCRIPTOR;

public abstract class IntegrationTest {

    private final Consumer<CodeInsightTestFixture> SAVE_ACTION_MANAGER = (fixture) ->
            new WriteCommandAction.Simple(fixture.getProject()) {
                @Override
                protected void run() {
                    // set modification timestamp ++
                    ((PsiFileImpl) fixture.getFile()).clearCaches();

                    // call plugin on document
                    getSaveActionManager().beforeDocumentSaving(fixture.getDocument(fixture.getFile()));
                }
            }.execute();

    private final Consumer<CodeInsightTestFixture> SAVE_ACTION_SHORTCUT_MANAGER = (fixture) ->
            new WriteCommandAction.Simple(fixture.getProject()) {
                @Override
                protected void run() {
                    // set modification timestamp ++
                    ((PsiFileImpl) fixture.getFile()).clearCaches();

                    ActionManager actionManager = ActionManager.getInstance();
                    AnAction action = actionManager.getAction("com.dubreuia.core.SaveActionShortcutManager");

                    Map<String, Object> data = new HashMap<>();
                    data.put(PROJECT.getName(), fixture.getProject());
                    data.put(PSI_FILE.getName(), fixture.getFile());
                    DataContext dataContext = getSimpleContext(data, null);

                    AnActionEvent event = AnActionEvent.createFromAnAction(action, null, "save-actions", dataContext);
                    event.getProject();

                    // call plugin on document
                    new SaveActionShortcutManager().actionPerformed(event);
                }
            }.execute();

    private CodeInsightTestFixture fixture;

    Storage storage;

    @BeforeEach
    public void before() throws Exception {
        IdeaTestFixtureFactory factory = IdeaTestFixtureFactory.getFixtureFactory();
        IdeaProjectTestFixture testFixture = factory.createLightFixtureBuilder(EMPTY_PROJECT_DESCRIPTOR).getFixture();
        fixture = factory.createCodeInsightFixture(testFixture, new LightTempDirTestFixtureImpl(true));
        fixture.setUp();
        fixture.setTestDataPath(getTestDataPath());
        storage = ServiceManager.getService(testFixture.getProject(), Storage.class);
    }

    @AfterEach
    public void after() throws Exception {
        fixture.tearDown();
        storage.clear();
    }

    void assertSaveAction(ActionTestFile before, ActionTestFile after) {
        fixture.configureByFile(before.getFilename());
        SAVE_ACTION_MANAGER.accept(fixture);
        fixture.checkResultByFile(after.getFilename());
    }

    void assertSaveActionShortcut(ActionTestFile before, ActionTestFile after) {
        fixture.configureByFile(before.getFilename());
        SAVE_ACTION_SHORTCUT_MANAGER.accept(fixture);
        fixture.checkResultByFile(after.getFilename());
    }

    private String getTestDataPath() {
        Path classes = Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        Path resources = Paths.get(classes.getParent().toString(), "resources");
        Path root = Paths.get(resources.toString(), getClass().getPackage().getName().split("[.]"));
        return root.toString();
    }

    abstract SaveActionManager getSaveActionManager();

}
