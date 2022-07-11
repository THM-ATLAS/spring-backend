package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import java.sql.Timestamp

data class NotificationRead(@Id var notification_id: Int,
                            var title: String,
                            var content: String,
                            var time: Timestamp,
                            var read: Boolean,
                            var type_id: Int,
                            var module_id: Int?,
                            var exercise_id: Int?,
                            var submission_id: Int?){

}