package com.example.atlasbackend.classes

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("mc_answer")
data class MultipleChoiceAnswer(@Id var answer_id: Int,
                                var content: String,
                                @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) var correct: Boolean
    ) {
    var question_id: Int = 0
    @org.springframework.data.annotation.Transient var marked: Boolean = false
}