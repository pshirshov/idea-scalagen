package com.github.pshirshov.util;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;

public final class IdeaUtils {
    private IdeaUtils() {
    }

    public static void showErrorNotification(String title, Throwable t) {
        Notification notification = new Notification("scalagen", title, ExceptionUtils
                .toString(t), NotificationType.ERROR, null);
        ApplicationManager
                .getApplication()
                .getMessageBus()
                .syncPublisher(Notifications.TOPIC)
                .notify(notification);
    }

//                    JBPopupFactory.getInstance()
//                                  .createHtmlTextBalloonBuilder("Scalagen failed", MessageType.ERROR, null)
//                                  .createBalloon()
//                                  .showInCenterOf(editor.getComponent());
}
