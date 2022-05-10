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
        errors.add("Task not found")
        val err = ApiError(HttpStatus.NOT_FOUND, "Task couldn't be found", errors)

        return ResponseEntity<Any>("Task not found", HttpStatus.NOT_FOUND)

        // TODO: Fix empty object return
        //return ResponseEntity<Any>(ApiError(HttpStatus.NOT_FOUND,"Task not found", "Task couldn't be found"), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [InternalServerErrorException::class])
    fun exception(exception: InternalServerErrorException?): ResponseEntity<Any> {

        return ResponseEntity<Any>("Internal Server Error", HttpStatus.NOT_FOUND)

        // TODO: Fix empty object return
        //return ResponseEntity<Any>(ApiError(HttpStatus.NOT_FOUND,"Task not found", "Task couldn't be found"), HttpStatus.NOT_FOUND)
    }

    // TODO: Add more errors

}
