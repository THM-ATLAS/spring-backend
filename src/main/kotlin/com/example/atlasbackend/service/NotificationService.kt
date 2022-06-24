package com.example.atlasbackend.service

import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.Notification
import com.example.atlasbackend.classes.NotificationRead
import com.example.atlasbackend.classes.NotificationType
import com.example.atlasbackend.exception.*
import com.example.atlasbackend.repository.*
import org.springframework.stereotype.Service

@Service
class NotificationService(var notifRep: NotificationRepository, var notifTyRep: NotificationTypeRepository,var userRep: UserRepository, var exRep: ExerciseRepository, var subRep: SubmissionRepository, var modRep: ModuleRepository) {
    fun loadNotificationTypes(user: AtlasUser): List<NotificationType> {

        // Error Catching
        if (!user.roles.any { r -> r.role_id == 1 }) throw AccessDeniedException  // Check for admin

        // Functionality
        return notifTyRep.findAll().toList()
    }

    fun loadNotificationsByUser(user: AtlasUser, userID: Int): List<NotificationRead> {
        // Error Catching
        if (!userRep.existsById(userID)) throw UserNotFoundException
        if (!user.roles.any { r -> r.role_id == 1 } &&   // Check for admin
                user.user_id != userID)     //Check for self
            throw AccessDeniedException

        // Functionality
        val nr = notifRep.getNotificationsByUser(userID).map { n ->
            NotificationRead(n.notification_id, n.title, n.content, n.time, notifRep.getReadStatus(userID, n.notification_id), n.type_id, n.module_id, n.exercise_id, n.submission_id)
        }
        return nr
    }

    fun loadNotificationsByModule(user: AtlasUser, moduleID: Int): List<Notification> {
        // Error Catching
        if (!modRep.existsById(moduleID))throw ModuleNotFoundException
        if (!user.roles.any { r -> r.role_id == 1} &&   // Check for admin
                !modRep.getUsersByModule(moduleID).any { m -> m.user_id == user.user_id })   // Check if user in module
            throw AccessDeniedException

        // Functionality
        return notifRep.getNotificationsByModule(moduleID)
    }


    fun removeNotificationByUser(user: AtlasUser, notificationID: Int, userID: Int): Notification {
        // Error Catching
        if (!notifRep.existsById(notificationID)) throw NotificationNotFoundException
        if (!userRep.existsById(userID)) throw UserNotFoundException
        if (!user.roles.any { r -> r.role_id == 1 } &&   // Check for admin
                user.user_id != notifRep.getUserIdByNotification(notificationID))   // Check for self
            throw NoPermissionToRemoveNotificationRelationException

        // Functionality
        val n = notifRep.findById(notificationID).get()
        notifRep.deleteByIdByUser(notificationID, userID)
        if (notifRep.countNotificationRelations(notificationID) == 0) notifRep.deleteById(notificationID)
        return n
    }

    fun removeNotificationsByUser(user: AtlasUser, userID: Int): List<Notification> {
        // Error Catching
        if (!userRep.existsById(userID)) throw UserNotFoundException
        if (!user.roles.any { r -> r.role_id == 1 } &&   // Check for admin
                user.user_id != userID)     // Check for self
            throw NoPermissionToRemoveNotificationRelationException

        // Functionality
        val n = notifRep.getNotificationsByUser(userID)
        n.forEach { nn -> if (notifRep.countNotificationRelations(nn.notification_id) == 0) notifRep.deleteById(nn.notification_id) }
        notifRep.deleteByUser(userID)
        return n
    }

    fun addNotificationForAll(user: AtlasUser, n: Notification) {
        // Error Catching
        if (n.notification_id != 0) throw InvalidNotificationIDException
        if (!user.roles.any { r -> r.role_id == 1 }) throw NoPermissionToPostNotificationException  // Check for admin

        // Functionality
        notifRep.save(n)
        userRep.findAll().forEach { u ->
            notifRep.addNotificationByUser(u.user_id, n.notification_id)
        }
    }

    fun addNotificationForModule(user: AtlasUser, n: Notification) {
        // Error Catching
        if (n.notification_id != 0) throw InvalidNotificationIDException
        if (modRep.existsById(n.module_id!!).not()) throw ModuleNotFoundException
        if (!user.roles.any { r -> r.role_id == 1 } &&   // Check for admin
                modRep.getModuleRoleByUser(user.user_id, n.module_id!!).let { mru -> mru == null || mru.role_id > 3 })   // Check for tutor/teacher
            throw NoPermissionToPostNotificationException

        // Functionality
        notifRep.save(n)
        modRep.getUsersByModule(n.module_id!!).forEach { u ->
            notifRep.addNotificationByUser(u.user_id, n.notification_id)
        }
    }

    fun removeNotificationFromModule(user: AtlasUser, notificationID: Int,moduleID: Int): Notification {
        // Error Catching
        if (!notifRep.existsById(notificationID))throw NotificationNotFoundException
        if (!modRep.existsById(moduleID)) throw ModuleNotFoundException
        if (!user.roles.any { r -> r.role_id == 1 } &&   // Check for admin
                modRep.getModuleRoleByUser(user.user_id, moduleID).let { mru -> mru == null || mru.role_id > 3 })   // Check for tutor/teacher
            throw  NoPermissionToDeleteNotificationException

        // Functionality
        val n = notifRep.findById(notificationID).get()
        notifRep.deleteById(notificationID)
        return n
    }

    fun removeNotification(user: AtlasUser, notificationID: Int): Notification {
        // Error Catching
        if (!notifRep.existsById(notificationID) ) throw NotificationNotFoundException
        if (!user.roles.any { r -> r.role_id == 1 }) throw NoPermissionToDeleteNotificationException  // Check for admin

        // Functionality
        val n = notifRep.findById(notificationID).get()
        notifRep.deleteById(notificationID)
        return n
    }
}

