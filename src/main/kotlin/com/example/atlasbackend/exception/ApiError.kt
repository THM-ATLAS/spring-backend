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
    // var errors: List<String>

    // Error
    constructor(code: Int, status: HttpStatus, message: String) : super() {
        this.code = code
        this.status = status
        this.message = message
    }

    // Unexpected error
    constructor(code: Int, status: HttpStatus) : super() {
        this.code = code
        this.status = status
        this.message = "Unexpected Error"
    }


    /* Need this later maybe

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
    }*/
}