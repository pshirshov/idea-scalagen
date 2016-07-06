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

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public abstract class OpenScratchAction extends AnAction {
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
            final Path path = Paths.get(PathUtil.getJarPathForClass(getClass()));
            final Path snippetPath;

            if (path.toFile().isDirectory()) {
                snippetPath = path.resolve(getDefaultSnippetName());
            } else {
                URI uri = new URI("jar", path.toUri().toString(), null);
                FileSystem fileSystem = FileSystems.newFileSystem(uri, new HashMap<>());
                snippetPath = fileSystem.getPath(getDefaultSnippetName());
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
