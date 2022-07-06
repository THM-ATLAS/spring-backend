package com.example.atlasbackend.classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("submission_type")
data class SubmissionType(@Id val type_id: Int, val name: String)