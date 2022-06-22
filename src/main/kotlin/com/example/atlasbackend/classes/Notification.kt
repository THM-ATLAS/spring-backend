package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("notification")
data class Notification(@Id var notification_id: Int, var title: String, var content: String, var read: Boolean, var type: String, var exercise_id: String, var submission_id: Int) {
}