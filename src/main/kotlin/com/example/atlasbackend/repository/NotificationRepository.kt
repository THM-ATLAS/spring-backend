package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.Notification
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationRepository:CrudRepository<Notification,Int> {

}