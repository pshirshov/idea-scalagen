package com.github.pshirshov.scratches;

import com.github.pshirshov.util.IdeaUtils;
import com.github.pshirshov.util.ScratchUtils;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.util.PathUtil;
import kotlin.text.Charsets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public abstract class OpenScratchAction extends AnAction {
    private static final Path CLASS_JAR = Paths.get(PathUtil.getJarPathForClass(OpenScratchAction.class));
    @Nullable
    private static final FileSystem JAR_FS;

    static {
        try {
            URI uri = new URI("jar", CLASS_JAR.toUri().toString(), null);
            if (CLASS_JAR.toFile().isDirectory()) {
                JAR_FS = null;
            } else {
                JAR_FS = FileSystems.newFileSystem(uri, new HashMap<>());
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        String snippet = getDefaultSnippet();
        final Language language = getLanguage();
        final String bufferName = System.currentTimeMillis() + "." + language.getAssociatedFileType().getDefaultExtension();
        ScratchUtils.doCreateNewScratch(project, bufferName, language, snippet);
    }


    @NotNull
    protected String getDefaultSnippet() {
        String snippet;
        try {
            final Path snippetPath;

            if (CLASS_JAR.toFile().isDirectory()) {
                snippetPath = CLASS_JAR.resolve(getDefaultSnippetName());
            } else {
                assert JAR_FS != null;
                snippetPath = JAR_FS.getPath(getDefaultSnippetName());
            }

            snippet = new String(Files.readAllBytes(snippetPath), Charsets.UTF_8);
        } catch (Throwable e1) {
            IdeaUtils.showErrorNotification("Can't read resource" , e1);
            snippet = "";
        }
        return snippet;
    }

    @NotNull
    protected abstract Language getLanguage();

    @NotNull
    protected abstract String getDefaultSnippetName();
}
