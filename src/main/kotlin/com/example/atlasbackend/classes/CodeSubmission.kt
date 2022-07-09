package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("submission_code")
data class CodeSubmission(@Id var submission_id: Int, val content: String, val language: Int): SubmissionTemplate("code") {

}