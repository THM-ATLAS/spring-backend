package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.Notification
import com.example.atlasbackend.classes.NotificationType
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.PostMapping

@Repository
interface NotificationRepository:CrudRepository<Notification,Int> {

    @Query("SELECT * FROM notification n JOIN user_notification un ON n.notification_id = un.notification_id WHERE un.user_id = :user")
    fun getNotificationByUser(@Param("user") user_id:Int): List<Notification>

    @Query("SELECT user_id FROM user_notification WHERE notification_id = :notification")
    fun getUserIdByNotification(@Param("notification") notification_id: Int): Int

    @Query("DELETE FROM user_notification WHERE notification_id = :notification AND user_id = :user")
    @Modifying
    fun deleteByIdByUser(@Param("notification") notificationID: Int, @Param("user") userID: Int)

    @Query("DELETE FROM user_notification WHERE user_id = :user")
    @Modifying
    fun deleteByUser( @Param("user") userID: Int)

    @Query("INSERT INTO user_notification (user_id, notification_id, read) VALUES (:user, :notification, false)")
    @Modifying
    fun addNotificationByUser(@Param("user")userID: Int,@Param("notification")notificationID: Int)
}

