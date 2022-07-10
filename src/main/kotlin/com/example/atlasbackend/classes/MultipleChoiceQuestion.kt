package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table


@Table("mc_question")
data class MultipleChoiceQuestion (@Id var question_id: Int, var content: String, var exercise_id: Int) {
    @org.springframework.data.annotation.Transient var answers: List<MultipleChoiceAnswer>? = null
}