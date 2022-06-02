package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.Exercise
import com.example.atlasbackend.classes.Submission
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface SubmissionRepository: CrudRepository<Submission, Int> {

    @Query("SELECT * FROM user_exercise_submission WHERE exercise_id = :id")
    fun getSubmissionsByExercise(@Param("id") id: Int): List<Submission>

    @Query("SELECT * FROM user_exercise_submission WHERE user_id = :id")
    fun getSubmissionsByUser(@Param("id") id: Int): List<Submission>
}