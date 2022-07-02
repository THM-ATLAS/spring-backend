package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.FreeSubmission
import com.example.atlasbackend.classes.Submission
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface FreeSubmissionRepository: CrudRepository<FreeSubmission, Int> {

    @Query("SELECT * FROM free_submission WHERE user_id = :user AND exercise_id = :exercise")
    fun getExerciseSubmissionForUser(@Param("user") user_id: Int, @Param("exercise") exercise_id: Int): Submission?

}