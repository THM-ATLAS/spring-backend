package com.example.atlasbackend.classes

import org.springframework.data.relational.core.mapping.Table


@Table("mc_submission")
data class McSubmission(val submission_id: Int): SubmissionTemplate() {
    @org.springframework.data.annotation.Transient val questions: List<MultipleChoiceQuestion>? = null
}