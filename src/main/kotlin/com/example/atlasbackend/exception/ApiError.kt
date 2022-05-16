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
    var errors: List<String>
    lateinit var message: String

    // Error
    constructor(code: Int, status: HttpStatus, error: String, message: String) : super() {
        this.code = code
        this.status = status
        errors = listOf(error)
        this.message = message
    }

    // Unexpected error
    constructor(code: Int, status: HttpStatus) : super() {
        this.code = code
        this.status = status
        errors = listOf("Unexpected Error")
    }
}