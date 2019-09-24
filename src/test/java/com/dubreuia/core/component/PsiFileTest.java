package com.dubreuia.core.component;


import com.dubreuia.model.ProjectStorage;
import com.dubreuia.model.Storage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;

class PsiFileTest {

    @ParameterizedTest(name = "[{index}] included={0}, psiFile={1}, inclusion={2}, exclusion={3}")
    @MethodSource("parameters")
    void test(boolean included, String psiFile, String inclusion, String exclusion) {
        Storage storage = new ProjectStorage();
        storage.setInclusions(toSet(inclusion));
        storage.setExclusions(toSet(exclusion));

        Engine engine = new Engine(storage, null, null, null, null, null);

        assertThat(engine.isIncludedAndNotExcluded(psiFile))
                .isEqualTo(included);
    }

    static Stream<Arguments> parameters() {
        return Stream.of(
                // Only excludes - taken from PsiFilesIsUrlIncludedTest
                // Default cases and invalid regex
                Arguments.of(true, null, "", "*"),
                Arguments.of(true, "file", "", ""),
                Arguments.of(true, "/home/alex/projects/project1/ignore.java", "", "*"),

                // Base cases
                Arguments.of(false, "/project/Ignore.java", "", "Ignore.java"),
                Arguments.of(true, "/project/Ignore.java", "", "ignore.java"),
                Arguments.of(false, "/project/Ignore.java", "", ".*\\.java"),

                // With different project strings
                Arguments.of(true, "/home/alex/projects/project1/ignore.java", "", ".*\\.properties"),
                Arguments.of(false, "/home/alex/projects/project1/ignore.properties", "", ".*\\.properties"),
                Arguments.of(false, "c://projects/project/ignore.properties", "", ".*\\.properties"),

                // With specific paths
                Arguments.of(true, "/home/alex/projects/project1/ignore.properties", "", "src/.*\\.properties"),
                Arguments.of(false, "/home/alex/projects/project1/src/ignore.properties", "", "src/.*\\.properties"),

                // With specific folders recursive
                Arguments.of(false, "/project1/src/ignore/Ignore.java", "", "ignore/.*"),
                Arguments.of(false, "/project1/src/ignore/sub/Ignore.java", "", "ignore/.*"),
                Arguments.of(false, "/project1/src/ignore/sub/Ignore.java", "", "ignore/.*\\.java"),
                Arguments.of(false, "/project1/src/ignore/sub/Ignore.java", "", "ignore/.*/.*\\.java"),

                // Only includes
                Arguments.of(true, "/project/Include.java", "Include.java", ""),
                Arguments.of(false, "/project/Include.java", "include.java", ""),
                Arguments.of(true, "/project/Include.java", ".*\\.java", ""),

                // Includes and excludes
                Arguments.of(false, "/project/Include.java", ".*\\.java", ".*\\.java"),
                Arguments.of(true, "/project/Include.java", ".*\\.java", ".*\\.xml"),
                Arguments.of(false, "/project/Include.xml", ".*\\.java", ".*\\.xml")
        );
    }

    private Set<String> toSet(String inclusion) {
        if (null == inclusion || inclusion.isEmpty()) {
            return emptySet();
        }
        return singleton(inclusion);
    }

}
