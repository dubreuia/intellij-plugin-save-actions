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
        HashSet<String> inclusions = new HashSet<String>();
        HashSet<String> exclusions = new HashSet<String>();
        assertTrue(isIncludedAndNotExcluded("file", inclusions, exclusions));
    }


    @Test
    public void should_inclusion_work_over_unique_element() {
        HashSet<String> inclusions = new HashSet<String>(singletonList("included"));
        HashSet<String> exclusions = new HashSet<String>();
        assertTrue(isIncludedAndNotExcluded("included", inclusions, exclusions));
        assertFalse(isIncludedAndNotExcluded("file", inclusions, exclusions));
    }

    @Test
    public void should_exclusion_work_over_inclusion() {
        HashSet<String> inclusions = new HashSet<String>(singletonList("src/.*"));
        HashSet<String> exclusions = new HashSet<String>(singletonList("excluded"));
        assertTrue(isIncludedAndNotExcluded("src/included", inclusions, exclusions));
        assertFalse(isIncludedAndNotExcluded("src/excluded", inclusions, exclusions));
    }

}
