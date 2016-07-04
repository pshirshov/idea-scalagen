import com.intellij.featureStatistics.FeatureUsageTracker;
import com.intellij.ide.scratch.ScratchFileService;
import com.intellij.ide.scratch.ScratchRootType;
import com.intellij.lang.Language;
import com.intellij.lang.StdLanguages;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.openapi.vfs.VirtualFile;
import com.mysema.scalagen.ConversionSettings;
import com.mysema.scalagen.Converter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.scala.ScalaLanguage;


public class ScalagenClass extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        final Document document = editor.getDocument();

        final SelectionModel selectionModel = editor.getSelectionModel();

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

                    if (document.isWritable()) {
                        if (selectionModel.hasSelection()) {
                            final int start = selectionModel.getSelectionStart();
                            final int end = selectionModel.getSelectionEnd();
                            document.replaceString(start, end, replacement);
                        } else {
                            document.setText(replacement);
                        }
                    } else {
                        Language language = getScalaLanguage();
                        doCreateNewScratch(project, false, language, replacement);
                    }
                } catch (Throwable e) {
                    String message = "Failed to invoke scalagen: "+e.getMessage();
                    JBPopupFactory.getInstance()
                                  .createHtmlTextBalloonBuilder(message, MessageType.ERROR, null)
                                  .createBalloon()
                                  .showInCenterOf(editor.getComponent());
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


    static void doCreateNewScratch(@NotNull Project project, boolean buffer, @NotNull Language language, @NotNull String text) {
        FeatureUsageTracker.getInstance().triggerFeatureUsed("scratch");

        String fileName = buffer ? "buffer" + nextBufferIndex() : "scratch";
        ScratchFileService.Option option = buffer ? ScratchFileService.Option.create_if_missing : ScratchFileService.Option.create_new_always;
        VirtualFile f = ScratchRootType.getInstance().createScratchFile(project, fileName, language, text, option);
        if (f != null) {
            FileEditorManager.getInstance(project).openFile(f, true);
        }
    }

    private static int ourCurrentBuffer = 0;
    private static int nextBufferIndex() {
        ourCurrentBuffer = (ourCurrentBuffer % Registry.intValue("ide.scratch.buffers")) + 1;
        return ourCurrentBuffer;
    }
}
