package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("exercise_type")
data class ExerciseType(@Id var type_id: Int, var name: String)
