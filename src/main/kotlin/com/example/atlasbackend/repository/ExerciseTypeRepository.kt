package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.ExerciseType
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ExerciseTypeRepository: CrudRepository<ExerciseType, Int> {

    @Query("SELECT name FROM exercise_type WHERE type_id = :id")
    fun getExerciseTypeName(@Param("id") id: Int?): String?

    @Query("SELECT type_id FROM exercise_type WHERE name = :name")
    fun getExerciseTypeID(@Param("name") id: String?): Int?
}