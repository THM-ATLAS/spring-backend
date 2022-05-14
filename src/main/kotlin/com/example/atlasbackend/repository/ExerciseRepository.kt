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

    @Query("SELECT * FROM exercise e WHERE e.public = true UNION SELECT e.exercise_id, e.content, e.public, e.title, e.module_id, e.description FROM exercise e JOIN user_module_role u_m_r ON e.module_id = u_m_r.module_id WHERE u_m_r.user_id = :id")
    fun getExercisesByUser(@Param("id") id: Int): Set<Exercise>
}