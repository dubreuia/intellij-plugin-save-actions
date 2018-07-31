package com.dubreuia.utils;

import org.junit.Test;

import java.util.HashSet;

import static com.dubreuia.utils.PsiFiles.isIncludedAndNotExcluded;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PsiFilesTest {

    @Test
    public void should_all_file_included_if_no_inclusion() {
        HashSet<String> inclusions = new HashSet<>();
        HashSet<String> exclusions = new HashSet<>();
        assertTrue(isIncludedAndNotExcluded("file", inclusions, exclusions));
    }

    @Test
    public void should_inclusion_work_over_unique_element() {
        HashSet<String> inclusions = new HashSet<>(singletonList("included"));
        HashSet<String> exclusions = new HashSet<>();
        assertTrue(isIncludedAndNotExcluded("included", inclusions, exclusions));
        assertFalse(isIncludedAndNotExcluded("file", inclusions, exclusions));
    }

    @Test
    public void should_exclusion_work_over_inclusion() {
        HashSet<String> inclusions = new HashSet<>(singletonList("src/.*"));
        HashSet<String> exclusions = new HashSet<>(singletonList("excluded"));
        assertTrue(isIncludedAndNotExcluded("src/included", inclusions, exclusions));
        assertFalse(isIncludedAndNotExcluded("src/excluded", inclusions, exclusions));
    }

}
