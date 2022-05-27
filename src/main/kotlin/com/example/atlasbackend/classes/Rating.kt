package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import javax.persistence.IdClass

data class PrimaryKey(var user_id: Int, var exercise_id: Int)

@Table("user_exercise_rating")
@IdClass(PrimaryKey::class)
data class Rating(@Id var user_id: Int, var exercise_id: Int, var value: Int)