package com.dubreuia.integration;

import com.dubreuia.core.component.SaveActionManager;
import com.dubreuia.model.Storage;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;
import com.intellij.testFramework.fixtures.IdeaProjectTestFixture;
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory;
import com.intellij.testFramework.fixtures.impl.LightTempDirTestFixtureImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.dubreuia.core.action.BatchActionConstants.SAVE_ACTION_BATCH_MANAGER;
import static com.dubreuia.core.action.ShortcutActionConstants.SAVE_ACTION_SHORTCUT_MANAGER;
import static com.dubreuia.core.component.SaveActionsManagerConstants.SAVE_ACTION_MANAGER;
import static com.dubreuia.junit.JUnit5Utils.rethrowAsJunit5Error;
import static com.intellij.testFramework.LightProjectDescriptor.EMPTY_PROJECT_DESCRIPTOR;

public abstract class IntegrationTest {

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
        SAVE_ACTION_MANAGER.accept(fixture, getSaveActionManager());
        rethrowAsJunit5Error(() -> fixture.checkResultByFile(after.getFilename()));
    }

    void assertSaveActionShortcut(ActionTestFile before, ActionTestFile after) {
        fixture.configureByFile(before.getFilename());
        SAVE_ACTION_SHORTCUT_MANAGER.accept(fixture);
        rethrowAsJunit5Error(() -> fixture.checkResultByFile(after.getFilename()));
    }

    void assertSaveActionBatch(ActionTestFile before, ActionTestFile after) {
        fixture.configureByFile(before.getFilename());
        SAVE_ACTION_BATCH_MANAGER.accept(fixture);
        rethrowAsJunit5Error(() -> fixture.checkResultByFile(after.getFilename()));
    }

    private String getTestDataPath() {
        Path classes = Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        Path resources = Paths.get(classes.getParent().toString(), "resources");
        Path root = Paths.get(resources.toString(), getClass().getPackage().getName().split("[.]"));
        return root.toString();
    }

    abstract SaveActionManager getSaveActionManager();

}
