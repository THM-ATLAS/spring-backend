package com.example.atlasbackend.classes

import org.springframework.data.relational.core.mapping.Table


data class McSubmission(var submission_id: Int): SubmissionTemplate("mc") {
    @org.springframework.data.annotation.Transient
    var questions: List<MultipleChoiceQuestion>? = null
}