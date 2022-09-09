/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 Alexandre DuBreuil
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.dubreuia.integration;

import com.dubreuia.core.service.SaveActionsServiceManager;
import com.dubreuia.model.Storage;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;
import com.intellij.testFramework.fixtures.IdeaProjectTestFixture;
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory;
import com.intellij.testFramework.fixtures.impl.LightTempDirTestFixtureImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.dubreuia.core.action.BatchActionConstants.SAVE_ACTION_BATCH_MANAGER;
import static com.dubreuia.core.action.ShortcutActionConstants.SAVE_ACTION_SHORTCUT_MANAGER;
import static com.dubreuia.core.component.SaveActionManagerConstants.SAVE_ACTION_MANAGER;
import static com.dubreuia.junit.JUnit5Utils.rethrowAsJunit5Error;
import static com.intellij.testFramework.LightProjectDescriptor.EMPTY_PROJECT_DESCRIPTOR;

public abstract class IntegrationTest {

    private CodeInsightTestFixture fixture;

    Storage storage;

    @BeforeEach
    public void before(TestInfo testInfo) throws Exception {
        IdeaTestFixtureFactory factory = IdeaTestFixtureFactory.getFixtureFactory();
        IdeaProjectTestFixture testFixture = factory.createLightFixtureBuilder(EMPTY_PROJECT_DESCRIPTOR, testInfo.getDisplayName()).getFixture();
        fixture = factory.createCodeInsightFixture(testFixture, new LightTempDirTestFixtureImpl(true));
        fixture.setUp();
        fixture.setTestDataPath(getTestDataPath());
        storage = testFixture.getProject().getService(Storage.class);
    }

    @AfterEach
    public void after() throws Exception {
        fixture.tearDown();
        storage.clear();
    }

    void assertSaveAction(ActionTestFile before, ActionTestFile after) {
        fixture.configureByFile(before.getFilename());
        SAVE_ACTION_MANAGER.accept(fixture, SaveActionsServiceManager.getService());
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
        /* See gradle config. Previous implementation not compatible with intellij gradle plugin >= 1.6.0 */
        Path resources = Paths.get("./build/classes/java/resources");
        Path root = Paths.get(resources.toString(), getClass().getPackage().getName().split("[.]"));
        return root.toString();
    }

}
