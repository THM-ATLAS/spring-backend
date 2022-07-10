package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.McSubmission
import com.example.atlasbackend.classes.MultipleChoiceAnswer
import com.example.atlasbackend.classes.MultipleChoiceQuestion
import com.example.atlasbackend.classes.SubmissionMcAnswer
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface McAnswerRepository: CrudRepository<MultipleChoiceAnswer, Int> {
    @Query("SELECT * FROM mc_answer WHERE question_id = :id")
    fun getAnswersForQuestion(@Param("id") id: Int): List<MultipleChoiceAnswer>

    @Modifying
    @Query("DELETE FROM mc_answer WHERE question_id = :id")
    fun delAnswersForQuestion(@Param("id") id: Int)

    @Query("SELECT * FROM mc_submission WHERE submission_id = :id")
    fun getAnswersBySubmission(@Param("id") submissionID: Int): List<SubmissionMcAnswer>

    @Modifying
    @Query("INSERT INTO mc_submission (submission_id, answer_id, marked) VALUES (:subId, :ansId, :marked)")
    fun addAnswersBySubmission(@Param("subId") submissionID: Int, @Param("ansId") answerId: Int, @Param("marked") marked: Boolean)

    @Modifying
    @Query("UPDATE mc_submission SET submission_id = :subId, answer_id = :ansId, marked = :marked WHERE submission_id = :subId AND answer_id = :ansId")
    fun editAnswersBySubmission(@Param("subId") submissionID: Int, @Param("ansId") answerId: Int, @Param("marked") marked: Boolean)

    @Modifying
    @Query("DELETE FROM mc_submission WHERE submission_id = :subId")
    fun deleteAnswersBySubmission(@Param("subId") submissionID: Int)
}