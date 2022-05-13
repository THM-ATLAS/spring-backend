package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("exercise")
class Exercise(@Id var exercise_id: Int,var module_id: Int, var title: String, var content: String, var description: String, @field:Column("public") var exercisePublic: Boolean)
