package com.example.atlasbackend.exception

import org.springframework.http.HttpStatus


class ApiError {
    private var status: HttpStatus
    private var message: String
    private var errors: List<String>

    constructor(status: HttpStatus, message: String, errors: List<String>) : super() {
        this.status = status
        this.message = message
        this.errors = errors
    }

    constructor(status: HttpStatus, message: String, error: String) : super() {
        this.status = status
        this.message = message
        errors = listOf(error)
    }
}