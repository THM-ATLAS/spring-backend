package com.example.atlasbackend.exception

object ExerciseNotFoundException : RuntimeException() {
    private const val serialVersionUID = 1L
}

object InternalServerErrorException : RuntimeException() {
    private const val serialVersionUID = 1L
}