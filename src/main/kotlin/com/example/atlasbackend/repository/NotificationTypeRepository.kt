package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.NotificationType
import org.springframework.data.repository.CrudRepository

interface NotificationTypeRepository: CrudRepository<NotificationType,Int> {
}