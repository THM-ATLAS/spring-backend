package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("user_exercise_rating")
data class Rating(@Id var user_id: Int, @Id var exercise_id: Int, var value: Int)