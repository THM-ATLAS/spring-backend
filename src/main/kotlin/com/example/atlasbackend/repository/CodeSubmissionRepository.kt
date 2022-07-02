package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.CodeSubmission
import org.springframework.data.repository.CrudRepository

interface CodeSubmissionRepository: CrudRepository<CodeSubmission, Int> {
}