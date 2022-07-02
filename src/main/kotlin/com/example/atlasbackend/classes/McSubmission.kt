package com.example.atlasbackend.classes

import org.springframework.data.relational.core.mapping.Table


data class McSubmission(val submission_id: Int): SubmissionTemplate() {
    @org.springframework.data.annotation.Transient
    var questions: List<MultipleChoiceQuestion>? = null
}