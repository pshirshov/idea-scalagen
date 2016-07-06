package com.github.pshirshov.scratches;

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.StdFileTypes;
import org.jetbrains.annotations.NotNull;

public class OpenJavaScratchAction extends OpenScratchAction {
    @NotNull
    @Override
    protected Language getLanguage() {
        return StdFileTypes.JAVA.getLanguage();
    }


    @NotNull
    @Override
    protected String getDefaultSnippetName() {
        return "default-scratch.java";
    }
}


