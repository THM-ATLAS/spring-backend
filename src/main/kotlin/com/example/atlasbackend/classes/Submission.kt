package com.example.atlasbackend.classes

import io.swagger.v3.core.util.Json
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("user_exercise_submission")
data class Submission(@Id var submission_id: Int, var exercise_id: Int, var user_id: Int, var file: Json, var grade: Int)
