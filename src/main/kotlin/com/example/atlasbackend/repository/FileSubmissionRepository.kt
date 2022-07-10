package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.FileSubmission
import org.springframework.data.repository.CrudRepository

interface FileSubmissionRepository: CrudRepository<FileSubmission, Int> {
}