package com.github.pshirshov.util;

import com.intellij.featureStatistics.FeatureUsageTracker;
import com.intellij.ide.scratch.ScratchFileService;
import com.intellij.ide.scratch.ScratchRootType;
import com.intellij.lang.Language;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public final class ScratchUtils {
    private ScratchUtils() {
    }

    public static void doCreateNewScratch(
            @NotNull Project project
            , @NotNull String title
            , @NotNull Language language
            , @NotNull String text
    ) {
        FeatureUsageTracker.getInstance().triggerFeatureUsed("scratch");

        ScratchFileService.Option option = ScratchFileService.Option.create_new_always;
        VirtualFile f = ScratchRootType.getInstance().createScratchFile(project, title, language, text, option);
        if (f != null) {
            FileEditorManager.getInstance(project).openFile(f, true);
        }
    }

}
