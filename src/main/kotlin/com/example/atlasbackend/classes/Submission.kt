package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("user_exercise_submission")
data class Submission(@Id var submission_id: Int, var exercise_id: Int, var user_id: Int, var file: String, var grade: Int)
