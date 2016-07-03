import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.mysema.scalagen.ConversionSettings;
import com.mysema.scalagen.Converter;


public class ScalagenClass extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        final Document document = editor.getDocument();

        final SelectionModel selectionModel = editor.getSelectionModel();
        if (!selectionModel.hasSelection()) {
            selectionModel.setSelection(0, document.getTextLength());
        }
        final int start = selectionModel.getSelectionStart();
        final int end = selectionModel.getSelectionEnd();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                try {
                    ConversionSettings options = new ConversionSettings(true);
                    String replacement = Converter.instance().convert(selectionModel.getSelectedText(), options);
                    document.replaceString(start, end, replacement);
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
}
