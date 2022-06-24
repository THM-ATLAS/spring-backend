package com.example.atlasbackend.controller

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.Notification
import com.example.atlasbackend.classes.NotificationRead
import com.example.atlasbackend.classes.NotificationType
import com.example.atlasbackend.service.NotificationService
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/")
class NotificationController(var notificationService: NotificationService) {

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns all Types of Notifications"),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("notifications/types")
    fun getNotificationTypes(@Parameter(hidden = true) @AuthenticationPrincipal user: AtlasUser): List<NotificationType> {
        return notificationService.loadNotificationTypes(user)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns all Notifications of a User"),
                ApiResponse(responseCode = "404", description = "UserNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("notifications/user/{userID}")
    fun getNotificationByUser(@Parameter(hidden = true) @AuthenticationPrincipal user: AtlasUser, @PathVariable userID: Int): List<NotificationRead> {
        return notificationService.loadNotificationsByUser(user, userID)
    }
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Returns all Notification sent by a Module"),
                ApiResponse(responseCode = "404", description = "ModuleNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "AccessDeniedException", content = [Content(schema = Schema(hidden = true))])
            ])
    @GetMapping("notifications/module/{moduleID}")
    fun removeModuleNotificationsByModule(@Parameter(hidden = true) @AuthenticationPrincipal user: AtlasUser,@PathVariable moduleID: Int): List<Notification>{
        return notificationService.loadNotificationsByModule(user,moduleID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Sends a Notification to every user"),
                ApiResponse(responseCode = "400", description = "InvalidNotificationIDException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToPostNotificationException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PostMapping("notifications")
    fun postNotificationForAll(@Parameter(hidden = true) @AuthenticationPrincipal user: AtlasUser, @RequestBody notification: Notification){
        return notificationService.addNotificationForAll(user,notification)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Sends a Notification to every module member"),
                ApiResponse(responseCode = "400", description = "InvalidNotificationIDException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "404", description = "ModuleNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToPostNotificationException", content = [Content(schema = Schema(hidden = true))])
            ])
    @PostMapping("notifications/module")
    fun postNotificationForModule(@Parameter(hidden = true) @AuthenticationPrincipal user: AtlasUser, @RequestBody notification: Notification){
        return notificationService.addNotificationForModule(user,notification)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Removes a notification for a user"),
                ApiResponse(responseCode = "404", description = "NotificationNotFoundException || UserNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToRemoveNotificationRelationException", content = [Content(schema = Schema(hidden = true))])
            ])
    @DeleteMapping("notifications/user/{notificationID}/{userID}")
    fun removeNotificationByUser(@Parameter(hidden = true) @AuthenticationPrincipal user: AtlasUser, @PathVariable notificationID: Int, @PathVariable userID: Int): Notification{
        return notificationService.removeNotificationByUser(user, notificationID, userID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Removes all notifications for a user"),
                ApiResponse(responseCode = "404", description = "UserNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToRemoveNotificationRelationException", content = [Content(schema = Schema(hidden = true))])
            ])
    @DeleteMapping("notifications/user/{userID}")
    fun removeNotificationsByUser(@Parameter(hidden = true) @AuthenticationPrincipal user: AtlasUser, @PathVariable userID: Int): List<Notification>{
        return notificationService.removeNotificationsByUser(user, userID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Removes a notification for all"),
                ApiResponse(responseCode = "404", description = "NotificationNotFoundException || ModuleNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToDeleteNotificationException", content = [Content(schema = Schema(hidden = true))])
            ])
    @DeleteMapping("notifications/module/{notificationID}/{moduleID}")
    fun removeNotificationFromModule(@Parameter(hidden = true) @AuthenticationPrincipal user: AtlasUser, @PathVariable notificationID: Int, @PathVariable moduleID: Int): Notification{
        return  notificationService.removeNotificationFromModule(user, notificationID, moduleID)
    }

    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK - Removes a notification for all"),
                ApiResponse(responseCode = "404", description = "NotificationNotFoundException", content = [Content(schema = Schema(hidden = true))]),
                ApiResponse(responseCode = "403", description = "NoPermissionToDeleteNotificationException", content = [Content(schema = Schema(hidden = true))])
            ])
    @DeleteMapping("notifications/{notificationID}")
    fun removeNotification(@Parameter(hidden = true) @AuthenticationPrincipal user: AtlasUser,@PathVariable notificationID: Int): Notification{
        return notificationService.removeNotification(user,notificationID)
    }

}