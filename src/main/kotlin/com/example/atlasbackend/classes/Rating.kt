package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("user_exercise_rating")
data class Rating(@Id val rating_id: Int, var user_id: Int, var exercise_id: Int, var value: Int)