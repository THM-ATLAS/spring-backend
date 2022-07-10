package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.sql.Timestamp

@Table("user_exercise_submission")
data class Submission(@Id var submission_id: Int,
                      var exercise_id: Int,
                      var user_id: Int,
                      var upload_time: Timestamp,
                      var grade: Int?,
                      var teacher_id: Int?,
                      var comment: String?,
                      var type: Int) {
    @org.springframework.data.annotation.Transient var content: SubmissionTemplate? = null
}