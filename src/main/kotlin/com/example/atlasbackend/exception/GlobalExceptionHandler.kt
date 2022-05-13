package com.example.atlasbackend.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [ExerciseNotFoundException::class])
    fun exception(exception: ExerciseNotFoundException?): ResponseEntity<ApiError> {

        val errors: MutableList<String> = ArrayList()
        errors.add("Exercise not found")
        val err = ApiError(404, HttpStatus.NOT_FOUND, "Couldn't find exercise", errors)

        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [InternalServerErrorException::class])
    fun exception(exception: InternalServerErrorException?): ResponseEntity<ApiError> {

        val errors: MutableList<String> = ArrayList()
        errors.add("Exercise not found")
        val err = ApiError(500, HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", errors)

        return ResponseEntity<ApiError>(err, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    // TODO: Add more errors

}
