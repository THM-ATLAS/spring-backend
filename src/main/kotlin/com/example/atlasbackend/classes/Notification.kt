package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.sql.Timestamp

@Table("notification")
data class Notification(@Id var notification_id: Int,
                        var title: String,
                        var content: String,
                        var time: Timestamp,
                        var type_id: Int,
                        var module_id: Int?,
                        var exercise_id: Int?,
                        var submission_id: Int?) {
}