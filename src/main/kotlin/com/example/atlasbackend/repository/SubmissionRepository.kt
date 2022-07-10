package com.example.atlasbackend.repository

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

    @Query("SELECT * FROM user_exercise_submission WHERE user_id = :user AND exercise_id = :exercise")
    fun getExerciseSubmissionForUser(@Param("user") user_id: Int, @Param("exercise") exercise_id: Int): Submission?

    @Query("SELECT * FROM user_exercise_submission ORDER BY submission_id LIMIT :size OFFSET :offset")
    fun loadPage(@Param("size") size: Int, @Param("offset") offset: Int): List<Submission>

    @Query("SELECT * FROM user_exercise_submission WHERE exercise_id = :id ORDER BY submission_id LIMIT :size OFFSET :offset")
    fun getSubmissionsByExerciseByPage(@Param("id") id: Int, @Param("size") size: Int, @Param("offset") offset: Int): List<Submission>

    @Query("SELECT * FROM user_exercise_submission WHERE user_id = :id ORDER BY submission_id LIMIT :size OFFSET :offset")
    fun getSubmissionsByUserByPage(@Param("id") id: Int, @Param("size") size: Int, @Param("offset") offset: Int): List<Submission>



}