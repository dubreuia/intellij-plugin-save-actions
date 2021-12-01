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
package com.intellij.codeInspection.visibility;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.codeInsight.daemon.impl.UnusedSymbolUtil;
import com.intellij.codeInspection.InspectionProfile;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.deadCode.UnusedDeclarationInspectionBase;
import com.intellij.openapi.progress.EmptyProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Comparing;
import com.intellij.profile.codeInspection.InspectionProjectProfileManager;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.JavaRecursiveElementWalkingVisitor;
import com.intellij.psi.LambdaUtil;
import com.intellij.psi.PsiAnonymousClass;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiEnumConstant;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiKeyword;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiNewExpression;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiSyntheticClass;
import com.intellij.psi.PsiTypeParameter;
import com.intellij.psi.SyntheticElement;
import com.intellij.psi.search.searches.FunctionalExpressionSearch;
import com.intellij.psi.util.ClassUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.util.VisibilityUtil;
import com.intellij.util.containers.ContainerUtil;
import com.siyeh.ig.GroupDisplayNameUtil;
import com.siyeh.ig.fixes.ChangeModifierFix;
import com.siyeh.ig.psiutils.MethodUtils;
import gnu.trove.TObjectIntHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Copy pasting because: cannot extend. Do not reformat (useful for diffs)
 *
 * @see com.intellij.codeInspection.visibility.AccessCanBeTightenedInspection
 */
@SuppressWarnings("deprecation")
public class CustomAccessCanBeTightenedInspection extends com.intellij.codeInspection.BaseJavaBatchLocalInspectionTool {
  private final VisibilityInspection myVisibilityInspection;

  public CustomAccessCanBeTightenedInspection(@NotNull VisibilityInspection visibilityInspection) {
    myVisibilityInspection = visibilityInspection;
  }

  @Override
  public boolean isEnabledByDefault() {
    return true;
  }

  @Override
  @NotNull
  public String getGroupDisplayName() {
    return "group.names.visibility.issues";
  }

  @Override
  @NotNull
  public String getDisplayName() {
    return "Member access can be tightened";
  }

