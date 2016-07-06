package com.github.pshirshov.scratches;

import com.github.pshirshov.InvokeScalagenAction;
import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;


public class OpenScalaScratchAction extends OpenScratchAction {
    @NotNull
    @Override
    protected Language getLanguage() {
        return InvokeScalagenAction.getScalaLanguage();
    }


    @NotNull
    @Override
    protected String getDefaultSnippetName() {
        return "default-scratch.scala";
    }
}


