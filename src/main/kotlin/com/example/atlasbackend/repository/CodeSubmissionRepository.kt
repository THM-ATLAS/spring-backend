package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.CodeSubmission
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface CodeSubmissionRepository: CrudRepository<CodeSubmission, Int> {

    @Modifying
    @Query("INSERT INTO  submission_code (submission_id, content, language) VALUES (:subID, :con, :lang)")
    fun insertCodeSubmission(@Param("subID") submissionID: Int, @Param("con") content: String, @Param("lang") language: Int)
}