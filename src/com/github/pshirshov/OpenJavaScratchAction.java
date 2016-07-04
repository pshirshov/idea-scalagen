package com.github.pshirshov;

import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;

public class OpenJavaScratchAction extends AnAction {
    private static final String JAVA_SCRATCH = "public class Scratch {\n" +
            "    public static void main(String[] args) {\n" +
            "        System.out.println(\"Hi!\");\n" +
            "    }\n" +
            '}';

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        Language language = StdFileTypes.JAVA.getLanguage();
        ScalagenClass.doCreateNewScratch(project, false, language, JAVA_SCRATCH);
    }
}


