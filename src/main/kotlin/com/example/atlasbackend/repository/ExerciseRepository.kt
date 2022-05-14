package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.Exercise
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ExerciseRepository: CrudRepository<Exercise, Int> {

    @Query("SELECT * FROM exercise WHERE module_id = :id")
    fun getExercisesByModule(@Param("id") id: Int): List<Exercise>
}