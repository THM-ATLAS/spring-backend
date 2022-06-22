package com.example.atlasbackend.service

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.Notification
import com.example.atlasbackend.classes.NotificationType
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.*
import org.springframework.stereotype.Service

@Service
public class NotificationService(var notifRep: NotificationRepository, var notifTyRep: NotificationTypeRepository) {
    fun loadNotificationTypes(user: AtlasUser): List<NotificationType> {

        // Error Catching
        if (!user.roles.any { r -> r.role_id == 1}) throw AccessDeniedException  // Check for admin

        // Functionality
        return notifTyRep.findAll().toList()
    }

    fun loadNotificationByUser(user: AtlasUser, userID: Int): List<Notification> {
        // Error Catching
        if (!user.roles.any { r -> r.role_id == 1} &&
            user.user_id != userID)
            throw AccessDeniedException

        // Functionality
        return notifRep.getNotificationByUser(userID)
    }

    fun removeNotification(user: AtlasUser, notificationID: Int, userID: Int): Notification {
        // Error Catching
        if (!user.roles.any { r -> r.role_id == 1} &&
            user.user_id != notifRep.getUserIdByNotification(notificationID))
            throw AccessDeniedException

        // Functionality
        val notification = notifRep.findById(notificationID).get()
        notifRep.deleteByIdByUser(notificationID, userID)
        return notification
    }

    fun removeNotificationByUser(user: AtlasUser, userID: Int): List<Notification> {
        // Error Catching
        if (!user.roles.any { r -> r.role_id == 1} &&
                user.user_id != userID)
            throw AccessDeniedException

        // Functionality
        val notifications = notifRep.getNotificationByUser(userID)
        notifRep.deleteByUser(userID)
        return notifications
    }
}
