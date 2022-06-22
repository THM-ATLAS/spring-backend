package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.Notification
import com.example.atlasbackend.classes.NotificationType
import com.example.atlasbackend.service.NotificationService
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/")
class NotificationController(var notificationService: NotificationService) {

    @GetMapping("notifications/types")
    fun getNotificationTypes(@Parameter(hidden = true) @AuthenticationPrincipal user: AtlasUser): List<NotificationType> {
        return notificationService.loadNotificationTypes(user)
    }

    @GetMapping("notifications/users/{userID}")
    fun getNotificationByUser(@Parameter(hidden = true) @AuthenticationPrincipal user: AtlasUser, @PathVariable userID: Int): List<Notification> {
        return notificationService.loadNotificationByUser(user, userID)
    }

    @DeleteMapping("notifications/user/{notificationID}/{userID}")
    fun removeNotificationByUser(@Parameter(hidden = true) @AuthenticationPrincipal user: AtlasUser, @PathVariable notificationID: Int, @PathVariable userID: Int): Notification{
        return notificationService.removeNotification(user, notificationID, userID)
    }

    @DeleteMapping("notifications/user/{userID}")
    fun removeNotificationsByUser(@Parameter(hidden = true) @AuthenticationPrincipal user: AtlasUser, @PathVariable userID: Int): List<Notification>{
        return notificationService.removeNotificationByUser(user, userID)
    }
}