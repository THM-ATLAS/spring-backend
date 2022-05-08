package com.example.atlasbackend.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [TaskNotFoundException::class])
    fun exception(exception: TaskNotFoundException?): ResponseEntity<Any> {

        val errors: MutableList<String> = ArrayList()
        errors.add("Error1")
        val err = ApiError(HttpStatus.NOT_FOUND, "message1", errors)

        return ResponseEntity<Any>("Product not found", HttpStatus.NOT_FOUND)

        // TODO: fix empty object return
        //return ResponseEntity<Any>(ApiError(HttpStatus.NOT_FOUND,"MESSAGE TEST", "test"), HttpStatus.NOT_FOUND)
    }
}