  @Override
  @NotNull
  public String getShortName() {
    return VisibilityInspection.SHORT_NAME;
  }

  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
    return new MyVisitor(holder);
  }

  private class MyVisitor extends JavaElementVisitor {
    private final ProblemsHolder myHolder;
    private final UnusedDeclarationInspectionBase myDeadCodeInspection;

    public MyVisitor(@NotNull ProblemsHolder holder) {
      myHolder = holder;
      InspectionProfile profile = InspectionProjectProfileManager.getInstance(holder.getProject()).getCurrentProfile();
      UnusedDeclarationInspectionBase tool = (UnusedDeclarationInspectionBase)profile.getUnwrappedTool(UnusedDeclarationInspectionBase.SHORT_NAME, holder.getFile());
      myDeadCodeInspection = tool == null ? new UnusedDeclarationInspectionBase() : tool;
    }
    private final TObjectIntHashMap<PsiClass> maxSuggestedLevelForChildMembers = new TObjectIntHashMap<>();

    @Override
    public void visitClass(PsiClass aClass) {
      checkMember(aClass);
    }

    @Override
    public void visitMethod(PsiMethod method) {
      checkMember(method);
    }

    @Override
    public void visitField(PsiField field) {
      checkMember(field);
    }

    private void checkMember(@NotNull PsiMember member) {
      PsiClass memberClass = member.getContainingClass();
      PsiModifierList memberModifierList = member.getModifierList();
      if (memberModifierList == null) {
        return;
      }
      int currentLevel = PsiUtil.getAccessLevel(memberModifierList);
      int suggestedLevel = suggestLevel(member, memberClass, currentLevel);
      if (memberClass != null) {
        synchronized (maxSuggestedLevelForChildMembers) {
          int prevMax = maxSuggestedLevelForChildMembers.get(memberClass);
          maxSuggestedLevelForChildMembers.put(memberClass, Math.max(prevMax, suggestedLevel));
        }
      }

      log(member.getName() + ": effective level is '" + PsiUtil.getAccessModifier(suggestedLevel) + "'");

      if (suggestedLevel < currentLevel) {
        if (member instanceof PsiClass) {
          int memberMaxLevel;
          synchronized (maxSuggestedLevelForChildMembers) {
            memberMaxLevel = maxSuggestedLevelForChildMembers.get((PsiClass)member);
          }
          if (memberMaxLevel > suggestedLevel) {
            // a class can't have visibility less than its members
            return;
          }
        }
        PsiElement toHighlight = currentLevel == PsiUtil.ACCESS_LEVEL_PACKAGE_LOCAL ? ((PsiNameIdentifierOwner)member).getNameIdentifier() : ContainerUtil.find(
          memberModifierList.getChildren(),
          element -> element instanceof PsiKeyword && element.getText().equals(PsiUtil.getAccessModifier(currentLevel)));
        assert toHighlight != null : member +" ; " + ((PsiNameIdentifierOwner)member).getNameIdentifier() + "; "+ memberModifierList.getText();
        String suggestedModifier = PsiUtil.getAccessModifier(suggestedLevel);
        myHolder.registerProblem(toHighlight, "Access can be " + VisibilityUtil.toPresentableText(suggestedModifier), new ChangeModifierFix(suggestedModifier));
      }
    }

    @PsiUtil.AccessLevel
    private int suggestLevel(@NotNull PsiMember member, PsiClass memberClass, @PsiUtil.AccessLevel int currentLevel) {
      if (member.hasModifierProperty(PsiModifier.PRIVATE) || member.hasModifierProperty(PsiModifier.NATIVE)) {
        return currentLevel;
      }
      if (member instanceof PsiMethod && member instanceof SyntheticElement || !member.isPhysical()) {
        return currentLevel;
      }

      if (member instanceof PsiMethod) {
        PsiMethod method = (PsiMethod)member;
        if (!method.getHierarchicalMethodSignature().getSuperSignatures().isEmpty()) {
          log(member.getName() + " overrides");
          return currentLevel; // overrides
        }
        if (MethodUtils.isOverridden(method)) {
          log(member.getName() + " overridden");
          return currentLevel;
        }
      }
      if (member instanceof PsiEnumConstant) {
        return currentLevel;
      }
      if (member instanceof PsiClass && (member instanceof PsiAnonymousClass ||
                                         member instanceof PsiTypeParameter ||
                                         member instanceof PsiSyntheticClass ||
                                         PsiUtil.isLocalClass((PsiClass)member))) {
        return currentLevel;
      }
      if (memberClass != null && (memberClass.isInterface() || memberClass.isEnum() || memberClass.isAnnotationType() || PsiUtil.isLocalClass(memberClass) && member instanceof PsiClass)) {
        return currentLevel;
      }
      PsiFile memberFile = member.getContainingFile();
      Project project = memberFile.getProject();

      if (myDeadCodeInspection.isEntryPoint(member)) {
        log(member.getName() +" is entry point");
        return currentLevel;
      }

      PsiDirectory memberDirectory = memberFile.getContainingDirectory();
      PsiPackage memberPackage = memberDirectory == null ? null : JavaDirectoryService.getInstance().getPackage(memberDirectory);
      log(member.getName()+ ": checking effective level for "+member);

      AtomicInteger maxLevel = new AtomicInteger(PsiUtil.ACCESS_LEVEL_PRIVATE);
      AtomicBoolean foundUsage = new AtomicBoolean();
      boolean proceed = UnusedSymbolUtil.processUsages(project, memberFile, member, new EmptyProgressIndicator(), null, info -> {
        PsiElement element = info.getElement();
        if (element == null) {
          return true;
        }
        PsiFile psiFile = info.getFile();
        if (psiFile == null) {
          return true;
        }

        return handleUsage(member, memberClass, memberFile, maxLevel, memberPackage, element, psiFile, foundUsage);
      });

      if (proceed && member instanceof PsiClass && LambdaUtil.isFunctionalClass((PsiClass)member)) {
        // there can be lambda implementing this interface implicitly
        FunctionalExpressionSearch.search((PsiClass)member).forEach(functionalExpression -> {
          PsiFile psiFile = functionalExpression.getContainingFile();
          return handleUsage(member, memberClass, memberFile, maxLevel, memberPackage, functionalExpression, psiFile, foundUsage);
        });
      }
      if (!foundUsage.get()) {
        log(member.getName() + " unused; ignore");
        return currentLevel; // do not propose private for unused method
      }

      @PsiUtil.AccessLevel
      int suggestedLevel = maxLevel.get();
      if (suggestedLevel == PsiUtil.ACCESS_LEVEL_PRIVATE && memberClass == null) {
        suggestedLevel = suggestPackageLocal(member);
      }

      String suggestedModifier = PsiUtil.getAccessModifier(suggestedLevel);
      log(member.getName() + ": effective level is '" + suggestedModifier + "'");

      return suggestedLevel;
    }


    private boolean handleUsage(@NotNull PsiMember member,
                                @Nullable PsiClass memberClass,
                                @NotNull PsiFile memberFile,
                                @NotNull AtomicInteger maxLevel,
                                @Nullable PsiPackage memberPackage,
                                @NotNull PsiElement element,
                                @NotNull PsiFile psiFile,
                                @NotNull AtomicBoolean foundUsage) {
      foundUsage.set(true);
      if (!(psiFile instanceof PsiJavaFile)) {
        log("     refd from " + psiFile.getName() + "; set to public");
        maxLevel.set(PsiUtil.ACCESS_LEVEL_PUBLIC);
        return false; // referenced from XML, has to be public
      }
      @PsiUtil.AccessLevel
      int level = getEffectiveLevel(element, psiFile, member, memberFile, memberClass, memberPackage);
      log("    ref in file " + psiFile.getName() + "; level = " + PsiUtil.getAccessModifier(level) + "; (" + element + ")");
      maxLevel.getAndAccumulate(level, Math::max);

      return level != PsiUtil.ACCESS_LEVEL_PUBLIC;
    }

    @PsiUtil.AccessLevel
    private int getEffectiveLevel(@NotNull PsiElement element,
                                  @NotNull PsiFile file,
                                  @NotNull PsiMember member,
                                  @NotNull PsiFile memberFile,
                                  PsiClass memberClass,
                                  PsiPackage memberPackage) {
      PsiClass innerClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
      boolean isAbstractMember = member.hasModifierProperty(PsiModifier.ABSTRACT);
      if (memberClass != null && PsiTreeUtil.isAncestor(innerClass, memberClass, false) ||
          innerClass != null && PsiTreeUtil.isAncestor(memberClass, innerClass, false) && !innerClass.hasModifierProperty(PsiModifier.STATIC)) {
        // access from the same file can be via private
        // except when used in annotation:
        // @Ann(value = C.VAL) class C { public static final String VAL = "xx"; }
        // or in implements/extends clauses
        if (isInReferenceList(innerClass.getModifierList(), member) ||
            isInReferenceList(innerClass.getImplementsList(), member) ||
            isInReferenceList(innerClass.getExtendsList(), member)) {
          return suggestPackageLocal(member);
        }

        return !isAbstractMember && (myVisibilityInspection.SUGGEST_PRIVATE_FOR_INNERS ||
               !isInnerClass(memberClass)) ? PsiUtil.ACCESS_LEVEL_PRIVATE : suggestPackageLocal(member);
      }
      //if (file == memberFile) {
      //  return PsiUtil.ACCESS_LEVEL_PACKAGE_LOCAL;
      //}
      PsiDirectory directory = file.getContainingDirectory();
      PsiPackage aPackage = directory == null ? null : JavaDirectoryService.getInstance().getPackage(directory);
      if (aPackage == memberPackage || aPackage != null && memberPackage != null && Comparing.strEqual(aPackage.getQualifiedName(), memberPackage.getQualifiedName())) {
        return suggestPackageLocal(member);
      }
      if (innerClass != null && memberClass != null && innerClass.isInheritor(memberClass, true)) {
        //access from subclass can be via protected, except for constructors
        PsiElement resolved = element instanceof PsiReference ? ((PsiReference)element).resolve() : null;
        boolean isConstructor = resolved instanceof PsiClass && element.getParent() instanceof PsiNewExpression
                                || resolved instanceof PsiMethod && ((PsiMethod)resolved).isConstructor();
        if (!isConstructor) {
          return PsiUtil.ACCESS_LEVEL_PROTECTED;
        }
      }
      return PsiUtil.ACCESS_LEVEL_PUBLIC;
    }
  }

  private static boolean isInnerClass(@NotNull PsiClass memberClass) {
    return memberClass.getContainingClass() != null || memberClass instanceof PsiAnonymousClass;
  }

  private static boolean isInReferenceList(@Nullable PsiElement list, @NotNull PsiMember member) {
    if (list == null) {
      return false;
    }
    PsiManager psiManager = member.getManager();
    boolean[] result = new boolean[1];
    list.accept(new JavaRecursiveElementWalkingVisitor() {
      @Override
      public void visitReferenceElement(PsiJavaCodeReferenceElement reference) {
        super.visitReferenceElement(reference);
        if (psiManager.areElementsEquivalent(reference.resolve(), member)) {
          result[0] = true;
          stopWalking();
        }
      }
    });
    return result[0];
  }

  @PsiUtil.AccessLevel
  private int suggestPackageLocal(@NotNull PsiMember member) {
    boolean suggestPackageLocal = member instanceof PsiClass && ClassUtil.isTopLevelClass((PsiClass)member)
                ? myVisibilityInspection.SUGGEST_PACKAGE_LOCAL_FOR_TOP_CLASSES
                : myVisibilityInspection.SUGGEST_PACKAGE_LOCAL_FOR_MEMBERS;
    return suggestPackageLocal ? PsiUtil.ACCESS_LEVEL_PACKAGE_LOCAL : PsiUtil.ACCESS_LEVEL_PUBLIC;
  }

  private static void log(String s) {
    //System.out.println(s);
  }
}
