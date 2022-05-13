package com.example.atlasbackend.exception

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class ApiError {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private val timestamp: LocalDateTime = LocalDateTime.now()
    // TODO: See how these can be made private and still be accessible
    var code: Int
    var status: HttpStatus
    var message: String
    var errors: List<String>

    constructor(code: Int, status: HttpStatus, message: String, errors: List<String>) : super() {
        this.code = code
        this.status = status
        this.message = message
        this.errors = errors
    }

    constructor(code: Int, status: HttpStatus, message: String, error: String) : super() {
        this.code = code
        this.status = status
        this.message = message
        errors = listOf(error)
    }
}