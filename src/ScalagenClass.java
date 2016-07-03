import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.mysema.scalagen.ConversionSettings;
import com.mysema.scalagen.Converter;


public class ScalagenClass extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        //Get all the required data from data keys
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        //Access document, caret, and selection
        final Document document = editor.getDocument();
        final SelectionModel selectionModel = editor.getSelectionModel();

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
                    throw new RuntimeException(e);
                }
            }
        };

        WriteCommandAction.runWriteCommandAction(project, runnable);
        selectionModel.removeSelection();
    }
}
