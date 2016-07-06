package com.github.pshirshov;

import com.github.pshirshov.util.IdeaUtils;
import com.github.pshirshov.util.ScratchUtils;
import com.intellij.lang.Language;
import com.intellij.lang.StdLanguages;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.mysema.scalagen.ConversionSettings;
import com.mysema.scalagen.Converter;
import org.jetbrains.plugins.scala.ScalaLanguage;

import static com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE;


public class InvokeScalagenAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return;
        }
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        final Document document = editor.getDocument();
        final SelectionModel selectionModel = editor.getSelectionModel();
        final VirtualFile file = e.getData(VIRTUAL_FILE);
        assert file != null;
        final String bufferName = file.getNameWithoutExtension() + ".scala";

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                try {
                    final String sourceToConvert;
                    if (selectionModel.hasSelection()) {
                        sourceToConvert = selectionModel.getSelectedText();
                    } else {
                        sourceToConvert = document.getText();
                    }

                    ConversionSettings options = new ConversionSettings(true);
                    String replacement = Converter.instance().convert(sourceToConvert, options);

//                    if (document.isWritable()) {
//                        if (selectionModel.hasSelection()) {
//                            final int start = selectionModel.getSelectionStart();
//                            final int end = selectionModel.getSelectionEnd();
//                            document.replaceString(start, end, replacement);
//                        } else {
//                            document.setText(replacement);
//                        }
//                    }

                    ScratchUtils.doCreateNewScratch(project, bufferName, getScalaLanguage(), replacement);
                } catch (Throwable e) {
                    IdeaUtils.showErrorNotification("Scalagen failed", e);
                }
            }
        };

        WriteCommandAction.runWriteCommandAction(project, runnable);
        selectionModel.removeSelection();
    }




    public static Language getScalaLanguage() {
        Language language = Language.findLanguageByID(ScalaLanguage.Instance.getID());
        if (language == null) {
            language = StdLanguages.TEXT;
        }
        return language;
    }



}
