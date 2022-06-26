package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.web.bind.annotation.RequestMapping
import java.sql.Timestamp

@Table("notification_type")
data class NotificationType(@Id var type_id: Int, var name: String) {
}