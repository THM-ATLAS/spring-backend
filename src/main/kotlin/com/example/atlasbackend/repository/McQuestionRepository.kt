package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.MultipleChoiceQuestion
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository


@Repository
interface McQuestionRepository: CrudRepository<MultipleChoiceQuestion, Int> {
    @Query("SELECT * FROM mc_question WHERE exercise_id = :id")
    fun getMcForExercise(@Param("id") id: Int): List<MultipleChoiceQuestion>

    @Modifying
    @Query("DELETE FROM mc_question WHERE exercise_id = :id")
    fun delQuestionsForExercise(@Param("id") id: Int)
}