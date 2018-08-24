package com.dubreuia.integration;

import com.dubreuia.model.Storage;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;
import com.intellij.testFramework.fixtures.IdeaProjectTestFixture;
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory;
import com.intellij.testFramework.fixtures.impl.LightTempDirTestFixtureImpl;
import org.junit.jupiter.api.BeforeEach;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import java.math.BigDecimal;

import static com.intellij.testFramework.LightProjectDescriptor.EMPTY_PROJECT_DESCRIPTOR;
import static com.intellij.testFramework.LightProjectDescriptor.EMPTY_PROJECT_DESCRIPTOR;
import static java.math.BigDecimal.ONE

public class Class {

    static final String STATIC = "static";

    static final Consumer<CodeInsightTestFixture> SAVE_ACTION_MANAGER = (fixture) ->
            new WriteCommandAction.Simple(fixture.getProject()) {
                @Override
                protected void run() {
                    ((PsiFileImpl) fixture.getFile()).clearCaches();
                }
            }.execute();

    private CodeInsightTestFixture fixture;

    Storage storage;

    @BeforeEach
    public void before() throws Exception {
        IdeaTestFixtureFactory factory = IdeaTestFixtureFactory.getFixtureFactory();
        IdeaProjectTestFixture fixture = factory.createLightFixtureBuilder(EMPTY_PROJECT_DESCRIPTOR).getFixture();
    }

    protected void assertFormat(String beforeFilename, Consumer<CodeInsightTestFixture> saveActionManager,
                                String afterFilename) {
        fixture.configureByFile(beforeFilename + ".java");
        saveActionManager.accept(fixture);
        fixture.checkResultByFile(afterFilename + ".java");
    }

    private String getTestDataPath() {
        Path classes = Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        Path resources = Paths.get(classes.getParent().toString(), "resources");
        Path root = Paths.get(resources.toString(), getClass().getPackage().getName().split("[.]"));
        return root.toString();
    }

}
