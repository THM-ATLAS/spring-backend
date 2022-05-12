package com.example.atlasbackend.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class Exceptions {

    @ExceptionHandler(value = [ExerciseNotFoundException::class])
    fun exception(exception: ExerciseNotFoundException?): ResponseEntity<Any> {

        val errors: MutableList<String> = ArrayList()
        errors.add("Exercise not found")
        val err = ApiError(HttpStatus.NOT_FOUND, "Exercise couldn't be found", errors)

        return ResponseEntity<Any>("Exercise not found", HttpStatus.NOT_FOUND)

        // TODO: Fix empty object return
        //return ResponseEntity<Any>(ApiError(HttpStatus.NOT_FOUND,"Exercise not found", "Couldn't find exercise"), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [InternalServerErrorException::class])
    fun exception(exception: InternalServerErrorException?): ResponseEntity<Any> {

        return ResponseEntity<Any>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR)

        // TODO: Fix empty object return
        //return ResponseEntity<Any>(ApiError(HttpStatus.INTERNAL_SERVER_ERROR,"Internal Server Error", "Server Error"), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    // TODO: Add more errors

}
