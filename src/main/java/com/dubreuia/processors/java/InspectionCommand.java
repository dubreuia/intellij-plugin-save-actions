package com.dubreuia.processors.java;

import com.dubreuia.core.ExecutionMode;
import com.dubreuia.model.Action;
import com.dubreuia.processors.ResultCode;
import com.dubreuia.processors.SaveCommand;
import com.intellij.codeInspection.GlobalInspectionContext;
import com.intellij.codeInspection.InspectionEngine;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.QuickFix;
import com.intellij.codeInspection.ex.InspectionToolWrapper;
import com.intellij.codeInspection.ex.LocalInspectionToolWrapper;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.dubreuia.core.component.SaveActionManager.LOGGER;
import static com.dubreuia.processors.ResultCode.OK;

/**
 * Implements a {@link SaveCommand} for inspections commands.
 */
class InspectionCommand extends SaveCommand {

    private final LocalInspectionTool inspectionTool;

    InspectionCommand(Project project, Set<PsiFile> psiFiles, Set<ExecutionMode> modes, Action action,
                      LocalInspectionTool inspectionTool) {
        super(project, psiFiles, modes, action);
        this.inspectionTool = inspectionTool;
    }

    @Override
    protected void run(@NotNull Result<ResultCode> result) {
        InspectionManager inspectionManager = InspectionManager.getInstance(getProject());
        GlobalInspectionContext context = inspectionManager.createNewGlobalContext(false);
        InspectionToolWrapper toolWrapper = new LocalInspectionToolWrapper(inspectionTool);
        for (PsiFile psiFile : getPsiFiles()) {
            List<ProblemDescriptor> problemDescriptors = getProblemDescriptors(context, toolWrapper, psiFile);
            for (ProblemDescriptor problemDescriptor : problemDescriptors) {
                QuickFix[] fixes = problemDescriptor.getFixes();
                if (fixes != null) {
                    writeQuickFixes(problemDescriptor, fixes);
                }
            }
        }
        result.setResult(OK);
    }

    private List<ProblemDescriptor> getProblemDescriptors(GlobalInspectionContext context,
                                                          InspectionToolWrapper toolWrapper,
                                                          PsiFile psiFile) {
        List<ProblemDescriptor> problemDescriptors;
        try {
            problemDescriptors = InspectionEngine.runInspectionOnFile(psiFile, toolWrapper, context);
        } catch (IndexNotReadyException exception) {
            LOGGER.info("Cannot inspect files: index not ready (" + exception.getMessage() + ")");
            return Collections.emptyList();
        }
        return problemDescriptors;
    }

    private void writeQuickFixes(ProblemDescriptor problemDescriptor, QuickFix[] fixes) {
        for (QuickFix fix : fixes) {
            @SuppressWarnings("unchecked")
            QuickFix<ProblemDescriptor> typedFix = (QuickFix<ProblemDescriptor>) fix;
            if (fix != null) {
                try {
                    typedFix.applyFix(getProject(), problemDescriptor);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }

}
