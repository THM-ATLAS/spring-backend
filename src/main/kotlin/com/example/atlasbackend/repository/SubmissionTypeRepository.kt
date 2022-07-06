package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.SubmissionType
import org.springframework.data.repository.CrudRepository

interface SubmissionTypeRepository: CrudRepository<SubmissionType, Int> {
}