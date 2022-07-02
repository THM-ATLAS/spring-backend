package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table


@Table("submission_file")
data class FileSubmission(@Id val submission_id: Int, val file: String): SubmissionTemplate() {
}