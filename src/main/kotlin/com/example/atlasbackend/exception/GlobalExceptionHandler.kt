package com.example.atlasbackend.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [TaskNotFoundException::class])
    fun exception(exception: TaskNotFoundException?): ResponseEntity<Any> {
        return ResponseEntity("Task not found", HttpStatus.NOT_FOUND)
    }
}
