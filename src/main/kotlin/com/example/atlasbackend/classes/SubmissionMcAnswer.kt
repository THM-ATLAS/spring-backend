package com.example.atlasbackend.classes

import org.springframework.data.relational.core.mapping.Table


@Table("mc_submission")
data class SubmissionMcAnswer(val submission_id: Int, val answer_id: Int, val marked: Boolean)