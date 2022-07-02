package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table


@Table("submission_free")
data class FreeSubmission(@Id val submission_id: Int, val content: String): SubmissionTemplate() {
}