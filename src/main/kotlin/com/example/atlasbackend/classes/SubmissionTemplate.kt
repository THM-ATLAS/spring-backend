package com.example.atlasbackend.classes

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo


@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    visible = true)
@JsonSubTypes(
    JsonSubTypes.Type(value = CodeSubmission::class, name = "code"),
    JsonSubTypes.Type(value = FileSubmission::class, name = "file"),
    JsonSubTypes.Type(value = FreeSubmission::class, name = "free"),
    JsonSubTypes.Type(value = McSubmission::class, name = "mc"))
abstract class SubmissionTemplate(@org.springframework.data.annotation.Transient val type: String)