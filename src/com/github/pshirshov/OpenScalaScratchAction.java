package com.github.pshirshov;

import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;

public class OpenScalaScratchAction extends AnAction {
    private static final String SCALA_SCRATCH = "class Scratch {\n" +
            "  \n" +
            "}\n" +
            '\n' +
            "object Scratch {\n" +
            "  def main(args: Array[String]) {\n" +
            "    println(\"hi\")\n" +
            "  }  \n" +
            "}\n";

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        Language language = ScalagenClass.getScalaLanguage();
        ScalagenClass.doCreateNewScratch(project, false, language, SCALA_SCRATCH);
    }
}


