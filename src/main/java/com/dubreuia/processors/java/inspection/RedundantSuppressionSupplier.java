package com.dubreuia.processors.java.inspection;

import com.intellij.codeInspection.LanguageInspectionSuppressors;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.RedundantSuppressInspection;
import com.intellij.codeInspection.RedundantSuppressionDetector;
import com.intellij.lang.java.JavaLanguage;

import java.util.function.Supplier;

public class RedundantSuppressionSupplier implements Supplier<LocalInspectionTool> {
    @Override
    public LocalInspectionTool get() {
        return (new RedundantSuppressInspection()).
                createLocalTool(
                        (RedundantSuppressionDetector) LanguageInspectionSuppressors.INSTANCE.forLanguage(JavaLanguage.INSTANCE)
                        , null
                        , null);
    }
}
