package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.Submission
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface SubmissionRepository: CrudRepository<Submission, Int> {
    // TODO: Add Queries
}